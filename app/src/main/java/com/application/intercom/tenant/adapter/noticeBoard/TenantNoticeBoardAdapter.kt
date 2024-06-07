package com.application.intercom.tenant.adapter.noticeBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerNoticBoardListRes
import com.application.intercom.databinding.NoticeItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class TenantNoticeBoardAdapter(
    val con: Context,
    val list: ArrayList<OwnerNoticBoardListRes.Data>,
    val onPress: OwnerNoticeClick,
    val key: String,
    val onPressImg: CommunityImgAdapter.ClickImg
) :
    RecyclerView.Adapter<TenantNoticeBoardAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: NoticeDetailsAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: NoticeItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(NoticeItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("---ad$key")

        holder.mView.textView371.text = list[position].title
        holder.mView.textView39.text = list[position].content
        val date = setFormatDate(list[position].createdAt)
        holder.mView.textView37.text = date
        holder.mView.mainNoticeLay.setOnClickListener {
            onPress.onClick(position, list[position]._id)
        }
        notificationsAdapterItem =
            NoticeDetailsAdapter(
                con,
                list[position].images,
                onPressImg
            )
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OwnerNoticeClick {
        fun onClick(position: Int, id: String)
    }
}