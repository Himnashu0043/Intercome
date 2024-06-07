package com.application.intercom.user.property

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.databinding.NewUserPropertyMapItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class NewUserPropertyMapAdapter(
    val con: Context,
    val list: ArrayList<PropertyLisRes.Data>,
    val onPress: CLickRcyOnMap
) :
    RecyclerView.Adapter<NewUserPropertyMapAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: NewUserPropertyMapItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            NewUserPropertyMapItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
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
            holder.mView.tvDate.text = "${con.getString(R.string.date_of_regi)} :${setFormatDate(item.createdAt)}"
        } else {
            holder.mView.tvDate.text = "${con.getString(R.string.date_of_regi)} :${setFormatDate(item.createdAt)}"
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
        }
        holder.mView.mainLayMap.setOnClickListener {
            onPress.onClickMapRcy(
                list[position],
                position,
                if (!item.buildingName.isNullOrEmpty()) {
                    item.buildingName
                } else {
                    item.title
                },
                if (item.district.isNullOrEmpty()) {
                    item.address
                } else {
                    "${item.division},${item.division}"
                },
                if (item.flatInfo != null) {
                    item.flatInfo!!.price.toString()
                } else {
                    item.price.toString()
                },
                if (item.flatDetail != null) {
                    item.flatDetail!!.bedroom.toString()
                } else {
                    item.bedroom.toString()
                },
                if (item.flatDetail != null) {
                    item.flatDetail!!.sqft.toString()
                } else {
                    item.sqft.toString()
                },
                if (item.flatDetail != null) {
                    item.flatDetail!!.bathroom.toString()
                } else {
                    item.bathroom.toString()
                },
                /*dis2.toString(),*/"0.0",
                if (item.flatInfo != null) {
                    item.flatInfo!!.description
                } else {
                    item.description
                },
                if (item.flatDetail != null) {
                    item.flatDetail!!._id
                } else {
                    item._id
                },
                if (item.flatDetail != null) {
                    item.flatInfo!!.buildingId
                } else {
                    item._id
                },
                if (item.flatInfo != null) {
                    item.flatInfo!!.photo
                } else if (item.photos != null) {
                    item.photos
                } else {
                    arrayListOf()
                },
                if (item.flatInfo != null) {
                    item.flatInfo!!.amentities
                } else {
                    item.amentities
                },
                item.latitude,
                item.longitude,
                item.addedBy
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CLickRcyOnMap {
        fun onClickMapRcy(
            msg: PropertyLisRes.Data,
            position: Int,
            buildingName: String,
            dis: String,
            price: String,
            bedRoom: String,
            ft: String,
            bathRoom: String,
            disT2: String,
            discrption: String,
            flatDetailsId: String,
            buildingId: String,
            photoList: ArrayList<String>,
            amList: ArrayList<PropertyLisRes.Data.Amentity>,
            lati: Double,
            longi: Double,
            addedBy:String?
        )
    }
}