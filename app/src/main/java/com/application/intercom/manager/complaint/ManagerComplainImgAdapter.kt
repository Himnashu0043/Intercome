package com.application.intercom.manager.complaint

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.databinding.ManagerComplainImgItemsBinding
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.loadImagesWithGlideExt

class ManagerComplainImgAdapter(
    val con: Context,
    val list: ArrayList<String>,
    val key: String,
    val onPress: CommunityImgAdapter.ClickImg
) :
    RecyclerView.Adapter<ManagerComplainImgAdapter.MyViewHolder>() {

    class MyViewHolder(val mView: ManagerComplainImgItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ManagerComplainImgItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (key.equals("pending")) {
            if (!list[position].isNullOrEmpty()) {
                list[position].let { holder.mView.ivOne.loadImagesWithGlideExt(it) }
            } else {
                holder.mView.ivOne.visibility = View.GONE
            }
        } else if (key.equals("resolve")) {
            if (!list[position].isNullOrEmpty()) {
                list[position].let { holder.mView.ivOne.loadImagesWithGlideExt(it) }
            } else {
                holder.mView.ivOne.visibility = View.GONE
            }
        } else if (key.equals("resolved")) {
            if (!list[position].isNullOrEmpty()) {
                list[position].let { holder.mView.ivOne.loadImagesWithGlideExt(it) }
            } else {
                holder.mView.ivOne.visibility = View.GONE
            }
        }
        holder.mView.ivOne.setOnClickListener {
            onPress.showImg(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size

    }
}