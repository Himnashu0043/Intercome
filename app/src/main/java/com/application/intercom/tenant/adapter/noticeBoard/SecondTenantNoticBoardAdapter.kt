package com.application.intercom.tenant.adapter.noticeBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantNoticeListRes
import com.application.intercom.databinding.NoticeItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class SecondTenantNoticBoardAdapter(
    val con: Context,
    val tenant_list: ArrayList<TenantNoticeListRes.Data>,
    val onPress: TenantNoticeClick,
    val onPressimg: CommunityImgAdapter.ClickImg
) : RecyclerView.Adapter<SecondTenantNoticBoardAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapter: NoticeDetailsAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: NoticeItemsBinding) : RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(NoticeItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.mView.textView371.text = tenant_list[position].title
        holder.mView.textView39.text = tenant_list[position].content
        val date = setFormatDate(tenant_list[position].createdAt)
        holder.mView.textView37.text = date
        holder.mView.mainNoticeLay.setOnClickListener {
            onPress.onTenantClick(position, tenant_list[position]._id)
        }
        notificationsAdapter = NoticeDetailsAdapter(
            con,
            tenant_list[position].images,
            onPressimg
        )
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapter
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }

    }

    override fun getItemCount(): Int {
        return tenant_list.size
    }

    interface TenantNoticeClick {
        fun onTenantClick(position: Int, id: String)
    }
}