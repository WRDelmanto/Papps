package com.wrdelmanto.papps.apps.cronometer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wrdelmanto.papps.R

class CronometerAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<CronometerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lapItem: TextView

        init {
            lapItem = view.findViewById(R.id.lap_list_item)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.lap_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.lapItem.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}