package com.application.intercom.user.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ItemHomeServicesBinding
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.user.property.PropertyDetailsActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class HomeServicesAdapter(
    private val context: Context, val list: ArrayList<ServicesCategoryTable>
) : RecyclerView.Adapter<HomeServicesAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemHomeServicesBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHomeServicesBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        item.image?.let { holder.mView.ivServiceIcon.loadImagesWithGlideExt(it) }
         holder.mView.textView13.text = item.category_name

//        for(i in item.content?.indices!!){
//            holder.mView.visaName.text = item.content?.get(i)?.title
//            holder.mView.visaDescription.text = item.content?.get(i)?.description
//            holder.mView.visaLink.text = item.content?.get(i)?.websiteLink
//            holder.mView.visaLink.setOnClickListener {
//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.content?.get(i)?.websiteLink))
//                context.startActivity(browserIntent)
//            }
//        }
//
//        holder.mView.ivBookmark.background = ContextCompat.getDrawable(context,R.drawable.ic_star_filled)
////        pankaj
//        holder.mView.ivBookmark.visibility = View.GONE
//        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context, PropertyDetailsActivity::class.java))
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun notifiyData(list: ArrayList<ServicesCategoryTable>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}