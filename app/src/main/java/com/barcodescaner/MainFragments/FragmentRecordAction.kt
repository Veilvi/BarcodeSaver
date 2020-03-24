package com.barcodescaner.MainFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.DialogFragment
import com.barcodescaner.TXTMechanic

class FragmentRecordAction : DialogFragment() {
    var actionTypes = arrayOfNulls<String>(2) //{, };
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        actionTypes[0] = "Поделиться"
        actionTypes[1] = "Добавить напоминание"
        val builder =
            AlertDialog.Builder(activity)
        builder.setTitle("Выберете действие")
            .setItems(actionTypes) { dialog, which ->
                val bundle = arguments
                if (which == 0) share(bundle)
                if (which == 1) calendar(bundle)
            }
        return builder.create()
    }

    fun calendar(bundle: Bundle?) {
        val calendarIntent =
            Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
        if (bundle != null) {
            val title = bundle.getString("Title")
            val description = bundle.getString("Description")
            calendarIntent.putExtra(CalendarContract.Events.TITLE, title)
            calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, description)
            startActivity(calendarIntent)
        }
    }

    fun share(bundle: Bundle?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        if (bundle != null) {

            shareIntent.type = "text/plain"
            val title = bundle.getString("Title")
            val description = bundle.getString("Description")
            val sendString = description + " " + TXTMechanic.spaceFilter(title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, sendString)
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }
}