package com.cdek.courier.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UpdateDao {

    @Query("SELECT updateTime FROM updates WHERE id = 1")
            /*suspend*/ fun getUpdateTime(): String

}