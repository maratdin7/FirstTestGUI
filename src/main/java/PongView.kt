import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.FXEvent
import tornadofx.View
import tornadofx.canvas
import kotlin.collections.set
import kotlin.concurrent.timer

class Paint(private val ball: Ball) {

    fun paintBall(canvas: Canvas) {
        canvas.graphicsContext2D.fill = Color.WHITE
        with(ball) {
            canvas.graphicsContext2D.fillOval(
                    x - radius,
                    y - radius,
                    2 * radius,
                    2 * radius)
        }
    }

    fun rePaintBall(canvas: Canvas, deskPlayer1: Desk, deskPlayer2: Desk): Int {
        with(ball) {
            canvas.graphicsContext2D.clearRect(
                    x - radius,
                    y - radius,
                    2 * radius,
                    2 * radius)
        }
        val ans = ball.step(canvas.width, canvas.height, deskPlayer1, deskPlayer2)
        paintBall(canvas)
        return ans
    }

    fun paintDesk(canvas: Canvas, desk: Desk) {
        canvas.graphicsContext2D.fill = Color.WHITE
        with(desk) {
            canvas.graphicsContext2D.fillRect(
                    x,
                    y,
                    weight,
                    length)
        }
    }

    fun rePaintDesk(canvas: Canvas, desk: Desk, ddy: Int) {
        with(desk) {
            canvas.graphicsContext2D.clearRect(
                    x,
                    y,
                    weight,
                    length)
        }
        desk.step(canvas.height, ball.radius, ddy)
        paintDesk(canvas, desk)
    }

    fun stepDesk(canvas: Canvas, deskPlayer1: Desk, deskPlayer2: Desk, keys: Map<KeyCode, Boolean>) {
        if (keys[KeyCode.W]!!) rePaintDesk(canvas, deskPlayer1, -2)
        if (keys[KeyCode.UP]!!) rePaintDesk(canvas, deskPlayer2, -2)
        if (keys[KeyCode.S]!!) rePaintDesk(canvas, deskPlayer1, 2)
        if (keys[KeyCode.DOWN]!!) rePaintDesk(canvas, deskPlayer2, 2)
    }

    fun paintLine(backGround: Canvas) {
        with(backGround) {
            graphicsContext2D.stroke = Color.WHITE
            graphicsContext2D.lineWidth = 2.0
            graphicsContext2D.strokeLine(width / 2, 0.0, width / 2, height)
        }
    }

    fun playerWin(backGround: Canvas, player: Char) {
        with(backGround) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 30.0)
            graphicsContext2D.fillText("Player $player wins!!!", width / 2 - 100, height / 3 + 10)
        }
    }


    fun paintText(backGround: Canvas) {
        with(backGround) {
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 20.0)
            graphicsContext2D.fillText("Press key to start", width / 2 - 90, height / 2)
        }
    }

    fun paintScore(backGround: Canvas, score: Int, widthPlayer: Double) {
        with(backGround) {
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 20.0)
            graphicsContext2D.fillText("$score", widthPlayer, height / 4)
        }
    }

    fun rePaintScore(backGround: Canvas, score: Int, widthPlayer: Double) {
        with(backGround) {
            graphicsContext2D.clearRect(widthPlayer, height / 4 - 20, 40.0, 20.0)
            paintScore(backGround, score, widthPlayer)
        }
    }

    fun paintStartGame(backGround: Canvas, canvas: Canvas, deskPlayer1: Desk, deskPlayer2: Desk) {
        with(backGround) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            paintLine(backGround)
            rePaintScore(backGround, 0, width / 3 - 10)
            rePaintScore(backGround, 0, 2 * width / 3 + 10)
        }
        with(canvas) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            paintBall(canvas)
            paintDesk(canvas, deskPlayer1)
            paintDesk(canvas, deskPlayer2)
        }
    }
}

object MoveEvent : FXEvent()

enum class Game {
    Start,
    Play,
    Finish
}

class PongView : View() {
    override var root = Pane()

    private val canvasWidth = 700.0

    private val canvasHeight = 360.0

    private var canvas: Canvas = Canvas(canvasWidth, canvasHeight)

    private var backGround: Canvas = Canvas(canvasWidth, canvasHeight)

    private var scorePlayer1 = 0

    private var scorePlayer2 = 0

    private val ball = Ball(canvas.width / 2, canvas.height / 2, -1.0, 0.5, Player.One, 10.0)

    private val deskPlayer1 = Desk((canvas.width * 0.1), canvas.height / 3, canvas.height / 3, ball.radius)

    private val deskPlayer2 = Desk(
            canvasWidth - canvasWidth * 0.1 - ball.radius,
            canvasHeight / 3,
            canvasHeight / 3, ball.radius)

    private val keys = mutableMapOf(
            Pair(KeyCode.W, false),
            Pair(KeyCode.S, false),
            Pair(KeyCode.UP, false),
            Pair(KeyCode.DOWN, false)
    )

    private fun winner(firstScore: Int, secondScore: Int, player: Char) {
        if (firstScore >= 10 && (firstScore - secondScore) >= 2) {
            game = Game.Finish
            paint.playerWin(backGround, player)
        }
    }

    private var paint = Paint(ball)

    private var keyPress = false

    private var game = Game.Start

    init {
        title = "Pong"

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
            when {
                game == Game.Start && keyPress -> {
                    paint.paintStartGame(backGround, canvas, deskPlayer1, deskPlayer2)
                }
                game == Game.Start -> {
                    paint.paintText(backGround)
                }
                game == Game.Finish -> {
                    scorePlayer1 = 0
                    scorePlayer2 = 0
                    keyPress = false
                    game = Game.Start
                    canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
                }

            }

            paint.stepDesk(canvas, deskPlayer1, deskPlayer2, keys)

            if (keyPress) {
                game = Game.Play
                val gool = paint.rePaintBall(canvas, deskPlayer1, deskPlayer2)
                if (gool != 0) {
                    if (gool == 1) {
                        scorePlayer2++
                        paint.rePaintScore(backGround, scorePlayer2, backGround.width * 2 / 3 + 10)
                    }
                    if (gool == -1) {
                        scorePlayer1++
                        paint.rePaintScore(backGround, scorePlayer1, backGround.width / 3 - 10)
                    }
                    winner(scorePlayer1, scorePlayer2, '1')
                    winner(scorePlayer2, scorePlayer1, '2')
                    with(ball) {
                        x = canvas.width / 2
                        y = canvas.height / 2
                        dx = gool.toDouble()
                        dy = 0.5
                        nowDesk = if (gool == -1) Player.One else Player.Two
                        deskPlayer1.y = y - deskPlayer1.length / 2
                        deskPlayer2.y = y - deskPlayer2.length / 2
                    }
                    canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
                    paint.paintBall(canvas)
                    paint.paintDesk(canvas, deskPlayer1)
                    paint.paintDesk(canvas, deskPlayer2)
                    keyPress = false
                }
            }
        }
    }
}

