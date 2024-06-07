package com.application.intercom.data.repository.managerRepo.managerSideRepo

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.model.local.manager.ManagerAddNoticeBoardPostModel
import com.application.intercom.data.model.local.manager.gatePass.ManagerAddGatePassPostModel
import com.application.intercom.data.model.local.manager.managerSide.actionToComplain.ManagerActionToComplainPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.EditPendingManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerAddBillPostModel
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerCreateGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerEditGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.AddExpensesManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.AddNoteBalanceSheetManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.PdFGenertePostModel
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerEditServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerNewEditServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerSecondEditServiceChargeModel
import com.application.intercom.data.model.local.newFlow.PayUnPaidManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.newflow.EditUnPaidManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.repository.EmpBaseRepository
import retrofit2.http.Query

class ManagerSideRepo(private val apiService: ApiService) : EmpBaseRepository() {
    suspend fun managerGateKeeperList(token: String) = safeApiCall {
        apiService.getGateKeeperList(token)
    }

    suspend fun billCountManagerRes(token: String, buildingId: String) = safeApiCall {
        apiService.billCountManagerList(token, buildingId)
    }
    suspend fun deleteUnPaidBillManagerRes(token: String, billId: String) = safeApiCall {
        apiService.deleteUnPaidList(token, billId)
    }

    suspend fun addNoteManagerRes(token: String, model: AddNoteBalanceSheetManagerPostModel) =
        safeApiCall {
            apiService.addNoteManager(token, model)
        }

    suspend fun incomeReportManager(token: String, model: IncomeReportManagerPostModel) =
        safeApiCall {
            apiService.incomeReportManager(token, model)
        }

    suspend fun pdfManager(token: String, model: PdFGenertePostModel) =
        safeApiCall {
            apiService.pdfManager(token, model)
        }

    suspend fun dueReportManager(token: String, model: IncomeReportManagerPostModel) =
        safeApiCall {
            apiService.dueReportManagerList(token, model)
        }

    suspend fun expensesReportManager(token: String, model: IncomeReportManagerPostModel) =
        safeApiCall {
            apiService.expensesReportManager(token, model)
        }

    suspend fun managerNoticeBoardList(token: String, _id: String?) = safeApiCall {
        apiService.getNoticeBoardList(token, _id)
    }

    suspend fun deleteUnPaidManager(token: String, billId: String) = safeApiCall {
        apiService.deleteUnpaidManager(token, billId)
    }

    suspend fun editUnPaidManager(token: String, model: EditUnPaidManagerPostModel) = safeApiCall {
        apiService.editUnPaidManager(token, model)
    }

    suspend fun addNoticeBoard(token: String, model: ManagerAddNoticeBoardPostModel) = safeApiCall {
        apiService.addNoticeBoard(token, model)
    }

    suspend fun addExpensesManager(token: String, model: AddExpensesManagerPostModel) =
        safeApiCall {
            apiService.addExpensesManager(token, model)
        }

    suspend fun createGateKeeper(token: String, model: ManagerCreateGateKeeperPostModel) =
        safeApiCall {
            apiService.createGateKeeper(token, model)
        }

    suspend fun editGateKeeper(token: String, model: ManagerEditGateKeeperPostModel) = safeApiCall {
        apiService.editGateKeeper(token, model)
    }

    suspend fun flatOfBuildingList(token: String) = safeApiCall {
        apiService.getFlatOfBuildingList(token)
    }

    suspend fun expensesCategoryList(token: String) = safeApiCall {
        apiService.expensesCategoryListManager(token)
    }

    suspend fun addGatePass(token: String, model: ManagerAddGatePassPostModel) = safeApiCall {
        apiService.addGatePass(token, model)
    }

    suspend fun editBillPending(token: String, model: EditPendingManagerPostModel) = safeApiCall {
        apiService.editBillPendingManager(token, model)
    }

    suspend fun editRentBill(token: String, model: RentEditManagerPostModel) = safeApiCall {
        apiService.editRentManager(token, model)
    }

    suspend fun gatePassList(token: String) = safeApiCall {
        apiService.gatePassHistoryList(token)
    }

    suspend fun pendingComplainList(token: String, status: String) = safeApiCall {
        apiService.getMangerPendingComplainList(token, status)
    }

    suspend fun managerActionToComplain(token: String, model: ManagerActionToComplainPostModel) =
        safeApiCall {
            apiService.managerActiontoComplain(token, model)
        }

    suspend fun billpendingList(token: String, status: String, flatId: String?) = safeApiCall {
        apiService.getBillMangerPendingList(token, status, flatId)
    }

