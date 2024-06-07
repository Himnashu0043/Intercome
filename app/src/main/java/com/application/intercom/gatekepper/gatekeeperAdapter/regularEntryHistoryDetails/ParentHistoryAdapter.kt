package com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.localModel.InTimeModel
import com.application.intercom.data.model.local.localModel.OutTimeModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.databinding.ParentHistoryItemBinding
import com.application.intercom.manager.complaint.ManagerComplainImgAdapter

class ParentHistoryAdapter(
    val con: Context,
    val list: ArrayList<RegularEntryHistoryDetailsList.Data.Result.History>,
    val key: String,
    val from: String
) :
    RecyclerView.Adapter<ParentHistoryAdapter.MyViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    private val viewPoolout = RecyclerView.RecycledViewPool()

    inner class MyViewHolder(val mView: ParentHistoryItemBinding) :
        RecyclerView.ViewHolder(mView.root) {
        val childItem =
            ChildHistoryAdapter(
                mView.root.context,
                arrayListOf()
            )
        val outItem = OutHistoryAdapter(
            con,
            arrayListOf()
        )

        init {
            mView.rcyOutHistory.apply {
                adapter = outItem
                layoutManager = LinearLayoutManager(con)
                setRecycledViewPool(viewPoolout)
            }

            mView.rcyChildHistory.apply {
                adapter = childItem
                layoutManager = LinearLayoutManager(mView.root.context)
                setRecycledViewPool(viewPool)
            }
        }
    }

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
    var inoutList = ArrayList<InTimeModel>()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("----key$key")
        println("----from$from")

        if (key.equals("Out")) {
            /*if (!list[position].history.isNullOrEmpty()) {
                holder.mView.textView180.text = list[position].history[position]._id
                for (out in list[position].history[0].list) {
                    outList.add(OutTimeModel(out.inTime, out.exitTime))
                }
            }
            println("------OutList$outList")
            holder.mView.rcyOutHistory.visibility = View.VISIBLE
            holder.mView.rcyChildHistory.visibility = View.GONE

            holder.outItem.list.clear()
            holder.outItem.list.addAll(outList)
            holder.outItem.notifyDataSetChanged()*/
            if (!list[position].list.isNullOrEmpty()) {
                holder.mView.textView180.text = list[position]._id
                for (out in list[position].list) {
                    outList.add(OutTimeModel(out.inTime, out.exitTime))
                }
            }
            holder.mView.rcyOutHistory.visibility = View.VISIBLE
            holder.mView.rcyChildHistory.visibility = View.GONE

            holder.outItem.list.clear()
            holder.outItem.list.addAll(outList)
            holder.outItem.notifyDataSetChanged()

        } else {
            if (from.equals("completed")) {
//                    if (!list[position].history.isNullOrEmpty()) {
//                        holder.mView.textView180.text = list[position].history[position]._id
//                        holder.mView.rcyOutHistory.visibility = View.VISIBLE
//                        holder.mView.rcyChildHistory.visibility = View.GONE
//                        for (out in list[position].history[0].list) {
//                            println("---compl${list[position]}")
//                            if (out.exitTime != null) {
//                                outList.add(OutTimeModel(out.inTime, out.exitTime ?: ""))
//                            }
//
//
//                        }
//                    }
//                    holder.outItem.list.clear()
//                    holder.outItem.list.addAll(outList)
//                    holder.outItem.notifyDataSetChanged()
                if (!list[position].list.isNullOrEmpty()) {
                    holder.mView.textView180.text = list[position]._id
                    holder.mView.rcyOutHistory.visibility = View.VISIBLE
                    holder.mView.rcyChildHistory.visibility = View.GONE
                    for (out in list[position].list) {
                        println("---compl${list[position]}")
                        if (out.exitTime != null) {
                            outList.add(OutTimeModel(out.inTime, out.exitTime ?: ""))
                        }


                    }
                }
                holder.outItem.list.clear()
                holder.outItem.list.addAll(outList)
                holder.outItem.notifyDataSetChanged()

            } else {

                holder.mView.rcyOutHistory.visibility = View.GONE
                holder.mView.rcyChildHistory.visibility = View.VISIBLE
//                    if (!list[position].history.isNullOrEmpty()) {
//                        for (out in list[position].history) {
//                            holder.mView.textView180.text = list[position].history[position]._id
//                            inoutList.add(
//                                InTimeModel(
//                                    out.list.get(position).inTime,
//                                    out.list.get(position).exitTime ?: ""
//                                )
//                            )
//                        }
//                    }
                if (!list[position].list.isNullOrEmpty()) {
                    for (out in list[position].list) {
                        holder.mView.textView180.text = list[position]._id
                        inoutList.add(
                            InTimeModel(
                                out.inTime,
                                out.exitTime ?: ""
                            )
                        )
                    }
                }

                holder.childItem.list.clear()
                holder.childItem.list.addAll(inoutList)
                holder.childItem.notifyDataSetChanged()
            }

        }
    }


    /* private fun getModifyData(list: List<RegularEntryHistoryDetailsList.Data.Result.History.List1>): ArrayList<InTimeModel> {
         var inoutList = ArrayList<InTimeModel>()
         if (!list.isNullOrEmpty()) {
             for (out in list) {
                 inoutList.add(InTimeModel(out.inTime, out.exitTime ?: ""))
             }
         }

         return inoutList
         println("-----inoutList$inoutList")
     }*/

    override fun getItemCount(): Int {
        return list.size
    }
}