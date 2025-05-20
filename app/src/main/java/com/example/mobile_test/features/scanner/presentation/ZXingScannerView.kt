package com.example.mobile_test.features.scanner.presentation

import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.common.HybridBinarizer

/**
 * A Composable QR scanner view built using CameraX and ZXing.
 *
 * This component displays a live camera preview and continuously analyzes frames for QR codes.
 * When a QR code is detected, the provided [onQRCodeScanned] callback is triggered with the decoded text.
 *
 * @param modifier Modifier to apply layout changes to the camera preview.
 * @param onQRCodeScanned Lambda that receives the scanned QR code string once detected.
 *
 * Note:
 * - This scanner stops after detecting the first valid QR code.
 * - Requires camera permission to function properly.
 */
@OptIn(ExperimentalGetImage::class)
@Composable
fun ZXingScannerView(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val hasScanned = remember { mutableStateOf(false) }

    AndroidView(factory = { previewView }, modifier = modifier)

    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analysisUseCase = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val reader = MultiFormatReader().apply {
            setHints(
                mapOf(
                    DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
                    DecodeHintType.TRY_HARDER to true
                )
            )
        }

        analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
            try {
                if (!hasScanned.value) {
                    val source = imageProxy.toLuminanceSource()
                    val bitmap = BinaryBitmap(HybridBinarizer(source))
                    val result = reader.decodeWithState(bitmap)

                    hasScanned.value = true
                    onQRCodeScanned(result.text)
                    cameraProvider.unbindAll()
                }
            } catch (e: NotFoundException) {
                // No QR code found – this is expected in most frames.
            } catch (e: Exception) {
                Log.e("ZXingScanner", "Unexpected error decoding QR", e)
            } finally {
                imageProxy.close()
            }
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysisUseCase
            )
        } catch (e: Exception) {
            Log.e("ZXingScanner", "Error binding camera use cases", e)
        }
    }
}

/**
 * Converts an [ImageProxy] to a ZXing-compatible [LuminanceSource].
 *
 * This utility function extracts the luminance data from the camera frame to allow
 * ZXing's QR decoder to process it.
 *
 * @throws IllegalStateException if the image is null.
 *
 * @receiver The [ImageProxy] frame from CameraX analysis use case.
 * @return A [LuminanceSource] containing the grayscale pixel data.
 */
@OptIn(ExperimentalGetImage::class)
fun ImageProxy.toLuminanceSource(): LuminanceSource {
    val image: Image = this.image ?: throw IllegalStateException("Image is null")
    val buffer = image.planes[0].buffer
    val data = ByteArray(buffer.remaining())
    buffer.get(data)

    val width = image.width
    val height = image.height

    return object : LuminanceSource(width, height) {
        override fun getRow(y: Int, row: ByteArray?): ByteArray {
            val rowArray = row ?: ByteArray(width)
            System.arraycopy(data, y * width, rowArray, 0, width)
            return rowArray
        }

        override fun getMatrix(): ByteArray = data
    }
}