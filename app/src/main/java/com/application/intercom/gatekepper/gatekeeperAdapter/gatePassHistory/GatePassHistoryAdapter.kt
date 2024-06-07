package com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.databinding.GatePassHistoryItemsBinding
import com.application.intercom.gatekepper.activity.gatePass.SecondGatePassActivity
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class GatePassHistoryAdapter(
    val con: Context,
    val list: ArrayList<ManagerGatePassHistoryListRes.Data.Result>,
    val onPress: Click,
    val onPressimg: CommunityImgAdapter.ClickImg
) :
    RecyclerView.Adapter<GatePassHistoryAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: NoticeDetailsAdapter
    private val viewPool = RecyclerView.RecycledViewPool()
    private var imgList = ArrayList<String>()

    class MyViewHolder(val mView: GatePassHistoryItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            GatePassHistoryItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.mainLay1.setOnClickListener {
            onPress.onclick(position)
            // con.startActivity(Intent(con, SecondGatePassActivity::class.java))
        }
        if (imgList.size > 0) {
            imgList.clear()
        }
        for (ii in list) {
            imgList.add(ii.photo.get(0))
        }
        /*list[position].photo.forEach {
            imgList.add(list[position].photo.get(0))
        }*/
        notificationsAdapterItem =
            NoticeDetailsAdapter(
                con,
                imgList,
                onPressimg
            )
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }
        holder.mView.textView50.text = list[position].activity
        holder.mView.textView53.text = list[position].flatInfo.get(0).name
        holder.mView.textView57.text = list[position].entryTime
        val date = setFormatDate(list[position].createdAt)
        holder.mView.textView56.text = date

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onclick(position: Int)
    }
}