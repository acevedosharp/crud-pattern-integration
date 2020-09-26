package xyz.acevedosharp.crudpatternintegration.view.modules

import javafx.geometry.Pos
import tornadofx.*
import xyz.acevedosharp.crudpatternintegration.BicicletaModel
import xyz.acevedosharp.crudpatternintegration.GeneralController
import xyz.acevedosharp.crudpatternintegration.view.Styles

class NewBicicletaFormView : Fragment() {

    private val model = BicicletaModel()

    override val root = vbox(spacing = 0) {
        useMaxSize = true
        prefWidth = 800.0
        label("Nueva Biciclera") {
            useMaxWidth = true
            addClass(Styles.titleLabel, Styles.greenLabel)
        }
        form {
            fieldset {
                field("Empleado dueño") {
                    combobox(model.empleadoDueno, GeneralController.empleados).apply {
                        prefWidth = 300.0
                        makeAutocompletable(false)
                    }.validator {
                        when {
                            it == null -> error("Empleado dueño obligatorio")
                            else -> null
                        }
                    }
                }
                field("Serial") {
                    textfield(model.serial).validator {
                        when {
                            it.isNullOrBlank() -> error("Serial requerido")
                            else -> null
                        }
                    }
                }
                field("Marca") {
                    textfield(model.marca).validator {
                        when {
                            it.isNullOrBlank() -> error("Marca requerido")
                            else -> null
                        }
                    }
                }
                field("Modelo") {
                    textfield(model.modelo).validator {
                        when {
                            it.isNullOrBlank() -> error("Modelo requerido")
                            else -> null
                        }
                    }
                }
                hbox(spacing = 80, alignment = Pos.CENTER) {
                    button("Aceptar") {
                        addClass(Styles.coolBaseButton, Styles.greenButton, Styles.expandedButton)
                        action {
                            model.commit {
                                GeneralController.addBicicletaToEmp(model.empleadoDueno.value, model.serial.value, model.marca.value, model.modelo.value)
                                close()
                            }
                        }
                    }
                    button("Cancelar") {
                        addClass(Styles.coolBaseButton, Styles.redButton, Styles.expandedButton)
                        action {
                            close()
                        }
                    }
                }
            }
        }
    }
}