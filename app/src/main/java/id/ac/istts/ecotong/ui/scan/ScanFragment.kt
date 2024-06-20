package id.ac.istts.ecotong.ui.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.ScaleGestureDetector
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.data.repository.State
import id.ac.istts.ecotong.databinding.FragmentScanBinding
import id.ac.istts.ecotong.ui.base.BaseFragment
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.invisible
import id.ac.istts.ecotong.util.toastLong
import id.ac.istts.ecotong.util.visible
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate) {
    private val viewModel: ScanViewModel by viewModels()
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            setDisableCamera(true)
        }
    }
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var camera: Camera? = null
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
        viewModel.predictionResponse.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {
                        requireActivity().toastLong(getString(R.string.an_unexpected_error_has_occurred))
                        loadingCapture.invisible()
                        btnCapture.visible()
                        startCamera()
                    }

                    State.Loading -> {
                        btnCapture.invisible()
                        loadingCapture.visible()
                    }

                    is State.Success -> {
                        val predictionMap = mutableMapOf<String, Float>()
                        it.data.forEach {
                            when {
                                it.glass != null -> {
                                    predictionMap["glass"] = it.glass
                                }

                                it.metal != null -> {
                                    predictionMap["metal"] = it.metal
                                }

                                it.paper != null -> {
                                    predictionMap["paper"] = it.paper
                                }

                                it.plastic != null -> {
                                    predictionMap["plastic"] = it.plastic
                                }

                                it.biodegradable != null -> {
                                    predictionMap["biodegradable"] = it.biodegradable
                                }

                                it.cardboard != null -> {
                                    predictionMap["cardboard"] = it.cardboard
                                }
                            }
                        }
                        val sortedPredictions =
                            predictionMap.entries.sortedByDescending { it.value }
                        val topPrediction = sortedPredictions[0]
                        sortedPredictions.forEach { println("${it.key}: ${it.value}") }
                        Timber.tag("Top Prediction").d(topPrediction.toString())
                        if (topPrediction.value < 0.75) {
                            Timber.d(topPrediction.value.toString())
                            requireActivity().toastLong(getString(R.string.unable_to_identify_object))
                            loadingCapture.invisible()
                            btnCapture.visible()
                            startCamera()
                        } else {
                            findNavController().navigate(
                                ScanFragmentDirections.actionScanFragmentToScanResultFragment(
                                    topPrediction.key
                                )
                            )

                        }
                    }
                }
            }
        }
    }

    private fun startCamera() {
        setDisableCamera(false)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
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
                Timber.e(exc.message)
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
                    cameraProviderFuture.get().unbindAll()
                    viewModel.predictImage(photoFile)
                }

                override fun onError(exc: ImageCaptureException) {
                    Timber.e("onError: ${exc.message}")
                }
            })
    }


    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        val fileName = "temp_scan.jpg"
        return File(filesDir, fileName)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProviderFuture.cancel(true)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}