package com.application.intercom.owner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.model.remote.owner.gatePass.OwnerGatepassList
import com.application.intercom.databinding.SecondGatepassHistoryItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter

class OwnerGatePassAdapter(
    val con: Context,
    val list: ArrayList<OwnerGatepassList.Data.Result>,
    val onPress: Click
) :
    RecyclerView.Adapter<OwnerGatePassAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: ShowImgAdapter
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.textView52.text = con.getString(R.string.edit)
        notificationsAdapterItem = ShowImgAdapter(con, list[position].photo)
        holder.mView.rcyChildGatePassHistory.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }
        holder.mView.textView50.text = list[position].contactName
        holder.mView.textView53.text = list[position].flatInfo.name
        holder.mView.textView56.text = setNewFormatDate(list[position].toDate)
        holder.mView.textView57.text = list[position].exitTime
        holder.mView.textView52.setOnClickListener {
            onPress.onEditCLick(position, list[position])
        }
        holder.itemView.setOnClickListener {
            onPress.onViewPass(list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onEditCLick(position: Int, msg: OwnerGatepassList.Data.Result)
        fun onViewPass(msg: OwnerGatepassList.Data.Result, position: Int)
    }
}