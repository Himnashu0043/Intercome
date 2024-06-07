package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.databinding.ActiveItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.user.newflow.LetGetReadyActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class ActiveAdapter(
    val con: Context,
    val list: ArrayList<ActiveNewPhaseList.Data>,
    val onPress: InActiveClick
) : RecyclerView.Adapter<ActiveAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ActiveItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ActiveItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.imageView134.loadImagesWithGlideExt(list[position].photos?.get(0) ?: "")
        holder.mView.textView234.text = list[position].title
        holder.mView.tvLocation.text = list[position].address
        if (list[position].flatStatus.equals("To-Let")) {
            holder.mView.tvPropertyType.text = con.getText(R.string.rent)
        } else {
            holder.mView.tvPropertyType.text = list[position].flatStatus
        }
        holder.mView.tvPropertyType1.text = list[position].propertyType
        holder.mView.tvPropertyPrice.text = "৳${list[position].price}"
        holder.mView.tvFit.text = "${list[position].sqft} ${con.getString(R.string.ft)}"
        holder.mView.tvBedroom.text = "${list[position].bedroom} ${con.getString(R.string.bhk)}"
        holder.mView.tvBathroom.text = "${list[position].bathroom} ${con.getString(R.string.bath)}"
        val date = setNewFormatDate(list[position].createdAt)
        holder.mView.textView235.text = date
        holder.mView.imageView138.setOnClickListener {
            if (holder.mView.listPop.visibility == View.VISIBLE) {
                holder.mView.listPop.visibility = View.GONE
            } else {
                holder.mView.listPop.visibility = View.VISIBLE
            }

        }
        holder.mView.tvfloor.text =
            "${list[position].floorLevel ?: 0} / ${list[position].totalFloor ?: 0}Floor"

        holder.mView.tvEdit.setOnClickListener {
            /* con.startActivity(
                 Intent(con, LetGetReadyActivity::class.java).putExtra(
                     "editList",
                     list[position]
                 ).putExtra("editFrom", "editData")
             )*/
            con.startActivity(
                Intent(con, LetGetReadyActivity::class.java).putExtra(
                    "editList",
                    list[position]
                ).putExtra("editFrom", "editData").putExtra("edit_id", list[position]._id)
            )
        }
        holder.mView.tvInActive.setOnClickListener {
            onPress.onClick(list[position]._id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface InActiveClick {
        fun onClick(id: String)
    }
}