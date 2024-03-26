package com.example.homesecurity.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homesecurity.R

class PeopleBoxAdapter(private val context: Context, private val boxList: List<PeopleBox>) :
    RecyclerView.Adapter<PeopleBoxAdapter.BoxViewHolder>() {

    inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frameLayout: FrameLayout = itemView.findViewById(R.id.frameLayout)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.people_box, parent, false)
        return BoxViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoxViewHolder, position: Int) {
        val currentItem = boxList[position]
        holder.textView.text = currentItem.name
        holder.frameLayout.setOnClickListener {
            // Gestisci il clic sul box
            Toast.makeText(context, "Box cliccato: ${currentItem.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = boxList.size
}