package br.com.confchat.testia

import android.app.Application
import br.com.confchat.testia.data.DB
import br.com.confchat.testia.data.MyDatabase
import br.com.confchat.testia.data.repository.contract.IImageDbRepository
import br.com.confchat.testia.data.repository.implementation.ImageDbRepository

class MyApplication:Application() {
    lateinit var myDatabase: MyDatabase
    lateinit var imageDbRepository: IImageDbRepository

    @Override
    override fun onCreate() {
        super.onCreate()
        myDatabase = DB.getService(this)
        imageDbRepository = ImageDbRepository(myDatabase)
    }
}