package com.application.intercom.user.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.local.localModel.LocalAmentitiesModel
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.databinding.ItemHomePropertiesBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class HomePropertyAdapter(
    private val context: Context,
    val list: ArrayList<PropertyLisRes.Data>,
    val onPress: ClickProperty
) : RecyclerView.Adapter<HomePropertyAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomePropertiesBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomePropertiesBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
/*        val item = list[position]
        for(i in item.content?.indices!!){
            holder.mView.visaName.text = item.content?.get(i)?.title
            holder.mView.visaDescription.text = item.content?.get(i)?.description
            holder.mView.visaLink.text = item.content?.get(i)?.websiteLink
            holder.mView.visaLink.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.content?.get(i)?.websiteLink))
                context.startActivity(browserIntent)
            }
        }

        holder.mView.ivBookmark.background = ContextCompat.getDrawable(context,R.drawable.ic_star_filled)
//        pankaj
        holder.mView.ivBookmark.visibility = View.GONE*/
        val item = list[position]
        println("iiii${item.photos}")
        if (list[position].flatInfo != null) {
            holder.mView.ivProperty.loadImagesWithGlideExt(list[position].flatInfo!!.photo.get(0))
        }
//        if (item.flatInfo == null) {
//            //holder.mView.ivProperty.loadImagesWithGlideExt(item.photos.get(0))
//        } else {
//            holder.mView.ivProperty.loadImagesWithGlideExt(item.flatInfo!!.photo.get(0))
//        }

        val disee: Double = /*item.distance / 1000*/0.0
       /* val dis2: Double = String.format("%.1f", disee).toDouble()*/
        holder.mView.tvPropertyName.text = if (item.flatInfo == null) {
            item.title
        } else {
            item.buildingName
        }
        val dis = "${item.district} , ${item.division}"
        holder.mView.tvLocation.text = "${item.district} ${item.division}"
        holder.mView.tvBedroom.text = if (item.flatDetail == null) {
            "${item.bedroom} ${context.getString(R.string.bedroom)}"
        } else {
            "${item.flatDetail!!.bedroom} ${context.getString(R.string.bedroom)}"
        }

        holder.mView.tvPropertyPrice.text = if (item.flatInfo == null) {
            "৳${item.price}"
        } else {
            "৳${item.flatInfo!!.price}"
        }
        holder.itemView.setOnClickListener {
            onPress.onCLickProperty(
                list[position],
                position,
                if (!item.buildingName.isNullOrEmpty()) {
                    item.buildingName
                } else {
                    item.title
                },
                if (item.district != null) {
                    dis
                } else {
                    item.address
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
                /*dis2.toString()*/0.0.toString(),
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
                } else {
                    item.photos
                },
                if (item.flatInfo != null) {
                    item.flatInfo!!.amentities
                } else {
                    item.amentities
                },
                item.latitude,
                item.longitude,
                item.addedBy,
                if (item.flatDetail == null) {
                    item.flatStatus
                } else {
                    item.flatDetail!!.flatStatus
                },
                item.propertyType ?: ""
            )
//            context.startActivity(Intent(context, PropertyDetailsActivity::class.java))
//            context.startActivity(
//                Intent(
//                    context,
//                    CompleteParkingToLetDetailsActivity::class.java
//                ).putExtra("from", key).putExtra("sendList", sendList)
//            )

        }
        if (item.isWishList) {
            holder.mView.favImg.setImageResource(R.drawable.fav_active_icon)
        } else {
            holder.mView.favImg.setImageResource(R.drawable.unfav)
        }
        holder.mView.favImg.setOnClickListener {
            updatelist(position)
            onPress.addFavLstnr(if (item.flatInfo == null) item._id else item.flatInfo!!.flatId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifiyData(list: ArrayList<PropertyLisRes.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private fun updatelist(pos: Int) {
        for (i in list.indices) {
            if (i == pos)
                list.get(i).isWishList = true
            else
                list.get(i).isWishList = false
        }
        notifyDataSetChanged()
    }

    interface ClickProperty {
        fun onCLickProperty(
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
            amList: java.util.ArrayList<PropertyLisRes.Data.Amentity>,
            lati: Double,
            longi: Double,
            addedBy: String?,
            propertyType: String,
            property_Resi: String?
        )

        fun addFavLstnr(propertyId: String)
    }
}