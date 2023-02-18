package www.iesmurgi.u9_proyprofesoressqlite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto4_alumnos_sqlite.R

class UsuariosAdapter (
        private val lista:MutableList<Usuarios>,
        private val onItemDelete:(Int)->Unit,
        private val onItemUpdate:(Usuarios) -> Unit
):RecyclerView.Adapter<UsuariosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder { //2.
        val v=LayoutInflater.from(parent.context).inflate(R.layout.usuarios_esqueleto,parent,false)
        return UsuariosViewHolder(v)
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        holder.inflar(lista[position],onItemDelete,onItemUpdate)
    }

    override fun getItemCount()=lista.size  //1

}
