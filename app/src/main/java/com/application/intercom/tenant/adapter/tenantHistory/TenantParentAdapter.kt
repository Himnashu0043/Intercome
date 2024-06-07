package com.application.intercom.tenant.adapter.tenantHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.TenantHistoryParentItemsBinding


class TenantParentAdapter(val con: Context) :
    RecyclerView.Adapter<TenantParentAdapter.MyViewHolder>() {
    private lateinit var ChildItem: TenantChildAdapter
    private val viewPool = RecyclerView.RecycledViewPool()
    class MyViewHolder(val mView: TenantHistoryParentItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantHistoryParentItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        ChildItem =
            TenantChildAdapter(con)
        holder.mView.rcyChildTenant.apply {
            adapter = ChildItem
            layoutManager = LinearLayoutManager(con, RecyclerView.VERTICAL, false)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}