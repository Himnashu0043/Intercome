package com.application.intercom.manager.newFlow.finance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.databinding.FinanceItemBinding

class BillingFinanceAdapter(val con: Context) :
    RecyclerView.Adapter<BillingFinanceAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FinanceItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        return MyViewHolder(FinanceItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.mView.imageView150.setBackgroundResource(R.drawable.red_stock_icon)
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_red_gradient_bg)
        } else if (position == 1) {
            holder.mView.imageView150.visibility = View.INVISIBLE
            holder.mView.imageView1501.visibility = View.VISIBLE
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_green_gradint_bg)
            holder.mView.textView261.text = "Total Expenses"
            holder.mView.textView262.text = "Expesnse:"
            holder.mView.textView263.text = "32"
            holder.mView.textView264.setTextColor(ContextCompat.getColor(con, R.color.green))
        }

    }

    override fun getItemCount(): Int {
        return 2
    }
}