package com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.DateModel
import com.application.intercom.data.model.local.FIlter_MonthsModel
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.model.local.manager.ManagerAddNoticeBoardPostModel
import com.application.intercom.data.model.local.manager.gatePass.ManagerAddGatePassPostModel
import com.application.intercom.data.model.local.manager.managerSide.actionToComplain.ManagerActionToComplainPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.EditPendingManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerAddBillPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerBillCategoryListRes
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerCreateGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerEditGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.AddExpensesManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.AddNoteBalanceSheetManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.PdFGenertePostModel
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerNewEditServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerSecondEditServiceChargeModel
import com.application.intercom.data.model.local.newFlow.PayUnPaidManagerPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.*
import com.application.intercom.data.model.remote.manager.managerSide.ManagerGateKepperListRes
import com.application.intercom.data.model.remote.manager.managerSide.bill.*
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.model.remote.manager.managerSide.finance.BillCountManagerRes
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.*
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerAddGatePassListRes
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerFlatOfBuildingListRes
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.data.model.remote.manager.managerSide.newflow.*
import com.application.intercom.data.model.remote.manager.managerSide.noticeBoard.ManagerNoticeBoardListRes
import com.application.intercom.data.model.remote.manager.managerSide.notifyuser.ManagerNotifyRes
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.*
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo

import kotlinx.coroutines.launch

class ManagerSideViewModel(private val repository: ManagerSideRepo) : ViewModel() {
    private val _gateKeeperListLiveData = MutableLiveData<EmpResource<ManagerGateKepperListRes>>()
    val gateKeeperListLiveData: LiveData<EmpResource<ManagerGateKepperListRes>>
        get() = _gateKeeperListLiveData

    fun gateKeeperList(token: String) {
        viewModelScope.launch {
            _gateKeeperListLiveData.value = EmpResource.Loading
            _gateKeeperListLiveData.value = repository.managerGateKeeperList(token)
        }
    }

    private val _billCountManagerLiveData = MutableLiveData<EmpResource<BillCountManagerRes>>()
    val billCountManagerLiveData: LiveData<EmpResource<BillCountManagerRes>>
        get() = _billCountManagerLiveData

    fun billCountManagerRes(token: String, buildingId: String) {
        viewModelScope.launch {
            _billCountManagerLiveData.value = EmpResource.Loading
            _billCountManagerLiveData.value = repository.billCountManagerRes(token, buildingId)
        }
    }

    private val _pdfManagerLiveData = MutableLiveData<EmpResource<PdfGenerateRes>>()
    val pdfManagerLiveData: LiveData<EmpResource<PdfGenerateRes>>
        get() = _pdfManagerLiveData

    fun pdfManagerRes(token: String, model: PdFGenertePostModel) {
        viewModelScope.launch {
            _pdfManagerLiveData.value = EmpResource.Loading
            _pdfManagerLiveData.value = repository.pdfManager(token, model)
        }
    }

    private val _dueReportManagerLiveData = MutableLiveData<EmpResource<DueReportManagerList>>()
    val dueReportManagerLiveData: LiveData<EmpResource<DueReportManagerList>>
        get() = _dueReportManagerLiveData

    fun dueReportManager(token: String, model: IncomeReportManagerPostModel) {
        viewModelScope.launch {
            _dueReportManagerLiveData.value = EmpResource.Loading
            _dueReportManagerLiveData.value = repository.dueReportManager(token, model)
        }
    }

    private val _deleteUnPaidManagerLiveData =
        MutableLiveData<EmpResource<DeleteUnPaidManagerRes>>()
    val deleteUnPaidManagerLiveData: LiveData<EmpResource<DeleteUnPaidManagerRes>>
        get() = _deleteUnPaidManagerLiveData

    fun deleteUnPaidManager(token: String, billId: String) {
        viewModelScope.launch {
            _deleteUnPaidManagerLiveData.value = EmpResource.Loading
            _deleteUnPaidManagerLiveData.value = repository.deleteUnPaidManager(token, billId)
        }
    }

    private val _editUnPaidManagerLiveData =
        MutableLiveData<EmpResource<EditUnPaidManagerRes>>()
    val editUnPaidManagerLiveData: LiveData<EmpResource<EditUnPaidManagerRes>>
        get() = _editUnPaidManagerLiveData

