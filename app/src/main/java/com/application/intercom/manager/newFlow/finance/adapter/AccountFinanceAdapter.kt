package com.application.intercom.manager.newFlow.finance.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.finance.BillCountManagerRes
import com.application.intercom.databinding.FinanceItemBinding
import com.application.intercom.manager.newFlow.expenses.ManagerExpensesActivity

class AccountFinanceAdapter(val con: Context, val list: ArrayList<BillCountManagerRes.Data>) :
    RecyclerView.Adapter<AccountFinanceAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: FinanceItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(FinanceItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.mView.imageView150.visibility = View.VISIBLE
            holder.mView.imageView1501.visibility = View.INVISIBLE
            holder.mView.imageView1502.visibility = View.INVISIBLE
            holder.mView.imageView1503.visibility = View.INVISIBLE
            holder.mView.imageView150.setBackgroundResource(R.drawable.red_stock_icon)
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_red_gradient_bg)
        } else if (position == 1) {
            holder.mView.imageView150.visibility = View.INVISIBLE
            holder.mView.imageView1501.visibility = View.VISIBLE
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_green_gradint_bg)
            holder.mView.textView261.text = "Total Expenses"
            holder.mView.textView262.text = "Expenses:"
            holder.mView.textView263.text = "32"
            holder.mView.textView264.setTextColor(ContextCompat.getColor(con, R.color.green))
        } else if (position == 2) {
            holder.mView.imageView150.visibility = View.INVISIBLE
            holder.mView.imageView1501.visibility = View.INVISIBLE
            holder.mView.imageView1502.visibility = View.VISIBLE
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_yellow_gradient_bg)
            holder.mView.textView261.text = "Pending foApproval"
            holder.mView.textView262.text = "Total Pending:"
            holder.mView.textView263.text = "20"
            holder.mView.textView264.setTextColor(ContextCompat.getColor(con, R.color.yellow))
        } else if (position == 3) {
            holder.mView.imageView150.visibility = View.INVISIBLE
            holder.mView.imageView1501.visibility = View.INVISIBLE
            holder.mView.imageView1502.visibility = View.INVISIBLE
            holder.mView.imageView1503.visibility = View.VISIBLE
            holder.mView.card.background =
                ContextCompat.getDrawable(con, R.drawable.light_blue_gradient_bg)
            holder.mView.textView261.text = "Expenses"
            holder.mView.textView262.text = "Total Bills:"
            holder.mView.textView263.text = "24"
            holder.mView.textView264.setTextColor(ContextCompat.getColor(con, R.color.blue))
            holder.mView.card.setOnClickListener {
                con.startActivity(Intent(con, ManagerExpensesActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}