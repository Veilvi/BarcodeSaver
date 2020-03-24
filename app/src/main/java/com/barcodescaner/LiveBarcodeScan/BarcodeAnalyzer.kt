package com.barcodescaner.LiveBarcodeScan

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata


class BarCodeAnalyzer(
    private val onQrCodesDetected: (qrCode: List<FirebaseVisionBarcode>) -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy, rotationDegrees: Int) {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
            .build()

        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

        val rotation = rotationDegreesToFirebaseRotation(rotationDegrees)
        val visionImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)
        detector.detectInImage(visionImage)
            .addOnSuccessListener { barcodes ->
                onQrCodesDetected(barcodes)
                detector.close()
            }
            .addOnFailureListener {
                Log.e("QrCodeAnalyzer", "something went wrong", it)

            }.addOnCompleteListener() {}
    }

    private fun rotationDegreesToFirebaseRotation(rotationDegrees: Int): Int {
        return when (rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException("Not supported")
        }
    }
}

