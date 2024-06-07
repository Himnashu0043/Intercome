package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntry

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.FlatListOfVisitorGateKeeperList
import com.application.intercom.databinding.RegularEntryItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class RegularEntryAdapter(
    val con: Context,
    val onPress: RegularClick,
    val list: ArrayList<FlatListOfVisitorGateKeeperList.Data.Result>,
    val flatName: String
) :
    RecyclerView.Adapter<RegularEntryAdapter.MyViewHolde>() {
    class MyViewHolde(val mView: RegularEntryItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolde {
        return MyViewHolde(RegularEntryItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolde, position: Int) {
        holder.mView.tvPayNow.setOnClickListener {
            if (!list[position].currentStatus.isNullOrEmpty()) {
                if (list[position].currentStatus.equals("Out")) {
                    onPress.onAddEntry(list[position].flatId, list[position]._id)
                } else {
                    onPress.onOutEntry(list[position]._id)
                }
            }
        }
        val toDate = setNewFormatDate(list[position].toDate)
        if (list[position].currentStatus == "In") {
            holder.mView.tvPayNow.text = con.getString(R.string.out_1)
            holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#FF0000"))
            holder.mView.tvComletedOutTime.text = "${list[position].entryTime ?: ""} ${toDate}"
            holder.mView.textView17851.text = "${con.getString(R.string.last_in)}"

        } else {
            holder.mView.tvPayNow.text = con.getString(R.string.in_1)
            holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#4FBF67"))
            holder.mView.tvComletedOutTime.text = "${list[position].exitTime ?: ""} ${toDate}"
            holder.mView.textView17851.text = "${con.getString(R.string.last_out)}"

        }
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber

//        holder.mView.tvComletedOutTime.text = "${list[position].toTime} , ${toDate}"
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${con.getString(R.string.regular_entry)}"
        holder.mView.textView178.text = flatName
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.itemView.setOnClickListener {
            onPress.onClick1(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface RegularClick {
        fun onClick1(position: Int)
        fun onAddEntry(flatId: String, visitorId: String)
        fun onOutEntry(visitorId: String)
    }
}