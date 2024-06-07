package com.application.intercom.tenant.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.databinding.TenanatHomeVisitorItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt


class TenantHomeVisitorAdapter(
    val con: Context,
    val list: ArrayList</*RegularHistoryList.Data.Result*/OwnerTenantSingleEntryHistoryList.Data>
) :
    RecyclerView.Adapter<TenantHomeVisitorAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenanatHomeVisitorItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenanatHomeVisitorItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        if (!list[position].currentStatus.isNullOrEmpty()) {
//            if (list[position].currentStatus == "In") {
//                holder.mView.onlineView.visibility = View.VISIBLE
//            } else {
//                holder.mView.onlineView.visibility = View.INVISIBLE
//            }
//           // holder.mView.onlineView.visibility = View.INVISIBLE
//
//        }
        if (list[position].visitorStatus.equals("Accept")) {
            holder.mView.onlineView.visibility = View.VISIBLE
        } else {
            holder.mView.onlineView.visibility = View.INVISIBLE
        }
        holder.mView.textView90.text = list[position].visitorName
        println("----tenant${list[position].visitorName}")
        holder.mView.imageView47.loadImagesWithGlideExt(list[position].photo)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}