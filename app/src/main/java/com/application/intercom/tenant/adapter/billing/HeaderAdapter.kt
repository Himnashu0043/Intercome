package com.application.intercom.tenant.adapter.billing

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.databinding.HeaderItemsBinding
import com.application.intercom.helper.getYearMonthOfDate
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.bills.ReceiptManagerActivity
import java.lang.Math.abs
import java.text.DecimalFormat
import java.text.NumberFormat


class HeaderAdapter(
    val con: Context,
    var changeText: String,
    val list: ArrayList<TenantUnPaidListRes.Data.Result>,
    val onPresss: PaidClick
) :
    RecyclerView.Adapter<HeaderAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: HeaderItemsBinding) : RecyclerView.ViewHolder(mView.root) {
        fun startTimer(timeValue: Long) {
            mView.tvPayNow.visibility = View.INVISIBLE
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
                        mView.tvPayNow.visibility = View.VISIBLE
                        mView.tvPayNow.text = mView.root.context.getString(R.string.notify)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(HeaderItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (changeText.equals("paid")) {
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.ivDoc.visibility = View.VISIBLE
            holder.mView.textView42.text = list[position].flatInfo.get(0).name
            holder.mView.textView142.text = "৳${list[position].amount}"
            holder.mView.tvRejectReason.visibility = View.GONE
            holder.mView.ivList.visibility = View.VISIBLE
            holder.mView.ivList.setOnClickListener {
                if (holder.mView.viewReceiptlistPop.visibility == View.VISIBLE) {
                    holder.mView.textView142.visibility = View.VISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.GONE
                } else {
                    holder.mView.textView142.visibility = View.INVISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.VISIBLE
                    holder.mView.tvRejectManager.visibility = View.GONE
                }

            }
            holder.mView.tvViewRecient.setOnClickListener {
                holder.mView.viewReceiptlistPop.visibility = View.GONE
                onPresss.onViewReceiptTenant(
                    list[position].referenceNo ?: "",
                    list[position].uploadDocument ?: ""
                )
            }
            /*if (list[position].billType.equals("Service")) {
                if (list[position].serviceCategory.isEmpty()) {
                    holder.mView.textView41.text = "$months - ${con.getString(R.string.service_charge)}"
                } else {
                    holder.mView.textView41.text =
                        list[position].billType + " - " + list[position].serviceCategory.get(0).name
                }
            } else {
                holder.mView.textView41.text = "$months" + " - " + list[position].billType
            }*/
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].billType == "Rent") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months}  ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }
            } else {
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "Due Date :$date"
            }

            holder.mView.ivDoc.setOnClickListener {
                if (list[position].recieptLink.isNullOrEmpty()) {
                    Toast.makeText(con, "Receipt not generated yet!!", Toast.LENGTH_SHORT).show()
                } else {
                    con.startActivity(
                        Intent(
                            con,
                            ReceiptManagerActivity::class.java
                        ).putExtra("pdfUrl", list[position].recieptLink ?: "")
                    )
                }
            }
        } else if (changeText.equals("pending")) {
            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.ivDoc.visibility = View.INVISIBLE
            holder.mView.tvPayNow.text = "${con.getString(R.string.notify)} ${list[position].userType}"
            holder.mView.textView42.text = list[position].flatInfo.get(0).name
            holder.mView.textView142.text = "৳${list[position].amount}"
            holder.mView.tvRejectReason.visibility = View.GONE
            /*if (list[position].billType.equals("Service")) {
                if (list[position].serviceCategory.isEmpty()) {
                    holder.mView.textView41.text = "$months - ${con.getString(R.string.service_charge)}"
                } else {
                    holder.mView.textView41.text =
                        list[position].billType + " - " + list[position].serviceCategory.get(0).name
                }
            } else {
                holder.mView.textView41.text = "$months" + list[position].billType
            }*/
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].billType == "Rent") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months}  ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }
            } else {
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "Due Date :$date"
            }
            if (!list[position].notifyDate.isNullOrEmpty()) {
                holder.mView.paynow.visibility = View.INVISIBLE
                val timeInMillis =
                    com.application.intercom.helper.getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
                if (timeInMillis >= (1800000)) {
//                    holder.mView.tvPayNow.text =
//                        "${con.getString(R.string.notify)} /*${list[position].userType}*/"
                    holder.mView.tvPayNow.text =
                        "${con.getString(R.string.notify)}"
                    holder.mView.paynow.visibility = View.VISIBLE
                    holder.mView.tvTimerValue.visibility = View.INVISIBLE
                } else {
                    holder.mView.paynow.visibility = View.INVISIBLE
                    holder.mView.tvTimerValue.visibility = View.VISIBLE
                    holder.startTimer(abs(1800000 - timeInMillis))
                }
            } else {
               // holder.mView.tvPayNow.text = "${con.getString(R.string.notify)} ${list[position].userType}"
                holder.mView.tvPayNow.text = "${con.getString(R.string.notify)}"
                holder.mView.paynow.visibility = View.VISIBLE
                holder.mView.tvTimerValue.visibility = View.INVISIBLE
            }
            println("-----Pending${list[position]}")
            holder.mView.tvPayNow.setOnClickListener {
                onPresss.onNotifyTenant(list[position]._id, position)
            }
        } else if (changeText.equals("approval")) {
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.ivDoc.visibility = View.INVISIBLE
            holder.mView.tvPayNow.text = con.getString(R.string.pay_now)
            //holder.mView.textView41.text = list[position].billType
            holder.mView.textView42.text = list[position].flatInfo.get(0).name
            holder.mView.textView142.text = "৳${list[position].amount}"
            holder.mView.tvRejectReason.visibility = View.GONE
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].billType == "Rent") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months}  ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }
            } else {
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "Due Date :$date"
            }
        } else {
            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.ivDoc.visibility = View.INVISIBLE
            holder.mView.tvPayNow.text = con.getString(R.string.pay)
            if (!list[position].rejectReason.isNullOrEmpty()) {
                holder.mView.tvRejectReason.visibility = View.VISIBLE
                holder.mView.tvRejectReason.text =
                    "Reject Reason : ${list[position].rejectReason ?: ""}"
            } else {
                holder.mView.tvRejectReason.visibility = View.GONE
            }

            /*  if (list[position].billType == "Service") {
                  if (list[position].serviceCategory.isEmpty()) {
                      holder.mView.textView41.text =
                          "$months - ${con.getString(R.string.service_charge)}"
                  } else {
                      holder.mView.textView41.text =
                          list[position].billType + " - " + list[position].serviceCategory.get(0).name
                  }
              } else {
                  holder.mView.textView41.text = "$months - ${list[position].billType}"
              }*/
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].billType == "Rent") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months}  ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }
            } else {
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "Due Date :$date"
            }
            if (!list[position].flatInfo.isNullOrEmpty()) {
                holder.mView.textView42.text = list[position].flatInfo.get(0).name
            }

            holder.mView.textView142.text = "৳${list[position].amount}"

            holder.mView.tvPayNow.setOnClickListener {
                onPresss.onClick(list[position]._id, position)
            }

        }

    }

    override fun getItemCount(): Int {

        return list.size
    }

    interface PaidClick {
        fun onClick(id: String, position: Int)
        fun onNotifyTenant(id: String, position: Int)
        fun onViewReceiptTenant(refno: String, uploadDocument: String)
    }
}