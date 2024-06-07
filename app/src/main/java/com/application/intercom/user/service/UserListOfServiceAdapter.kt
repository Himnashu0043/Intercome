package com.application.intercom.user.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.UserServiceProviderResponse
import com.application.intercom.databinding.ListOfServiceItemsBinding
import com.application.intercom.utils.loadImagesWithGlideExt


class UserListOfServiceAdapter(
    val con: Context,
    val list: ArrayList<UserServiceProviderResponse.Data.Result>,
    var fromSearch: Boolean,
    val onPress:CallService
) :
    RecyclerView.Adapter<UserListOfServiceAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ListOfServiceItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ListOfServiceItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.mView.ivServiceProviderIcon.loadImagesWithGlideExt(list[position].photo)
        holder.mView.textView16.text = data.serviceProviderName
        holder.mView.textView17.text = data.address
        holder.mView.textView18.text = con.getString(R.string.service_provider_charge,data.charges.toString())
        holder.mView.imageView3.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:${list[position].contactNumber}")
//            con.startActivity(intent)
            onPress.onClickCall(list[position].contactNumber)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifiyData(list: List<UserServiceProviderResponse.Data.Result>, fromSearch: Boolean) {
        this.fromSearch = fromSearch
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    interface CallService{
        fun onClickCall(number:String)
    }
}