package com.barcodescaner.MainFragments

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barcodescaner.DataClass
import com.barcodescaner.R
import java.util.*


class FileViewAdapter(mDataSet: List<DataClass>) :
    RecyclerView.Adapter<FileViewAdapter.ViewHolder>() {
    var mDataSet: ArrayList<DataClass>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.mBarCodes.setText(mDataSet[position].getQrCode())
        holder.mSubTitle1.setText(mDataSet[position].getText1())
        holder.mSubTitle2.setText(mDataSet[position].getText2())
        holder.mSubTitle3.setText(mDataSet[position].getText3())
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {
        var mBarCodes: TextView
        var mSubTitle1: TextView
        var mSubTitle2: TextView
        var mSubTitle3: TextView
        override fun onClick(v: View) {
            clickListener!!.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View): Boolean {
            clickListener!!.onItemLongClick(adapterPosition, v)
            return false
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            mBarCodes = itemView.findViewById<View>(R.id.textView4) as TextView
            mSubTitle1 = itemView.findViewById<View>(R.id.textView5) as TextView
            mSubTitle2 = itemView.findViewById<View>(R.id.textView6) as TextView
            mSubTitle3 = itemView.findViewById<View>(R.id.textView7) as TextView
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        Companion.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }

    companion object {
        private var clickListener: ClickListener? = null
    }

    init {
        this.mDataSet = mDataSet as ArrayList<DataClass>
    }
}