    fun editUnPaidManager(token: String, model: EditUnPaidManagerPostModel) {
        viewModelScope.launch {
            _editUnPaidManagerLiveData.value = EmpResource.Loading
            _editUnPaidManagerLiveData.value = repository.editUnPaidManager(token, model)
        }
    }

    private val _noticeBoardListLiveData = MutableLiveData<EmpResource<ManagerNoticeBoardListRes>>()
    val noticeBoardListLiveData: LiveData<EmpResource<ManagerNoticeBoardListRes>>
        get() = _noticeBoardListLiveData

    fun noticeBoardList(token: String, _id: String?) {
        viewModelScope.launch {
            _noticeBoardListLiveData.value = EmpResource.Loading
            _noticeBoardListLiveData.value = repository.managerNoticeBoardList(token, _id)
        }
    }

    private val _addExpensesManagerLiveData = MutableLiveData<EmpResource<AddExpensesManagerRes>>()
    val addExpensesManagerLiveData: LiveData<EmpResource<AddExpensesManagerRes>>
        get() = _addExpensesManagerLiveData

    fun addExpensesManager(token: String, model: AddExpensesManagerPostModel) {
        viewModelScope.launch {
            _addExpensesManagerLiveData.value = EmpResource.Loading
            _addExpensesManagerLiveData.value = repository.addExpensesManager(token, model)
        }
    }

    private val _addNoticeBoardListLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val addNoticeBoardLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _addNoticeBoardListLiveData

    fun addnoticeBoard(token: String, model: ManagerAddNoticeBoardPostModel) {
        viewModelScope.launch {
            _addNoticeBoardListLiveData.value = EmpResource.Loading
            _addNoticeBoardListLiveData.value = repository.addNoticeBoard(token, model)
        }
    }

    private val _createGateKeeperListLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val createGateKeeperLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _createGateKeeperListLiveData

    fun createGateKeeper(token: String, model: ManagerCreateGateKeeperPostModel) {
        viewModelScope.launch {
            _createGateKeeperListLiveData.value = EmpResource.Loading
            _createGateKeeperListLiveData.value = repository.createGateKeeper(token, model)
        }
    }

    private val _editGateKeeperListLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val editGateKeeperLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _editGateKeeperListLiveData

    fun editGateKeeper(token: String, model: ManagerEditGateKeeperPostModel) {
        viewModelScope.launch {
            _editGateKeeperListLiveData.value = EmpResource.Loading
            _editGateKeeperListLiveData.value = repository.editGateKeeper(token, model)
        }
    }

    private val _flatOfBuildingListLiveData =
        MutableLiveData<EmpResource<ManagerFlatOfBuildingListRes>>()
    val flatOfBuildingListLiveData: LiveData<EmpResource<ManagerFlatOfBuildingListRes>>
        get() = _flatOfBuildingListLiveData

    private val _managerFlatBuildingIDLiveData = MutableLiveData<String>()
    val managerFlatBuildingIDLiveData: MutableLiveData<String>
        get() = _managerFlatBuildingIDLiveData

    private val _managerFilterKeyLiveData = MutableLiveData<FIlter_MonthsModel>()
    val managerFilterKeyLiveData: MutableLiveData<FIlter_MonthsModel>
        get() = _managerFilterKeyLiveData

    fun flatOfBuildingList(token: String) {
        viewModelScope.launch {
            _flatOfBuildingListLiveData.value = EmpResource.Loading
            _flatOfBuildingListLiveData.value = repository.flatOfBuildingList(token)
        }
    }

    private val _addGatePassLiveData = MutableLiveData<EmpResource<ManagerAddGatePassListRes>>()
    val addGatePassLiveData: LiveData<EmpResource<ManagerAddGatePassListRes>>
        get() = _addGatePassLiveData

    fun addGatePass(token: String, model: ManagerAddGatePassPostModel) {
        viewModelScope.launch {
            _addGatePassLiveData.value = EmpResource.Loading
            _addGatePassLiveData.value = repository.addGatePass(token, model)
        }
    }

