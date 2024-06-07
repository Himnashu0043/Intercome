package com.application.intercom.tenant.adapter.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserAdvertimentNewResponse
import com.application.intercom.data.model.remote.UserAdvertismentResponse
import com.application.intercom.databinding.TenantHomeOfferItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt


class TenantHomeOfferAdapter(val con: Context, val list: ArrayList<UserAdvertimentNewResponse.Data.TopUser>) :
    RecyclerView.Adapter<TenantHomeOfferAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantHomeOfferItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantHomeOfferItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
      /*  holder.mView.imageView46.loadImagesWithGlideExt(item.advertisementData[position].image)*/
        holder.itemView.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(item.advertisementData[position].url)
            con.startActivity(openURL)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}