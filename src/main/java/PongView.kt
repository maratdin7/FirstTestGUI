import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import tornadofx.FXEvent
import tornadofx.View
import tornadofx.canvas
import kotlin.collections.set
import kotlin.concurrent.timer

fun Canvas.paintBall(ball: Ball) {
    graphicsContext2D.fill = Color.WHITE
    with(ball) {
        graphicsContext2D.fillOval(
                (x - radius),
                (y - radius),
                (2 * radius),
                (2 * radius))
    }
}

fun Canvas.rePaintBall(ball: Ball, lim: Point, deskPlayer1: Desk, deskPlayer2: Desk): Int {
    graphicsContext2D.clearRect((ball.x - ball.radius), (ball.y - ball.radius), (2 * ball.radius), 2 * ball.radius)
    val ans = ball.step(lim, deskPlayer1, deskPlayer2)
    paintBall(ball)
    return ans
}

fun Canvas.paintDesk(desk: Desk) {
    graphicsContext2D.fill = Color.WHITE
    with(desk) {
        graphicsContext2D.fillRect(
                x,
                y,
                weight.toDouble(),
                length.toDouble())
    }
}

fun Canvas.rePaintDesk(desk: Desk, ddy: Int) {
    graphicsContext2D.clearRect(desk.x, desk.y, desk.weight.toDouble(), desk.length.toDouble())
    desk.step(height, 10.0, ddy)
    paintDesk(desk)
}

fun Canvas.paintLine() {
    graphicsContext2D.stroke = Color.WHITE
    graphicsContext2D.lineWidth = 2.0
    graphicsContext2D.strokeLine(width / 2, 0.0, width / 2, height)
}

object MoveEvent : FXEvent()


class PongView : View() {
    override var root = Pane()

    private val canvasWidth = 700.0

    private val canvasHeight = 360.0

    private var canvas: Canvas = Canvas(canvasWidth, canvasHeight)

    private var backGround: Canvas = Canvas(canvasWidth, canvasHeight)

    private var scorePlayer1 = 0

    private var scorePlayer2 = 0

    //private val ball = Ball(canvasWidth / 2, canvasHeight / 2, -0.5, 0.0,1.0, 10.0)
    private val ball = Ball(80.0, 275.0, -1.0, -3.0, 1.0, 10.0)

    private val deskPlayer1 = Desk((canvasWidth * 0.1), canvasHeight / 3, canvasHeight.toInt() / 3, 10)

    private val deskPlayer2 = Desk((canvasWidth - canvasWidth * 0.1 - 10),
            canvasHeight / 3,
            canvasHeight.toInt() / 3, 10)

    private val keys = mutableMapOf(
            Pair(KeyCode.W, false),
            Pair(KeyCode.S, false),
            Pair(KeyCode.UP, false),
            Pair(KeyCode.DOWN, false)
    )

    private var keyPress = false

    fun keyTest() {
        if (keys[KeyCode.W]!!) canvas.rePaintDesk(deskPlayer1, -2)
        if (keys[KeyCode.UP]!!) canvas.rePaintDesk(deskPlayer2, -2)
        if (keys[KeyCode.S]!!) canvas.rePaintDesk(deskPlayer1, 2)
        if (keys[KeyCode.DOWN]!!) canvas.rePaintDesk(deskPlayer2, 2)
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
                    keyPress = true
                }
            }
            root.add(backGround)
            canvas.widthProperty().bind((canvas.parent as Pane).widthProperty())
            canvas.heightProperty().bind((canvas.parent as Pane).heightProperty())
        }
        timer(daemon = true, period = 10) {
            fire(MoveEvent)
        }

        subscribe<MoveEvent> {
            //keyPress = false // чтобы шарик не мешал
            keyTest()
            if (keyPress) {
                val lim = Point(canvasWidth, canvasHeight)

                val ans = canvas.rePaintBall(ball, lim, deskPlayer1, deskPlayer2)
                //s  println(ans)
                if (ans != 0) {
                    if (ans == 1) scorePlayer1++
                    if (ans == -1) scorePlayer2++
                    ball.x = canvasWidth / 2.0
                    ball.y = canvasHeight / 2
                    ball.dx = ans.toDouble()
                    ball.dy = 0.0
                    ball.v = -1 * ans.toDouble()
                    deskPlayer1.y = ball.y - deskPlayer1.length / 2
                    deskPlayer2.y = ball.y - deskPlayer2.length / 2
                    canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
                    keyPress = false
                }
            } else {
                backGround.paintLine()

                canvas.paintBall(ball)
                backGround.graphicsContext2D.fill = Color.WHITE
                backGround.graphicsContext2D.fillText("Press key to start", canvas.width / 2 - 50, canvas.height / 2)
                //  backGround.graphicsContext2D.fillText("$scorePlayer1", 200.0,100.0)
                canvas.paintDesk(deskPlayer1)
                canvas.paintDesk(deskPlayer2)
            }
        }
    }
}
