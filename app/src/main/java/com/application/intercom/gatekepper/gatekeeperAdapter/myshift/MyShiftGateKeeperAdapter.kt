package com.application.intercom.gatekepper.gatekeeperAdapter.myshift

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperProfileList
import com.application.intercom.databinding.TenantGateKeeperItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class MyShiftGateKeeperAdapter(val con: Context, val list: ArrayList<GateKeeperProfileList.Data>) :
    RecyclerView.Adapter<MyShiftGateKeeperAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantGateKeeperItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantGateKeeperItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView60.text = list[position].fullName
        holder.mView.textView61.text = list[position].phoneNumber
        holder.mView.imageView27.loadImagesWithGlideExt(list[position].profilePic)
        holder.mView.textView62.text =
            "${con.getString(R.string.shift_timing)} ${list[position].shiftStart} ${con.getString(R.string.to)} ${list[position].shiftEnd}"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}