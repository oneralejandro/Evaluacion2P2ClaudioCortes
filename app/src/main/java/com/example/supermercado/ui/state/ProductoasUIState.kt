
package com.example.supermercado.ui.state
import com.example.supermercado.data.modelo.Producto


data class ProductoasUIState (
    val mensaje:String = "",
    val productos:List<Producto> = listOf<Producto>()
)