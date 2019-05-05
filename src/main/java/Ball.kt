import kotlin.math.abs
import kotlin.math.sign

class Ball(var x: Int, var y: Int, var dx: Int, var dy: Int, val radius: Int) {
    fun step(limit: Point, deskPlayer1: Desk, deskPlayer2: Desk):Int {

        x += dx
        y += dy
        val weightDesk1 = deskPlayer1.x+deskPlayer1.weight+1
        val weightDesk2 = deskPlayer2.x
        if (x-radius==weightDesk1) {
            return if  (!inDesk(deskPlayer1)) 1
            else 0
        }
        if (x+radius==weightDesk2) {
            return if (!inDesk(deskPlayer2)) -1
            else 0
        }

        if (y >= limit.y - radius) {
            y = limit.y - radius
            dy = -dy;
        }

        if (y < radius) {
            y = radius
            dy = -dy
        }
        return 0
    }

    private fun inDesk(deskPlayer: Desk):Boolean {

        val edgeDesk = deskPlayer.y + deskPlayer.length
        println("y $y\n edge $edgeDesk\n desk " +  deskPlayer.y )
        return if (y in deskPlayer.y..edgeDesk) {

            val center = deskPlayer.y + (deskPlayer.length / 2)
            val tempY = (y - center)/10
            // угол и расстояние
            println("indesk $dy $dx")
            dy = -tempY
            dx = - dx
            println("indesk $dy $dx")
            //speed()
            true
        }
        else false
        }

    fun speed() {

    }
}

data class Point(var x: Int, var y: Int)