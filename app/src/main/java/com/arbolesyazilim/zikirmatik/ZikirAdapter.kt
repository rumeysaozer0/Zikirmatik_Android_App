package com.arbolesyazilim.zikirmatik
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ZikirAdapter(
    private val zikirList: List<ZikirData>,
    private val onItemClick: (ZikirData) -> Unit
) : RecyclerView.Adapter<ZikirAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_zikir, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val zikirData = zikirList[position]
        holder.name.text = zikirData.name


        holder.itemView.setOnClickListener {
            onItemClick(zikirData)
        }
    }

    override fun getItemCount(): Int {
        return zikirList.size
    }
}
