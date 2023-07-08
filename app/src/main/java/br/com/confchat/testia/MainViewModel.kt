package br.com.confchat.testia

import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.confchat.testia.data.model.ImageDb
import br.com.confchat.testia.data.repository.contract.IImageDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: IImageDbRepository) : ViewModel() {
    val listImageDb = repository.getAll().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    fun setImageDb(convertBitmapToArray: Array<DoubleArray>, it: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.set(
                ImageDb(
                    image = convertBitmapToArray, Class = it
                )
            )
        }
    }

    fun getListImage(_listImageDb:List<ImageDb>?):List<Bitmap> {
        val listBitmap:MutableList<Bitmap> = mutableListOf()
        _listImageDb?.let {
            it.forEach {
                listBitmap.add(
                    convertArrayToBitmap(it.image)
                )
            }
        }
        return listBitmap
    }

    fun convertArrayToBitmap(imageArray: Array<DoubleArray>): Bitmap {
        val width = imageArray[0].size
        val height = imageArray.size

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val intensity = imageArray[y][x]
                val grayscaleValue = (intensity * 255).toInt()
                val pixel = Color.rgb(grayscaleValue, grayscaleValue, grayscaleValue)
                bitmap.setPixel(x, y, pixel)
            }
        }

        return bitmap
    }
}