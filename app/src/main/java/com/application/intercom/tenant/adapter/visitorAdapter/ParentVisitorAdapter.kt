package com.application.intercom.tenant.adapter.visitorAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ParentTenantVisitorItemsBinding


class ParentVisitorAdapter(val con: Context,val onPress:ChildVisitorAdapter.Click) :
    RecyclerView.Adapter<ParentVisitorAdapter.MyViewHolder>() {
    private lateinit var child: ChildVisitorAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: ParentTenantVisitorItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ParentTenantVisitorItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        child =
            ChildVisitorAdapter(
                con,onPress
            )
        holder.mView.rcyChild.apply {
            adapter = child
            layoutManager = LinearLayoutManager(con)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}