package com.application.intercom.owner.adapter.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.databinding.ItemPropertyParkingChatBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.timeWithCurrentTime
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.utils.SessionConstants
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerPropertyChatAdapter(
    val context: Context,
    val list: ArrayList<UserPropertyChatList.Data3>,
    val key: String
) :
    RecyclerView.Adapter<OwnerPropertyChatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemPropertyParkingChatBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ItemPropertyParkingChatBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    var id = prefs.getString(SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString())
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (list[position].reciverId._id.equals(id)) {
            holder.mView.tvProfileName.text = list[position].senderId.fullName
            //holder.mView.tvChatMessage.text = list[position].lastMessage
            if (list[position].msgType == "photo") {
                holder.mView.tvChatMessage.text = "photo"
            } else if (list[position].msgType == "video"
            ) {
                holder.mView.tvChatMessage.text = "video"
            } else {
                holder.mView.tvChatMessage.text = list[position].lastMessage
            }
            holder.mView.tvNotificationCount.text = list[position].count.toString()
            holder.mView.ivProfile.loadImagesWithGlideExt(list[position].senderId.profilePic)
            val time = timeWithCurrentTime(list[position].createdAt)
            holder.mView.tvChatTime.text = time
            holder.mView.lay.setOnClickListener {
                context.startActivity(
                    Intent(context, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "owner_chatProperty"
                    ).putExtra("chatPropertyList", list[position])
                )
            }
        } else {
            holder.mView.tvProfileName.text = list[position].reciverId.fullName
            if (list[position].msgType == "photo") {
                holder.mView.tvChatMessage.text = "photo"
            } else if (list[position].msgType == "video"
            ) {
                holder.mView.tvChatMessage.text = "video"
            } else {
                holder.mView.tvChatMessage.text = list[position].lastMessage
            }
            holder.mView.tvNotificationCount.text = list[position].count.toString()
            holder.mView.ivProfile.loadImagesWithGlideExt(list[position].reciverId.profilePic)
            val time = timeWithCurrentTime(list[position].createdAt)
            holder.mView.tvChatTime.text = time
            holder.mView.lay.setOnClickListener {
                context.startActivity(
                    Intent(context, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "tenant_chatProperty"
                    ).putExtra("chatPropertyList", list[position])
                )
            }

        }
        /* holder.mView.tvProfileName.text = list[position].senderId.fullName
         holder.mView.tvChatMessage.text = list[position].lastMessage
         holder.mView.tvNotificationCount.text = list[position].count.toString()
         holder.mView.ivProfile.loadImagesWithGlideExt(list[position].senderId.profilePic)
         val time = timeWithCurrentTime(list[position].createdAt)
         holder.mView.tvChatTime.text = time*/
    }

    override fun getItemCount(): Int {
        return list.size
    }
}