    private val _expensesCategoryListLiveData =
        MutableLiveData<EmpResource<ExpensesCategoryManagerRes>>()
    val expensesCategoryListLiveData: LiveData<EmpResource<ExpensesCategoryManagerRes>>
        get() = _expensesCategoryListLiveData

    fun expensesCategoryList(token: String) {
        viewModelScope.launch {
            _expensesCategoryListLiveData.value = EmpResource.Loading
            _expensesCategoryListLiveData.value = repository.expensesCategoryList(token)
        }
    }

    private val _gatePassHistoryListLiveData =
        MutableLiveData<EmpResource<ManagerGatePassHistoryListRes>>()
    val gatePassHistoryListLiveData: LiveData<EmpResource<ManagerGatePassHistoryListRes>>
        get() = _gatePassHistoryListLiveData

    fun gatePassHistoryList(token: String) {
        viewModelScope.launch {
            _gatePassHistoryListLiveData.value = EmpResource.Loading
            _gatePassHistoryListLiveData.value = repository.gatePassList(token)
        }
    }

    private val _pendingComplainLiveData = MutableLiveData<EmpResource<ManagerPendingListRes>>()
    val pendingComplainLiveData: LiveData<EmpResource<ManagerPendingListRes>>
        get() = _pendingComplainLiveData

    fun pendingComplain(token: String, status: String) {
        viewModelScope.launch {
            _pendingComplainLiveData.value = EmpResource.Loading
            _pendingComplainLiveData.value = repository.pendingComplainList(token, status)
        }
    }

    private val _actionToComplainLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val actionToComplainLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _actionToComplainLiveData

    fun actionToComplain(token: String, model: ManagerActionToComplainPostModel) {
        viewModelScope.launch {
            _actionToComplainLiveData.value = EmpResource.Loading
            _actionToComplainLiveData.value = repository.managerActionToComplain(token, model)
        }
    }

    private val _billpendingLiveData = MutableLiveData<EmpResource<MangerBillPendingListRes>>()
    val billpendingLiveData: LiveData<EmpResource<MangerBillPendingListRes>>
        get() = _billpendingLiveData
    private val _billPaidLiveData = MutableLiveData<EmpResource<MangerBillPendingListRes>>()
    val billPaidLiveData: LiveData<EmpResource<MangerBillPendingListRes>>
        get() = _billPaidLiveData

    private val _billApproveLiveData = MutableLiveData<EmpResource<MangerBillPendingListRes>>()
    val billApproveLiveData: LiveData<EmpResource<MangerBillPendingListRes>>
        get() = _billApproveLiveData

    fun billpending(token: String, status: String, flatId: String?) {
        viewModelScope.launch {
            if (status == "Pending") {
                _billpendingLiveData.value = EmpResource.Loading
                _billpendingLiveData.value = repository.billpendingList(token, status, flatId)
            } else if (status == "Paid") {
                _billPaidLiveData.value = EmpResource.Loading
                _billPaidLiveData.value = repository.billpendingList(token, status, flatId)
            } else {
                _billApproveLiveData.value = EmpResource.Loading
                _billApproveLiveData.value = repository.billpendingList(token, status, flatId)
            }
        }
    }

    private val _billTestpendingLiveData = MutableLiveData<EmpResource<MangerBillPendingListRes>>()
    val billTestpendingLiveData: LiveData<EmpResource<MangerBillPendingListRes>>
        get() = _billpendingLiveData


    fun billTestpending(token: String, status: String) {
        viewModelScope.launch {
            _billTestpendingLiveData.value = EmpResource.Loading
            _billTestpendingLiveData.value = repository.billTestpendingList(token, status)
        }
    }

    private val _addbillMangerLiveData = MutableLiveData<EmpResource<AddManagerBillRes>>()
    val addbilManagerLiveData: LiveData<EmpResource<AddManagerBillRes>>
        get() = _addbillMangerLiveData

    fun addbillManager(token: String, model: ManagerAddBillPostModel) {
        viewModelScope.launch {
            _addbillMangerLiveData.value = EmpResource.Loading
            _addbillMangerLiveData.value = repository.addBillmanager(token, model)
        }
    }

