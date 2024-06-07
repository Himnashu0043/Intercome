package com.application.intercom.owner.adapter.ownerHome

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.databinding.TenantMycommunityItemsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.owner.activity.showImgSlider.ShowImgSliderActivity
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.SessionConstants
import com.application.intercom.utils.loadImagesWithGlideExt
import kotlin.math.abs


class OwnerPostingAdapter(
    val con: Context,
    val list: ArrayList<OwnerCommunityListRes.Data>,
    val onPress: Click,
    val onPressChild: CommunityImgAdapter.ClickImg
) :
    RecyclerView.Adapter<OwnerPostingAdapter.MyViewHolder>() {
    private lateinit var adapterItem: CommunityImgAdapter
    private val viewPool = RecyclerView.RecycledViewPool()
    var userId =
        prefs.getString(SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString())

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
        val date = setFormatDate(list[position].createdAt)
        holder.mView.textView63.text = list[position].userId.fullName
        holder.mView.textView64.text = date
        holder.mView.textView65.text = list[position].userId.role
        holder.mView.textView66.text = list[position].description
        holder.mView.tvLike.text = list[position].likesCount.toString()
        holder.mView.tvComment.text = list[position].commentsCount.toString()
        holder.mView.textView67.text = list[position].totalViews.toString()
        holder.mView.imageView31.loadImagesWithGlideExt(list[position].userId.profilePic)
//        holder.mView.ivcmmt.setOnClickListener {
//            onPress.onPostCLick(position, list[position]._id)
//        }
        holder.mView.ivcmmt.setOnClickListener {
            if (list[position].userId._id.equals(userId)) {
                onPress.onPostCLick(position, list[position]._id, true, list[position].flatId.name)
            } else {
                onPress.onPostCLick(position, list[position]._id, false,list[position].flatId.name)

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
                onPress.isLike(
                    list[position].userId._id,
                    list[position]._id,
                    "dislike",
                    holder.absoluteAdapterPosition,
                    false,
                    list[position].flatId.name
                )
            } else {
                holder.mView.tvLike.text = "${count++} "
                onPress.isLike(
                    list[position].userId._id,
                    list[position]._id,
                    "like",
                    holder.absoluteAdapterPosition,
                    true,
                    list[position].flatId.name
                )

            }

        }
        adapterItem =
            CommunityImgAdapter(
                con,
                list[position].file.getImageList(), onPressChild
            ){
                con.startActivity(
                    Intent(con, ShowImgSliderActivity::class.java).putExtra(
                        "img_url",
                        list[position].file
                    ).putExtra("key", "community")
                )
            }

        holder.mView.rcyCommunity.apply {
            adapter = adapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            //adapterItem?.notifiyData(list)
            setRecycledViewPool(viewPool)

        }

        if (userId.equals(list[position].userId._id)) {
            holder.mView.imageView32.visibility = View.INVISIBLE
        } else {
            holder.mView.imageView32.visibility = View.VISIBLE
            holder.mView.imageView32.setOnClickListener {
                if (!list[position].flatId.tenantId.isNullOrEmpty()) {
                    var role = "tenant"
                    con.startActivity(
                        Intent(
                            con,
                            ChatDetailsActivity::class.java
                        ).putExtra("key", "owner_home_community").putExtra("from", role)
                            .putExtra("home_communityList", list[position])
                    )
                } else {
                    var role = "owner"
                    con.startActivity(
                        Intent(
                            con,
                            ChatDetailsActivity::class.java
                        ).putExtra("key", "owner_home_community").putExtra("from", role)
                            .putExtra("home_communityList", list[position])
                    )
                }

            }
        }
//        holder.mView.imageView35.setOnClickListener {
//            onPress.viewDetailCommunityOwner(list[position]._id)
//
//        }

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

//    data class ImageModels(val image: String, val remainingCount: Int = 0)

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onPostCLick(position: Int, id: String, postStatus: Boolean, flatName: String)
        fun isLike(
            likedBy: String,
            postBy: String,
            status: String,
            position: Int,
            myLikeStatus: Boolean,
            flatName: String
        )

        fun viewDetailCommunityOwner(postId: String)
    }
}