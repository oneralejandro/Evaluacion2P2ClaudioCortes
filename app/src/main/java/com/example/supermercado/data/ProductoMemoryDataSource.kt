
package com.example.supermercado.data

import android.util.Log
import com.example.supermercado.data.modelo.Producto


class ProductoMemoryDataSource {
    private val _productos = mutableListOf<Producto>()

    init {
        _productos.addAll(productosPrueba())
    }

    fun obtenerTodas():List<Producto> {
        return _productos
    }

    fun insertar(vararg productos: Producto) {
        _productos.addAll( productos.asList() )
    }

    fun eliminar(producto: Producto) {
        _productos.remove(producto)
        Log.v("DataSource", _productos.toString())
    }

    private fun productosPrueba():List<Producto> = listOf(

    )
}