    private val _billCategoryListMangerLiveData =
        MutableLiveData<EmpResource<ManagerBillCategoryListRes>>()
    val bilCategoryListManagerLiveData: LiveData<EmpResource<ManagerBillCategoryListRes>>
        get() = _billCategoryListMangerLiveData

    fun billCategoryListManager(token: String) {
        viewModelScope.launch {
            _billCategoryListMangerLiveData.value = EmpResource.Loading
            _billCategoryListMangerLiveData.value = repository.billCategorymanagerList(token)
        }
    }

    private val _markAsPaidMangerLiveData = MutableLiveData<EmpResource<ManagerMarkAsPaidRes>>()
    val markAsPaidManagerLiveData: LiveData<EmpResource<ManagerMarkAsPaidRes>>
        get() = _markAsPaidMangerLiveData

    fun markAsPaidManager(token: String, billId: String) {
        viewModelScope.launch {
            _markAsPaidMangerLiveData.value = EmpResource.Loading
            _markAsPaidMangerLiveData.value = repository.markAsPaidManagerRes(token, billId)
        }
    }

    private val _rejectBillMangerLiveData = MutableLiveData<EmpResource<RejectBillManagerRes>>()
    val rejectBillManagerLiveData: LiveData<EmpResource<RejectBillManagerRes>>
        get() = _rejectBillMangerLiveData

    fun rejectBillManager(token: String, billId: String, rejectReason: String) {
        viewModelScope.launch {
            _rejectBillMangerLiveData.value = EmpResource.Loading
            _rejectBillMangerLiveData.value =
                repository.rejectBillManager(token, billId, rejectReason)
        }
    }

    private val _deleteUnPaidBillMangerLiveData =
        MutableLiveData<EmpResource<DeleteUnPaidManagerRes>>()
    val deleteUnPaidBillManagerLiveData: LiveData<EmpResource<DeleteUnPaidManagerRes>>
        get() = _deleteUnPaidBillMangerLiveData

    fun deleteUnPaidBillManager(token: String, billId: String) {
        viewModelScope.launch {
            _deleteUnPaidBillMangerLiveData.value = EmpResource.Loading
            _deleteUnPaidBillMangerLiveData.value =
                repository.deleteUnPaidBillManagerRes(token, billId)
        }
    }

    private val _addServiceChargeMangerLiveData =
        MutableLiveData<EmpResource<ManagerAddServiceChargesListRes>>()
    val addServiceChargeManagerLiveData: LiveData<EmpResource<ManagerAddServiceChargesListRes>>
        get() = _addServiceChargeMangerLiveData

    fun addServiceChargeManager(token: String, model: ManagerAddServiceChargePostModel) {
        viewModelScope.launch {
            _addServiceChargeMangerLiveData.value = EmpResource.Loading
            _addServiceChargeMangerLiveData.value = repository.addServiceCharge(token, model)
        }
    }

    private val _addRentMangerLiveData =
        MutableLiveData<EmpResource<ManagerAddServiceChargesListRes>>()
    val addRentManagerLiveData: LiveData<EmpResource<ManagerAddServiceChargesListRes>>
        get() = _addRentMangerLiveData

    fun addRentManager(token: String, model: AddRentManagerPostModel) {
        viewModelScope.launch {
            _addRentMangerLiveData.value = EmpResource.Loading
            _addRentMangerLiveData.value = repository.addRentManager(token, model)
        }
    }

    private val _viewServiceChargeMangerLiveData =
        MutableLiveData<EmpResource<ManagerServiceChargeList>>()
    val viewServiceChargeManagerLiveData: LiveData<EmpResource<ManagerServiceChargeList>>
        get() = _viewServiceChargeMangerLiveData

    fun viewServiceChargeManager(token: String) {
        viewModelScope.launch {
            _viewServiceChargeMangerLiveData.value = EmpResource.Loading
            _viewServiceChargeMangerLiveData.value = repository.viewServiceCharge(token)
        }
    }

    private val _getRentLiveData =
        MutableLiveData<EmpResource<RentManagerListRes>>()
    val _getRentManagerLiveData: LiveData<EmpResource<RentManagerListRes>>
        get() = _getRentLiveData

    fun getRentManager(token: String) {
        viewModelScope.launch {
            _getRentLiveData.value = EmpResource.Loading
            _getRentLiveData.value = repository.getRentApi(token)
        }
    }

