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

class UserServiceListAdapter(
    val con: Context,
    val list: ArrayList<ServicesCategoryTable>,
    val listener: ServicesCategoryListener
   /* var fromSearch: Boolean*/
   /* val onPress: Click*/
) :
    RecyclerView.Adapter<UserServiceListAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: RecentlyItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(RecentlyItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.mView.textView13.text = data.category_name
       // holder.mView.textView13.text = data.category_name
        data.image?.let { holder.mView.ivServiceIcon.loadImagesWithGlideExt(it) }
       /* if (fromSearch) {
            holder.mView.root.setOnClickListener {
                data.isRecent = true
                listener.servicesCategoryClick(data)
            }
        }*/
        holder.mView.root.setOnClickListener {
            data.isRecent = true
            listener.servicesCategoryClick(data,position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifiyData(list: ArrayList<ServicesCategoryTable>/*, fromSearch: Boolean*/) {
     /*   this.fromSearch = fromSearch*/
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    /*interface Click {
        fun onMainListClick(category_name: String, id: String)
    }*/
}