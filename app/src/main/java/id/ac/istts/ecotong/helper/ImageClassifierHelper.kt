//package id.ac.istts.ecotong.helper
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.os.SystemClock
//import android.provider.MediaStore
//import android.view.Surface
//import androidx.camera.core.ImageProxy
//import id.ac.istts.ecotong.R
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.common.ops.CastOp
//import org.tensorflow.lite.support.common.ops.NormalizeOp
//import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.image.ops.ResizeOp
//import org.tensorflow.lite.task.core.BaseOptions
//import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
//import org.tensorflow.lite.task.vision.classifier.Classifications
//import org.tensorflow.lite.task.vision.classifier.ImageClassifier
//import timber.log.Timber
//
//class ImageClassifierHelper(
//    private var threshold: Float = 0.5f,
//    private var maxResults: Int = 3,
//    private val modelName: String = "ecobin_model.tflite",
//    val context: Context,
//    val classifierListener: ClassifierListener?
//) {
//    private var imageClassifier: ImageClassifier? = null
//
//    init {
//        setupImageClassifier()
//    }
//
//    private fun setupImageClassifier() {
//        val optionsBuilder =
//            ImageClassifier.ImageClassifierOptions.builder().setScoreThreshold(threshold)
//                .setMaxResults(maxResults)
//        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(4)
//        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())
//
//        try {
//            imageClassifier = ImageClassifier.createFromFileAndOptions(
//                context, modelName, optionsBuilder.build()
//            )
//        } catch (e: IllegalStateException) {
//            classifierListener?.onError("Failed to initialize classifier")
//            Timber.e(e.message.toString())
//        }
//    }
//
//    fun classifyStaticImage(imageUri: Uri) {
//        if (imageClassifier == null) {
//            setupImageClassifier()
//        }
//
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
//            .add(NormalizeOp(0f, 1f)) // Normalize pixel values to range [0, 1]
//            .add(CastOp(DataType.FLOAT32)) // Ensure the TensorImage is of type FLOAT32
//            .build()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//            ImageDecoder.decodeBitmap(source)
//        } else {
//            @Suppress("DEPRECATION") MediaStore.Images.Media.getBitmap(
//                context.contentResolver,
//                imageUri
//            )
//        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
//            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
//            var inferenceTime = SystemClock.uptimeMillis()
//            val results = imageClassifier?.classify(tensorImage)
//            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
//            classifierListener?.onResults(
//                results, inferenceTime
//            )
//        }
//    }
//
////    private fun toBitmap(image: ImageProxy): Bitmap {
////        val bitmapBuffer = Bitmap.createBitmap(
////            image.width, image.height, Bitmap.Config.ARGB_8888
////        )
////        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
////        image.close()
////        return bitmapBuffer
////    }
//
////    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
////        return when (rotation) {
////            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
////            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
////            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
////            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
////        }
////    }
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResults(
//            results: List<Classifications>?, inferenceTime: Long
//        )
//    }
//
//
//}