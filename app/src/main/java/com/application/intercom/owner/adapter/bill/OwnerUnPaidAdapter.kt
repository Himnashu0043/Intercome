package com.application.intercom.owner.adapter.bill

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.databinding.HeaderItemsBinding
import com.application.intercom.helper.*
import com.application.intercom.manager.bills.ReceiptManagerActivity
import java.lang.Math.abs
import java.text.DecimalFormat
import java.text.NumberFormat

class OwnerUnPaidAdapter(
    val con: Context,
    val list: ArrayList<OwnerUnPaidBillListRes.Data.Result>,
    var changeText: String,
    val onPresss: PaidClick,
    val timer_value: String
) : RecyclerView.Adapter<OwnerUnPaidAdapter.MyViewHOlder>() {
    class MyViewHOlder(val mView: HeaderItemsBinding) : RecyclerView.ViewHolder(mView.root) {
        fun startTimer(timeValue: Long) {
            mView.tvPayNow.visibility = View.INVISIBLE
            mView.tvTimerValue.visibility = View.VISIBLE

            object : CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    mView.tvTimerValue.text = mView.root.context.getString(
                        R.string.min_sec,
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHOlder {
        return MyViewHOlder(
            HeaderItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHOlder, position: Int) {
        println("====================$changeText")
        if (changeText.equals("paid")) {
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.ivDoc.visibility = View.VISIBLE
            holder.mView.ivList.visibility = View.VISIBLE
            holder.mView.tvRejectManager.visibility = View.GONE
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "${con.getString(R.string.due_date)}:$date"
            }
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].is_bill_type_new ?: "" != "Service") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months} - ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "- ${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }

            } else {
                /* holder.mView.textView41.text =
                     "${con.getString(R.string.service)} - ${list[position].serviceCategory.get(0).name}"*/
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            holder.mView.textView42.text = list[position].flatInfo.get(0).name
            holder.mView.textView142.text = "৳${list[position].amount}"
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
            holder.mView.ivList.setOnClickListener {
                if (holder.mView.viewReceiptlistPop.visibility == View.VISIBLE) {
                    holder.mView.textView142.visibility = View.VISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.GONE
                } else {
                    holder.mView.textView142.visibility = View.INVISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.VISIBLE
                }

            }
            holder.mView.tvViewRecient.setOnClickListener {
                holder.mView.viewReceiptlistPop.visibility = View.GONE
                onPresss.onViewReceipt(
                    list[position].referenceNo ?: "",
                    list[position].uploadDocument ?: ""
                )
            }
        } else if (changeText.equals("pending")) {
//            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.ivDoc.visibility = View.INVISIBLE
            holder.mView.tvRejectReason.visibility = View.GONE

            if (!list[position].flatInfo.isNullOrEmpty()) {
                holder.mView.textView42.text = list[position].flatInfo.get(0).name
            }
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].is_bill_type_new ?: "" != "Service") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months} - ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "- ${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getString(R.string.service_charge)
                }

            } else {
                /* holder.mView.textView41.text =
                     "${con.getString(R.string.service)} - ${list[position].serviceCategory.get(0).name}"*/
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }


            if (!list[position].flatInfo.isNullOrEmpty()) {
                holder.mView.textView42.text = list[position].flatInfo.get(0).name
            }

            holder.mView.textView142.text = "৳${list[position].amount}"
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "${con.getString(R.string.due_date)}:$date"
            }


            if (list[position].userType == "tenant") {
                if (!list[position].notifyDate.isNullOrEmpty()) {
                    holder.mView.paynow.visibility = View.INVISIBLE
                    val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
                    if (timeInMillis >= (1800000)) {
                        holder.mView.tvPayNow.text = con.getString(R.string.notify)
                        holder.mView.paynow.visibility = View.VISIBLE
                        holder.mView.tvTimerValue.visibility = View.INVISIBLE
                    } else {
                        holder.mView.paynow.visibility = View.INVISIBLE
                        holder.mView.tvTimerValue.visibility = View.VISIBLE
                        holder.startTimer(abs(1800000 - timeInMillis))
                    }
                } else {
                    holder.mView.tvPayNow.text = con.getString(R.string.notify)
                    holder.mView.paynow.visibility = View.VISIBLE
                    holder.mView.tvTimerValue.visibility = View.INVISIBLE
                    holder.mView.paynow.setOnClickListener {
                        onPresss.onOwnertoTenantNotify(list[position]._id, position)
                    }
                }
                /*holder.mView.paynow.visibility = View.INVISIBLE*/
            } else if (!list[position].notifyDate.isNullOrEmpty()) {
                holder.mView.paynow.visibility = View.INVISIBLE
                val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
                if (timeInMillis >= (1800000)) {
                    holder.mView.tvPayNow.text = con.getString(R.string.notify)
                    holder.mView.paynow.visibility = View.VISIBLE
                    holder.mView.tvTimerValue.visibility = View.INVISIBLE
                } else {
                    holder.mView.paynow.visibility = View.INVISIBLE
                    holder.mView.tvTimerValue.visibility = View.VISIBLE
                    holder.startTimer(abs(1800000 - timeInMillis))
                }
            } else {
                holder.mView.tvPayNow.text = con.getString(R.string.notify)
                holder.mView.paynow.visibility = View.VISIBLE
                holder.mView.tvTimerValue.visibility = View.INVISIBLE
                holder.mView.tvPayNow.setOnClickListener {
                    onPresss.onNotifyOwner(list[position]._id, position)
                }
            }

            /*if (!list[position].notifyDate.isNullOrEmpty()) {
                holder.mView.paynow.visibility = View.INVISIBLE

                checkUpdatedAt(list[position].updatedAt)
                val timeInMillis = getTimeDifferenceFromCurrentTime(list[position].notifyDate!!)
                if (timeInMillis > (1800000)) {
                    holder.mView.tvPayNow.text = "Notify"
                    holder.mView.paynow.visibility = View.VISIBLE
                    holder.mView.tvTimerValue.visibility = View.INVISIBLE
                    Log.d("startTimerForPendings", "bigger than 30 mins $position")
                } else {
                    Log.d("startTimerForPendings", "start count down timer $position")
                    holder.mView.paynow.visibility = View.INVISIBLE
                    holder.mView.tvTimerValue.visibility = View.VISIBLE
                    startCountDownTimer(holder, timeInMillis)
                }
            } else {
                Log.d("startTimerForPendings", "notify date is empty $position")
                holder.mView.tvPayNow.text = "Notify"
                holder.mView.paynow.visibility = View.VISIBLE
                holder.mView.tvTimerValue.visibility = View.INVISIBLE
            }*/


        } else if (changeText.equals("approval")) {
            holder.mView.paynow.visibility = View.VISIBLE
            holder.mView.ivDoc.visibility = View.INVISIBLE
            holder.mView.ivList.visibility = View.VISIBLE
            holder.mView.tvPayNow.text = con.getString(R.string.mark_as_paid)
            /* holder.mView.tvRejectManager.visibility = View.GONE*/
            if (!list[position].date.isNullOrEmpty()) {
                val date = setNewFormatDate(list[position].date)
                holder.mView.textView43.text = "${con.getString(R.string.due_date)} :$date"

            }
            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].is_bill_type_new ?: "" != "Service") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months}-${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "-${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getText(R.string.service_charge)
                }
            } else {
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
                /*holder.mView.textView41.text = "${months}-${list[position].billType}"*/
            }
            if (!list[position].flatInfo.isNullOrEmpty()) {
                holder.mView.textView42.text = list[position].flatInfo.get(0).name
            }

            holder.mView.textView142.text = "৳${list[position].amount}"

            holder.mView.tvPayNow.setOnClickListener {
                onPresss.onOwnerClick(list[position]._id, position)
            }
            holder.mView.ivList.setOnClickListener {
                if (holder.mView.viewReceiptlistPop.visibility == View.VISIBLE) {
                    holder.mView.textView142.visibility = View.VISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.GONE
                    holder.mView.paynow.visibility = View.VISIBLE
                } else {
                    holder.mView.textView142.visibility = View.INVISIBLE
                    holder.mView.viewReceiptlistPop.visibility = View.VISIBLE
                    holder.mView.paynow.visibility = View.INVISIBLE
                }
            }
            holder.mView.tvViewRecient.setOnClickListener {
                holder.mView.viewReceiptlistPop.visibility = View.GONE
                onPresss.onViewReceipt(
                    list[position].referenceNo ?: "", list[position].uploadDocument ?: ""
                )
            }
            holder.mView.tvRejectManager.setOnClickListener {
                holder.mView.viewReceiptlistPop.visibility = View.GONE
                onPresss.onReject(
                    list[position]._id ?: ""
                )
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
            holder.mView.textView142.text = "৳${list[position].amount}"
            if (!list[position].dueDate.isNullOrEmpty()) {
                val date = setFormatDate(list[position].dueDate)
                holder.mView.textView43.text = "${con.getString(R.string.due_date)} : $date"
            }

            /* if (!list[position].date.isNullOrEmpty()) {

             }*/

            if (list[position].serviceCategory.isEmpty()) {
                if (list[position].is_bill_type_new ?: "" != "Service") {
                    if (!list[position].date.isNullOrEmpty()) {
                        val months = getYearMonthOfDate(list[position].date)
                        holder.mView.textView41.text = "${months} - ${list[position].billType}"
                    } else {
                        holder.mView.textView41.text = "- ${list[position].billType}"
                    }

                } else {
                    holder.mView.textView41.text = con.getText(R.string.service_charge)
                }

            } else {
                /* holder.mView.textView41.text =
                     "${con.getString(R.string.service)} - ${list[position].serviceCategory.get(0).name}"*/
                holder.mView.textView41.text =
                    "${list[position].serviceCategory.get(0).name}"
            }
            if (!list[position].flatInfo.isNullOrEmpty()) {
                holder.mView.textView42.text = list[position].flatInfo.get(0).name
            }
            holder.mView.tvPayNow.setOnClickListener {
                onPresss.onOwnerClick(list[position]._id, position)
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PaidClick {
        fun onOwnerClick(id: String, position: Int)
        fun onNotifyOwner(id: String, position: Int)
        fun onViewReceipt(refno: String, uploadDocument: String)
        fun onReject(billID: String)
        fun onOwnertoTenantNotify(billID: String, position: Int)
    }
}