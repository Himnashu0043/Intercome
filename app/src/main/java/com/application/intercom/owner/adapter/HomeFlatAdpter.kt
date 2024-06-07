package com.application.intercom.owner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerDetailsRes
import com.application.intercom.databinding.HomeFlatItemBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.SessionConstants

class HomeFlatAdpter(
    val con: Context,
    val list: ArrayList<OwnerDetailsRes.Data.UserData>,
    val onPress: HomeFlat

    ) :
    RecyclerView.Adapter<HomeFlatAdpter.MyViewHolder>() {
    var selectdpostion = 0
    var userDataid: String = ""

    class MyViewHolder(val mView: HomeFlatItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(HomeFlatItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        userDataid = prefs.getString(
            SessionConstants.USERDATAID,
            GPSService.mLastLocation?.latitude.toString()
        )
        println("----useradpetrId$userDataid")
        var isHome = false
        list.forEach {
            if (it.is_home){
                isHome = true
            }
        }
        if (!isHome) {
            holder.mView.radioButton.isChecked = false
        } else if (userDataid == list[position]._id) {
            holder.mView.radioButton.isChecked = true
        } else {
            holder.mView.radioButton.isChecked = false
        }
        holder.mView.radioButton.setOnClickListener {
//            selectdpostion = holder.absoluteAdapterPosition
            onPress.onClickFlat(
                list[position].name ?: "",
                list[position].buildingId.projectId._id,
                selectdpostion,
                list[position]._id,
                list[position].buildingId._id
            )
            prefs.put(SessionConstants.USERDATAID, list[holder.absoluteAdapterPosition]._id)
            notifyDataSetChanged()
        }
        holder.mView.radioButton.text =
            "${list[position].buildingId.buildingName} (${
                list[position].name
            })"
        println("----List1$list")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface HomeFlat {
        fun onClickFlat(
            name: String,
            projectgetId: String,
            seletedPostion: Int,
            flatId: String,
            buildingId: String
        )
    }
}