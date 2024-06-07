package com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.databinding.SecondGatepassHistoryItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class SecondGatePassHistoryAdapter(
    val con: Context,
    val list: ArrayList<GateKeeperListRes.Data.Result>,
    val onPress: Click,
    val onPressimg: CommunityImgAdapter.ClickImg,
    val from: String
) :
    RecyclerView.Adapter<SecondGatePassHistoryAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: NoticeDetailsAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: SecondGatepassHistoryItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            SecondGatepassHistoryItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        if (from == "ongoing_exit_gatePass") {
            holder.mView.textView521.visibility = View.VISIBLE
            holder.mView.textView52.visibility = View.INVISIBLE
        } else {
            holder.mView.textView521.visibility = View.INVISIBLE
            holder.mView.textView52.visibility = View.VISIBLE
        }
        /*holder.mView.textView52.visibility = View.VISIBLE*/
        holder.mView.textView50.text = list[position].contactName
        holder.mView.textView53.text = list[position].flatInfo.name
        val date = setNewFormatDate(list[position].toDate)
        holder.mView.textView56.text = date
        holder.mView.textView57.text = list[position].exitTime
        notificationsAdapterItem =
            NoticeDetailsAdapter(
                con,
                list[position].photo,
                onPressimg
            )
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(con, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }
        holder.itemView.setOnClickListener {
            println("========${list[position]._id}")
            onPress.onViewPass(list[position], position)
        }
        holder.mView.textView521.setOnClickListener {
            onPress.onExitGatePass(list[position]._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onViewPass(msg: GateKeeperListRes.Data.Result, position: Int)
        fun onExitGatePass(id: String)
    }
}