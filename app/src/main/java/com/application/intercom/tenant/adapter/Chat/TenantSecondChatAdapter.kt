package com.application.intercom.tenant.adapter.Chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.TenantSecondChatItemsBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity

class TenantSecondChatAdapter(val con: Context) :
    RecyclerView.Adapter<TenantSecondChatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantSecondChatItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantSecondChatItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.lay.setOnClickListener {
            con.startActivity(Intent(con, ChatDetailsActivity::class.java))
        }
        when (position) {
            1 -> {
                holder.mView.tvNotificationCount.visibility = View.VISIBLE
            }
            2 -> {
                holder.mView.tvNotificationCount.visibility = View.VISIBLE
            }
            else -> {
                holder.mView.tvNotificationCount.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return 8
    }
}