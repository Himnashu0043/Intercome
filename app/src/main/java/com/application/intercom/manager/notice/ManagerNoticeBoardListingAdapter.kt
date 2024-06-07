package com.application.intercom.manager.notice

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.noticeBoard.ManagerNoticeBoardListRes
import com.application.intercom.databinding.NoticeItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.notice.listener.NoticeBoardDialogListener

class ManagerNoticeBoardListingAdapter(
    private val context: Context, val list: ArrayList<ManagerNoticeBoardListRes.Data>,
    private val listener: NoticeBoardDialogListener
) : RecyclerView.Adapter<ManagerNoticeBoardListingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: NoticeItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NoticeItemsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.showNoticeBoardDialog(position,list[position]._id)
//        context.startActivity(Intent(context, ManagerParkingToLetParkingActivity::class.java))
        }
        if (!list[position].is_view) {
            holder.mView.cardNoticeLay.setCardBackgroundColor(Color.parseColor("#FFEDE2"))
        } else {
            holder.mView.cardNoticeLay.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.mView.textView371.text = list[position].title
        holder.mView.textView39.text = list[position].content
        val date = setFormatDate(list[position].createdAt)
        holder.mView.textView37.text = date
    }

    override fun getItemCount(): Int {
        return list.size

    }
}
