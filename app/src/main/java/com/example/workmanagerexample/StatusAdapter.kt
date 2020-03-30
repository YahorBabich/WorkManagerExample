package com.example.workmanagerexample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.status_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatusAdapter : RecyclerView.Adapter<StatusAdapter.StatusHolder>() {
    private val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
    private val items = mutableListOf<String>()

    fun update(update: String) {
        Log.d(MainActivity.TAG, update)
        items.add(0, "${sdf.format(Date())}: $update")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent, false)
        return StatusHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StatusHolder, position: Int) {
        holder.bind(items[position])
    }

    class StatusHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(status: String) {
            itemView.item.text = status
        }
    }
}