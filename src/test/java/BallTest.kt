import org.junit.Test

class BallTest {

    @Test
    fun step() {
        val deskPlayer1 = Desk(70.0, 120.0, 120.0,10.0)
        val deskPlayer2 = Desk(620.0, 120.0, 120.0, 10.0)

      val ball = Ball(80.0,120.0,-1.0, 3.0,Player.One,10.0)
        ball.step(700.0,360.0,deskPlayer1,deskPlayer2)
        println("dx = ${ball.dx}  dy = ${ball.dy}")
        ball.step(700.0,360.0,deskPlayer1,deskPlayer2)
        println("dx = ${ball.dx}  dy = ${ball.dy}")
        ball.step(700.0,360.0,deskPlayer1,deskPlayer2)
        println("dx = ${ball.dx}  dy = ${ball.dy}")

    }

    @Test
    fun inDesk() {
        val desk = Desk(70.0, 100.0, 100.0,10.0)
         for (i in 0 .. 120) {
             val ball = Ball(80.0,90.0+i,-1.0, 0.0,Player.One,10.0)
             val y = ball.y
             print("$y --- ")
             ball.inDesk(desk)
         }
    }
}
