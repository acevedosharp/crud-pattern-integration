package xyz.acevedosharp.crudpatternintegration.view.shared

import xyz.acevedosharp.crudpatternintegration.view.Styles
import javafx.geometry.Pos
import tornadofx.*
import xyz.acevedosharp.crudpatternintegration.view.CurrentModule
import xyz.acevedosharp.crudpatternintegration.view.CurrentModule.*
import xyz.acevedosharp.crudpatternintegration.view.modules.EmpleadoView
import xyz.acevedosharp.crudpatternintegration.view.modules.EmpresaView

class SideNavigation(currentModule: CurrentModule, root: View): Fragment() {
    override val root = vbox(alignment = Pos.TOP_CENTER) {
        rectangle(width = 0, height = 40)
        imageview("images/velo.png") {
            fitWidth = 150.0
            fitHeight = 150.0
        }
        rectangle(width = 0, height = 25)
        line(startX = 0, endX = 185).style {
            stroke = c(255, 255, 255, 0.35)
        }
        button("Empresa") {
            val tag = EMPRESA
            addClass(Styles.navigationButton, if (tag == currentModule) Styles.selectedButton else Styles.unselectedButton)
            graphic = imageview("images/empresa.png") {
                fitWidth = 75.0
                fitHeight = 75.0
            }
            action {
                root.replaceWith(EmpresaView())
            }
        }
        button("Employee") {
            tag = EMPLEADO
            addClass(Styles.navigationButton, if (tag == currentModule) Styles.selectedButton else Styles.unselectedButton)
            graphic = imageview("images/empleado.png") {
                fitWidth = 75.0
                fitHeight = 75.0
            }
            action {
                root.replaceWith(EmpleadoView())
            }
        }

        style {
            backgroundColor += c(21, 55, 83)
        }
    }
}