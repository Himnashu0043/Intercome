package com.application.intercom.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicesCategoryTable")
class ServicesCategoryTable(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var _id: String = "",

    @ColumnInfo(name = "category_number")
    var category_number: String? = null,

    @ColumnInfo(name = "category_name")
    var category_name: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "is_delete")
    var isDelete: Boolean? = null,


    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: String? = null,

    @ColumnInfo(name = "__v")
    var __v: Int? = null,

    @ColumnInfo(name = "is_recent")
    var isRecent: Boolean = false

) : java.io.Serializable
