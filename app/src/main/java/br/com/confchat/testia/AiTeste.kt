package br.com.confchat.testia

import br.com.confchat.testia.data.model.ImageDb
import java.lang.Math.abs

class AiTeste {
    private fun checkImage(imagedb:Array<DoubleArray>,imagecheck:Array<DoubleArray>):Double{
        val largura = imagedb[0].size
        var somaDiferenca = 0.0
        val altura = imagedb.size
        for (y in 0 until altura){
            for (x in 0 until largura){
                val pixelImagedb = imagedb[y][x]
                val pixelImagecheck = imagecheck[y][x]
                somaDiferenca += abs(pixelImagedb - pixelImagecheck)
            }
        }
        val tamanhoImagem = largura*altura
        return somaDiferenca/tamanhoImagem
    }
    fun identificarClass(image:Array<DoubleArray>,listImageClassDefinidas:List<ImageDb>):List<String>{
        val result:MutableList<String> = mutableListOf()
        for (it in listImageClassDefinidas) {
            val checkObject = checkImage(it.image,image)
            if(checkObject < 0.1){
                result.add(it.Class)
            }
        }
        return result
    }
}