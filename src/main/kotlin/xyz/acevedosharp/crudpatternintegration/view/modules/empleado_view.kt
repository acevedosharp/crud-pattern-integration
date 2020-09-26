package xyz.acevedosharp.crudpatternintegration.view.modules

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*
import xyz.acevedosharp.crudpatternintegration.*
import xyz.acevedosharp.crudpatternintegration.view.CurrentModule
import xyz.acevedosharp.crudpatternintegration.view.FormType
import xyz.acevedosharp.crudpatternintegration.view.FormType.*
import xyz.acevedosharp.crudpatternintegration.view.Styles

import xyz.acevedosharp.crudpatternintegration.view.shared.SideNavigation

class EmpleadoView : View("Empleados") {

    private val model: EmpleadoModel by inject()
    private val existsSelection = SimpleBooleanProperty(false)
    private var table: TableView<Empleado> by singleAssign()
    private val view = this

    override val root = hbox {
        setPrefSize(1280.0, 720.0)
        add(SideNavigation(CurrentModule.EMPLEADO, view))
        borderpane {
            setPrefSize(1080.0, 720.0)
            top {
                hbox {
                    addClass(Styles.topBar)
                    paddingBottom = 4
                    useMaxWidth = true
                    button("Nuevo Empleado") {
                        addClass(Styles.coolBaseButton, Styles.greenButton)
                        action {
                            openInternalWindow<NewEmpleadoFormView>(closeButton = false, modal = true)
                        }
                    }
                    button("Editar selección") {
                        enableWhen(existsSelection)
                        addClass(Styles.coolBaseButton, Styles.blueButton)
                        action {
                            openInternalWindow<EditEmpleadoFormView>(
                                    closeButton = false,
                                    modal = true
                            )
                        }
                    }
                    button("Eliminar selección") {
                        enableWhen(existsSelection)
                        addClass(Styles.coolBaseButton, Styles.redButton)
                        action {
                            GeneralController.deleteEmpleado(model.item) // the table selection is bound to the model prop
                        }
                    }
                    rectangle(width = 10, height = 0)
                    line(0, 0, 0, 35).style {
                        stroke = c(255, 255, 255, 0.25)
                    }
                    rectangle(width = 10, height = 0)
                    button("Agregar bicicleta") {
                        addClass(Styles.coolBaseButton, Styles.greenButton)
                        action {
                            openInternalWindow<NewBicicletaFormView>(closeButton = false, modal = true)
                        }
                    }
                }
            }

            center {
                hbox {
                    table = tableview(GeneralController.empleados) {
                        column("Parent", Empleado::parentString)
                        column("Código", Empleado::codigo)
                        column("Nombre", Empleado::nombre)
                        column("Apellido", Empleado::apellido)
                        column("documento", Empleado::documento)
                        column("F. Nac.", Empleado::fechaNacimiento)
                        column("Dirección", Empleado::direccion)
                        column("Teléfono", Empleado::telefono)
                        column("Bicicletas", Empleado::bicicletasString)

                        smartResize()

                        bindSelected(model)
                        selectionModel.selectedItemProperty().onChange {
                            existsSelection.value = it != null
                            model.codigo.value = it?.codigo
                        }

                        hgrow = Priority.ALWAYS
                    }
                    paddingAll = 6
                    style {
                        backgroundColor += Color.WHITE
                    }
                }
            }

            style {
                backgroundColor += Color.WHITE
            }
        }
    }
}

class BaseEmpleadoFormView(formType: FormType) : Fragment() {

    private val model = if (formType == CREATE) EmpleadoModel() else find(EmpleadoModel::class)
    private var combo: ComboBox<Empresa> by singleAssign()

    override val root = vbox(spacing = 0) {
        useMaxSize = true
        prefWidth = 800.0
        label(if (formType == CREATE) "Nuevo Empleado" else "Editar Empleado") {
            useMaxWidth = true
            addClass(Styles.titleLabel)
            addClass(if (formType == CREATE) Styles.greenLabel else Styles.blueLabel)
        }
        form {
            fieldset {
                field("Parent") {
                    hbox(8.00, Pos.CENTER_LEFT) {
                        if (model.parent.value == null) {
                            combo = combobox(model.parent, GeneralController.empresas).apply {
                                prefWidth = 300.0
                                makeAutocompletable(false)
                            }
                            button("Clear selection") {
                                addClass(Styles.coolBaseButton, Styles.grayButton)
                                action {
                                    combo.selectionModel.clearSelection()
                                }
                            }
                        } else {
                            text(model.parent.value.toString())
                        }
                    }
                }
                field("Código") { if (formType == CREATE) textfield(model.codigo).validator {
                    when {
                        it.isNullOrBlank() -> error("NIT requerido")
                        else -> null
                    }
                } else text(model.codigo) }
                field("Nombre") {
                    textfield(model.nombre).validator {
                        when {
                            it.isNullOrBlank() -> error("Nombre requerido")
                            else -> null
                        }
                    }
                }
                field("Apellido") {
                    textfield(model.apellido).validator {
                        when {
                            it.isNullOrBlank() -> error("Apellido requerido")
                            else -> null
                        }
                    }
                }
                field("Documento") {
                    textfield(model.documento).validator {
                        when {
                            it.isNullOrBlank() -> error("Documento requerido")
                            else -> null
                        }
                    }
                }
                field("Fecha de nacimiento") {
                    datepicker(model.fechaNacimiento)
                }
                field("Dirección") {
                    textfield(model.direccion).validator {
                        when {
                            it.isNullOrBlank() -> error("Dirección requerido")
                            else -> null
                        }
                    }
                }
                field("Teléfono") {
                    textfield(model.telefono).validator {
                        when {
                            it.isNullOrBlank() -> error("Teléfono requerido")
                            else -> null
                        }
                    }
                }
                hbox(spacing = 80, alignment = Pos.CENTER) {
                    button("Aceptar") {
                        addClass(Styles.coolBaseButton, Styles.greenButton, Styles.expandedButton)
                        action {
                            if (formType == CREATE) {
                                model.commit {
                                    GeneralController.addEmpleado(model.parent.value, model.codigo.value, model.nombre.value, model.apellido.value, model.documento.value, model.fechaNacimiento.value, model.direccion.value, model.telefono.value)
                                    close()
                                }

                            } else {
                                model.commit {
                                    GeneralController.editEmpleado(model.parent.value, model.codigo.value)
                                    close()
                                }
                            }
                        }
                    }
                    button("Cancelar") {
                        addClass(Styles.coolBaseButton, Styles.redButton, Styles.expandedButton)
                        action {
                            if (formType == CREATE) {
                                close()
                            } else {
                                model.rollback()
                                close()
                            }
                        }
                    }
                }
            }
        }
    }
}

// 1. These com.acevedosharp.views need to be accesible from anywhere so that they can be used in other modules for convenience.
class NewEmpleadoFormView : Fragment() {
    override val root = BaseEmpleadoFormView(CREATE).root
}

class EditEmpleadoFormView : Fragment() {
    override val root = BaseEmpleadoFormView(EDIT).root
}
