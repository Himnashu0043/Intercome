package com.application.intercom.tenant.adapter.myCommunity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.databinding.TenantMycommunityItemsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.chat.TenantSecondChatActivity
import com.application.intercom.tenant.activity.writePost.TenantWritePostActivity
import com.application.intercom.utils.SessionConstants
import com.application.intercom.utils.loadImagesWithGlideExt
import kotlin.math.abs

class OwnerMyPostAdapter(
    val con: Context,
    val onPres: Click,
    var from: String,
    val list: ArrayList<OwnerMyCommunityListRes.Data>,
    val onPressChild: CommunityImgAdapter.ClickImg
) :
    RecyclerView.Adapter<OwnerMyPostAdapter.MyViewHolder>() {
    private lateinit var adapterItem: CommunityImgAdapter
    private val viewPool = RecyclerView.RecycledViewPool()
    var ownerId = prefs.getString(
        SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString()
    )

    class MyViewHolder(val mView: TenantMycommunityItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantMycommunityItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("----iddd${list[position]._id}")
        if (from.equals("post")) {
            holder.mView.textView65.visibility = View.INVISIBLE
            holder.mView.imageView321.visibility = View.VISIBLE
            holder.mView.imageView32.visibility = View.INVISIBLE
        } else {
            holder.mView.textView65.visibility = View.VISIBLE
            holder.mView.imageView321.visibility = View.INVISIBLE
            holder.mView.imageView32.visibility = View.VISIBLE
        }
        val date = setFormatDate(list[position].createdAt)
        holder.mView.textView63.text = list[position].userId.fullName
        holder.mView.textView64.text = date
        holder.mView.textView65.text = list[position].userId.role
        holder.mView.textView66.text = list[position].description
        holder.mView.tvLike.text = list[position].likesCount.toString()
        holder.mView.tvComment.text = list[position].commentsCount.toString()
        holder.mView.textView67.text = list[position].totalViews.toString()
        holder.mView.imageView31.loadImagesWithGlideExt(list[position].userId.profilePic)
        holder.mView.imageView32.setOnClickListener {
            con.startActivity(Intent(con, TenantSecondChatActivity::class.java))
        }
//        holder.mView.ivcmmt.setOnClickListener {
//            onPres.onCLick(position, list[position]._id/*,true*/)
//        }
        holder.mView.ivcmmt.setOnClickListener {
            if (list[position].userId._id.equals(ownerId)) {
                onPres.onCLick(position, list[position]._id, true,list[position].flatId.name)
            } else {
                onPres.onCLick(position, list[position]._id, false,list[position].flatId.name)
            }
        }

        if (list[position].myLikeStatus) {
            holder.mView.imageView34.setImageResource(R.drawable.select_heart_icon)
        } else {
            holder.mView.imageView34.setImageResource(R.drawable.like_icon)
        }

        holder.mView.imageView34.setOnClickListener {
            var count = list[position].likesCount ?: 0
            if (list[position].myLikeStatus) {
                onPres.isLike(
                    list[position].userId._id,
                    list[position]._id,
                    "dislike",
                    holder.absoluteAdapterPosition,
                    false,
                    list[position].flatId.name
                )
            } else {
                holder.mView.tvLike.text = "${count++} "
                onPres.isLike(
                    list[position].userId._id,
                    list[position]._id,
                    "like",
                    holder.absoluteAdapterPosition,
                    true,
                    list[position].flatId.name
                )

            }

        }
        holder.mView.imageView321.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
            }

        }
        holder.mView.tvEdit.setOnClickListener {
            con.startActivity(
                Intent(con, TenantWritePostActivity::class.java).putExtra(
                    "key",
                    "edit"
                ).putExtra("edit_list", list[position])
            )
        }

        adapterItem =
            CommunityImgAdapter(
                con,
                list[position].file.getImageList(), onPressChild

            )
        holder.mView.rcyCommunity.apply {
            adapter = adapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            //adapterItem?.notifiyData(list)
            setRecycledViewPool(viewPool)

        }
        holder.mView.imageView35.setOnClickListener {
            onPres.viewDetailCommunityOwner(list[position]._id)
//            con.startActivity(
//                Intent(con, ViewPostDetailsActivity::class.java).putExtra(
//                    "postId",
//                    list[position]._id
//                )
//            )
        }
        holder.mView.imageView32.setOnClickListener {
            con.startActivity(
                Intent(
                    con,
                    ChatDetailsActivity::class.java
                ).putExtra("key", "owner_home_community")
                    .putExtra("home_communityList", list[position])
            )
        }
        holder.mView.tvInActive.setOnClickListener {
            onPres.deletePost(position, list[position]._id)
        }
    }
    fun List<String>.getImageList(): List<ImageModels> {
        val list = mutableListOf<ImageModels>()
        for (position in 0 until count()) {
            if (position == 2) {
                list.add(ImageModels(get(position), abs(count() - (position + 1))))
                break
            } else {
                list.add(ImageModels(get(position), 0))
            }
        }

        return list
    }
    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onCLick(position: Int, id: String, postStatus: Boolean,flatName: String)

        fun isLike(
            likedBy: String,
            postBy: String,
            status: String,
            position: Int,
            myLikeStatus: Boolean,
            flatName:String
        )

        fun viewDetailCommunityOwner(postId: String)
        fun deletePost(pos: Int, id: String)

    }
}