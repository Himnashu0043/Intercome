package com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet

data class PdFGenertePostModel(
    var buildingId: String?,
    var expenseCashInBank: String?,
    var expenseCashInHand: String?,
    var expenseFlatData: ArrayList<ExpenseFlatData>?,
    var expenseNote: String?,
    var expenseReportData: ArrayList<ExpenseReportData>?,
    var incomeCashInBank: String?,
    var incomeCashInHand: String?,
    var incomeCategoryAmount: String?,
    var incomeNote: String?,
    var incomeOpeningBallance: String?,
    var incomeReportData: ArrayList<IncomeReportData>?,
    var incomeServiceAmount: String?,
    var totalCashBankBallance: String?,
    var totalEarningInBank: String?,
    var totalEarningInHand: String?,
    var totalEarningIncome: String?,
    var totalExpenseBallance: String?,
    var totalExpenseCategory: String?,
    var totalExpenseInBank: String?,
    var totalExpenseInHand: String?,
    var totalExpense: String?,
    var totalIncome: String?,
    var totalIncomeBallance: String?,
    var dueNote:String?,
    var incomeExpenseNote:String?,
    var monthYear:String
) {
    data class ExpenseFlatData(
        var amount: String?,
        var billDate: String?,
        var month: String?,
        var ownerDetail: String?,
        var paymentDate: String?,
        var status: String?,
        var voucher: String?,
        var flatName:String?,
        var category: String?
    )

    data class ExpenseReportData(
        var amount: String?,
        var billDate: String?,
        var category: String?,
        var month: String?,
        var paymentDate: String?,
        var voucher: String?,
        var payType:String?
    )

    data class IncomeReportData(
        var amount: String?,
        var billDate: String?,
        var category: String?,
        var month: String?,
        var ownerDetail: String?,
        var paymentDate: String?,
        var voucher: String?,
        var payType:String?
    )
}