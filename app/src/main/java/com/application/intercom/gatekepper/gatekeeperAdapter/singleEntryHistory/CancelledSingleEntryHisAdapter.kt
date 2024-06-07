package com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.databinding.OngoingSingleEntryItemBinding
import com.application.intercom.helper.getTimeDifferenceFromCurrentTime
import com.application.intercom.utils.loadImagesWithGlideExt
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.abs

class CancelledSingleEntryHisAdapter(val con: Context, val onPress: CancelledClick,val list:ArrayList<SingleEntryHistoryList.Data.Result>) :
    RecyclerView.Adapter<CancelledSingleEntryHisAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: OngoingSingleEntryItemBinding) :
        RecyclerView.ViewHolder(mView.root){
        fun startTimer(timeValue: Long) {
            mView.paynow.visibility = View.INVISIBLE
            mView.tvTimerValue.visibility = View.VISIBLE

            object : CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    mView.tvTimerValue.text = mView.root.context.getString(
                        R.string.min_sec, f.format(min), f.format(sec)
                    )
                }

                override fun onFinish() {
                    try {
                        mView.tvTimerValue.visibility = View.INVISIBLE
                        //  mView.tvPayNow.text = mView.root.context.getString(R.string.notify)
                        mView.paynow.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            OngoingSingleEntryItemBinding.inflate(
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
        holder.mView.textView175.text = list[position].visitorName
        holder.mView.textView176.text = list[position].mobileNumber
        holder.mView.textView178.text = list[position].flatInfo.get(0).name
        holder.mView.textView177.text =
            "${list[position].visitCategoryName} | ${list[position].visitorType} ${con.getString(R.string.entry)}"
        holder.mView.imageView93.loadImagesWithGlideExt(list[position].photo)
        holder.itemView.setOnClickListener {
            onPress.onClick(position,list[position])
        }
        if (!list[position].notifyDate.isNullOrEmpty()) {
            holder.mView.paynow.visibility = View.INVISIBLE
            val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate ?: "")
            if (timeInMillis >= (300000)) {
                //holder.mView.tvPayNow.text = con.getString(R.string.notify)
                holder.mView.paynow.visibility = View.VISIBLE
                holder.mView.tvTimerValue.visibility = View.INVISIBLE
            } else {
                holder.mView.paynow.visibility = View.INVISIBLE
                holder.mView.tvTimerValue.visibility = View.VISIBLE
                holder.startTimer(abs(300000 - timeInMillis))
            }
        } else {
            // holder.mView.tvPayNow.text = con.getString(R.string.notify)
            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.tvTimerValue.visibility = View.INVISIBLE
        }
        holder.mView.paynow.setOnClickListener {
            onPress.onNotify(list[position]._id, position)
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.tvTimerValue.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CancelledClick {
        fun onClick(position: Int,msg:SingleEntryHistoryList.Data.Result)
        fun onNotify(visitorId: String, position: Int)
    }
}