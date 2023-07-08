package br.com.confchat.testia.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_IMG_DB")
class ImageDb (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val Id:Int = 0,
    @ColumnInfo(name = "CLASS")
    val Class:String,
    @ColumnInfo(name = "IMG")
    val image:Array<DoubleArray>
)