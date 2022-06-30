package com.helloworldstudios.yetis

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.helloworldstudios.yetis.databinding.RecyclerRowBinding


class ServiceRecyclerAdapter(val context: Context, private var serviceList: ArrayList<Service>) : RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceHolder>() {
    class ServiceHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        holder.binding.recyclerServiceTitle.text = serviceList.get(position).serviceTitle
        holder.binding.recyclerServiceDescription.text = serviceList.get(position).serviceDescription
        holder.binding.intentForGoogleMaps.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:<" + serviceList.get(position).serviceLatitude.toString()
                        + ">,<" + serviceList.get(position).serviceLongitude.toString()
                        + ">?q=<" + serviceList.get(position).serviceLatitude.toString()
                        + ">,<" + serviceList.get(position).serviceLongitude.toString()
                        + ">(" + serviceList.get(position).serviceTitle + ")")
            )
            this.context.startActivity(Intent.createChooser(intent, "Launch Google Maps"))
        }

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}