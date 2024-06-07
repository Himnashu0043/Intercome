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

class OwnerParkingChatAdapter(
    val context: Context,
    val list: ArrayList<UserPropertyChatList.Data3>,
    var key: String
) :
    RecyclerView.Adapter<OwnerParkingChatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemPropertyParkingChatBinding) :
        RecyclerView.ViewHolder(mView.root)

    var id = prefs.getString(SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString())
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (list[position].reciverId._id.equals(id)) {
            holder.mView.tvProfileName.text = list[position].senderId.fullName
            holder.mView.tvChatMessage.text = list[position].lastMessage
            holder.mView.tvNotificationCount.text = list[position].count.toString()
            holder.mView.ivProfile.loadImagesWithGlideExt(list[position].senderId.profilePic)
            val time = timeWithCurrentTime(list[position].createdAt)
            holder.mView.tvChatTime.text = time
            holder.mView.lay.setOnClickListener {
                context.startActivity(
                    Intent(context, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "owner_chatParking"
                    ).putExtra("chatParkingList", list[position])
                )
            }

        } else {
            holder.mView.tvProfileName.text = list[position].reciverId.fullName
            holder.mView.tvChatMessage.text = list[position].lastMessage
            holder.mView.tvNotificationCount.text = list[position].count.toString()
            holder.mView.ivProfile.loadImagesWithGlideExt(list[position].reciverId.profilePic)
            val time = timeWithCurrentTime(list[position].createdAt)
            holder.mView.tvChatTime.text = time
            holder.mView.lay.setOnClickListener {
                context.startActivity(
                    Intent(context, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "tenant_chatParking"
                    ).putExtra("chatParkingList", list[position])
                )
            }


        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}