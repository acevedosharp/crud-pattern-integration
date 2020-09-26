package xyz.acevedosharp

import tornadofx.*
import xyz.acevedosharp.crudpatternintegration.view.Styles
import xyz.acevedosharp.crudpatternintegration.view.modules.EmpresaView

class MyApp: App(EmpresaView::class, Styles::class)

fun main() = launch<MyApp>()