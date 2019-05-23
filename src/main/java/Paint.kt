import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.text.Font

class Paint(private val ball: Ball, private val canvas: Canvas, private val backGround: Canvas) {

    private fun paintBall() {
        canvas.graphicsContext2D.fill = Color.WHITE
        with(ball) {
            canvas.graphicsContext2D.fillOval(
                    x - radius,
                    y - radius,
                    2 * radius,
                    2 * radius)
        }
    }

    private fun deleteBall() {
        with(ball) {
            canvas.graphicsContext2D.clearRect(
                    x - radius,
                    y - radius,
                    2 * radius,
                    2 * radius)
        }
    }

    private fun paintDesk(desk: Desk) {
        canvas.graphicsContext2D.fill = Color.WHITE
        with(desk) {
            canvas.graphicsContext2D.fillRect(
                    x,
                    y,
                    width,
                    length)
        }
    }

    private fun deleteDesk(desk: Desk) {
        with(desk) {
            canvas.graphicsContext2D.clearRect(
                    x,
                    y,
                    width,
                    length)
        }
    }

    fun paintAll(deskPlayer1: Desk, deskPlayer2: Desk) {
        paintBall()
        paintDesk(deskPlayer1)
        paintDesk(deskPlayer2)
    }

    fun deleteAll(deskPlayer1: Desk, deskPlayer2: Desk) {
        deleteBall()
        deleteDesk(deskPlayer1)
        deleteDesk(deskPlayer2)
    }

    private fun paintLine() {
        with(backGround) {
            graphicsContext2D.stroke = Color.WHITE
            graphicsContext2D.lineWidth = 2.0
            graphicsContext2D.strokeLine(width / 2, 0.0, width / 2, height)
        }
    }

    fun playerWin(player: Player) {
        with(backGround) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 30.0)
            graphicsContext2D.fillText("Player ${player.name} wins!!!", width / 2 - 140, height / 3 + 10)
        }
    }


    fun paintText() {
        with(backGround) {
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 20.0)
            graphicsContext2D.fillText("Press space to start", width / 2 - 100, height / 2)
        }
    }

    private fun paintScore(score: Int, widthForScore: Double) {
        with(backGround) {
            graphicsContext2D.fill = Color.WHITE
            graphicsContext2D.font = Font("Arial", 20.0)
            graphicsContext2D.fillText("$score", widthForScore, height / 4)
        }
    }

    fun rePaintScore(player: Player) {
        val widthForScore = if (player == Player.One) backGround.width / 3 - 10 else backGround.width * 2 / 3 + 10
        with(backGround) {
            graphicsContext2D.clearRect(widthForScore, height / 4 - 20, 40.0, 20.0)
            paintScore(player.score, widthForScore)
        }
    }

    fun paintStartGame(deskPlayer1: Desk, deskPlayer2: Desk) {
        with(backGround) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            paintLine()
            rePaintScore(Player.One)
            rePaintScore(Player.Two)
        }
        with(canvas) {
            graphicsContext2D.clearRect(0.0, 0.0, width, height)
            paintBall()
            paintDesk(deskPlayer1)
            paintDesk(deskPlayer2)
        }
    }
}
