package com.application.intercom.manager.property

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.ItemAmenitiesBinding


class ManagerPropertyAmenitiesAdapter(
    private val context: Context, val list:ArrayList<String>
) : RecyclerView.Adapter<ManagerPropertyAmenitiesAdapter.MyViewHolder>() {
    class MyViewHolder (mView: ItemAmenitiesBinding): RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(ItemAmenitiesBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = list[position]
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


    }

    override fun getItemCount(): Int {
//        return list.size
        return 8
    }
}