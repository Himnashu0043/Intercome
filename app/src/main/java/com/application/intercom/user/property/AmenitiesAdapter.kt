package com.application.intercom.user.property

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.localModel.LocalAmentitiesModel
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.databinding.ItemAmenitiesBinding
import com.application.intercom.utils.loadImagesWithGlideExt


class AmenitiesAdapter(
    private val context: Context,
    val list: ArrayList<PropertyLisRes.Data.Amentity>,
    val from: String,
    /*val proLis: ArrayList<PropertyLisRes.Data>*/
) : RecyclerView.Adapter<AmenitiesAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemAmenitiesBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAmenitiesBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (from.equals("user_parking")) {


        } else {
            holder.mView.ivProperty.loadImagesWithGlideExt(list[position].image)
            holder.mView.tvLocation.text = list[position].name
        }
//        val item = list[position]
//        for(i in item.content?.indices!!){
//            holder.mView.visaName.text = item.content?.get(i)?.title
//            holder.mView.visaDescription.text = item.content?.get(i)?.description
//            holder.mView.visaLink.text = item.content?.get(i)?.websiteLink
//            holder.mView.visaLink.setOnClickListener {
//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.content?.get(i)?.websiteLink))
//                context.startActivity(browserIntent)
//            }
//        }
//
//        holder.mView.ivBookmark.background = ContextCompat.getDrawable(context,R.drawable.ic_star_filled)
////        pankaj
//        holder.mView.ivBookmark.visibility = View.GONE

//        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context, UserCompletePropertyDetailsActivity::class.java))
//        }
    }

    override fun getItemCount(): Int {
        /*if (from.equals("user_parking")) {
            return list.size
        } else {
            return proLis.size
        }*/
//        return list.size
        return list.size

    }
}