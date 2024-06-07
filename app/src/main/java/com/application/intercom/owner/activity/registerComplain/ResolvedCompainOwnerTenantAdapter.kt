package com.application.intercom.owner.activity.registerComplain

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainList
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.databinding.TenantRegisterComplainItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class ResolvedCompainOwnerTenantAdapter(
    val con: Context, val list: ArrayList<OwnerRegisterComplainList.Data.Result>,
    val tenantlist: ArrayList<TenantComplainListRes.Data.Result>,
    var from: String,
    val onPressimg: CommunityImgAdapter.ClickImg
) : RecyclerView.Adapter<ResolvedCompainOwnerTenantAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: NoticeDetailsAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: TenantRegisterComplainItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TenantRegisterComplainItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (from == "tenant") {
            return tenantlist.size
        } else {
            return list.size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (from == "tenant") {

            holder.mView.tvComplain.text =
                "#${tenantlist[position].compId}"
//            holder.mView.tvServiceName.text =
//                "${tenantlist[position].serviceCategory.category_name}"
            holder.mView.tvComplainName.text = "${tenantlist[position].complainName}"
            val date = setFormatDate(tenantlist[position].createdAt)
            holder.mView.tvDate.text = date
            holder.mView.textView105.text = tenantlist[position].description
            holder.mView.tvconfrmNow.visibility = View.INVISIBLE
            holder.mView.tvPayNow1.text = con.getString(R.string.resolved)
            holder.mView.paynow1.visibility = View.VISIBLE
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.paynow1.setCardBackgroundColor(Color.parseColor("#00FF19"))

        } else if (from == "owner") {
            println("===================${list[position].status}")
            holder.mView.tvComplain.text = "#${list[position].compId}"
            // holder.mView.tvServiceName.text = "${list[position].serviceCategory.category_name}"
            holder.mView.tvComplainName.text = "${list[position].complainName}"
            val date = setFormatDate(list[position].createdAt)
            holder.mView.tvDate.text = date
            holder.mView.textView105.text = list[position].description
            holder.mView.tvconfrmNow.visibility = View.INVISIBLE
            holder.mView.tvPayNow1.text = con.getString(R.string.resolved)
            holder.mView.paynow1.visibility = View.VISIBLE
            holder.mView.paynow.visibility = View.INVISIBLE
            holder.mView.paynow1.setCardBackgroundColor(Color.parseColor("#00FF19"))
        }
        if (from.equals("tenant")) {
            notificationsAdapterItem =
                NoticeDetailsAdapter(
                    con,
                    tenantlist[position].photo,
                    onPressimg
                )
        } else {
            notificationsAdapterItem =
                NoticeDetailsAdapter(
                    con,
                    list[position].photo!!, onPressimg
                )
        }
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }

    }
}