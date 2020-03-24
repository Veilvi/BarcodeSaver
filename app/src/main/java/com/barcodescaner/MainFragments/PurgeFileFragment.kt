package com.barcodescaner.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.barcodescaner.R
import com.barcodescaner.TXTMechanic


class PurgeFileFragment : DialogFragment(),
    View.OnClickListener {
    private var editText: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.fragment_purge_file, container, false)
        val purgeButton =
            view.findViewById<View>(R.id.button4) as Button
        purgeButton.setOnClickListener(this)
        editText = view.findViewById<View>(R.id.ClearEdit) as EditText
        return view
    }

    override fun onClick(v: View) {
        if (editText!!.text.toString().contains("Очистить")) {

            TXTMechanic.purgeFile()
            dismiss()
        } else {
            Toast.makeText(context, "Введите 'Очистить'", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelClick() {}
}
