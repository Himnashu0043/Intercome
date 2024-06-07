package com.application.intercom.manager.bills

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.local.NameIdModel
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.databinding.ManagerFlatsItemsBinding

class FlatBillManagerAdapter(
    val con: Context,
    var list: ArrayList<ManagerPropertyListRes.Data>,
    val onPress: CLickFlatManager
) : RecyclerView.Adapter<FlatBillManagerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ManagerFlatsItemsBinding) : RecyclerView.ViewHolder(mView.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ManagerFlatsItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView225.text = list[position].name
        if (list[position].isSelected) {
            holder.mView.amLay.setBackgroundResource(R.drawable.oragne_strock_with_white_bg)
            holder.mView.textView225.setTextColor(con.getColor(R.color.black))
        } else {
            holder.mView.amLay.setBackgroundResource(R.drawable.bg_login_type)
            holder.mView.textView225.setTextColor(con.getColor(R.color.black))
        }
        holder.mView.amLay.setOnClickListener {
            list[position].isSelected = !list[position].isSelected
            onPress.onCLick(
                list[position].isSelected,
                NameIdModel(list[position]._id, list[position].name, true)
            )
            notifyDataSetChanged()
        }


    }
}

interface CLickFlatManager {
    fun onCLick(isClick: Boolean, obj: NameIdModel)
}
