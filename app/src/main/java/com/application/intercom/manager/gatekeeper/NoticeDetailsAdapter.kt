package com.application.intercom.manager.gatekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.noticeBoard.ManagerNoticeBoardListRes
import com.application.intercom.databinding.NoticeDetailsItemsBinding
import com.application.intercom.owner.activity.playVideo.PlayVideoActivity
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.loadImagesWithGlideExt

class NoticeDetailsAdapter(val con: Context, val list: ArrayList<String>,val onPress: CommunityImgAdapter.ClickImg) :
    RecyclerView.Adapter<NoticeDetailsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: NoticeDetailsItemsBinding) : RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            NoticeDetailsItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        if (!data.isNullOrEmpty()) {
            if (data.contains(".mp4")) {
                holder.mView.ply.visibility = View.VISIBLE
            } else {
                holder.mView.ply.visibility = View.INVISIBLE
            }
            holder.mView.imageView14.loadImagesWithGlideExt(data)
        }
        /*holder.mView.ply.setOnClickListener {
            con.startActivity(Intent(con,PlayVideoActivity::class.java).putExtra("video_url",data))
        }*/
        holder.mView.imageView14.setOnClickListener {
            onPress.showImg(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}