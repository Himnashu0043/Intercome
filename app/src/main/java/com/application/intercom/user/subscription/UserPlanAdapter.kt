package com.application.intercom.user.subscription

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.databinding.UserPlanItemsBinding

class UserPlanAdapter(
    val con: Context,
    val list: ArrayList<UserPlanListRes.Data>,
    val onPress: Click,
    val discount: String
) :
    RecyclerView.Adapter<UserPlanAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: UserPlanItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(UserPlanItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    var select: Boolean = false
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("-----discounyt$discount")
        if (list[position].isSelected) {
            holder.mView.tvSubscriptionName.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.black
                )
            )
            holder.mView.tvSubscriptionDuration.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.black
                )
            )
            holder.mView.tvSubscriptionDetails.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.black
                )
            )
            holder.mView.tvSubscriptionPrice.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.black
                )
            )
            holder.mView.layoutSilver.background =
                ContextCompat.getDrawable(con, R.drawable.bg_subscription_item)
        } else {
            holder.mView.tvSubscriptionName.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.white
                )
            )
            holder.mView.tvSubscriptionDuration.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.white
                )
            )
            holder.mView.tvSubscriptionDetails.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.white
                )
            )
            holder.mView.tvSubscriptionPrice.setTextColor(
                ContextCompat.getColor(
                    con,
                    R.color.white
                )
            )
            holder.mView.layoutSilver.background =
                ContextCompat.getDrawable(con, R.drawable.bg_subscription_stroke_item)
        }

        holder.mView.userPlanLay.setOnClickListener {
            updatelist(position)
            onPress.onClick(list[position])
        }
        holder.mView.tvSubscriptionName.text = list[position].title
        if (discount.equals("null")) {
            holder.mView.tvSubscriptionPrice.text = "৳${list[position].price}"

        } else {
            val dis = list[position].price - discount.toInt()
            holder.mView.tvSubscriptionPrice.text = "৳${dis}"
        }
        val htmlAsString = list[position].description
        val htmlAsSpanned = Html.fromHtml(htmlAsString)
        holder.mView.tvSubscriptionDetails.text = htmlAsSpanned
        val months: Int = list[position].validFor.toInt() / 12
        val perMonths: Double = list[position].price / months.toDouble()
        val duration = String.format("%.1f", perMonths).toDouble()
        holder.mView.tvSubscriptionDuration.text = "৳${duration} / month"

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun updatelist(pos: Int) {
        for (i in list.indices) {
            if (i == pos)
                list.get(i).isSelected = true
            else
                list.get(i).isSelected = false
        }
        notifyDataSetChanged()
    }

    interface Click {
        fun onClick(msg: UserPlanListRes.Data)
    }
}