    suspend fun billTestpendingList(token: String, status: String) = safeApiCall {
        apiService.getTestBillMangerPendingList(token, status)
    }

    suspend fun payUnPaidManager(token: String, model: PayUnPaidManagerPostModel) = safeApiCall {
        apiService.payUnPaidManager(token, model)
    }

    suspend fun addBillmanager(
        token: String,
        model: ManagerAddBillPostModel
    ) =
        safeApiCall {
            apiService.addBillmanager(token, model)
        }

    suspend fun billCategorymanagerList(
        token: String,
    ) =
        safeApiCall {
            apiService.billCategorymanagerList(token)
        }

    suspend fun markAsPaidManagerRes(token: String, billId: String) =
        safeApiCall {
            apiService.markAsPaidMangerRes(token, billId)
        }
    suspend fun rejectBillManager(token: String, billId: String,rejectReason: String) =
        safeApiCall {
            apiService.rejectBillManager(token, billId,rejectReason)
        }

    suspend fun addServiceCharge(token: String, model: ManagerAddServiceChargePostModel) =
        safeApiCall {
            apiService.addServiceCharge(token, model)
        }
    suspend fun addRentManager(token: String, model: AddRentManagerPostModel) =
        safeApiCall {
            apiService.addRent(token, model)
        }

    suspend fun viewServiceCharge(
        token: String,
    ) =
        safeApiCall {
            apiService.viewServiceCharge(token)
        }

    suspend fun getRentApi(
        token: String,
    ) =
        safeApiCall {
            apiService.getRent(token)
        }

    suspend fun editServiceCharge(token: String, model: ManagerNewEditServiceChargePostModel) =
        safeApiCall {
            apiService.editServiceCharge(token, model)
        }

    suspend fun deleteServiceChargeList(token: String, chargeId: String) =
        safeApiCall {
            apiService.deleteServiceCharge(token, chargeId)
        }

    suspend fun manager_SingleEntryHistoryList(
        token: String,
        visitorStatus: String,
        flatId: String
    ) =
        safeApiCall {
            apiService.manager_singleEntryHistroyList(token, visitorStatus, flatId)
        }


    suspend fun managerVisitorNotifyToUser(token: String, visitorId: String) =
        safeApiCall {
            apiService.manager_visitorNotifyToUserRes(token, visitorId)
        }

    suspend fun managerOutSingleVisitorEntryGateKeeper(token: String, visitorId: String) =
        safeApiCall {
            apiService.managerOutSingleVisitorEntryGateKeeper(token, visitorId)
        }

    suspend fun managerAddSingleEntry(token: String, model: AddSingleEntryGateKeeperPostModel) =
        safeApiCall {
            apiService.managerAddSingleEntry(token, model)
        }

    suspend fun managerRegularVisitorHistoryGateKeeper(
        token: String,
        visitorStatus: String,
        flatId: String,
        buildingId: String
    ) =
        safeApiCall {
            apiService.managerRegularVisitorHistoryList(token, visitorStatus, flatId, buildingId)
        }

    suspend fun managerAddRegularVisitorEntry(
        token: String,
        model: AddRegularVisitorEntryPostModel
    ) =
        safeApiCall {
            apiService.managerAddRegularVisitorEntry(token, model)
        }

    suspend fun managerOutRegularVisitorEntry(token: String, visitorId: String) =
        safeApiCall {
            apiService.managerOutEntryGateKeeper(token, visitorId)
        }

    suspend fun managerFlatListOfVisitor(token: String, flatId: String) =
        safeApiCall {
            apiService.managerFlatListOfVisitor(token, flatId)
        }

    suspend fun managerGateKeeperList(
        token: String,
        buildingId: String?,
        flatId: String?,
        status: String
    ) =
        safeApiCall {
            apiService.managerGateKeeperList(token, buildingId, flatId, status)
        }
    suspend fun exitGatePassManager(token: String, gatePassId: String) =
        safeApiCall {
            apiService.exitGatePassManager(token, gatePassId)
        }
    suspend fun managerNotifyUser(token: String, billId: String) =
        safeApiCall {
            apiService.managerNotifyRes(token, billId)
        }
    suspend fun managerNotifyAll(token: String, flatId: String) =
        safeApiCall {
            apiService.managerNotifyAll(token, flatId)
        }
    suspend fun unpaidExpensesList(token: String, billStatus: String, buildingId: String) =
        safeApiCall {
            apiService.unpaidExpensesList(token, billStatus, buildingId)
        }
}
