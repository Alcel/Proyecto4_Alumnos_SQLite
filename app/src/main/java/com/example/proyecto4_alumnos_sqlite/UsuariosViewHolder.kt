package com.example.proyecto4_alumnos_sqlite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto4_alumnos_sqlite.databinding.UsuariosEsqueletoBinding


class UsuariosViewHolder (vista: View):RecyclerView.ViewHolder(vista){
  //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding= UsuariosEsqueletoBinding.bind(vista)
    fun inflar(profesor: Usuarios,
               onItemDelete:(Int)->Unit,
               onItemUpdate:(Usuarios)->Unit)
    {
        var imgConv = DbBitmapUtility.getImage(profesor.imagen)
        miBinding.tvId.text=profesor.id.toString()
        miBinding.tvNombre.text=profesor.nombre
        miBinding.tvAsignatura.text=profesor.asig
        miBinding.tvEmail.text=profesor.email
        miBinding.imageView.setImageBitmap(imgConv)

        miBinding.btnBorrar.setOnClickListener{
        onItemDelete(adapterPosition)
        }
        itemView.setOnClickListener { onItemUpdate(profesor) }
    }
}