package com.example.proyecto4_alumnos_sqlite

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.proyecto4_alumnos_sqlite.DbBitmapUtility
import com.example.proyecto4_alumnos_sqlite.databinding.ActivityAddUpdateBinding
import java.io.FileDescriptor
import java.io.IOException


class AddUpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUpdateBinding
    var nombre=""
    var asignatura=""
    var email=""
    var id: Int? = null
    lateinit var imagen:Bitmap
    lateinit var conexion: BaseDatosProfes
    var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion= BaseDatosProfes(this)
        binding.button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
        cogerDatos()
        setListeners()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var compro:Bitmap?

        if (resultCode == RESULT_OK && requestCode == 100) {
            var imageUri = data?.data
            if (imageUri != null) {
                compro=uriToBitmap(imageUri)
                if(compro!=null){
                    imagen=compro
                }

            }

        }
    }
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    private fun cogerDatos() {
        val datos = intent.extras
        if(datos!=null){
            editar= true
            binding.btnCrear.text="EDITAR"
            val usuario = datos.getSerializable("USUARIO") as Usuarios
            id=usuario.id
            binding.etNombre.setText(usuario.nombre)
            binding.etAsignatura.setText(usuario.asig)//No esta en el esqueleto
            binding.etEmail.setText(usuario.email)
        }
    }
    private fun setListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }
    private fun crearRegistro() {
        var imagenConv = DbBitmapUtility.getBytes(imagen)
        nombre=binding.etNombre.text.toString().trim()
        email=binding.etEmail.text.toString().trim()
        asignatura=binding.etAsignatura.text.toString().trim()
        if(nombre.length<3){
            binding.etNombre.setError("El campo nombre debe tener al menos 3 caracteres")
            return
        }
        if(email.length<6){
            binding.etEmail.setError("El campo email debe tener al menos 6 caracteres")
            binding.etEmail.requestFocus()
            return
        }
        //el email no esta duplicado

        if(conexion.existeEmail(email, id)){
            binding.etEmail.setError("El email YA está registrado.")
            binding.etEmail.requestFocus()
            return
        }
        if(!editar){
            val usuario=Usuarios(1, nombre,asignatura,email,imagenConv)
            if(conexion.crear(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo guardar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }else{
            val usuario=Usuarios(id, nombre,asignatura, email,imagenConv)
            if(conexion.update(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo editar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}