package www.iesmurgi.u9_proyprofesoressqlite

data class Usuarios(
    var id:Int?,
    var nombre:String,
    var asig:String,
    var email:String,
    var imagen: ByteArray
):java.io.Serializable
