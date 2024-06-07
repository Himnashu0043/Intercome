package com.application.intercom.owner.activity.rent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.local.NameIdModel
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.databinding.ManagerFlatsItemsBinding

class OwnerRentFlatAdapter(
    val con: Context,
    var list: ArrayList<OwnerFlatListRes.Data>,
    val onPress: OwnerFlat,
    val check_list: ArrayList<IdModel>
) :
    RecyclerView.Adapter<OwnerRentFlatAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ManagerFlatsItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ManagerFlatsItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*holder.mView.textView225.text = list[position].name
        if (list[position].isSelected) {
            holder.mView.amLay.setBackgroundResource(R.drawable.red_bg)
            holder.mView.textView225.setTextColor(con.getColor(R.color.white))
        } else if (list[position].isSelected1) {
            holder.mView.amLay.setBackgroundResource(R.drawable.oragne_strock_with_white_bg)
            holder.mView.textView225.setTextColor(con.getColor(R.color.black))
        } else {
            holder.mView.amLay.setBackgroundResource(R.drawable.bg_login_type)
            holder.mView.textView225.setTextColor(con.getColor(R.color.black))
        }*/
        holder.mView.textView225.text = list[position].name
        if (list[position].isSelected) {
            holder.mView.amLay.setBackgroundResource(R.drawable.red_bg)
            holder.mView.textView225.setTextColor(con.getColor(R.color.white))
        } else if (list[position].isSelected1) {
            holder.mView.amLay.setBackgroundResource(R.drawable.oragne_strock_with_white_bg)
            holder.mView.textView225.setTextColor(con.getColor(R.color.black))

        } else {
            if (list[position].isSelected1) {
                holder.mView.amLay.setBackgroundResource(R.drawable.oragne_strock_with_white_bg)
                holder.mView.textView225.setTextColor(con.getColor(R.color.black))
            } else {
                holder.mView.amLay.setBackgroundResource(R.drawable.bg_login_type)
                holder.mView.textView225.setTextColor(con.getColor(R.color.black))
            }
        }
        holder.mView.amLay.setOnClickListener {
            if (check_list.contains(IdModel(list[position]._id, "", ""))) {
                Toast.makeText(con, "Rent Already exits!!", Toast.LENGTH_SHORT).show()
            } else {
                if (list[position].tenant.isNullOrEmpty()) {
                    Toast.makeText(con, "Tenant not Assign on this Flat", Toast.LENGTH_SHORT).show()
                } else if (!list[position].is_assign) {
                    Toast.makeText(con, "Tenant not Assign on this Flat", Toast.LENGTH_SHORT).show()
                }else{
                    list[position].isSelected1 = !list[position].isSelected1
                    onPress.onCLick(
                        list[position].isSelected1,
                        NameIdModel(list[position]._id, list[position].name, true)
                    )
                    notifyDataSetChanged()
                }

            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OwnerFlat {
        fun onCLick(isClick: Boolean, obj: NameIdModel)
    }
}