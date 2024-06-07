package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularVisitorGateKeeperList
import com.application.intercom.databinding.OngoingRegularEntryHisItemBinding
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistroyDetails.RegularEntryHistoryDetailsActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntry.RegularEntryAdapter
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.helper.setnewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class OngoingRegularEntryHisAdapter(
    val con: Context,
    val list: ArrayList<RegularVisitorGateKeeperList.Data.Result>,
    val onPress: RegularEntryAdapter.RegularClick
) :
    RecyclerView.Adapter<OngoingRegularEntryHisAdapter.MyViewHOlder>() {
    class MyViewHOlder(val mView: OngoingRegularEntryHisItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHOlder {
        return MyViewHOlder(
            OngoingRegularEntryHisItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHOlder,
        position: Int
    ) {
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType}"
        val date = setnewFormatDate(list[position].fromDate)
        holder.mView.tvComletedInDate.text = date
        if (!list[position].currentStatus.isNullOrEmpty()) {
            if (list[position].currentStatus.equals("In")) {
                holder.mView.tvPayNow.text = con.getString(R.string.out_1)
                holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#FF0000"))
                holder.mView.tvComletedInTime.text = list[position].entryTime
                holder.mView.textView1785.text = con.getString(R.string.last_in)
            } else {
                holder.mView.tvPayNow.text = con.getString(R.string.in_1)
                holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#4FBF67"))
                holder.mView.tvComletedInTime.text = list[position].exitTime
                holder.mView.textView1785.text = con.getString(R.string.last_out)
            }
        } else {
            holder.mView.textView1785.text = con.getString(R.string.last_out)
            holder.mView.tvComletedInTime.visibility = View.INVISIBLE

        }
        holder.mView.tvPayNow.setOnClickListener {
            if (!list[position].currentStatus.isNullOrEmpty()) {
                if (list[position].currentStatus.equals("Out")) {
                    onPress.onAddEntry(list[position].flatId, list[position]._id)
                } else {
                    onPress.onOutEntry(list[position]._id)
                }
            } else {
                onPress.onAddEntry(list[position].flatId, list[position]._id)
            }

        }

        holder.itemView.setOnClickListener {
            con.startActivity(
                Intent(
                    con,
                    RegularEntryHistoryDetailsActivity::class.java
                ).putExtra("visitorId", list[position]._id)
            )
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface RegularClick {
        fun onClick(position: Int)
        fun onAddEntry(flatId: String, visitorId: String)
        fun onOutEntry(visitorId: String)
    }
}