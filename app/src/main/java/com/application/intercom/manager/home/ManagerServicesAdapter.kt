package com.application.intercom.manager.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ItemHomeServicesBinding
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.utils.loadImagesWithGlideExt


class ManagerServicesAdapter(
    private val context: Context, val list: ArrayList<ServicesCategoryTable>,val onPress:ServiceHome
) : RecyclerView.Adapter<ManagerServicesAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomeServicesBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomeServicesBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        data.image?.let { holder.mView.ivServiceIcon.loadImagesWithGlideExt(it) }
        holder.mView.textView13.text = data.category_name
        holder.itemView.setOnClickListener {
            onPress.onServiceClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface ServiceHome {
        fun onServiceClick(position: Int)
    }
}