package com.application.intercom.tenant.adapter.Profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ProfileItemsBinding
import com.application.intercom.tenant.Model.ProfileModal

class ProfileAdapter(
    val con: Context,
    val profileList: ArrayList<ProfileModal>,
    var from: String,
    val onPress: ProfileClick
) :
    RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ProfileItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ProfileItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvName.text = profileList[position].name
        holder.mView.iv.setImageResource(profileList[position].img)
        if (from.equals("user")) {
            when (position) {
                4 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                5 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                6 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                7 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
            }
        } else if (from.equals("owner")) {
            when (position) {
                0 ->{
                    holder.mView.ivlock.visibility = View.INVISIBLE
                }
                1 -> {
                    holder.mView.ivlock.visibility = View.INVISIBLE
                }
                2 -> {
                    holder.mView.ivlock.visibility = View.INVISIBLE
                }
                3 -> {
                    holder.mView.ivlock.visibility = View.INVISIBLE
                }
                4 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                5 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                6 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                7 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                8 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                9 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                10 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
                11 -> {
                    holder.mView.ivDiamond.visibility = View.INVISIBLE
                }
            }
        } else {
            holder.mView.ivDiamond.visibility = View.GONE
            holder.mView.ivlock.visibility = View.GONE
        }
        holder.mView.profileLay.setOnClickListener {
            onPress.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    interface ProfileClick {
        fun onClick(position: Int)
    }
}