    private val _editServiceChargeMangerLiveData =
        MutableLiveData<EmpResource<ManagerEditServiceChargeListRes>>()
    val editServiceChargeManagerLiveData: LiveData<EmpResource<ManagerEditServiceChargeListRes>>
        get() = _editServiceChargeMangerLiveData

    fun editServiceChargeManager(token: String, model: ManagerNewEditServiceChargePostModel) {
        viewModelScope.launch {
            _editServiceChargeMangerLiveData.value = EmpResource.Loading
            _editServiceChargeMangerLiveData.value = repository.editServiceCharge(token, model)
        }
    }

    private val _deleteServiceChargeListMangerLiveData =
        MutableLiveData<EmpResource<ManagerDeleteServiceChargeList>>()
    val deleteServiceChargeListManagerLiveData: LiveData<EmpResource<ManagerDeleteServiceChargeList>>
        get() = _deleteServiceChargeListMangerLiveData

    fun deleteServiceChargeListManager(token: String, chargeId: String) {
        viewModelScope.launch {
            _deleteServiceChargeListMangerLiveData.value = EmpResource.Loading
            _deleteServiceChargeListMangerLiveData.value =
                repository.deleteServiceChargeList(token, chargeId)
        }
    }


    private val _manager_singleEntryHistroyListLiveData =
        MutableLiveData<EmpResource<SingleEntryHistoryList>>()
    val manager_singleEntryHistoryListLiveData: LiveData<EmpResource<SingleEntryHistoryList>>
        get() = _manager_singleEntryHistroyListLiveData

    fun manager_singleEntryHistoryList(token: String, visitorStatus: String, flatId: String) {
        viewModelScope.launch {
            _manager_singleEntryHistroyListLiveData.value = EmpResource.Loading
            _manager_singleEntryHistroyListLiveData.value =
                repository.manager_SingleEntryHistoryList(token, visitorStatus, flatId)
        }
    }


    private val _managerVisitorNotifyToUserLiveData =
        MutableLiveData<EmpResource<VisitorNotifyToUserRes>>()
    val managerVisitorNotifyToUserLiveData: LiveData<EmpResource<VisitorNotifyToUserRes>>
        get() = _managerVisitorNotifyToUserLiveData

    fun managerVisitorNotifyToUser(token: String, visitorId: String) {
        viewModelScope.launch {
            _managerVisitorNotifyToUserLiveData.value = EmpResource.Loading
            _managerVisitorNotifyToUserLiveData.value =
                repository.managerVisitorNotifyToUser(token, visitorId)
        }
    }

    private val _managerOutSingleVisitorEntryGateKeeperListLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val managerOutSingleVisitorEntryGateKeeperLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _managerOutSingleVisitorEntryGateKeeperListLiveData

    fun managerOutSingleVisitorEntryGateKeeper(token: String, visitorId: String) {
        viewModelScope.launch {
            _managerOutSingleVisitorEntryGateKeeperListLiveData.value = EmpResource.Loading
            _managerOutSingleVisitorEntryGateKeeperListLiveData.value =
                repository.managerOutSingleVisitorEntryGateKeeper(token, visitorId)
        }
    }

    private val _managerAddSingleEntryLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val managerAddSingleEntryLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _managerAddSingleEntryLiveData

    fun managerAddSingleEntry(token: String, model: AddSingleEntryGateKeeperPostModel) {
        viewModelScope.launch {
            _managerAddSingleEntryLiveData.value = EmpResource.Loading
            _managerAddSingleEntryLiveData.value = repository.managerAddSingleEntry(token, model)
        }
    }

    private val _managerRegularVisitorHistoryListLiveData =
        MutableLiveData<EmpResource<RegularVisitorGateKeeperList>>()
    val managerRegularVisitorHistoryListLiveData: LiveData<EmpResource<RegularVisitorGateKeeperList>>
        get() = _managerRegularVisitorHistoryListLiveData

