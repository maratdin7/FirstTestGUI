import javafx.scene.SubScene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*
import kotlin.collections.listOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.concurrent.timer

fun Canvas.paintBall(ball: Ball) {
    graphicsContext2D.fill = Color.WHITE
    with(ball) {
        graphicsContext2D.fillOval(
                (x - radius).toDouble(),
                (y - radius).toDouble(),
                (2 * radius).toDouble(),
                (2 * radius).toDouble())
    }
}

fun Canvas.rePaintBall(ball: Ball,lim : Point,deskPlayer1: Desk,deskPlayer2: Desk):Int {
    graphicsContext2D.clearRect((ball.x-ball.radius).toDouble(),(ball.y-ball.radius).toDouble(),2*ball.radius.toDouble(),2*ball.radius.toDouble())
    val ans = ball.step(lim, deskPlayer1, deskPlayer2)
    paintBall(ball)
    return ans
}

fun Canvas.paintDesk(desk: Desk) {
    graphicsContext2D.fill = Color.WHITE
    with(desk) {
        graphicsContext2D.fillRect(
                x.toDouble(),
                y.toDouble(),
                weight.toDouble(),
                length.toDouble())
    }
}

fun Canvas.rePaintDesk(desk:Desk, ddy: Int) {
    graphicsContext2D.clearRect(desk.x.toDouble(), desk.y.toDouble(), desk.weight.toDouble(), desk.length.toDouble())
    desk.step(height.toInt(), 10, ddy)
    paintDesk(desk)
}

object MoveEvent : FXEvent()


class PongView : View() {
    override var root = Pane()

    private  val canvasWidth = 700

    private val canvasHeight = 300

    private var canvas: Canvas = Canvas(canvasWidth.toDouble(), canvasHeight.toDouble())

    private val ball = Ball(canvasWidth / 2, canvasHeight / 2, -1, 1, 10)

    private val deskPlayer1 = Desk((canvasWidth * 0.1).toInt(), canvasHeight / 3, canvasHeight / 3, 10)

    private val deskPlayer2 = Desk((canvasWidth - canvasWidth * 0.1 - 10).toInt(), canvasHeight / 3, canvasHeight / 3, 10)

    private val keys = mutableMapOf(
            Pair(KeyCode.W, false),
            Pair(KeyCode.S, false),
            Pair(KeyCode.UP, false),
            Pair(KeyCode.DOWN, false)
    )

    private var keyPress = false

    fun keyTest() {
        if (keys[KeyCode.W]!!) canvas.rePaintDesk(deskPlayer1, - 10)
        if (keys[KeyCode.UP]!!) canvas.rePaintDesk(deskPlayer2, -10)
        if (keys[KeyCode.S]!!) canvas.rePaintDesk(deskPlayer1,  10)
        if (keys[KeyCode.DOWN]!!) canvas.rePaintDesk(deskPlayer2, 10)
    }

    init {
        with(root) {
            style = "-fx-background-color: black"
            canvas = canvas {
                canvas = this
                isFocusTraversable = true

                setOnKeyReleased {
                    keys[it.code] = false
                }

                setOnKeyPressed {
                    keys[it.code] = true
                    keyTest()
                    keyPress = true
                }
            }
            canvas.widthProperty().bind((canvas.parent as Pane).widthProperty())
            canvas.heightProperty().bind((canvas.parent as Pane).heightProperty())
        }
        timer(daemon = true, period = 10) {
            fire(MoveEvent)
        }

        subscribe<MoveEvent> {
            keyPress = false // чтобы шарик не мешал
            if (keyPress) {
                val lim = Point(canvasWidth, canvasHeight)

                val ans = canvas.rePaintBall(ball,lim,deskPlayer1,deskPlayer2)
                if (ans != 0) {
                    ball.x = canvasWidth / 2
                    ball.y = canvasHeight / 2
                    ball.dx = ans
                    ball.dy = 0

                    deskPlayer1.y = ball.y - deskPlayer1.length/2
                    deskPlayer2.y = ball.y - deskPlayer2.length/2
                    keyPress = false
                }
            }
            else {
                canvas.graphicsContext2D.clearRect(0.0,0.0, canvas.width, canvas.height)
                canvas.paintBall(ball)
                canvas.paintDesk(deskPlayer1)
                canvas.paintDesk(deskPlayer2)
            }
        }
    }
}
