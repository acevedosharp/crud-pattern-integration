package xyz.acevedosharp.crudpatternintegration.view.modules

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*
import xyz.acevedosharp.crudpatternintegration.GeneralController
import xyz.acevedosharp.crudpatternintegration.Empresa
import xyz.acevedosharp.crudpatternintegration.EmpresaModel
import xyz.acevedosharp.crudpatternintegration.view.CurrentModule
import xyz.acevedosharp.crudpatternintegration.view.FormType
import xyz.acevedosharp.crudpatternintegration.view.FormType.*
import xyz.acevedosharp.crudpatternintegration.view.Styles

import xyz.acevedosharp.crudpatternintegration.view.shared.SideNavigation

class EmpresaView : View("Empresas") {

    private val model: EmpresaModel by inject()
    private val existsSelection = SimpleBooleanProperty(false)
    private var table: TableView<Empresa> by singleAssign()
    private val view = this

    override val root = hbox {
        setPrefSize(1280.0, 720.0)
        add(SideNavigation(CurrentModule.EMPRESA, view))
        borderpane {
            setPrefSize(1080.0, 720.0)
            top {
                hbox {
                    addClass(Styles.topBar)
                    paddingBottom = 4
                    useMaxWidth = true
                    button("Nueva Empresa") {
                        addClass(Styles.coolBaseButton, Styles.greenButton)
                        action {
                            openInternalWindow<NewEmpresaFormView>(closeButton = false, modal = true)
                        }
                    }
                    button("Editar selección") {
                        enableWhen(existsSelection)
                        addClass(Styles.coolBaseButton, Styles.blueButton)
                        action {
                            openInternalWindow<EditEmpresaFormView>(
                                    closeButton = false,
                                    modal = true
                            )
                        }
                    }
                    button("Eliminar selección") {
                        enableWhen(existsSelection)
                        addClass(Styles.coolBaseButton, Styles.redButton)
                        action {
                            GeneralController.deleteEmpresa(model.item) // the table selection is bound to the model prop
                        }
                    }
                }
            }

            center {
                hbox {
                    table = tableview(GeneralController.empresas) {
                        column("Parent", Empresa::parentString)
                        column("NIT", Empresa::nit)
                        column("Nombre", Empresa::nombre)
                        column("Dirección", Empresa::direccion)
                        column("Children", Empresa::childrenString)

                        smartResize()

                        bindSelected(model)
                        selectionModel.selectedItemProperty().onChange {
                            existsSelection.value = it != null
                            model.nit.value = it?.nit
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

class BaseEmpresaFormView(formType: FormType) : Fragment() {

    private val model = if (formType == CREATE) EmpresaModel() else find(EmpresaModel::class)
    private var combo: ComboBox<Empresa> by singleAssign()

    override val root = vbox(spacing = 0) {
        useMaxSize = true
        prefWidth = 800.0
        label(if (formType == CREATE) "Nueva Empresa" else "Editar Empresa") {
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
                field("NIT") { if (formType == CREATE) textfield(model.nit).validator {
                    when {
                        it.isNullOrBlank() -> error("NIT requerido")
                        else -> null
                    }
                } else text(model.nit) }
                field("Nombre") {
                    textfield(model.nombre).validator {
                        when {
                            it.isNullOrBlank() -> error("Nombre requerido")
                            else -> null
                        }
                    }
                }
                field("Dirección") {
                    textfield(model.direccion).validator {
                        when {
                            it.isNullOrBlank() -> error("Dirección requerido")
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
                                    GeneralController.addEmpresa(model.parent.value, model.nit.value, model.nombre.value, model.direccion.value)
                                    close()
                                }

                            } else {
                                model.commit {
                                    GeneralController.editEmpresa(model.parent.value, model.nit.value)
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
class NewEmpresaFormView : Fragment() {
    override val root = BaseEmpresaFormView(CREATE).root
}

class EditEmpresaFormView : Fragment() {
    override val root = BaseEmpresaFormView(EDIT).root
}
