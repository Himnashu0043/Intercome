package com.application.intercom.user.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.databinding.ItemPropertyParkingChatBinding
import com.application.intercom.helper.getLocalTime
import com.application.intercom.helper.gmtToLocalDate
import com.application.intercom.helper.timeWithCurrentTime
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class PropertyChatListingAdapter(
    private val context: Context, val list: ArrayList<UserPropertyChatList.Data3>
) : RecyclerView.Adapter<PropertyChatListingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemPropertyParkingChatBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPropertyParkingChatBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.lay.setOnClickListener {
            context.startActivity(
                Intent(context, ChatDetailsActivity::class.java).putExtra(
                    "key",
                    "chatProperty"
                ).putExtra("chatPropertyList", list[position])
            )
        }
        holder.mView.tvProfileName.text = list[position].reciverId.fullName

        holder.mView.tvNotificationCount.text = list[position].count.toString()
        holder.mView.ivProfile.loadImagesWithGlideExt(list[position].reciverId.profilePic)
        val time = gmtToLocalDate(list[position].createdAt)
        holder.mView.tvChatTime.text = time
        println("---listChat${list[position]}")
        if (list[position].msgType != null) {
            if (list[position].msgType.equals("photo")) {
                holder.mView.tvChatMessage.text = "photo"
            } else if (list[position].msgType.equals("video")) {
                holder.mView.tvChatMessage.text = "video"
            } else {
                holder.mView.tvChatMessage.text = list[position].lastMessage
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun notifiyData(list: ArrayList<UserPropertyChatList.Data3>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}