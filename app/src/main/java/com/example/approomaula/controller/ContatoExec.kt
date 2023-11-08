package com.example.approomaula.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomaula.model.Contato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContatoExec(
    private val dao: ContatoDao
): ViewModel(){
    private val _tipos = MutableStateFlow(Tipos.nome)
    private val _contatos = _tipos
        .flatMapLatest { tipos ->
            when(tipos){
                Tipos.nome -> dao.getContatoOrdenadoPeloNome()
                Tipos.sobrenome -> dao.getContatoOrdenadoPeloSobrenome()
                Tipos.telefone -> dao.getContatoOrdenadoPeloTelefone()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _estado = MutableStateFlow(Contato())
    val estado = combine(_estado, _tipos, _contatos){ estado, tipos, contato ->
        estado.copy(
            contato = contato,
            tipos = tipos
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Contato())

    fun evento(acao: ContatoAcoes){
        when(acao){
            is ContatoAcoes.DeletarContato -> {
                viewModelScope.launch {
                    dao.deleteContato(acao.contato)
                }
            }
            ContatoAcoes.OcultarDialog -> {
                _estado.update {
                    it.copy(
                        adicionarContato = false
                    )
                }
            }
            ContatoAcoes.CadastrarContato -> {
                val nome = estado.value.nome
                val sobrenome = estado.value.sobrenome
                val telefone = estado.value.telefone

                if(nome.isBlank() || sobrenome.isBlank() || telefone.isBlank()){
                    return
                }

                val contato = com.example.approomaula.controller.Contato(
                    nome = nome,
                    sobrenome = sobrenome,
                    telefone = telefone
                )

                viewModelScope.launch {
                    dao.upsertContato(contato)
                }

                _estado.update {
                    it.copy(
                        adicionarContato = false,
                        nome = "",
                        sobrenome = "",
                        telefone = ""
                    )
                }
            }
            is ContatoAcoes.SetNome -> {
                _estado.update {
                    it.copy(
                        nome = acao.nome
                    )
                }
            }
            is ContatoAcoes.SetSobrenome -> {
                _estado.update {
                    it.copy(
                        sobrenome = acao.sobrenome
                    )
                }
            }
            is ContatoAcoes.SetTelefone -> {
                _estado.update {
                    it.copy(
                        telefone = acao.telefone
                    )
                }
            }
            is ContatoAcoes.VisualizarDialog -> {
                _estado.update {
                    it.copy(
                        adicionarContato = true
                    )
                }
            }
            is ContatoAcoes.SortearContatos -> {
                _tipos.value = acao.tipos
            }

            else -> {}
        }
    }
}