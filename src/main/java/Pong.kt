import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App

class Pong : App(PongView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        with (stage) {
            sizeToScene()
            minHeight = height
            maxHeight = height
            minWidth = width
            maxWidth = width
        }
    }
}


fun main(args: Array<String>) {
    Application.launch(Pong::class.java, *args)
}