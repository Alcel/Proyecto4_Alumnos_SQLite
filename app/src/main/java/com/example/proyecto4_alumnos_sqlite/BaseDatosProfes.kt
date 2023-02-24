package com.example.proyecto4_alumnos_sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream


class BaseDatosProfes(contexto: Context):SQLiteOpenHelper(contexto,DATABASE,null,VERSION){
    companion object{
        const val VERSION=1
        const val DATABASE="profes123.sql" //Cambio
        const val TABLA="alumnos_tbbt"   //Cambio
    }

    override fun onCreate(bd: SQLiteDatabase?) {
        val q = "CREATE TABLE $TABLA(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "asignatura TEXT NOT NULL, "+
                "email TEXT NOT NULL UNIQUE,"+
                "imagen BLOB NOT NULL)"

        bd?.execSQL(q)
        println("SI")
    }

    override fun onUpgrade(bd: SQLiteDatabase?, p1: Int, p2: Int) {
        val q="DROP TABLE IF EXISTS $TABLA"
        bd?.execSQL(q)
        onCreate(bd)
    }
    //CRUD create, read, update, delete
    //Crear un registro
    fun crear(profe: Usuarios) :Long{
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE", profe.nombre)
            put ("EMAIL", profe.email)
            put ("ASIGNATURA",profe.asig)
            put("IMAGEN",profe.imagen)
        }
        val cod=conexion.insert(TABLA, null, valores)
        conexion.close()
        println("Exito")
        return cod
    }
    @SuppressLint("Range")
    fun leerTodos(): MutableList<Usuarios>{ //ver todos los registros
        val lista = mutableListOf<Usuarios>()
        val conexion = this.readableDatabase
        val consulta="SELECT * FROM $TABLA ORDER BY nombre"


        try{
            val cursor = conexion.rawQuery(consulta, null)
            if(cursor.moveToFirst()){

                do{
                    val usuario=Usuarios(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("nombre")),
                        cursor.getString(cursor.getColumnIndex("asignatura")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getBlob(cursor.getColumnIndex("imagen"))
                        //array de bytes a imagen
                    )
                    lista.add(usuario)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return lista
    }
    //metodo para comprobar que el email es unico
    fun existeEmail(email: String, id: Int?): Boolean{
        val consulta = if(id==null) "SELECT id from $TABLA where email='$email'" else
            "SELECT id from $TABLA where email='$email' AND id!=$id"
        val conexion = this.readableDatabase
        var filas=0
        try{
            val cursor = conexion.rawQuery(consulta, null)
            filas =cursor.count
            cursor.close()
        }catch(e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return (filas!=0)
    }
    fun borrar(id: Int?){
        val q="DELETE FROM $TABLA WHERE id=$id"
        val conexion= this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }
    fun update(usuario: Usuarios): Int{
        //val q="UPDATE $TABLA SET nombre='${usuario.nombre}', email='${usuario.email}' where id=${usuario.id}"
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE", usuario.nombre)
            put("ASIGNATURA",usuario.asig)
            put("EMAIL", usuario.email)
            put("IMAGEN",usuario.imagen)
        }
        val update = conexion.update(TABLA, valores, "id=?", arrayOf(usuario.id.toString()))
        println("Actualizao")
        conexion.close()
        return  update
    }
    fun borrarTodo(){
        val q="DELETE FROM $TABLA"
        val conexion = this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }


}
