package www.iesmurgi.u9_proyprofesoressqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto4_alumnos_sqlite.R
import com.example.proyecto4_alumnos_sqlite.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var conexion: BaseDatosProfes
    lateinit var miAdapter: UsuariosAdapter
    var lista = mutableListOf<Usuarios>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    conexion = BaseDatosProfes(this)
    setRecycler()
    setListeners() //Cdo pulsemos el boton flotante
}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_opciones, menu)
    return super.onCreateOptionsMenu(menu)
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId){
        R.id.item_crear->{
            startActivity(Intent(this, AddUpdateActivity::class.java))
            true
        }
        R.id.item_borrar_todo->{ //Si tenemos implementado el adapter y la BD lo codificamos
           conexion.borrarTodo()
           lista.clear()
            miAdapter.notifyDataSetChanged()
            binding.tvNo.visibility = View.VISIBLE
            true
        }
        R.id.item_salir->{
            finish()
            true
        }
        else->true
    }
}

private fun setListeners() {
   binding.fabAdd.setOnClickListener {
        startActivity(Intent(this, AddUpdateActivity::class.java))
    }
}

private fun setRecycler() {
    lista = conexion.leerTodos()
    binding.tvNo.visibility = View.INVISIBLE
    if (lista.size == 0) {
        binding.tvNo.visibility = View.VISIBLE
        return
    }
    val layoutManager = LinearLayoutManager(this)
    binding.recUsuarios.layoutManager = layoutManager
    miAdapter = UsuariosAdapter(lista, { onItemDelete(it) }) {
            usuario->onItemUpdate(usuario)
    }
    binding.recUsuarios.adapter = miAdapter
}

private fun onItemUpdate(usuario: Usuarios) {
    //pasamos el usuario al activity updatecreate
    val i = Intent(this, AddUpdateActivity::class.java).apply {
        putExtra("USUARIO", usuario)
    }
    startActivity(i)
}

private fun onItemDelete(position: Int) {
    val usuario = lista[position]
    conexion.borrar(usuario.id)
    //borramos de la lista e indicamos al adapter que hemos
    //eliminado un registro
    lista.removeAt(position)
    if (lista.size == 0) {
        binding.tvNo.visibility = View.VISIBLE
    }
    miAdapter.notifyItemRemoved(position)
}

override fun onResume() {
    super.onResume()
    setRecycler()
}
}