package com.application.intercom.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.application.intercom.db.entity.ServicesCategoryTable

@Dao
abstract class ServicesCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertServiceCategory(servicesCategoryTable: ServicesCategoryTable):Long


    @Query("SELECT * FROM ServicesCategoryTable WHERE is_recent =:is_recent")
    abstract suspend  fun servicesCategoryList(is_recent:Boolean):List<ServicesCategoryTable>

}