package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularVisitorGateKeeperList
import com.application.intercom.databinding.CompletedRegularEntryHisItemBinding
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistroyDetails.RegularEntryHistoryDetailsActivity
import com.application.intercom.helper.setnewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class CompletedRegularEntryHisAdapter(
    val con: Context,
    val list: ArrayList<RegularVisitorGateKeeperList.Data.Result>
) :
    RecyclerView.Adapter<CompletedRegularEntryHisAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CompletedRegularEntryHisItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            CompletedRegularEntryHisItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.tvComletedInTime.text = list[position].fromTime
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType}"
        val date = setnewFormatDate(list[position].fromDate)
        holder.mView.tvComletedInTime.text = "${list[position].fromTime},${date}"
        val todate = setnewFormatDate(list[position].toDate)
        holder.mView.tvComletedOutTime.text = "${list[position].toTime},${todate}"

        holder.itemView.setOnClickListener {
            con.startActivity(
                Intent(
                    con,
                    RegularEntryHistoryDetailsActivity::class.java
                ).putExtra("from", "completed").putExtra("visitorId", list[position]._id)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}