package com.application.intercom.manager.bills

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.databinding.ItemBillsToApproveBinding
import com.application.intercom.helper.getTimeDifferenceFromCurrentTime
import com.application.intercom.helper.setFormatDate
import java.text.DecimalFormat
import java.text.NumberFormat

class ManagerPendingBillsAdapter(
    private val context: Context,
    val list: ArrayList<MangerBillPendingListRes.Data.Result>,
    val onPress: ManagerUserNotify
) : RecyclerView.Adapter<ManagerPendingBillsAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemBillsToApproveBinding) : RecyclerView.ViewHolder(mView.root) {
        fun startTimer(timeValue: Long) {
            mView.tvPaidUnpaid.visibility = View.GONE
            mView.tvTimer.visibility = View.VISIBLE
            mView.ivEdit1111.visibility = View.INVISIBLE

            object : CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    mView.tvTimer.text = mView.root.context.getString(
                        com.application.intercom.R.string.min_sec,
                        f.format(min),
                        f.format(sec)
                    )
                }

                override fun onFinish() {
                    try {
                        mView.tvTimer.visibility = View.INVISIBLE
                        mView.tvPaidUnpaid.visibility = View.VISIBLE
                        mView.ivEdit1111.visibility = View.INVISIBLE
                        mView.tvPaidUnpaid.text = mView.root.context.getString(R.string.notify)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemBillsToApproveBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvPaidUnpaid.text =
            "${context.getString(R.string.notify)} ${list[position].userType}"
        holder.mView.tvTimer.visibility = View.INVISIBLE
        holder.mView.ivBillDownload.visibility = View.INVISIBLE
        holder.mView.ivEdit1111.visibility = View.INVISIBLE
        holder.mView.ivList.visibility = View.VISIBLE
        if (list[position].serviceCategory!!.isEmpty()) {
            if (list[position].is_bill_type_new ?: "" != "Service") {
                holder.mView.tvBillName.text =
                    "${list[position].billType}- ${list[position].billMonth} ${list[position].billYear}"
            } else {
                holder.mView.tvBillName.text = context.getString(R.string.service_charge)
            }
            holder.mView.tvEditBilling.visibility = View.GONE
            holder.mView.tvDeleteBilling.visibility = View.VISIBLE
        } else {
            holder.mView.tvBillName.text = list[position].serviceCategory?.get(0)?.name ?: ""

        }
        if (!list[position].flatInfo.isNullOrEmpty()) {
            holder.mView.tvFlatNumber.text = list[position].flatInfo?.get(0)?.name ?: ""
        }

        if (!list[position].dueDate.isNullOrEmpty()) {
            val date = setFormatDate(list[position].dueDate)
            holder.mView.tvDate.text = "${context.getString(R.string.due_date)} :$date"
        }

        holder.mView.tvPrice.text = "৳${list[position].amount}"
        if (!list[position].notifyDate.isNullOrEmpty()) {
            holder.mView.tvPaidUnpaid.visibility = View.INVISIBLE
            val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
            if (timeInMillis >= (1800000)) {
                holder.mView.tvPaidUnpaid.text =
                    "${context.getString(R.string.notify)}${list[position].userType}"
                holder.mView.tvPaidUnpaid.visibility = View.VISIBLE
                holder.mView.tvTimer.visibility = View.INVISIBLE
                holder.mView.ivEdit1111.visibility = View.INVISIBLE
            } else {
                holder.mView.tvPaidUnpaid.visibility = View.GONE
                holder.mView.tvTimer.visibility = View.VISIBLE
                holder.mView.ivEdit1111.visibility = View.INVISIBLE
                holder.startTimer(Math.abs(1800000 - timeInMillis))
            }
        } else {
            holder.mView.tvPaidUnpaid.text =
                "${context.getString(R.string.notify)}${list[position].userType}"
            holder.mView.tvPaidUnpaid.visibility = View.VISIBLE
            holder.mView.tvTimer.visibility = View.INVISIBLE
            holder.mView.ivEdit1111.visibility = View.INVISIBLE
        }

        holder.mView.tvPaidUnpaid.setOnClickListener {
            onPress.onClickNotify(position, list[position]._id ?: "")
        }
        holder.mView.tvPayManager.setOnClickListener {
            onPress.onClickPay(position, list[position]._id ?: "")
        }
        holder.mView.ivList.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
            }

        }
        holder.mView.tvEditBilling.setOnClickListener {
            context.startActivity(
                Intent(context, AddBillsActivity::class.java).putExtra(
                    "from",
                    "edit"
                ).putExtra("editList", list[position])
            )
        }
        holder.mView.tvDeleteBilling.setOnClickListener {
            onPress.onDelete(position, list[position]._id ?: "")
        }
        println("--------${list[position]._id ?: ""}")
    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface ManagerUserNotify {
        fun onClickNotify(position: Int, billingId: String?)
        fun onClickPay(position: Int, billingId: String?)
        fun onDelete(position: Int, billId: String?)
    }
}