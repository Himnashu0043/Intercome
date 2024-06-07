package com.application.intercom.tenant.adapter.myCommunity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.CommunityImgItemsBinding
import com.application.intercom.owner.activity.playVideo.PlayVideoActivity
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.utils.loadImagesWithGlideExt

class CommunityImgAdapter(
    val con: Context,
    val list: List<ImageModels>,
    val onPress: ClickImg,
    val showAllImages:()->Unit = {}
) :
    RecyclerView.Adapter<CommunityImgAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CommunityImgItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CommunityImgItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]

        if (position == 2) {
            if (currentItem.remainingCount > 0) {
                // visible view
               holder.mView.tvShowText.visibility = View.VISIBLE
               holder.mView.viewBlur.visibility = View.VISIBLE
                holder.mView.tvShowText.text = "+${currentItem.remainingCount}"
                // set count of remainingItems
            } else {
                // hide view
               holder.mView.tvShowText.visibility = View.GONE
                holder.mView.viewBlur.visibility = View.GONE
            }
        } else {
            // hide view
            holder.mView.viewBlur.visibility = View.GONE
            holder.mView.tvShowText.visibility = View.GONE
        }

        if (list[position].image.contains(".mp4")) {
            holder.mView.playImg.visibility = View.VISIBLE
        } else {
            holder.mView.playImg.visibility = View.INVISIBLE
        }
        holder.mView.img.loadImagesWithGlideExt(list[position].image)
        /*  holder.mView.img.setOnClickListener {
              onPress.showImg(list[position])
          }*/
        holder.itemView.setOnClickListener {
            showAllImages.invoke()
        }
        holder.mView.playImg.setOnClickListener {
            con.startActivity(
                Intent(con, PlayVideoActivity::class.java).putExtra(
                    "video_url",
                    list[position].image
                ).putExtra("key", "community")
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //    fun notifiyData(list: ArrayList<OwnerCommunityListRes.Data>) {
//        this.list.clear()
//        this.list.addAll(list)
//        notifyDataSetChanged()
//    }
    interface ClickImg {
        fun showImg(img: String)

    }
}