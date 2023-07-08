package br.com.confchat.testia.data.repository.implementation

import br.com.confchat.testia.data.MyDatabase
import br.com.confchat.testia.data.model.ImageDb
import br.com.confchat.testia.data.repository.contract.IImageDbRepository
import kotlinx.coroutines.flow.Flow

class ImageDbRepository constructor(private val db:MyDatabase):IImageDbRepository {
    override fun getAll(): Flow<List<ImageDb>> {
        return db.getImageDbDao().getAll()
    }

    override fun set(ImageDb: ImageDb) {
        db.getImageDbDao().set(ImageDb)
    }
}