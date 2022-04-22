package com.sample.healthcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.healthcheck.R
import com.sample.healthcheck.model.Accessible

class AccessibleAdapter(private val context: Context, private val accessibleList: List<Accessible>): RecyclerView.Adapter<AccessibleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accessible_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val accessibleDetails: Accessible = accessibleList[position]

        if(accessibleDetails.type != null) {
            holder.txtType.text = accessibleDetails.type
        }

        /*Check whether status is success or not
        * if success set green background
        * else set red background*/
        if(accessibleDetails.success!!){
            holder.txtStatus.text = accessibleDetails.success.toString()
            holder.txtStatus.setTextColor(context.resources.getColor(R.color.green))
            holder.txtStatus.setBackgroundResource(R.drawable.status_true_bg)
        }else {
            holder.txtStatus.text = accessibleDetails.success.toString()
            holder.txtStatus.setTextColor(context.resources.getColor(R.color.red))
            holder.txtStatus.setBackgroundResource(R.drawable.status_false_bg)
        }

    }

    override fun getItemCount(): Int {
        return accessibleList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView
        val txtStatus: TextView

        init {
            txtType = itemView.findViewById(R.id.txt_type) as TextView
            txtStatus = itemView.findViewById(R.id.txt_status) as TextView
        }
    }
}