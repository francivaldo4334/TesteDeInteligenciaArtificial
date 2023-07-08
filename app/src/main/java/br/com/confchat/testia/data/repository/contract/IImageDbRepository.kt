package br.com.confchat.testia.data.repository.contract

import br.com.confchat.testia.data.model.ImageDb
import kotlinx.coroutines.flow.Flow

interface IImageDbRepository {
    fun getAll(): Flow<List<ImageDb>>
    fun set(ImageDb:ImageDb)
}