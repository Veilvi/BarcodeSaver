package com.barcodescaner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.barcodescaner.DataAnalysysFragments.AddingData
import com.barcodescaner.DataAnalysysFragments.EditData
import com.barcodescaner.DataAnalysysFragments.ScannedData
import com.barcodescaner.LiveBarcodeScan.LiveScan
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class DataAnalyse : AppCompatActivity(), OnFragment1DataListener {

    private var editText: EditText?=null
    val addingData= AddingData()
    val scannedData= ScannedData()
    val editData= EditData()
    var lines: List<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data__analysis)
        editText= findViewById<EditText>(R.id.textView)
        editText?.setText(data)
    }

    override fun onResume() {
        super.onResume()
        requestPermission()

    }

    fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) { // Permission is not granted
// Here, thisActivity is the current activity
// Permission is not granted
// Should we show an explanation?
// permission denied, boo! Disable the
// functionality that depends on this permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) { // Show an explanation to the user *asynchronously* -- don't block
// this thread waiting for the user's response! After the user
// sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.storage_acces_denied))
                    .setMessage(getString(R.string.Your_denied_storage_access)).setPositiveButton(
                        getString(R.string.permi_acces)
                    ) { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this@DataAnalyse,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }.setNegativeButton(
                        getString(R.string.Exit)
                    ) { dialog, id -> dialog.dismiss() }.create().show()
            } else { // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
// app-defined int constant. The callback method gets the
// result of the request.
            }
        } else { // Permission has already been granted

            fileAnalysis()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) { // permission was granted, yay! Do the
// contacts-related task you need to do.
                } else {
                }
                return
            }
        }
    }



    fun fileAnalysis() {

        val filePath = getString(R.string.file_path)
        val file = File(filePath)

        if(!file.exists()) {
                try {
                   if (Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED) {

                        val dirPath= getString(R.string.dirPath)
                        val dir= File(dirPath)
                       dir.mkdirs()
                       file.createNewFile()
                   }
                } catch (e: IOException) {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.No_records_file))
                        .setMessage(getString(R.string.cant_create_file))
                        .setNeutralButton(
                            R.string.Exit
                        ) { dialog, id -> finish() }.show()
                }
            }

        //text from file to ArrayList;
//  List<String> lines = new ArrayList<String>();
        try {
            lines = getArrayListFromFile(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // ArrayList Analysing
        val search = data

        if (lines.indexOf(search) != -1) { // scaned data activity

            val bundle=Bundle()
            bundle.putString("QR_Value", data)
            bundle.putStringArrayList("list",ArrayList(lines))
            bundle.putInt("string_number",lines.indexOf(search))
            scannedData.arguments=bundle
            val manager = supportFragmentManager
            val transaction= manager.beginTransaction()
            transaction.replace(R.id.frgmWorkWithData,scannedData)
            transaction.commit()

        } else { // adding data activity

            val bundle= Bundle()
            bundle.putString("QR_Value", data)
            bundle.putStringArrayList("list",ArrayList(lines))

            addingData.arguments=bundle
            val manager=supportFragmentManager
            val transaction= manager.beginTransaction()
            transaction.replace(R.id.frgmWorkWithData,addingData)
            transaction.commit()

        }
    }

    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 0
        @Throws(FileNotFoundException::class)
        fun getArrayListFromFile(f: File?): ArrayList<String?> {
            val s: Scanner
            val list = ArrayList<String?>()
            s = Scanner(f)
            while (s.hasNext()) {
                list.add(s.next())
            }
            s.close()
            return list
        }
    }




    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        
    }

    private val data: String?
        get() {
            val arguments = intent.extras
            return arguments?.get("BarcodeValue")?.toString()

        }

   public fun onEditFragmentOpen(){
        val bundle=Bundle()
        bundle.putString("QR_Value", data)
        bundle.putStringArrayList("list",ArrayList(lines))
        bundle.putInt("string_number",lines.indexOf(data))
        editData.arguments=bundle
        val manager = supportFragmentManager
        val transaction= manager.beginTransaction()
        transaction.replace(R.id.frgmWorkWithData,editData)
        transaction.commit()
    }

    override fun onOpenEditFragment() {
        onEditFragmentOpen()
    }

    private val  WRITE_REQUEST_CODE = 43;




}
