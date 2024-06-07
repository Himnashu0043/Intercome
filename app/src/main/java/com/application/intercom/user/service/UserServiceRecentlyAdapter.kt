package com.application.intercom.user.service

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.RecentlyItemsBinding
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.utils.AppConstants
import com.application.intercom.utils.loadImagesWithGlideExt

class UserServiceRecentlyAdapter(
    val context: Context,
    var key: String,
    private val list: ArrayList<ServicesCategoryTable>
) : RecyclerView.Adapter<UserServiceRecentlyAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: RecentlyItemsBinding) : RecyclerView.ViewHolder(mView.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecentlyItemsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        println("---keyService$key")
        data.image?.let { holder.mView.ivServiceIcon.loadImagesWithGlideExt(it) }
        holder.mView.textView13.text = data.category_name
        holder.mView.lay.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    UserListOfServicesActivity::class.java
                ).putExtra("from", key)
                    .putExtra(AppConstants.CATEGORY_ID, data._id)
                    .putExtra("category_name", data.category_name)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifiyData(list: ArrayList<ServicesCategoryTable>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}