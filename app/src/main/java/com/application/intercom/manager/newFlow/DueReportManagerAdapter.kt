package com.application.intercom.manager.newFlow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.DueReportManagerList
import com.application.intercom.databinding.DueReportItemsBinding
import com.application.intercom.manager.newFlow.balancesheet.BalanceFlatActivity

class DueReportManagerAdapter(
    val con: Context,
    var list: ArrayList<DueReportManagerList.Data.Result>
) :
    RecyclerView.Adapter<DueReportManagerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: DueReportItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(DueReportItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /* holder.mView.tvViewDetails.setOnClickListener {
             con.startActivity(Intent(con, FlatManagerActivity::class.java))
         }*/
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
        holder.mView.imageView148.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
                holder.mView.tvEdit.setOnClickListener {
                    Toast.makeText(con, "Coming soon..", Toast.LENGTH_SHORT).show()
                    holder.mView.listPop.visibility = View.GONE
                }
                holder.mView.tvInActive.setOnClickListener {
                    Toast.makeText(con, "Coming soon..", Toast.LENGTH_SHORT).show()
                    holder.mView.listPop.visibility = View.GONE
                }
                holder.mView.tvReport.setOnClickListener {
                    Toast.makeText(con, "Coming soon..", Toast.LENGTH_SHORT).show()
                    holder.mView.listPop.visibility = View.GONE
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}