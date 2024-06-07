package com.application.intercom.owner.adapter.ownerHome

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.RecentlyItemsBinding
import com.application.intercom.tenant.Model.HomeExtraModal
import com.application.intercom.tenant.adapter.home.TenantHomeExtraAdapter

class OwnerExtraAdapter(
    val con: Context,
    val onPress: ExtraClick,
    val extraList: ArrayList<HomeExtraModal>
) : RecyclerView.Adapter<OwnerExtraAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: RecentlyItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(RecentlyItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView13.text = extraList[position].name
        holder.mView.ivServiceIcon.setImageResource(extraList[position].img)
        holder.mView.lay.setOnClickListener {
            onPress.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return extraList.size
    }

    interface ExtraClick {
        fun onClick(position: Int)
    }
}