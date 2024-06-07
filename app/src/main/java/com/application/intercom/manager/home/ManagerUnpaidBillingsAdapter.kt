package com.application.intercom.manager.home

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.databinding.ItemUnpaidBillingsBinding
import com.application.intercom.helper.getTimeDifferenceFromCurrentTime
import com.application.intercom.helper.setFormatDate
import java.text.DecimalFormat
import java.text.NumberFormat


class ManagerUnpaidBillingsAdapter(
    private val context: Context, val list: ArrayList<MangerBillPendingListRes.Data.Result>,
    val onPress: ManagerUserNotify
) : RecyclerView.Adapter<ManagerUnpaidBillingsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemUnpaidBillingsBinding) : RecyclerView.ViewHolder(mView.root) {
        fun startTimer(timeValue: Long) {
            mView.tvNotifyTenatOwner.visibility = View.INVISIBLE
            mView.tvTimerValue.visibility = View.VISIBLE

            object : CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    mView.tvTimerValue.text = mView.root.context.getString(
                        com.application.intercom.R.string.min_sec,
                        f.format(min),
                        f.format(sec)
                    )
                }

                override fun onFinish() {
                    try {
                        mView.tvTimerValue.visibility = View.INVISIBLE
                        mView.tvNotifyTenatOwner.visibility = View.VISIBLE
                        mView.tvNotifyTenatOwner.text = mView.root.context.getString(R.string.notify)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemUnpaidBillingsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.mView.tvRent.text = list[position].billType
        if (!list[position].flatInfo.isNullOrEmpty()) {
            holder.mView.tvFlatNo.text = list[position].flatInfo?.get(0)?.name ?: ""
        }

        val date = setFormatDate(list[position].date)
        holder.mView.tvDate.text = "${context.getString(R.string.due_date)} :$date"
        holder.mView.tvPrice.text = "৳${list[position].amount}"
        holder.mView.tvNotifyTenatOwner.text =
            "${context.getString(R.string.notify)} ${list[position].userType}"

        if (list[position].serviceCategory!!.isEmpty()) {
            if (list[position].is_bill_type_new ?: "" != "Service") {
                holder.mView.tvRent.text = "${list[position].billType}- ${list[position].billMonth} ${list[position].billYear}"
            } else {
                holder.mView.tvRent.text = context.getString(R.string.service_charge)
            }
        } else {
            holder.mView.tvRent.text = list[position].serviceCategory?.get(0)?.name ?: ""
        }

        if (!list[position].notifyDate.isNullOrEmpty()) {
            holder.mView.tvNotifyTenatOwner.visibility = View.INVISIBLE
            val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
            if (timeInMillis >= (1800000)) {
                holder.mView.tvNotifyTenatOwner.text =
                    "${context.getString(R.string.notify)} ${list[position].userType}"
                holder.mView.tvNotifyTenatOwner.visibility = View.VISIBLE
                holder.mView.tvTimerValue.visibility = View.INVISIBLE
            } else {
                holder.mView.tvNotifyTenatOwner.visibility = View.INVISIBLE
                holder.mView.tvTimerValue.visibility = View.VISIBLE
                holder.startTimer(Math.abs(1800000 - timeInMillis))
            }
        } else {
            holder.mView.tvNotifyTenatOwner.text = "${context.getString(R.string.notify)} ${list[position].userType}"
            holder.mView.tvNotifyTenatOwner.visibility = View.VISIBLE
            holder.mView.tvTimerValue.visibility = View.INVISIBLE
        }

        holder.mView.tvNotifyTenatOwner.setOnClickListener {
            onPress.onClickNotify(position, list[position]._id?:"")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ManagerUserNotify {
        fun onClickNotify(position: Int, billingId: String?)
    }
}