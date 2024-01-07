
package com.example.supermercado.data


import com.example.supermercado.data.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.FileInputStream
import java.io.FileOutputStream

class ProductosRepository(
    private val productoMemoryDataSource: ProductoMemoryDataSource = ProductoMemoryDataSource(),
    private val productoDiskDataSource: ProductoDiskDataSource = ProductoDiskDataSource()
) {
    private val _tareasStream = MutableStateFlow(listOf<Producto>())

    fun getProductosEnDisco(fileInputStream:FileInputStream) {
        val productos = productoDiskDataSource.obtener(fileInputStream)
        insertar(*productos.toTypedArray())
    }

    fun guardarProductosEnDisco(fileOutputStream:FileOutputStream) {
        productoDiskDataSource.guardar(fileOutputStream, productoMemoryDataSource.obtenerTodas())
    }

    fun getProductosStream():StateFlow<List<Producto>> {
        _tareasStream.update {
            ArrayList(productoMemoryDataSource.obtenerTodas())
        }
        return _tareasStream.asStateFlow()
    }

    fun insertar(vararg productos:Producto) {
        productoMemoryDataSource.insertar(*productos)
        getProductosStream()
    }

    fun eliminar(producto:Producto) {
        productoMemoryDataSource.eliminar(producto)
        getProductosStream()
    }
}