    fun managerRegularVisitorHistoryList(
        token: String,
        visitorStatus: String,
        flatId: String,
        buildingId: String
    ) {
        viewModelScope.launch {
            _managerRegularVisitorHistoryListLiveData.value = EmpResource.Loading
            _managerRegularVisitorHistoryListLiveData.value =
                repository.managerRegularVisitorHistoryGateKeeper(
                    token,
                    visitorStatus,
                    flatId,
                    buildingId
                )
        }
    }

    private val _managerAddRegularVisitorEntryLiveData =
        MutableLiveData<EmpResource<AddRegularVisitorEntryRes>>()
    val managerAddRegularVisitorEntryLiveData: LiveData<EmpResource<AddRegularVisitorEntryRes>>
        get() = _managerAddRegularVisitorEntryLiveData

    fun managerAddRegularVisitorEntry(token: String, model: AddRegularVisitorEntryPostModel) {
        viewModelScope.launch {
            _managerAddRegularVisitorEntryLiveData.value = EmpResource.Loading
            _managerAddRegularVisitorEntryLiveData.value =
                repository.managerAddRegularVisitorEntry(token, model)
        }
    }

    private val _managerOutRegularVisitorEntryLiveData =
        MutableLiveData<EmpResource<OutEntryGateKeeperRes>>()
    val managerOutRegularVisitorEntryLiveData: LiveData<EmpResource<OutEntryGateKeeperRes>>
        get() = _managerOutRegularVisitorEntryLiveData

    fun managerOutRegularVisitorEntry(token: String, visitorId: String) {
        viewModelScope.launch {
            _managerOutRegularVisitorEntryLiveData.value = EmpResource.Loading
            _managerOutRegularVisitorEntryLiveData.value =
                repository.managerOutRegularVisitorEntry(token, visitorId)
        }
    }

    private val _managerFlatListOfVisitorLiveData =
        MutableLiveData<EmpResource<FlatListOfVisitorGateKeeperList>>()
    val managerFlatListOfVisitorLiveData: LiveData<EmpResource<FlatListOfVisitorGateKeeperList>>
        get() = _managerFlatListOfVisitorLiveData

    fun managerFlatListOfVisitor(token: String, flatId: String) {
        viewModelScope.launch {
            _managerFlatListOfVisitorLiveData.value = EmpResource.Loading
            _managerFlatListOfVisitorLiveData.value =
                repository.managerFlatListOfVisitor(token, flatId)
        }
    }

    private val _managerGateKeeperListLiveData =
        MutableLiveData<EmpResource<GateKeeperListRes>>()
    val managerGateKeeperListLiveData: LiveData<EmpResource<GateKeeperListRes>>
        get() = _managerGateKeeperListLiveData

    fun managergateKeeperList(token: String, buildingId: String?, flatId: String?, status: String) {
        viewModelScope.launch {
            _managerGateKeeperListLiveData.value = EmpResource.Loading
            _managerGateKeeperListLiveData.value =
                repository.managerGateKeeperList(token, buildingId, flatId, status)
        }
    }

    private val _exitGatePassManagerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val exitGatePassManagerListLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _exitGatePassManagerLiveData

    fun exitGatePassManager(token: String, gatePassId: String) {
        viewModelScope.launch {
            _exitGatePassManagerLiveData.value = EmpResource.Loading
            _exitGatePassManagerLiveData.value =
                repository.exitGatePassManager(token, gatePassId)
        }
    }

    private val _managerNotifyUserLiveData =
        MutableLiveData<EmpResource<ManagerNotifyRes>>()
    val managerNotifyUserLiveData: LiveData<EmpResource<ManagerNotifyRes>>
        get() = _managerNotifyUserLiveData

    fun managerNotifyUser(token: String, billId: String) {
        viewModelScope.launch {
            _managerNotifyUserLiveData.value = EmpResource.Loading
            _managerNotifyUserLiveData.value =
                repository.managerNotifyUser(token, billId)
        }
    }

    private val _managerNotifyAllLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val managerNotifyAllLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _managerNotifyAllLiveData

    fun managerNotifyAll(token: String, flatId: String) {
        viewModelScope.launch {
            _managerNotifyAllLiveData.value = EmpResource.Loading
            _managerNotifyAllLiveData.value =
                repository.managerNotifyAll(token, flatId)
        }
    }

