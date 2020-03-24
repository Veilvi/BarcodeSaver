package com.barcodescaner.LiveBarcodeScan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.barcodescaner.DataAnalyse
import com.barcodescaner.MainActivity

import com.barcodescaner.R
import java.util.concurrent.Executors

private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)



const val FLAGS_FULLSCREEN =
    View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION



private var ValueAccept: Boolean = true

class LiveScan : AppCompatActivity() {
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView
    lateinit var value: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_scan)
        if (savedInstanceState == null) {
            window.decorView.setSystemUiVisibility(FLAGS_FULLSCREEN)
            viewFinder = findViewById(R.id.view_finder)


        }
        ValueAccept =true
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera()}
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
               val builder= AlertDialog.Builder(this)
                   .setTitle("Доступ к камере запрещен")
                   .setMessage("Для сканирования кодов приложение необходимо рахоешение к камере?")
                   .setPositiveButton("Разрешить доступ"){dialog, which ->   ActivityCompat.requestPermissions(
                       this, REQUIRED_PERMISSIONS,
                       REQUEST_CODE_PERMISSIONS
                   )  }
                   .setNegativeButton("Запретить доступ"){dialog, which ->  finish() }
                val dialog= builder.create()
                dialog.show()

            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    fun startCamera() {
           val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(viewFinder.width/2, viewFinder.height/2))

        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {

            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()


        }



        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setTargetRotation(viewFinder.display.rotation)
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .build()
        val qrCodeAnalyzer = BarCodeAnalyzer { qrCodes ->

                if (qrCodes.isNotEmpty() && ValueAccept) {
                    CameraX.unbindAll()
                    val value = qrCodes[0].rawValue.toString()
                    val intent = Intent(
                        baseContext,
                        DataAnalyse::class.java
                    )
                    intent.putExtra("BarcodeValue", value)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    ValueAccept = false
                }
            }
        val imageAnalyzer =
            ImageAnalysis(imageAnalysisConfig).apply { setAnalyzer(executor, qrCodeAnalyzer) }
        CameraX.bindToLifecycle(this, preview, imageAnalyzer)
    }
    private fun updateTransform() {
        // TODO: Implement camera viewfinder transformations
        val matrix = Matrix()
            val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        viewFinder.setTransform(matrix)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}







