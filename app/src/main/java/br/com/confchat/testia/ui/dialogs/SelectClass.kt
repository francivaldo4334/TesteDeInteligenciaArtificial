package br.com.confchat.testia.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.confchat.testia.R

@Composable
fun MyDropDow(onSelect:(String)->Unit) {
    var value by remember{ mutableStateOf("Escolhes") }
    var openDropDown by remember { mutableStateOf(false) }
    val copo = stringResource(R.string.copo)
    val fone = stringResource(R.string.fone)
    val carregador = stringResource(R.string.carregador)
    Column {
        TextButton(onClick = {
                openDropDown = true
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Text(value, color = MaterialTheme.colorScheme.onBackground)
        }
        DropdownMenu(expanded = openDropDown, onDismissRequest = {openDropDown = false}) {
            DropdownMenuItem(text = { Text(text = copo) }, onClick = {
                onSelect(copo)
                value = copo
                openDropDown = false
            })
            DropdownMenuItem(text = { Text(text = fone) }, onClick = {
                onSelect(fone)
                value = fone
                openDropDown = false
            })
            DropdownMenuItem(text = { Text(text = carregador) }, onClick = {
                onSelect(carregador)
                value = carregador
                openDropDown = false
            })
        }
    }
}
@Composable
fun DialogSelect(show: Boolean,onDismiss:()->Unit,onSelect: (String) -> Unit){
    if(show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "Selecione uma classe")
                    Spacer(modifier = Modifier.height(16.dp))
                    MyDropDow(onSelect = onSelect)
                }
            }
        )
    }
}