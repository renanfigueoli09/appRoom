package com.example.approomaula

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.approomaula.controller.ContatoAcoes
import com.example.approomaula.controller.Tipos
import com.example.approomaula.model.Contato
import com.example.approomaula.ui.theme.CadastrarContato

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContatoTela(
    estado: Contato,
    evento: (ContatoAcoes) -> Unit
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                evento(ContatoAcoes.VisualizarDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Contato"
                )
            }
        },
    ) { _ ->
        if (estado.adicionarContato){
            CadastrarContato(estado = estado, evento = evento)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Tipos.values().forEach { sortType ->
                        Row(
                            modifier = Modifier.clickable {
                                evento(ContatoAcoes.SortearContatos(sortType))
                            },
                            verticalAlignment = CenterVertically
                        ){
                            RadioButton(
                                selected = estado.tipos == sortType ,
                                onClick = {
                                    evento(ContatoAcoes.SortearContatos(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(estado.contato){contatos ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${contatos.nome} ${contatos.sobrenome}",
                            fontSize = 20.sp
                        )
                        Text(text = contatos.telefone, fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        evento(ContatoAcoes.DeletarContato(contatos))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar Contato"
                        )
                    }
                }
            }
        }

    }
}
