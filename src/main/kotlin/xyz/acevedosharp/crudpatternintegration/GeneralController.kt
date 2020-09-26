package xyz.acevedosharp.crudpatternintegration

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.time.LocalDate

// Singleton & facade patterns
object GeneralController {
    val empresas: ObservableList<Empresa> = FXCollections.observableArrayList<Empresa>()
    val empleados: ObservableList<Empleado> = FXCollections.observableArrayList<Empleado>()

    val describables: ObservableList<Describable> = FXCollections.observableArrayList<Describable>()

    fun addEmpresa(parent: Empresa?, nit: String, nombre: String, direccion: String) {
        val empresa = Empresa(nit, nombre, direccion).apply { parentProperty.set(parent) }

        parent?.addChild(empresa)

        empresas.add(empresa)
    }

    fun editEmpresa(parent: Empresa?, nit: String) {
        val empresaRef = empresas.find { it.nit == nit }!!

        if (parent != null && empresaRef !in parent.children) parent.addChild(empresaRef) // null safe due to if
    }

    fun deleteEmpresa(empresa: Empresa) {
        empresas.remove(empresa)

        if (empresa.children.size > 0) {
            for (child in empresa.children) {
                if (child is Empresa)
                    deleteEmpresa(child)
                else
                    deleteEmpleado(child as Empleado)
            }
        }
    }

    fun deleteEmpleado(empleado: Empleado) {
        empleados.remove(empleado)
    }

    fun addEmpleado(parent: Empresa?, codigo: String, nombre: String, apellido: String, documento: String, fechaNacimiento: LocalDate, direccion: String, telefono: String) {
        val empleado = Empleado(codigo, nombre, apellido, documento, fechaNacimiento, direccion, telefono).apply { parentProperty.set(parent) }

        if (parent != null) empresas.find { it.nit == empleado.parent!!.nit }!!.addChild(empleado) // null safe due to if

        empleados.add(empleado)
    }

    fun editEmpleado(parent: Empresa?, codigo: String) {
        val empleadoRef = empleados.find { it.codigo == codigo }!!

        if (parent != null && empleadoRef !in parent.children) parent.addChild(empleadoRef) // null safe due to if
    }

    fun addBicicletaToEmp(empleado: Empleado, serial: String, marca: String, modelo: String) {
        empleado.addBicicleta(Bicicleta(empleado, serial, marca, modelo))
    }
}