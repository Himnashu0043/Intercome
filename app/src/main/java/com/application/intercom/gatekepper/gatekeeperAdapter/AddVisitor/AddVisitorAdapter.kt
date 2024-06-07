package com.application.intercom.gatekepper.gatekeeperAdapter.AddVisitor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.AddVisitorItemsBinding


class AddVisitorAdapter(val con: Context, val list: ArrayList<String>,val onPress: Click) :
    RecyclerView.Adapter<AddVisitorAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: AddVisitorItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(AddVisitorItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvName.text = list[position]
        holder.mView.mainLay.setOnClickListener {
            onPress.onClick(position)
        }
//        if (position == 0) {
//            con.startActivity(Intent(con, GuestActivity::class.java).putExtra("from", "guest"))
//        } else if (position == 1) {
//            con.startActivity(Intent(con, GuestActivity::class.java).putExtra("from", "deli"))
//        } else if (position == 2) {
//            con.startActivity(Intent(con, GuestActivity::class.java).putExtra("from", "service"))
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface Click{
        fun onClick(position: Int)
    }
}