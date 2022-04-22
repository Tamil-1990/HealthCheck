package com.sample.healthcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.healthcheck.R
import com.sample.healthcheck.model.Accessible
import com.sample.healthcheck.model.Health

class HealthAdapter(private val context: Context, private val healthList: List<Health>): RecyclerView.Adapter<HealthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.health_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val healthDetails: Health = healthList[position]

        holder.txtHeader.text = healthDetails.name

        /*initialize LinearLayoutManager,  RecyclerView, Adapter AccessibleList then load Accessible adapter */
        if(healthDetails.accessible.isNotEmpty()) {
            val accessibleList: List<Accessible> = healthDetails.accessible
            val linearLayoutManager = LinearLayoutManager(context)
            holder.accessibleRecyclerView.layoutManager = linearLayoutManager
            holder.accessibleRecyclerView.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            val accessibleAdapter = AccessibleAdapter(context, accessibleList)
            holder.accessibleRecyclerView.adapter = accessibleAdapter
        }
    }

    override fun getItemCount(): Int {
        return healthList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHeader: TextView
        val accessibleRecyclerView: RecyclerView

        init {
            txtHeader = itemView.findViewById(R.id.txt_header) as TextView
            accessibleRecyclerView = itemView.findViewById(R.id.accessible_recycler_view) as RecyclerView
        }
    }
}