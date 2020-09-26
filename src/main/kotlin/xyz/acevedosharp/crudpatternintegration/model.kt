package xyz.acevedosharp.crudpatternintegration

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import java.time.LocalDate
import tornadofx.*

abstract class Describable {
    val parentProperty = SimpleObjectProperty<Empresa>(this, "parent", null)
    var parent: Empresa? by parentProperty
    val parentString = SimpleStringProperty(this, "parentString", if (parent == null) "" else parent.toString())

    init {
        parentProperty.onChange {
            parentString.set(parent.toString())
        }
    }

    abstract fun getSummary(): Map<String, Describable>
}

class Bicicleta(empleadoDueno: Empleado, serial: String, marca: String, modelo: String) {
    val empleadoDuenoProperty = SimpleObjectProperty<Empleado>(this, "empleado", empleadoDueno)
    val empleadoDueno by empleadoDuenoProperty

    val serialProperty = SimpleStringProperty(this, "serial", serial)
    var serial by serialProperty

    val marcaProperty = SimpleStringProperty(this, "marca", marca)
    var marca by marcaProperty

    val modeloProperty = SimpleStringProperty(this, "modelo", modelo)
    var modelo by modeloProperty

    // This is here for a reason!
    override fun toString(): String {
        return if (serialProperty == null) "" else serialProperty.value
    }
}
class BicicletaModel : ItemViewModel<Bicicleta>() {
    val empleadoDueno = bind(Bicicleta::empleadoDueno)
    val serial =        bind(Bicicleta::serial)
    val marca =         bind(Bicicleta::marca)
    val modelo =        bind(Bicicleta::modelo)
}

class Empresa(nit: String, nombre: String, direccion: String) : Describable() {
    val children = FXCollections.observableArrayList<Describable>()
    val childrenString = SimpleStringProperty(this, "childrenString", children.joinToString())

    init {
        children.onChange {
            childrenString.set(children.joinToString())
        }
    }

    val nitProperty = SimpleStringProperty(this, "nit", nit)
    var nit by nitProperty
    
    val nombreProperty = SimpleStringProperty(this, "nombre", nombre)
    var nombre by nombreProperty

    val direccionProperty = SimpleStringProperty(this, "direccion", direccion)
    var direccion by direccionProperty

    fun addChild(describable: Describable) = children.add(describable.apply {
        parent = this@Empresa
    })

    override fun getSummary(): Map<String, Describable> {
        val res = mutableMapOf<String, Describable>()
        res["root"] = this
        children.forEachIndexed { i, it ->  res["describable_$i"] = it }
        return res
    }

    // This is here for a reason!
    override fun toString(): String {
        return if (nombreProperty == null) "" else nombreProperty.value
    }
}
class EmpresaModel : ItemViewModel<Empresa>() {
    val parent =    bind(Empresa::parent)
    val nit =       bind(Empresa::nit)
    val nombre =    bind(Empresa::nombre)
    val direccion = bind(Empresa::direccion)
}

class Empleado(codigo: String, nombre: String, apellido: String, documento: String, fechaNacimiento: LocalDate, direccion: String, telefono: String): Describable() {
    val bicicletas = FXCollections.observableArrayList<Bicicleta>()
    val bicicletasString = SimpleStringProperty(this, "bicicletasString", bicicletas.joinToString())

    init {
        bicicletas.onChange {
            bicicletasString.set(bicicletas.joinToString())
        }
    }

    val codigoProperty = SimpleStringProperty(this, "codigo", codigo)
    var codigo by codigoProperty
    
    val nombreProperty = SimpleStringProperty(this, "nombre", nombre)
    var nombre by nombreProperty

    val apellidoProperty = SimpleStringProperty(this, "apellido", apellido)
    var apellido by apellidoProperty

    val documentoProperty = SimpleStringProperty(this, "documento", documento)
    var documento by documentoProperty

    val fechaNacimientoProperty = SimpleObjectProperty<LocalDate>(this, "fechaNacimiento", fechaNacimiento)
    var fechaNacimiento by fechaNacimientoProperty

    val direccionProperty = SimpleStringProperty(this, "direccion", direccion)
    var direccion by direccionProperty

    val telefonoProperty = SimpleStringProperty(this, "telefono", telefono)
    var telefono by telefonoProperty

    fun addBicicleta(bicicleta: Bicicleta) = bicicletas.add(bicicleta)

    override fun getSummary(): Map<String, Describable> {
        val res = mutableMapOf<String, Describable>()
        res["root"] = this
        return res
    }

    // This is here for a reason!
    override fun toString(): String {
        return if (nombreProperty == null || apellidoProperty == null) "" else "${nombreProperty.value} ${apellidoProperty.value}"
    }
}
class EmpleadoModel : ItemViewModel<Empleado>() {
    val parent =          bind(Empleado::parent)
    val codigo =          bind(Empleado::codigo)
    val nombre =          bind(Empleado::nombre)
    val apellido =        bind(Empleado::apellido)
    val documento =       bind(Empleado::documento)
    val fechaNacimiento = bind(Empleado::fechaNacimiento)
    val direccion =       bind(Empleado::direccion)
    val telefono =        bind(Empleado::telefono)
}
