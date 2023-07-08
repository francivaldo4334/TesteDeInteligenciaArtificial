package br.com.confchat.testia.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.confchat.testia.data.dao.ImageDbDao
import br.com.confchat.testia.data.model.ImageDb
import br.com.fcr.gastin.data.database.resource.Converters

@Database(
    entities = arrayOf(
        ImageDb::class
    ),
    version = 1
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    companion object{
        val NAME:String = "TESETAI_DATABASE"
    }
    abstract fun getImageDbDao():ImageDbDao
}