package com.barcodescaner.DataAnalysysFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.barcodescaner.LiveBarcodeScan.LiveScan
import com.barcodescaner.MainActivity
import com.barcodescaner.OnFragment1DataListener
import com.barcodescaner.R
import com.barcodescaner.TXTMechanic
import java.util.*


class ScannedData: Fragment(){
    private var mListener: OnFragment1DataListener? = null

    var txt: ArrayList<String>? = null
    var RawBarcodeData: String? = null
    var stringNumber = 0
    private var textView2: TextView?=null
    private var textView3: TextView?=null
    private var textView4: TextView?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View =
            inflater.inflate(R.layout.activity_scaned_data, container, false)

        textView2 = view.findViewById<TextView>(R.id.TextViewOl)!!
        textView3 = view.findViewById<TextView>(R.id.TextViewO2)!!
        textView4 = view.findViewById<TextView>(R.id.TextViewO3)!!
        val editButton =view.findViewById<View>(R.id.editButton) as Button
        editButton.setOnClickListener(View.OnClickListener { mListener?.onOpenEditFragment() })

        txt= this.arguments?.getStringArrayList("list")
        RawBarcodeData= this.arguments?.getString("QR_Value")
        stringNumber= this.arguments?.getInt("string_number")!!
        fillEdits()
        return view
    }

    fun startMain() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    fun startLiveScaning() {
        val intent = Intent(context, LiveScan::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    fun fillEdits() {
      //  val textView1: TextView = findViewById<TextView>(R.id.textView2)

        val comment1: String
        val comment2: String
        val comment3: String



            comment1 = TXTMechanic.spaceFilter(txt!![stringNumber + 1])
            comment2 = TXTMechanic.spaceFilter(txt!![stringNumber + 2])
            comment3 = TXTMechanic.spaceFilter(txt!![stringNumber + 3])
            textView2?.setText(comment1)
            textView3?.setText(comment2)
            textView4?.setText(comment3)

    }





    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragment1DataListener) {
            context
        } else {
            throw RuntimeException(
                "$context must implement OnFragment1DataListener"
            )
        }
    }
}

