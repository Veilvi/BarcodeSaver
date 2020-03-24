package com.barcodescaner.DataAnalysysFragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.barcodescaner.FileViewActivity
import com.barcodescaner.LiveBarcodeScan.LiveScan
import com.barcodescaner.MainActivity
import com.barcodescaner.R
import com.barcodescaner.TXTMechanic
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.File
import java.io.IOException
import java.util.*


class EditData : Fragment(), View.OnClickListener {
    private val Button: Button? = null
    private var txt: ArrayList<String>? = null
    private var QRRawValue: String?=null
    private var stringNumber = 0
    var textView1: TextView? = null
    var textView2: TextView? = null
    var textView3: TextView? = null
    var textView4: TextView? = null
    var filePath:String?=null
    var file: File? = null
    var text: TextView? = null
    var edit1: EditText? = null
    var edit2: EditText? = null
    var edit3: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.activity_edit, container, false)
        val saveButton =view.findViewById<View>(R.id.save) as Button
        saveButton.setOnClickListener(this)
        val deliteButton =view.findViewById<View>(R.id.delite) as Button
        deliteButton.setOnClickListener(this)
        filePath = getString(R.string.file_path)
        edit1 =view.findViewById(R.id.EditText011)
        edit2 = view.findViewById(R.id.EditText012)
        edit3 = view.findViewById(R.id.EditText013)
        txt= this.arguments?.getStringArrayList("list")
        QRRawValue= this.arguments?.getString("QR_Value")
        text?.setText(QRRawValue)
        stringNumber= this.arguments?.getInt("string_number")!!

         edit1?.requestFocus()

    return view
    }





    override fun onResume() {
        super.onResume()
        fillEdits()
    }

    @Throws(IOException::class)
    fun onSaveClick(view: View?) {
        txt!![stringNumber + 1] = TXTMechanic.stringFilter(edit1!!.text.toString())
        txt!![stringNumber + 2] = TXTMechanic.stringFilter(edit2!!.text.toString())
        txt!![stringNumber + 3] = TXTMechanic.stringFilter(edit3!!.text.toString())
        file = File(filePath)
        file!!.delete()
        file!!.createNewFile()
        try {
            TXTMechanic.editRecord(context, txt, filePath)
            val intent = Intent(context, FileViewActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
        }

    }

    fun onDeleteClick(view: View?) {
        context?.let {
            AlertDialog.Builder(it).setTitle(R.string.message_text)
                .setPositiveButton(R.string.Yes,
                    DialogInterface.OnClickListener { dialog, which ->
                        TXTMechanic.deleteRecord(context, txt, filePath, stringNumber)
                        val intent =
                            Intent(context, FileViewActivity::class.java)
                        startActivity(intent)

                    }).setNegativeButton(R.string.No,
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).create()
                .show()
        }
    }

    fun fillEdits() {

        if (arguments != null) {

            val  comment1 = TXTMechanic.spaceFilter(txt!![stringNumber + 1])
            val comment2 = TXTMechanic.spaceFilter(txt!![stringNumber + 2])
            val comment3 = TXTMechanic.spaceFilter(txt!![stringNumber + 3])
            edit1?.setText(comment1)
            edit2?.setText(comment2)
            edit3?.setText(comment3)
        }
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

    override fun onClick(v: View?) {

when(v?.getId()){
    R.id.save-> onSaveClick(v)
    R.id.delite-> onDeleteClick(v)
}
    }




}
