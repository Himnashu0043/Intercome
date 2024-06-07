package com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet

data class ExpensesReportsManagerList(
    var data: Data?,
    var message: String?,
    var status: Int?
):java.io.Serializable {
   data class Data(
       var amountObj: List<AmountObj>?,
       var expenseCategoryData: ArrayList<ExpenseCategoryData>?,
       var openingBallance: Int?,
       var result: ArrayList<Result>?,
       var total: Int?
   ) :java.io.Serializable{
       data class AmountObj(
           var expenseAmount: Int?,
           var payType: String?
       ):java.io.Serializable

       data class ExpenseCategoryData(
           var _id: String?,
           var newfieldname: ArrayList<Newfieldname>?
       ):java.io.Serializable {
           data class Newfieldname(
               var __v: Int?,
               var _id: String?,
               var addedBy: String?,
               var billDate: String?,
               var billStatus: String?,
               var buildingId: String?,
               var categoryData: ArrayList<CategoryData>?,
               var categoryId: String?,
               var createdAt: String?,
               var createdBy: String?,
               var date: String?,
               var expenseAmount: Int?,
               var expenseDetail: String?,
               var expenseName: String?,
               var invoice: String?,
               var is_active: Boolean?,
               var is_delete: Boolean?,
               var payType: String?,
               var projectId: String?,
               var referenceNo: String?,
               var refernceId: String?,
               var updatedAt: String?,
               var uploadBill: ArrayList<String>?,
               var uploadReciept: String?
           ):java.io.Serializable {
               data class CategoryData(
                   var __v: Int?,
                   var _id: String?,
                   var adminId: String?,
                   var createdAt: String?,
                   var image: String?,
                   var is_delete: Boolean?,
                   var name: String?,
                   var status: String?,
                   var updatedAt: String?
               ):java.io.Serializable
           }
       }

       data class Result(
           var __v: Int?,
           var _id: String?,
           var addedBy: String?,
           var billDate: String?,
           var billStatus: String?,
           var buildingId: String?,
           var categoryId: String?,
           var createdAt: String?,
           var createdBy: String?,
           var date: String?,
           var expenseAmount: Int?,
           var expenseDetail: String?,
           var expenseName: String?,
           var invoice: String?,
           var is_active: Boolean?,
           var is_delete: Boolean?,
           var payType: String?,
           var projectId: String?,
           var referenceNo: String?,
           var refernceId: String?,
           var updatedAt: String?,
           var uploadBill: ArrayList<String>?,
           var uploadReciept: String?
       ):java.io.Serializable
   }
}