package com.application.intercom.owner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ShowImgItemBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class ShowImgAdapter(val con: Context, val list: ArrayList<String>) :
    RecyclerView.Adapter<ShowImgAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ShowImgItemBinding) : RecyclerView.ViewHolder(mView.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ShowImgItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.ivMain.loadImagesWithGlideExt(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}