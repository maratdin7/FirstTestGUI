class Model(private val ball: Ball, private val deskPlayer1: Desk, private val deskPlayer2: Desk) {
    private var player1 = Player.One
    private var player2 = Player.Two

    fun step(stepDeskPlayer1: StepDesk, stepDeskPlayer2: StepDesk, stepBall: Boolean, limitX: Double, limitY: Double): Pair<Player, Game> {
        deskPlayer1.stepDesk(limitY, ball.radius, stepDeskPlayer1)
        deskPlayer2.stepDesk(limitY, ball.radius, stepDeskPlayer2)

        var ans = Pair(Player.Nobody, Game.Play)
        if (stepBall) {
            val gool = ball.step(limitX, limitY, deskPlayer1, deskPlayer2)
            gool.score()
            ans = Pair(gool, ans.second)
            player1.score(gool)
            player2.score(gool)
        }
        val win = playerWin()
        return if (win != Player.Nobody) Pair(win, Game.Finish) else ans
    }


    private fun winner(firstScore: Int, secondScore: Int): Boolean = firstScore >= 10 && (firstScore - secondScore) >= 2

    private fun playerWin(): Player {
        if (winner(player1.score, player2.score)) {
            player1.score = 0
            player2.score = 0
            return player1
        }
        if (winner(player2.score, player1.score)) {
            player1.score = 0
            player2.score = 0
            return player2
        }
        return Player.Nobody
    }
}

enum class Player(var score: Int) {
    One(0),
    Two(0),
    Nobody(0);

    fun score(gool: Player) {
        if (this.name == gool.name) this.score = gool.score
    }

    fun score() {
        if (this != Nobody) this.score++
    }
}