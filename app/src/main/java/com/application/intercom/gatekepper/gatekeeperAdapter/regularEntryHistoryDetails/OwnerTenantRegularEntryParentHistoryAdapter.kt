package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.localModel.OutTimeModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.databinding.ParentHistoryItemBinding

class OwnerTenantRegularEntryParentHistoryAdapter(
    val con: Context,
    val list: ArrayList<RegularEntryHistoryDetailsList.Data.Result>,
    var from: String
) :
    RecyclerView.Adapter<OwnerTenantRegularEntryParentHistoryAdapter.MyViewHolder>() {
    private lateinit var childItem: OutHistoryAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: ParentHistoryItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ParentHistoryItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    var outList = ArrayList<OutTimeModel>()
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.mView.rcyOutHistory.visibility = View.VISIBLE
        holder.mView.rcyChildHistory.visibility = View.GONE
        println("-----from$from")
        if (from.equals("tenant")) {
            if (!list[position].history.isNullOrEmpty()) {
                for (out in list[position].history[0].list) {
                    outList.add(OutTimeModel(out.inTime, out.exitTime ?: ""))
                }
            }

            childItem =
                OutHistoryAdapter(
                    con,
                    outList

                )
            holder.mView.rcyOutHistory.apply {
                adapter = childItem
                layoutManager = LinearLayoutManager(con)
                setRecycledViewPool(viewPool)
            }
        } else {
            if (!list[position].history.isNullOrEmpty()) {
                for (out in list[position].history[0].list) {
                    outList.add(OutTimeModel(out.inTime, out.exitTime ?: ""))

                }
            }

            childItem =
                OutHistoryAdapter(
                    con,
                    outList

                )
            holder.mView.rcyOutHistory.apply {
                adapter = childItem
                layoutManager = LinearLayoutManager(con)
                setRecycledViewPool(viewPool)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}