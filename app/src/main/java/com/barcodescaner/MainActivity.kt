package com.barcodescaner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barcodescaner.LiveBarcodeScan.LiveScan
import com.barcodescaner.MainFragments.FileInfoFragment
import java.io.File
import com.barcodescaner.MainFragments.PurgeFileFragment

class MainActivity : AppCompatActivity() {


    enum class EntryMode(val titleResId: Int, val subtitleResId: Int) {
        ENTRY_Scan(
            R.string.entry_scan,
            R.string.entry_scan_subtitle
        ),
        ENTRY_View(R.string.entry_view, R.string.entry_view_subtitle);

    }

    fun startLiveScan() {
        startActivity(Intent(baseContext, LiveScan::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)
        val entryRecyclerView = findViewById<RecyclerView>(R.id.entry_recycler_view)
        entryRecyclerView.setHasFixedSize(true)
        entryRecyclerView.layoutManager = LinearLayoutManager(this)
        entryRecyclerView.adapter =
            EntryItemAdapter(EntryMode.values())
    }

    private class EntryItemAdapter internal constructor(private val entryModes: Array<EntryMode>) :
        RecyclerView.Adapter<EntryItemAdapter.EntryItemViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EntryItemViewHolder {
            return EntryItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.entry_item, parent, false)
            )
        }

        override fun onBindViewHolder(
            entryItemViewHolder: EntryItemViewHolder,
            position: Int
        ) {
            entryItemViewHolder.bindEntryMode(entryModes[position])
        }

        override fun getItemCount() = entryModes.size


        private inner class EntryItemViewHolder internal constructor(view: View) :
            RecyclerView.ViewHolder(view) {

            private val titleView: TextView
            private val subtitleView: TextView

            fun bindEntryMode(entryMode: EntryMode) {
                val bundle = Bundle.EMPTY
                titleView.setText(entryMode.titleResId)
                subtitleView.setText(entryMode.subtitleResId)
                itemView.setOnClickListener { view: View? ->
                    when (entryMode) {
                        EntryMode.ENTRY_Scan -> startActivity(
                            itemView.context,
                            Intent(itemView.context, LiveScan::class.java), bundle
                        )
                        EntryMode.ENTRY_View -> startActivity(
                            itemView.context,
                            Intent(itemView.context, FileViewActivity::class.java), bundle
                        )
                    }
                }


            }

            init {
                titleView = view.findViewById(R.id.entry_title)
                subtitleView = view.findViewById(R.id.entry_subtitle)

            }
        }
    }


        override fun onBackPressed() { // super.onBackPressed();
            AlertDialog.Builder(this).setTitle(getString(R.string.Want_to_exit))
                .setMessage("")
                .setPositiveButton(
                    getString(R.string.Yes)
                ) { dialog, id ->  finishAffinity() }
                .setNeutralButton(
                    getString(R.string.abort)
                ) { dialog, which -> dialog.dismiss() }.create().show()
        }

        fun onPurgeFile(v: View?) {
            val file = File(getString(R.string.file_path))
            if (file.exists() && file.length() != 0L) {
                val purgeFileFragment: PurgeFileFragment
                val fragmentManager = supportFragmentManager
                purgeFileFragment = PurgeFileFragment()
                purgeFileFragment.show(fragmentManager, "PurgeFileFragment")
            } else if (file.length() == 0L && file.exists()) Toast.makeText(
                this,
                "Файл пуст",
                Toast.LENGTH_LONG
            ).show() else {
                val fileInfoFragment: FileInfoFragment
                val fragmentManager = supportFragmentManager
                fileInfoFragment = FileInfoFragment()
                fileInfoFragment.show(fragmentManager, "FileInfoFragment")
            }
        }
    }

