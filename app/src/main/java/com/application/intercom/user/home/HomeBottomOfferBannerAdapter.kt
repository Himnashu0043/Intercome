package com.application.intercom.user.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserAdvertismentResponse
import com.application.intercom.databinding.ItemHomeOfferBannerBinding
import com.application.intercom.user.property.PropertyDetailsActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class HomeBottomOfferBannerAdapter(
    private val context: Context, val list: ArrayList<UserAdvertismentResponse.Data>
) : RecyclerView.Adapter<HomeBottomOfferBannerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomeOfferBannerBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomeOfferBannerBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
       holder.mView.ivProperty.loadImagesWithGlideExt(item.image)
        holder.itemView.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse(item.url)
            context.startActivity(openURL)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifiyData(list: ArrayList<UserAdvertismentResponse.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}