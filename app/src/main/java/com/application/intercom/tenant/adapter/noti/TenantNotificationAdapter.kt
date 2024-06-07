package com.application.intercom.tenant.adapter.noti

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.NotificationList
import com.application.intercom.databinding.TenantNotiItemsBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistory.OwnerTenantRegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantSingleEntryHistory.OwnerTenantSingleEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.timeSince
import com.application.intercom.manager.bills.ApprovalBillingManagerActivity
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.visitorAndGatePass.ManagerGatePassActivity
import com.application.intercom.owner.activity.gatepass.OwnerGatePassActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.utils.SessionConstants


class TenantNotificationAdapter(val con: Context, val list: ArrayList<NotificationList.Data>) :
    RecyclerView.Adapter<TenantNotificationAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantNotiItemsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        return MyViewHolder(TenantNotiItemsBinding.inflate(LayoutInflater.from(con), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (prefs.getString(SessionConstants.ROLE, "") == "owner") {
            if (list[position].type == "NEW_SERVICE_Bill") {
                holder.mView.tvMainTittle.text =
                    "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                holder.mView.tvDes.text =
                    "Service Charge Bill of Taka${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been generated."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "from",
                            prefs.getString(SessionConstants.ROLE, "")
                        )
                    )
                }

            } else if (list[position].type == "NEW_Bill") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    /*holder.mView.tvDes.text = "${list[position].amount ?: ""} ৳ Taka generated as ${
                        list[position].categoryData?.get(
                            0
                        )?.name ?: ""
                    } \n due Date :${
                        setFormatDate(list[position].dueDate ?: "")
                    }"*/
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Take ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been generated."
                }

                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "from",
                            prefs.getString(SessionConstants.ROLE, "")
                        )
                    )
                }
            } else if (list[position].type == "BILL_APPROVED") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    /*holder.mView.tvMainTittle.text =
                        "Bill Approved for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text = "${list[position].amount ?: ""} ৳ Taka generated as ${
                        list[position].categoryData?.get(
                            0
                        )?.name ?: ""
                    } \n due Date :${
                        setFormatDate(list[position].approvedDate ?: "")
                    }"*/
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Take ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been pain in."
                } else {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Service Charge Bill of Take ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been pain in."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "from",
                            prefs.getString(SessionConstants.ROLE, "")
                        ).putExtra("key", "paid")
                    )
                }

            } else if (list[position].type == "REGULAR_CHECKED_OUT") {
                holder.mView.tvMainTittle.text =
                    "Guest Check Out"
                holder.mView.tvDes.text = " Guest ${list[position].visitorName ?: ""} Checked Out"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantRegularEntryHistoryActivity::class.java
                        )
                    )
                }

            } else if (list[position].type == "REGULAR_CHECKED_IN") {
                holder.mView.tvMainTittle.text =
                    "Guest Check In"
                holder.mView.tvDes.text = " Guest ${list[position].visitorName ?: ""} Checked In"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantRegularEntryHistoryActivity::class.java
                        )
                    )
                }
            } else if (list[position].type == "NEW_NOTICE") {
                holder.mView.tvMainTittle.text =
                    "New Notice Posted"
                holder.mView.tvDes.text = "New Notice Posted"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con, TenantNoticBoardActivity::class.java
                        ).putExtra("from", "owner")
                    )
                }
            } else if (list[position].type == "CHECKED_IN") {
                holder.mView.tvMainTittle.text =
                    "Visitor Check In"
                holder.mView.tvDes.text =
                    "You have a new visitor awaiting for your confirmation to check in."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("noty", "noty_list_checkIn")
                    )
                }
            } else if (list[position].type == "CHECKED_OUT") {
                holder.mView.tvMainTittle.text =
                    "Visitor Check Out"
                holder.mView.tvDes.text =
                    "Visitor ${list[position].visitorName ?: ""} checked Out."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("noty", "noty_list_checkOut")
                    )
                }
            } else if (list[position].type == "NEW_COMPLAIN_RESOLVE") {
                holder.mView.tvMainTittle.text =
                    "Complain Resolved"
                holder.mView.tvDes.text =
                    "Your ${list[position].complainName ?: ""} complain has been resolved, please check."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantRegisterComplainActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )
                }
            } else if (list[position].type == "LIKED_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )
                }
            } else if (list[position].type == "COMMENT_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )
                }
            } else if (list[position].type == "NEW_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )
                }
            } else if (list[position].type == "RENT_PAID") {
                holder.mView.tvMainTittle.text =
                    "Rent for ${list[position].billMonth} ${list[position].billYear}"
                holder.mView.tvDes.text =
                    "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} has been paid in ${list[position].payType ?: ""}."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerBillingActivity::class.java
                        ).putExtra("key", "noti_approved")
                    )
                }
            } else if (list[position].type == "NEW_BILL_NOTIFY") {
                holder.mView.tvMainTittle.text =
                    "Bill is Pending"
                holder.mView.tvDes.text =
                    "Bill of Taka ${list[position].amount ?: 0} is Pending."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerBillingActivity::class.java
                        ).putExtra("key", "noti_All_notify").putExtra("from", "owner")
                    )
                }
            } else if (list[position].type == "PAYMENT_PENDING") {
                if (list[position].billType == "Service") {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "Service Charge of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                } else if (list[position].billType == "Normal Bill") {
                    if (list[position].categoryData?.size ?: 0 > 0) {
                        holder.mView.tvMainTittle.text =
                            "Bill for ${list[position].billMonth} ${list[position].billYear}"
                        holder.mView.tvDes.text =
                            "Bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                    } /*else {
                        holder.mView.tvMainTittle.text =
                            "Rent for ${list[position].billMonth} ${list[position].billYear}"
                        holder.mView.tvDes.text =
                            "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                    }*/
                } else {
                    holder.mView.tvMainTittle.text =
                        "Rent for ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                }

                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "key",
                            "noti_pending"
                        ).putExtra("from", "owner")
                    )
                }
            } else if (list[position].type == "BILL_PAID") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    /*holder.mView.tvDes.text =
                        "${list[position].amount ?: ""} ৳ Taka generated as ${
                            list[position].categoryData?.get(
                                0
                            )?.name ?: ""
                        } \n due Date :${
                            setFormatDate(list[position].dueDate ?: "")
                        }"*/
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid"
                } else {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Service Charge Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid"
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "key",
                            "notiy_paid"
                        ).putExtra("from", "owner")
                    )
                }

            } else if (list[position].type == "BILL_REJECT") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been Rejected."
                } else {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been Rejected."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, OwnerBillingActivity::class.java).putExtra(
                            "key",
                            "notiy_billReject"
                        ).putExtra("from", "owner")
                    )
                }

            } else if (list[position].type == "GATEPASS_COMPLETE") {
                holder.mView.tvMainTittle.text =
                    "GatePass Checked out"
                holder.mView.tvDes.text =
                    "Your GatePass has been successfully checked out."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con, OwnerGatePassActivity::class.java
                        ).putExtra("key", "kill_state").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                }

            }

        } else if (prefs.getString(SessionConstants.ROLE, "") == "manager") {
            if (list[position].type == "BILL_PAID") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    /*holder.mView.tvDes.text =
                        "${list[position].amount ?: ""} ৳ Taka generated as ${
                            list[position].categoryData?.get(
                                0
                            )?.name ?: ""
                        } \n due Date :${
                            setFormatDate(list[position].dueDate ?: "")
                        }"*/
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid in"
                } else {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Service Charge Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid"
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(Intent(con, ApprovalBillingManagerActivity::class.java))
                }

            } else if (list[position].type == "NEW_VISITOR") {
                holder.mView.tvMainTittle.text = "New Guest Entry"
                holder.mView.tvDes.text =
                    "${list[position].userName ?: ""} Added a Guest Please check."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, RegularEntryHistoryActivity::class.java).putExtra(
                            "noty",
                            "noty_list"
                        )
                    )
                }
            } else if (list[position].type == "REGULAR_CHECKED_IN_Accept") {
                holder.mView.tvMainTittle.text = "Visitor Check In"
                holder.mView.tvDes.text =
                    "${list[position].flatNumber ?: ""} approved visitor ${list[position].visitorName ?: ""} entry"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, SingleEntryHistoryActivity::class.java).putExtra(
                            "from",
                            "manager"
                        ).putExtra("noty", "noty_list")
                    )
                }

            } else if (list[position].type == "REGULAR_CHECKED_IN_Rejected") {
                holder.mView.tvMainTittle.text = "Visitor Check In"
                holder.mView.tvDes.text =
                    "${list[position].flatNumber ?: ""} rejected visitor ${list[position].visitorName ?: ""} entry"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, SingleEntryHistoryActivity::class.java).putExtra(
                            "from",
                            "manager"
                        ).putExtra("noty", "noty_list")
                    )
                }
            } else if (list[position].type == "GATEPASS_CREATED") {
                holder.mView.tvMainTittle.text = "New Gatepass entered"
                holder.mView.tvDes.text =
                    "A new gatepass has been submitted by the ${list[position].flatNumber ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            ManagerGatePassActivity::class.java
                        ).putExtra("noty", "noty_create_gatepass")
                    )
                }
            } else if (list[position].type == "NEW_COMPLAIN") {
                holder.mView.tvMainTittle.text = "Complain"
                holder.mView.tvDes.text =
                    "A New complain ${list[position].complainName ?: ""} has been registered from ${list[position].flatName ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(Intent(con, RegisterComplaintsActivity::class.java))
                }
            } else if (list[position].type == "COMPLAIN_DENY") {
                holder.mView.tvMainTittle.text = "Complain Denied"
                holder.mView.tvDes.text =
                    "Complain ${list[position].complainName ?: ""} has been marked as Unsolved from ${list[position].flatName ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(Intent(con, RegisterComplaintsActivity::class.java))
                }
            } else if (list[position].type == "COMPLAIN_CONFIRM") {
                holder.mView.tvMainTittle.text = "Complain"
                holder.mView.tvDes.text =
                    "Complain ${list[position].complainName ?: ""} has been marked as solved from ${list[position].flatName ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(Intent(con, RegisterComplaintsActivity::class.java))
                }
            } else if (list[position].type == "RENT_PAID") {
                holder.mView.tvMainTittle.text =
                    "Rent for ${list[position].billMonth} ${list[position].billYear}"
                holder.mView.tvDes.text =
                    "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} has been paid in ${list[position].payType ?: ""}."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            ApprovalBillingManagerActivity::class.java
                        )
                    )
                }
            } else if (list[position].type == "New_Bill_Msg") {
                holder.mView.tvMainTittle.text =
                    "Service Charge ${list[position].billMonth} ${list[position].billYear}"
                holder.mView.tvDes.text =
                    "Service Charge of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} has been Approval is Pending."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            ApprovalBillingManagerActivity::class.java
                        )
                    )
                }
            } else if (list[position].type == "SERVICE_PAID") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Service Charge ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} has been paid in ${list[position].payType ?: ""}."
                } else {
                    holder.mView.tvMainTittle.text =
                        "Service Charge ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "Service Charge of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} has been paid in ${list[position].payType ?: ""}."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            ApprovalBillingManagerActivity::class.java
                        )
                    )
                }
            }

        } else if (prefs.getString(SessionConstants.ROLE, "") == "gatekeeper") {
            if (list[position].type == "REGULAR_CHECKED_IN_Accept") {
                holder.mView.tvMainTittle.text = "Visitor Check In"
                holder.mView.tvDes.text =
                    "${list[position].flatNumber ?: ""} approved visitor ${list[position].visitorName ?: ""} entry"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, SingleEntryHistoryActivity::class.java).putExtra(
                            "noty",
                            "noty_list"
                        )
                    )
                }

            } else if (list[position].type == "REGULAR_CHECKED_IN_Rejected") {
                holder.mView.tvMainTittle.text = "Visitor Check In"
                holder.mView.tvDes.text =
                    "${list[position].flatNumber ?: ""} rejected visitor ${list[position].visitorName ?: ""} entry"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, SingleEntryHistoryActivity::class.java).putExtra(
                            "noty",
                            "noty_list_reject"
                        )
                    )
                }
            } else if (list[position].type == "NEW_VISITOR") {
                holder.mView.tvMainTittle.text = "New Guest Entry"
                holder.mView.tvDes.text =
                    "${list[position].userName ?: ""} Added a Guest Please check."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, RegularEntryHistoryActivity::class.java).putExtra(
                            "noty",
                            "noty_list"
                        )
                    )
                }
            } else if (list[position].type == "GATEPASS_CREATED") {
                holder.mView.tvMainTittle.text = "New Gatepass entered"
                holder.mView.tvDes.text =
                    "A new gatepass has been submitted by the ${list[position].flatNumber ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            MainGateKepperActivity::class.java
                        ).putExtra("from", "from_gate_create_pass")
                    )
                }
            }
        } else {
            if (list[position].type == "REGULAR_CHECKED_OUT") {
                holder.mView.tvMainTittle.text =
                    "Guest Check Out"
                holder.mView.tvDes.text = " Guest ${list[position].visitorName ?: ""} Checked Out"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantRegularEntryHistoryActivity::class.java
                        ).putExtra("from", "tenant")
                    )
                }
            } else if (list[position].type == "REGULAR_CHECKED_IN") {
                holder.mView.tvMainTittle.text =
                    "Guest Check In"
                holder.mView.tvDes.text = "Guest ${list[position].visitorName ?: ""} Checked In"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantRegularEntryHistoryActivity::class.java
                        ).putExtra("from", "tenant")
                    )
                }
            } else if (list[position].type == "NEW_NOTICE") {
                holder.mView.tvMainTittle.text =
                    "New Notice Posted"
                holder.mView.tvDes.text = "New Notice Posted"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con, TenantNoticBoardActivity::class.java
                        )
                    )
                }
            } else if (list[position].type == "NEW_SERVICE_Bill") {
                holder.mView.tvMainTittle.text =
                    "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                holder.mView.tvDes.text =
                    "Service Charge Bill of Taka${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been generated."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java)
                    )
                }
            } else if (list[position].type == "CHECKED_IN") {
                holder.mView.tvMainTittle.text =
                    "Visitor Check In"
                holder.mView.tvDes.text =
                    "You have a new visitor awaiting for your confirmation to check in."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("noty", "noty_list_checkIn").putExtra("from", "tenant")
                    )
                }
            } else if (list[position].type == "CHECKED_OUT") {
                holder.mView.tvMainTittle.text =
                    "Visitor Check Out"
                holder.mView.tvDes.text =
                    "Visitor ${list[position].visitorName ?: ""} checked Out."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            OwnerTenantSingleEntryHistoryActivity::class.java
                        ).putExtra("noty", "noty_list_checkOut").putExtra("from", "tenant")
                    )
                }
            } else if (list[position].type == "NEW_COMPLAIN_RESOLVE") {
                holder.mView.tvMainTittle.text =
                    "Complain Resolved"
                holder.mView.tvDes.text =
                    "Your ${list[position].complainName ?: ""} complain has been resolved, please check."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantRegisterComplainActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "BILL_APPROVED") {
                holder.mView.tvMainTittle.text =
                    "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                holder.mView.tvDes.text =
                    "Successfully paid Service Charge Bill of Taka ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        ).putExtra("key", "paid")
                    )
                }
            } else if (list[position].type == "NEW_RENT_Bill") {
                holder.mView.tvMainTittle.text =
                    "Rent for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                holder.mView.tvDes.text =
                    "Rent bill of ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been generated."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "New_Bill_Msg") {
                /* if (list[position].categoryData?.size ?: 0 > 0) {
                     holder.mView.tvMainTittle.text =
                         "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                     holder.mView.tvDes.text =
                         "Service Charge of ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been generated."
                 } else {*/
                holder.mView.tvMainTittle.text =
                    "Rent for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                holder.mView.tvDes.text =
                    "Rent bill of ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been generated."

                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "NEW_Bill") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Service Charge of Bill of Take ৳ ${list[position].amount ?: ""} for ${list[position].billMonth ?: ""}  ${list[position].billYear ?: ""} has been generated."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }

            } else if (list[position].type == "LIKED_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "COMMENT_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "NEW_POST") {
                holder.mView.tvMainTittle.text =
                    "Community Feed"
                holder.mView.tvDes.text =
                    "${list[position].notiTitle ?: ""}"
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantMyCommunityActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
            } else if (list[position].type == "NEW_BILL_PAID_APPROVED") {
                holder.mView.tvMainTittle.text =
                    "Rent for ${list[position].billMonth} ${list[position].billYear}"
                holder.mView.tvDes.text =
                    "Successfully paid Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear}."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "key",
                            "noti_paid"
                        )
                    )
                }
            } else if (list[position].type == "PAYMENT_PENDING") {
                /* if (list[position].categoryData?.size ?: 0 > 0) {
                     holder.mView.tvMainTittle.text =
                         "Bill for ${list[position].billMonth} ${list[position].billYear}"
                     holder.mView.tvDes.text =
                         "Bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                 } else {
                     holder.mView.tvMainTittle.text =
                         "Rent for ${list[position].billMonth} ${list[position].billYear}"
                     holder.mView.tvDes.text =
                         "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                 }*/
                if (list[position].billType == "Service") {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "Service Charge of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                } else if (list[position].billType == "Normal Bill") {
                    if (list[position].categoryData?.size ?: 0 > 0) {
                        holder.mView.tvMainTittle.text =
                            "Bill for ${list[position].billMonth} ${list[position].billYear}"
                        holder.mView.tvDes.text =
                            "Bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                    }
                } else {
                    holder.mView.tvMainTittle.text =
                        "Rent for ${list[position].billMonth} ${list[position].billYear}"
                    holder.mView.tvDes.text =
                        "Rent bill of Taka ${list[position].amount ?: 0} for ${list[position].billMonth} / ${list[position].billYear} is Pending."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "key",
                            "noti_pending"
                        )
                    )
                }
            } else if (list[position].type == "NEW_BILL_NOTIFY") {
                holder.mView.tvMainTittle.text =
                    "Bill for Pending."
                holder.mView.tvDes.text =
                    "Bill of Taka ${list[position].amount ?: 0} is Pending."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con,
                            TenantBillingsActivity::class.java
                        ).putExtra("key", "noti_All_notify")
                    )
                }
            } else if (list[position].type == "BILL_PAID") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    /*holder.mView.tvDes.text =
                        "${list[position].amount ?: ""} ৳ Taka generated as ${
                            list[position].categoryData?.get(
                                0
                            )?.name ?: ""
                        } \n due Date :${
                            setFormatDate(list[position].dueDate ?: "")
                        }"*/
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid"
                } else {
                    holder.mView.tvMainTittle.text =
                        "Service Charge for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Service Charge Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been paid"
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "key",
                            "notiy_paid"
                        )
                    )
                }

            } else if (list[position].type == "BILL_REJECT") {
                if (!list[position].categoryData.isNullOrEmpty()) {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "${list[position].categoryData?.get(0)?.name ?: ""} Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been Rejected"
                } else {
                    holder.mView.tvMainTittle.text =
                        "Bill for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""}"
                    holder.mView.tvDes.text =
                        "Bill of Taka ${list[position].amount ?: ""} ৳ for ${list[position].billMonth ?: ""} ${list[position].billYear ?: ""} has been Rejected."
                }
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(con, TenantBillingsActivity::class.java).putExtra(
                            "key",
                            "notiy_billReject"
                        )
                    )
                }
            } else if (list[position].type == "GATEPASS_COMPLETE") {
                holder.mView.tvMainTittle.text =
                    "GatePass Checked out"
                holder.mView.tvDes.text =
                    "Your GatePass has been successfully checked out."
                holder.itemView.setOnClickListener {
                    con.startActivity(
                        Intent(
                            con, OwnerGatePassActivity::class.java
                        ).putExtra("key", "kill_state").putExtra("from", "tenant").setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                }

            }

                val time = timeSince(list[position].createdAt ?: "", true)
                holder.mView.textView291.text = time
                /*if (position == 1) {
                holder.mView.confromnow.visibility = View.VISIBLE
                holder.mView.paynow.visibility = View.VISIBLE
            } else if (position == 2) {
                holder.mView.confromnow.visibility = View.INVISIBLE
                holder.mView.paynow.visibility = View.VISIBLE
                holder.mView.paynow.setCardBackgroundColor(Color.parseColor("#FF0F00"))
                holder.mView.tvPayNow.text = con.getString(R.string.reject)
            } else {
                holder.mView.confromnow.visibility = View.GONE
                holder.mView.paynow.visibility = View.GONE
            }*/
                /*  holder.mView.textView110.text = list[position].notiMessage ?: ""
              holder.mView.textView111.text = "Date : ${setFormatDate(list[position].createdAt ?: "")}"*/
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}