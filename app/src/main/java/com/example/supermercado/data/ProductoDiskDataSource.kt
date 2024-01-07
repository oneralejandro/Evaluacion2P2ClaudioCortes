
package com.example.supermercado.data

import android.util.Log
import com.example.supermercado.data.modelo.Producto
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ProductoDiskDataSource() {

    fun obtener(fileInputStream: FileInputStream):List<Producto> {
        return try {
            fileInputStream.use { fis ->
                ObjectInputStream(fis).use { ois ->
                    ois.readObject() as? List<Producto> ?: emptyList()
                }
            }
        } catch (fnfex:FileNotFoundException) {
            emptyList<Producto>()
        } catch (ex:Exception) {
            Log.e("TareaDiskDataSource", "obtener ex:Exception ${ex.toString()}")
            emptyList<Producto>()
        }
    }

    fun guardar(fileOutputStream: FileOutputStream, productos:List<Producto>) {
        fileOutputStream.use { fos ->
            ObjectOutputStream(fos).use { oos ->
                oos.writeObject(productos)
            }
        }
    }

}