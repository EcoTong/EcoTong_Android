package id.ac.istts.ecotong.ui.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.FragmentScanBinding
import id.ac.istts.ecotong.helper.ImageClassifierHelper
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.visible
import org.tensorflow.lite.task.vision.classifier.Classifications
import timber.log.Timber
import java.io.File

class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            setDisableCamera(true)
        }
    }
    private var camera: Camera? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var imageCapture: ImageCapture? = null

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(), REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    override fun setupUI() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            startCamera()
        }
    }

    override fun setupListeners() {
        with(binding) {
            btnFlash.setOnClickListener {
                toggleFlash()
            }
            btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            btnCapture.setOnClickListener {
                capturePhoto()
            }
        }
    }


    override fun setupObservers() {
    }

    private fun startCamera() {
        setDisableCamera(false)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                val scaleGestureDetector = ScaleGestureDetector(requireContext(),
                    object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        override fun onScale(detector: ScaleGestureDetector): Boolean {
                            val scale =
                                camera!!.cameraInfo.zoomState.value!!.zoomRatio * detector.scaleFactor
                            camera!!.cameraControl.setZoomRatio(scale)
                            return true
                        }
                    })

                binding.viewFinder.setOnTouchListener { view, event ->
                    view.performClick()
                    scaleGestureDetector.onTouchEvent(event)
                    return@setOnTouchListener true
                }

            } catch (exc: Exception) {

            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setDisableCamera(b: Boolean) {
        with(binding) {
            if (b) {
                layoutAccepted.gone()
                layoutDenied.visible()
            } else {
                layoutDenied.gone()
                layoutAccepted.visible()
            }
        }
    }

    private fun toggleFlash() {
        val cameraControl = camera?.cameraControl
        val currentFlashMode = camera?.cameraInfo?.torchState?.value
        val newFlashMode = when (currentFlashMode) {
            TorchState.ON -> TorchState.OFF
            else -> TorchState.ON
        }
        cameraControl?.enableTorch(newFlashMode == TorchState.ON)
        updateFlashIcon(newFlashMode == TorchState.ON)
    }

    private fun updateFlashIcon(isFlashOn: Boolean) {
        with(binding) {
            if (isFlashOn) {
                btnFlash.setBackgroundResource(R.drawable.circle)
            } else {
                btnFlash.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun capturePhoto() {
        val photoFile = createCustomTempFile(requireContext())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    classifyImage(output.savedUri!!)
                }

                override fun onError(exc: ImageCaptureException) {
//                    Toast.makeText(
//                        this@CameraActivity,
//                        "Gagal mengambil gambar.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Timber.e("onError: ${exc.message}")
                }
            })
//        imageCapture?.takePicture(outputFileOptions, cameraExecutor,
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(error: ImageCaptureException) {
//                    Log.e("ScanFragment", "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = output.savedUri ?: return
//                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePathString())
//                    classifyImage(bitmap)
//                }
//            }
//        )
    }

    private fun classifyImage(uri: Uri) {
        ImageClassifierHelper(context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(
                    results: List<Classifications>?, inferenceTime: Long
                ) {
                    Handler(Looper.getMainLooper()).post {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it.score }
                                val displayResult = sortedCategories.joinToString("\n") {
                                    "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                }
                                showResultsPopup(displayResult, "$inferenceTime ms")
                            } else {
                                showResultsPopup("", "")
                            }
                        }
                    }
                }
            }).classifyStaticImage(uri)
//        imageClassifierHelper.classify(bitmap)
    }

    private fun showResultsPopup(result: String, inferenceTime: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Classification Results")
        builder.setMessage("Results:\n$result\n\nInference Time:\n$inferenceTime")
        builder.setPositiveButton("OK", null)
        builder.show()
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir// Ensure external cache dir is available
        val fileName = "temp_scan.jpg" // Specify the desired file name
        return File(filesDir, fileName)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}