package com.application.intercom.manager.complaint

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ManagerComplainImgItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class ManagerDetailsComplainAdapter(val con: Context,val list:ArrayList<String>,val onPress:Show) :
    RecyclerView.Adapter<ManagerDetailsComplainAdapter.MyVIewHolder>() {
    class MyVIewHolder(val mView: ManagerComplainImgItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyVIewHolder {
       return MyVIewHolder(ManagerComplainImgItemsBinding.inflate(LayoutInflater.from(con),parent,false))
    }

    override fun onBindViewHolder(
        holder: MyVIewHolder,
        position: Int
    ) {
        holder.mView.ivOne.loadImagesWithGlideExt(list[position].toString())
        holder.mView.ivOne.setOnClickListener {
            onPress.onClickImgShow(list[position])
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }
    interface Show{
        fun onClickImgShow(img: String)
    }
}