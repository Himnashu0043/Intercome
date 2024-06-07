package com.application.intercom.manager.newFlow.balancesheet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.databinding.DueReportItemsBinding

class BalanceDueReportAdapter(
    val con: Context,
    var list: ArrayList<DueReportManagerList.Data.Result>
) :
    RecyclerView.Adapter<BalanceDueReportAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: DueReportItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            DueReportItemsBinding.inflate(
                LayoutInflater.from(
                    con
                ), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.imageView148.visibility = View.INVISIBLE

        holder.mView.tvViewDetails.setOnClickListener {
            con.startActivity(
                Intent(con, BalanceFlatActivity::class.java).putExtra(
                    "from",
                    "listData"
                ).putExtra("list", list[position].newfieldname)
            )
        }
        if (!list[position].newfieldname?.get(0)?.flatInfo.isNullOrEmpty()) {
            holder.mView.textView255.text =
                list[position].newfieldname?.get(0)?.flatInfo?.get(0)?.name ?: ""
        }

        var amount: Int = 0
        for (amt in list[position].newfieldname!!) {
            amount += (amt.amount ?: 0)
        }
        holder.mView.textView2591.text = "৳$amount"
        if (list[position].newfieldname?.get(0)?.ownerInfo?.size ?: 0 > 0) {
            holder.mView.textView268.text =
                list[position].newfieldname?.get(0)?.ownerInfo?.first()?.fullName ?: ""
        } else {
            holder.mView.textView268.text =
                list[position].newfieldname?.get(0)?.tenantInfo?.first()?.fullName ?: ""
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}