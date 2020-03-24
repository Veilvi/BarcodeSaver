package com.barcodescaner.DataAnalysysFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.barcodescaner.LiveBarcodeScan.LiveScan
import com.barcodescaner.MainActivity
import com.barcodescaner.R
import com.barcodescaner.TXTMechanic
import kotlinx.android.synthetic.main.activity_adding_data.*
import java.io.IOException
import java.util.zip.Inflater

class AddingData : Fragment(), View.OnClickListener {
    private var text: String?=null
    private var edit1: EditText? = null
    private var edit2: EditText? = null
    private var edit3: EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.activity_adding_data, container, false)

        text=this.arguments?.getString("QR_Value")
        edit1= view.findViewById(R.id.EditText01)
        edit2= view.findViewById(R.id.EditText02)
        edit3= view.findViewById(R.id.EditText03)
        val saveButton =view.findViewById<View>(R.id.saveNew) as Button
        saveButton.setOnClickListener(this)
        return view

    }

    fun startEntryChoice() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun startLiveScaning() {
        val intent = Intent(context, LiveScan::class.java)
        startActivity(intent)
    }

    @Throws(IOException::class)
    fun onSaveClick(view: View?) {

        val resultString =
            "\n" + text.toString() + " " + TXTMechanic.stringFilter(edit1!!.text.toString()) + " " + TXTMechanic.stringFilter(
                edit2!!.text.toString()
            ) + " " + TXTMechanic.stringFilter(edit3!!.text.toString())
        val filePath = getString(R.string.file_path)
        TXTMechanic.saveRecord(context, resultString, filePath)

    }

    override fun onClick(v: View?) {

        onSaveClick(v)
       startEntryChoice()

    }


}
