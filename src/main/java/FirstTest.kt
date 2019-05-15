import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App

class Pong : App(PongView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 700.0
        stage.height = 390.0 // stage меньше canvas
    }
}


fun main(args: Array<String>) {
    Application.launch(Pong::class.java, *args)
}