    private val _editBillPendingManagerLiveData =
        MutableLiveData<EmpResource<EditPendingBillManagerRes>>()
    val editBillPendingLiveData: LiveData<EmpResource<EditPendingBillManagerRes>>
        get() = _editBillPendingManagerLiveData

    fun editBillPending(token: String, model: EditPendingManagerPostModel) {
        viewModelScope.launch {
            _editBillPendingManagerLiveData.value = EmpResource.Loading
            _editBillPendingManagerLiveData.value = repository.editBillPending(token, model)
        }
    }

    private val _editRentManagerLiveData =
        MutableLiveData<EmpResource<EditPendingBillManagerRes>>()
    val editRentBillLiveData: LiveData<EmpResource<EditPendingBillManagerRes>>
        get() = _editRentManagerLiveData

    fun editRentBill(token: String, model: RentEditManagerPostModel) {
        viewModelScope.launch {
            _editRentManagerLiveData.value = EmpResource.Loading
            _editRentManagerLiveData.value = repository.editRentBill(token, model)
        }
    }


    private val _unpaidExpensesListManagerLiveData =
        MutableLiveData<EmpResource<UnPaidExpensesManagerRes>>()
    val unpaidExpensesLiveData: LiveData<EmpResource<UnPaidExpensesManagerRes>>
        get() = _unpaidExpensesListManagerLiveData

    fun unpaidExpensesList(token: String, billStatus: String, buildingId: String) {
        viewModelScope.launch {
            _unpaidExpensesListManagerLiveData.value = EmpResource.Loading
            _unpaidExpensesListManagerLiveData.value =
                repository.unpaidExpensesList(token, billStatus, buildingId)
        }
    }

    private val _payUnPaidManagerLiveData =
        MutableLiveData<EmpResource<CommonResponse>>()
    val payUnPaidManagerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _payUnPaidManagerLiveData

    fun payUnPaidManager(token: String, model: PayUnPaidManagerPostModel) {
        viewModelScope.launch {
            _payUnPaidManagerLiveData.value = EmpResource.Loading
            _payUnPaidManagerLiveData.value =
                repository.payUnPaidManager(token, model)
        }
    }

    private val _incomeReportManagerLiveData =
        MutableLiveData<EmpResource<IncomeReportManagerList>>()
    val incomeReportManagerLiveData: LiveData<EmpResource<IncomeReportManagerList>>
        get() = _incomeReportManagerLiveData

    private val _dateFilterKeyLiveData = MutableLiveData<DateModel>()
    val dateFilterKeyLiveData: MutableLiveData<DateModel>
        get() = _dateFilterKeyLiveData

    fun incomeReportManager(token: String, model: IncomeReportManagerPostModel) {
        viewModelScope.launch {
            _incomeReportManagerLiveData.value = EmpResource.Loading
            _incomeReportManagerLiveData.value =
                repository.incomeReportManager(token, model)
        }
    }

    private val _expensesReportManagerLiveData =
        MutableLiveData<EmpResource<ExpensesReportsManagerList>>()
    val expensesReportManagerLiveData: LiveData<EmpResource<ExpensesReportsManagerList>>
        get() = _expensesReportManagerLiveData

    private val _dateExpFilterKeyLiveData = MutableLiveData<DateModel>()
    val dateExpFilterKeyLiveData: MutableLiveData<DateModel>
        get() = _dateExpFilterKeyLiveData

    fun expensesReportManager(token: String, model: IncomeReportManagerPostModel) {
        viewModelScope.launch {
            _expensesReportManagerLiveData.value = EmpResource.Loading
            _expensesReportManagerLiveData.value =
                repository.expensesReportManager(token, model)
        }
    }

    private val _addNoteManagerLiveData =
        MutableLiveData<EmpResource<AddNoteBalanceSheetRes>>()
    val addNoteManagerLiveData: LiveData<EmpResource<AddNoteBalanceSheetRes>>
        get() = _addNoteManagerLiveData

    fun addNoteManager(token: String, model: AddNoteBalanceSheetManagerPostModel) {
        viewModelScope.launch {
            _addNoteManagerLiveData.value = EmpResource.Loading
            _addNoteManagerLiveData.value = repository.addNoteManagerRes(token, model)
        }
    }

}