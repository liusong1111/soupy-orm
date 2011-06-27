package soupy.mapper

object TableRegistry {
  var mapper = Map[String, Table[_]]()

  def register(modelClassName: String, table: Table[_]):Unit = {
    mapper = mapper + (modelClassName -> table)
  }

  def peek(modelClassName: String) = {
    mapper.get(modelClassName)
  }

  def register[M: ClassManifest](table: Table[_]):Unit = {
    val modelClassName = implicitly[ClassManifest[M]].erasure.getName
    register(modelClassName, table)
  }

  def peek[M: ClassManifest] = {
    val modelClassName = implicitly[ClassManifest[M]].erasure.getName
    mapper.get(modelClassName)
  }
}