package com.application.intercom.owner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.databinding.ItemPropertiesParkingBinding
import com.application.intercom.databinding.NewUserPropertyMapItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerSimilarPropertyAdapter(val con: Context, val list: ArrayList<PropertyLisRes.Data>) :
    RecyclerView.Adapter<OwnerSimilarPropertyAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: NewUserPropertyMapItemsBinding) :
    RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
       return MyViewHolder(NewUserPropertyMapItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        if (list[position].flatInfo != null) {
            holder.mView.imageView134.loadImagesWithGlideExt(item.flatInfo!!.photo.get(0))
        }
        if (item.addedBy != null) {
            holder.mView.textView234.text = "${list[position].title}"
            holder.mView.tvLocation.text = item.address
            if (item.flatInfo != null) {
                holder.mView.tvPropertyPrice.text = "৳${item.flatInfo!!.price}"
            } else {
                holder.mView.tvPropertyPrice.text = "৳${item.price}"
            }

            holder.mView.tvDistance.visibility = View.INVISIBLE
            holder.mView.view.visibility = View.INVISIBLE
            holder.mView.tvPropertyType.text = "${item.flatStatus}"
            holder.mView.tvDate.text = "Date of Registration :${setFormatDate(item.createdAt)}"
            holder.mView.tvPropertyType1.text = "${item.propertyType}"
        } else {
            holder.mView.tvDate.text = "Date of Registration :${setFormatDate(item.createdAt)}"
            holder.mView.tvDistance.visibility = View.INVISIBLE
            holder.mView.view.visibility = View.INVISIBLE
            // val disee = "${item.district} , ${item.division}"
//        holder.mView.ivProperty.loadImagesWithGlideExt(item.flatInfo.photo.get(0))
            holder.mView.textView234.text = item.buildingName
            holder.mView.tvLocation.text = "${item.district} , ${item.division}"
            holder.mView.tvPropertyPrice.text = "৳${item.flatInfo!!.price}"
            /*val dis: Double = item.distance / 1000
            val dis2: Double = String.format("%.1f", dis).toDouble()
            holder.mView.tvDistance.text = "${dis2} Km"*/
            holder.mView.tvPropertyType.text = "${item.flatDetail!!.flatStatus}"
            holder.mView.tvPropertyType1.text = "${item.propertyType}"
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}