package com.application.intercom.tenant.adapter.Chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ChatItemsBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity

class TenantChatAdapter(val con: Context) : RecyclerView.Adapter<TenantChatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView:ChatItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ChatItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.root.setOnClickListener {
            con.startActivity(Intent(con, ChatDetailsActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return 6
    }
}