package com.application.intercom.owner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.PhotoItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class PhotoUploadAdapter(val con: Context, val list: ArrayList<String>) :
    RecyclerView.Adapter<PhotoUploadAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: PhotoItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(PhotoItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("----edit$list")
        if (list[position].contains(".mp4")) {
            holder.mView.ivplay.visibility = View.VISIBLE
        } else {
            holder.mView.ivplay.visibility = View.INVISIBLE
        }
        holder.mView.ivMain.loadImagesWithGlideExt(list[position])
        holder.mView.ivCrossItems.setOnClickListener {
            list.removeAt(position)
            println("-----remove$list")
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}