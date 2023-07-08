package br.com.confchat.testia

import android.Manifest
import android.R.attr.data
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import br.com.confchat.testia.data.model.ImageDb
import br.com.confchat.testia.ui.dialogs.DialogSelect
import br.com.confchat.testia.ui.dialogs.MyDropDow
import br.com.confchat.testia.ui.theme.TestIATheme
import java.io.File


class MainActivity : ComponentActivity() {
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101
    lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    lateinit var cameraResultDataLauncher: ActivityResultLauncher<Intent>
    private var imageBitmap:Bitmap by mutableStateOf(Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888))
    private var imageBitmapDb:Bitmap by mutableStateOf(Bitmap.createBitmap(50,50,Bitmap.Config.ARGB_8888))
    private lateinit var listImageDbIn:List<ImageDb>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel((applicationContext as MyApplication).imageDbRepository)
        var opendialog by mutableStateOf(false)
        val AiTeste = AiTeste()
        var textResp by mutableStateOf("Resp")
        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == RESULT_OK){
                val resultbitmap =it.data?.extras?.get("data") as Bitmap
                val bitmapPretoEBranca = Bitmap.createScaledBitmap(resultbitmap,50,50,true)
                imageBitmap = convertToBlackAndWhite(bitmapPretoEBranca)
                val resp = AiTeste.identificarClass(convertBitmapToArray(imageBitmap),listImageDbIn)
                var groups = resp.groupBy { it }
                textResp = ""
                groups.forEach{
                    textResp += "${it.key}:${it.value.size}\n"
                }
            }
        }
        cameraResultDataLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == RESULT_OK){
                val resultbitmap =it.data?.extras?.get("data") as Bitmap
                val bitmapPretoEBranca = Bitmap.createScaledBitmap(resultbitmap,50,50,true)
                imageBitmapDb = convertToBlackAndWhite(bitmapPretoEBranca)
                opendialog = true
            }
        }
        setContent {
            val listImageDb by viewModel.listImageDb.collectAsState()
            listImageDbIn = listImageDb
            TestIATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        item{
                            Button(onClick = {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    openCameraData()
                                } else {
                                    requestCameraPermission()
                                }
                            }) {
                                Text(text = "carregar dados")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    openCamera()
                                } else {
                                    requestCameraPermission()
                                }
                            }) {
                                Text(text = "Iniciar verificacao")
                            }
                        }
                        item{
                            Text(text = textResp)
                        }
                        item {
                            Row {
                                Image(
                                    bitmap = imageBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(200.dp)
                                )
                                Column {
                                    viewModel.getListImage(listImageDb).forEach {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier.size(200.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    DialogSelect(show = opendialog, onDismiss = {opendialog = false}){
                        viewModel.setImageDb(
                            convertBitmapToArray(imageBitmapDb),
                            it
                        )
                    }
                }
            }
        }
    }
    // Função para converter um bitmap em uma matriz de doubles
    fun convertBitmapToArray(resizedBitmap: Bitmap): Array<DoubleArray> {
        val width = 50
        val height = 50

        val imageArray = Array(50) { DoubleArray(50) }

        for (y in 0 until 50) {
            for (x in 0 until 50) {
                val pixel = resizedBitmap.getPixel(x, y)
                val red = Color.red(pixel)

                val normalizedValue = red.toDouble() / 255.0
                Log.d("PIXEL",normalizedValue.toString())
                imageArray[y][x] = normalizedValue
            }
        }

        return imageArray
    }
    fun convertToBlackAndWhite(originalBitmap: Bitmap): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height

        val blackAndWhiteBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = originalBitmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                val grayscaleValue = (red + green + blue) / 3
                val blackAndWhitePixel = Color.rgb(grayscaleValue, grayscaleValue, grayscaleValue)

                blackAndWhiteBitmap.setPixel(x, y, blackAndWhitePixel)
            }
        }

        return blackAndWhiteBitmap
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(intent)
    }
    private fun openCameraData() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultDataLauncher.launch(intent)
    }
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
}