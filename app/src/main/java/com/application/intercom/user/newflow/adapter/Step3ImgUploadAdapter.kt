package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.Step3ImgItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt

class Step3ImgUploadAdapter(val con: Context, val list: ArrayList<String>,val onPress:ClickImg) :
    RecyclerView.Adapter<Step3ImgUploadAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: Step3ImgItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(Step3ImgItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (list.size == position) {
            holder.mView.ivMain.visibility = View.INVISIBLE
            holder.mView.ivCrossItems.visibility = View.INVISIBLE
            holder.mView.imageView140.visibility = View.VISIBLE
        } else {
            holder.mView.ivMain.visibility = View.VISIBLE
            holder.mView.ivCrossItems.visibility = View.VISIBLE
            holder.mView.imageView140.visibility = View.GONE
            holder.mView.ivMain.loadImagesWithGlideExt(list[position])
        }
        holder.mView.ivCrossItems.setOnClickListener {
            list.removeAt(position)
            println("-----remove$list")
            notifyDataSetChanged()
        }
        holder.mView.imageView140.setOnClickListener {
            onPress.onClickPlus(position)
        }
    }

    override fun getItemCount(): Int {
        if (list.size == 0) {
            return list.size
        } else {
            return list.size + 1
        }
    }
    interface ClickImg{
        fun onClickPlus(position: Int)
    }
}