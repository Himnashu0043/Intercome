package com.application.intercom.manager.newFlow.balancesheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.IncomeReportManagerList
import com.application.intercom.databinding.CategoryItemBinding

class CategoryAdapter(
    val con: Context,
    val list: ArrayList<IncomeReportManagerList.Data.CategoryData>
) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: CategoryItemBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    /*var category = ArrayList<String>()
    var amountCat = ArrayList<Int>()
    */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (!list[position].newfieldname?.get(0)?.categoryData!!.isNullOrEmpty()) {
            holder.mView.textView289.text =
                list[position].newfieldname?.get(0)?.categoryData?.get(0)?.name
                    ?: ""
        } else {
            holder.mView.textView289.text = "Service Charge"
        }
        holder.mView.textView290.text =
            (list[position].newfieldname?.get(0)?.amount ?: 0).toString()
      /*  list.forEach {
            if((it.newfieldname?.first()?.categoryData?.size ?: 0) > 0){
                category.add((it.newfieldname?.first()?.categoryData?.first()?.name ?: ""))
            }
            var amount = 0
            it.newfieldname?.forEach {
                amount += (it?.amount ?: 0)
            }
            amountCat.add(amount)
        }*/

    }

    override fun getItemCount(): Int {

        return list.size
    }
}