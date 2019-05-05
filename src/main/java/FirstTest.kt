import javafx.application.Application
import javafx.stage.Stage
import tornadofx.*

class Pong : App(PongView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 700.0
        stage.height = 300.0
    }
    }


fun main(args: Array<String>) {
    Application.launch(Pong::class.java,*args)
}