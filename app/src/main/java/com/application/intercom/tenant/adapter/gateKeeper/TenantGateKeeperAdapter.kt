package com.application.intercom.tenant.adapter.gateKeeper

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.owner.gateKeeper.OwnerGateKeeperList
import com.application.intercom.databinding.TenantGateKeeperItemsBinding
import com.application.intercom.helper.*
import com.application.intercom.utils.loadImagesWithGlideExt
import java.text.SimpleDateFormat

class TenantGateKeeperAdapter(
    val con: Context,
    val list: ArrayList<OwnerGateKeeperList.Data.Result>,
    val onPress:PicClicK
) :
    RecyclerView.Adapter<TenantGateKeeperAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantGateKeeperItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantGateKeeperItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("========${list[position]}")
        holder.mView.imageView27.loadImagesWithGlideExt(list[position].profilePic)
        holder.mView.textView60.text = list[position].fullName
        holder.mView.textView61.text = list[position].phoneNumber
        holder.mView.textView62.text =
            "${con.getString(R.string.shift_timing)} : ${list[position].shiftStart} ${
                con.getString(
                    R.string.to
                )
            } ${list[position].shiftEnd}"

        holder.mView.imageView29.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${list[position].phoneNumber}")
            con.startActivity(intent)
        }

        val test = getParseBetweenTimeStatus(list[position].shiftStart, list[position].shiftEnd)
        if (test) {
            holder.mView.cardView8.setCardBackgroundColor(ContextCompat.getColor(con, R.color.green))
            holder.mView.tvduty.text = con.getString(R.string.on_duty)
        } else {
            holder.mView.cardView8.setCardBackgroundColor(ContextCompat.getColor(con, R.color.red))
            holder.mView.tvduty.text = con.getString(R.string.off_duty)
        }
        holder.mView.imageView27.setOnClickListener {
            onPress.onImgShow(list[position].profilePic)
        }



    }

    interface PicClicK{
        fun onImgShow(img:String)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}