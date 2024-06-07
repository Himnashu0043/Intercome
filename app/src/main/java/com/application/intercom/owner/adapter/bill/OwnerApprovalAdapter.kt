package com.application.intercom.owner.adapter.bill

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.databinding.HeaderItemsBinding

class OwnerApprovalAdapter(
    val con: Context,
    val list: ArrayList<OwnerUnPaidBillListRes.Data.Result>
) :
    RecyclerView.Adapter<OwnerApprovalAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: HeaderItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(HeaderItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }
}