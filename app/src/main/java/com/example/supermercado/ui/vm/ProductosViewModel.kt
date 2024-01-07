package com.example.supermercado.ui.theme.viewmodels
//package cl.stgoneira.android.p2_u2_ej3.ui.theme.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermercado.data.ProductosRepository
import com.example.supermercado.data.modelo.Producto
import com.example.supermercado.ui.state.ProductoasUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

class ProductosViewModel(
    private val productosRepository: ProductosRepository = ProductosRepository()
) : ViewModel() {

    companion object {
        const val FILE_NAME = "tareas.data"
    }

    private var job: Job? = null

    // deja la versión mutable como privada
    private val _uiState = MutableStateFlow(ProductoasUIState())
    // y pública la versión de solo lectura
    // así se asegura que sólo el ViewModel pueda modificar
    val uiState:StateFlow<ProductoasUIState> = _uiState.asStateFlow()

    init {
        obtenerProductos()
    }

    fun obtenerProductosGuardadasEnDisco(fileInputStream: FileInputStream) {
        productosRepository.getProductosEnDisco(fileInputStream)
    }

    fun guardarProductosEnDisco(fileOutputStream: FileOutputStream) {
        productosRepository.guardarProductosEnDisco(fileOutputStream)
    }

    private fun obtenerProductos() {
        job?.cancel()
        job = viewModelScope.launch {
            val tareasStream = productosRepository.getProductosStream()
            tareasStream.collect { tareasActualizadas ->
                Log.v("TareasViewModel", "obtenerTareas() update{}")
                _uiState.update { currentState ->
                    currentState.copy(
                        productos = tareasActualizadas
                    )
                }
            }
        }
    }

    fun agregarProducto(tarea:String) {
        job = viewModelScope.launch {
            val t = Producto(UUID.randomUUID().toString(), tarea)
            productosRepository.insertar(t)
            _uiState.update {
                it.copy(mensaje = "Producto agregado: ${t.descripcion}")
            }
            obtenerProductos()
        }
    }

    fun eliminarProducto(producto:Producto) {
        job = viewModelScope.launch {
            productosRepository.eliminar(producto)
            _uiState.update {
                it.copy(mensaje = "ADVERTENCIA !! Producto eliminado: ${producto.descripcion}")
            }
            obtenerProductos()
        }
    }
}