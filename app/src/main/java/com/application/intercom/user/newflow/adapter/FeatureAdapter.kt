package com.application.intercom.user.newflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.NewUserAmenitiesList
import com.application.intercom.databinding.FeatureItemsBinding
import com.application.intercom.user.newflow.modal.UserTestAmenitiesModel
import com.application.intercom.user.newflow.modal.UserTestPostModel
import com.application.intercom.utils.loadImagesWithGlideExt

class FeatureAdapter(
    val con: Context,
    val list: ArrayList<NewUserAmenitiesList.Data>,
    val onPress: AmenitiesCLick,
    val nameList: ArrayList<String>
) : RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FeatureItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FeatureItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }





    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.mView.imageView126.loadImagesWithGlideExt(list[position].image)
        holder.mView.textView225.text = list[position].name



        if (list[position].isSelect) {
            holder.mView.amLay.background =
                ContextCompat.getDrawable(con, R.drawable.oragne_strock_with_white_bg)
        } else {
            holder.mView.amLay.background =
                ContextCompat.getDrawable(con, R.drawable.bg_edit_search)
        }
        holder.itemView.setOnClickListener {
            onPress.onClick(position, !list[position].isSelect, UserTestPostModel.Amentity(list.get(position).image, list.get(position).name))
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface AmenitiesCLick {
        fun onClick(position: Int, selectedStatus: Boolean,object1:UserTestPostModel.Amentity)
    }
}