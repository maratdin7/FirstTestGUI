import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import tornadofx.*
import kotlin.collections.set
import kotlin.concurrent.timer
import kotlin.math.sign

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

    private val ball = Ball(canvas.width / 2, canvas.height / 2, -1.0, 0.25, Player.One, 10.0)

    private val deskPlayer1 = Desk(
            canvas.width * 0.1,
            canvas.height / 3,
            canvas.height / 3, ball.radius)

    private val deskPlayer2 = Desk(
            canvasWidth - canvasWidth * 0.1 - ball.radius,
            canvas.height / 3,
            canvasHeight / 3, ball.radius)

    private var model = Model(ball,deskPlayer1,deskPlayer2)

    private val keys = mutableMapOf(
            Pair(KeyCode.W, false),
            Pair(KeyCode.S, false),
            Pair(KeyCode.UP, false),
            Pair(KeyCode.DOWN, false)
    )

    private var stepDeskPlayer1 = StepDesk.Nothing

    private var stepDeskPlayer2 = StepDesk.Nothing

    private lateinit var paint:Paint

    private var keyPress = false

    private var game = Game.Start

    init {
        title = "Pong"
//println("${canvas.height} ${canvas.width}")
        with(root) {
            style = "-fx-background-color: black"

            canvas = canvas {
                canvas = this
                isFocusTraversable = true

                setOnKeyReleased {
                   /* if (it.code == KeyCode.UP || it.code == KeyCode.DOWN) stepDeskPlayer2 = StepDesk.Nothing
                    if (it.code == KeyCode.W || it.code == KeyCode.S) stepDeskPlayer1 = StepDesk.Nothing*/
                    keys[it.code] = false
                }

                setOnKeyPressed {
                   /* when (it.code) {
                        KeyCode.UP -> stepDeskPlayer2 = StepDesk.UP
                        KeyCode.DOWN -> stepDeskPlayer2 = StepDesk.Down
                        KeyCode.W -> stepDeskPlayer1 = StepDesk.UP
                        KeyCode.S -> stepDeskPlayer1 = StepDesk.Down
                        else -> {}s
                    }*/
                    keys[it.code] = true
                    if (game == Game.Play) keyPress = true
                    if (it.code == KeyCode.SPACE && game == Game.Start) keyPress = true
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

                    paint.paintStartGame(deskPlayer1, deskPlayer2)
                    model = Model(ball,deskPlayer1,deskPlayer2)
                    game = Game.Play

                }
                game == Game.Start -> {

                    paint = Paint(ball,canvas,backGround)
                    paint.paintText()

                }
                game == Game.Finish -> {

                    keyPress = false
                    game = Game.Start
                    canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)

                }
                game == Game.Play && keyPress -> {

                    paint.deleteAll(deskPlayer1, deskPlayer2)

                    stepDeskPlayer2 = when {
                        keys[KeyCode.UP]!! -> StepDesk.UP
                        keys[KeyCode.DOWN]!! -> StepDesk.Down
                        else -> StepDesk.Nothing
                    }
                    stepDeskPlayer1 = when {
                        keys[KeyCode.W]!! -> StepDesk.UP
                        keys[KeyCode.S]!! -> StepDesk.Down
                        else -> StepDesk.Nothing
                    }
                    val gool = model.step(stepDeskPlayer1,
                            stepDeskPlayer2,
                            keyPress,
                            canvas.width,
                            canvas.height)

                    paint.paintAll(deskPlayer1, deskPlayer2)

                    if (gool.first != Player.Nobody) {
                        paint.rePaintScore(gool.first)
                        with(ball) {
                            x = canvas.width / 2
                            y = canvas.height / 2

                            dx = if (gool.first == Player.One) 1.0 else -1.0
                            dy = 0.25
                            nowDesk = if (dx.sign == -1.0) Player.One else Player.Two
                            deskPlayer1.y =  deskPlayer1.length
                            deskPlayer2.y =  deskPlayer2.length
                        }
                        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)

                        paint.paintAll(deskPlayer1, deskPlayer2)

                        keyPress = false
                    }
                    if (gool.second == Game.Finish) {
                        game = Game.Finish
                        paint.playerWin(gool.first)
                    }
                }
            }
        }
    }
}

