package com.application.intercom.tenant.activity.MyCommunity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.tenant.tenantSide.getAllMember.GetAllMemberListRes
import com.application.intercom.databinding.GetallMemberItemBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.utils.SessionConstants
import com.application.intercom.utils.loadImagesWithGlideExt

class GetAllMemberListAdapter(val con: Context, var list: ArrayList<GetAllMemberListRes.Data>) :
    RecyclerView.Adapter<GetAllMemberListAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: GetallMemberItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            GetallMemberItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    var role = ""
    var ownerId = prefs.getString(
        SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString()
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!list[position].tenatInfo.isNullOrEmpty()) {
            holder.mView.ivimg.loadImagesWithGlideExt(
                list[position].tenatInfo?.get(0)!!.profilePic ?: ""
            )
            holder.mView.tvName.text = list[position].tenatInfo?.get(0)!!.fullName ?: ""
            holder.mView.tvBlName.text = list[position].buildingDetail?.get(0)?.buildingName ?: ""
            holder.mView.tvFlatName.text = list[position].name ?: ""
            holder.mView.tvTenant.text = con.getString(R.string.tenant)
            role = list[position].tenatInfo?.get(0)?.role ?: ""
            if (ownerId.equals(list[position].tenatInfo?.get(0)?._id ?: "")) {
                holder.mView.ivChat.visibility = View.INVISIBLE
            }
        } else {
            holder.mView.ivimg.loadImagesWithGlideExt(
                list[position].ownerInfo?.get(0)!!.profilePic ?: ""
            )
            holder.mView.tvName.text = list[position].ownerInfo?.get(0)!!.fullName ?: ""
            holder.mView.tvBlName.text = list[position].buildingDetail?.get(0)?.buildingName ?: ""
            holder.mView.tvFlatName.text = list[position].name ?: ""
            holder.mView.tvTenant.text = con.getString(R.string.owner)
            role = list[position].ownerInfo?.get(0)?.role ?: ""
            if (ownerId.equals(list[position].ownerInfo?.get(0)?._id ?: "")) {
                holder.mView.ivChat.visibility = View.INVISIBLE
            }
        }


        holder.mView.ivChat.setOnClickListener {
            con.startActivity(
                Intent(
                    con,
                    ChatDetailsActivity::class.java
                ).putExtra("key", "get_all_member_community")
                   /* .putExtra("from", if (role.isNotEmpty()) role else "owner")*/
                    .putExtra("get_all_member_communityList", list[position])
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}