package br.com.confchat.testia.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.confchat.testia.data.model.ImageDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDbDao {
    @Query("SELECT * FROM TB_IMG_DB")
    fun getAll():Flow<List<ImageDb>>
    @Insert
    fun set(ImageDb:ImageDb)
}