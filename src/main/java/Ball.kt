import kotlin.math.abs

class Ball(var x: Double, var y: Double, var dx: Double, var dy: Double, var v: Double, val radius: Double) {

    fun step(limit: Point, deskPlayer1: Desk, deskPlayer2: Desk): Int {
        x += dx// добовление до deskPlayer1.x + deskPlayer1.weight
        y += dy

        if (x - radius <= deskPlayer1.weight + deskPlayer1.x && v == 1.0) {
            val tg = dy / dx
            val b = y - tg * x
            var tempX = deskPlayer1.x + deskPlayer1.weight + radius
            var tempY = tempX * tg + b
            val lasty = y
            x = tempX + 0.5
            y = tempY
            //w    println("tempX = $tempX  tempY = $tempY tg = $tg")
            if (tempY in deskPlayer1.y - radius + 1..deskPlayer1.y + deskPlayer1.length + radius) {

                inDesk(deskPlayer1)
                v = -1.0
            } else {

                if (lasty < deskPlayer1.y + 10) {
                    tempY = deskPlayer1.y - radius
                    tempX = (tempY - b) / tg
                    if (tempX - radius in deskPlayer1.x..deskPlayer1.x + deskPlayer1.weight + radius) {
                        x = tempX
                        y = tempY
                        dx = -dx
                        if (dy > 0) dy = -dy
                    }
                    if (tempX - radius in deskPlayer1.x - 2 * radius..deskPlayer1.x) {
                        x = tempX
                        y = tempY
                        if (dy > 0) dy = -dy
                    }
                } else {
                    tempY = deskPlayer1.y + deskPlayer1.length + radius
                    tempX = (tempY - b) / tg
                    if (tempX - radius in deskPlayer1.x..deskPlayer1.x + deskPlayer1.weight + radius) {
                        x = tempX
                        y = tempY
                        dx = -dx
                        if (dy < 0) dy = -dy
                    }
                    if (tempX - radius in deskPlayer1.x - 2 * radius..deskPlayer1.x) {
                        x = tempX
                        y = tempY
                        if (dy < 0) dy = -dy
                    }
                }
                v = -1.0
            }

        } else {

        }

        if (x + radius >= deskPlayer2.x && v == -1.0) {
            val tg = dy / dx
            val b = y - tg * x
            var tempX = deskPlayer2.x - radius
            var tempY = tempX * tg + b
            val lasty = y
            x = tempX - 0.5
            y = tempY
            //w    println("tempX = $tempX  tempY = $tempY tg = $tg")
            if (tempY in deskPlayer2.y - radius + 1..deskPlayer2.y + deskPlayer1.length + radius) {

                inDesk(deskPlayer2)
                v = 1.0
            } else {
                if (lasty < deskPlayer2.y + 10) {
                    tempY = deskPlayer2.y - radius
                    tempX = (tempY - b) / tg
                    if (tempX + radius in deskPlayer2.x - radius..deskPlayer2.x + deskPlayer2.weight) {
                        x = tempX
                        y = tempY
                        dx = -dx
                        if (dy > 0) dy = -dy
                    }
                    if (tempX + radius in deskPlayer2.x + deskPlayer2.weight..deskPlayer2.x + deskPlayer2.weight + 2 * radius) {
                        x = tempX
                        y = tempY
                        if (dy > 0) dy = -dy
                    }
                } else {
                    tempY = deskPlayer2.y + deskPlayer2.length + radius
                    tempX = (tempY - b) / tg
                    if (tempX + radius in deskPlayer2.x - radius..deskPlayer2.x + deskPlayer2.weight) {
                        x = tempX
                        y = tempY
                        dx = -dx
                        if (dy < 0) dy = -dy
                    }
                    if (tempX + radius in deskPlayer2.x + deskPlayer2.weight..deskPlayer2.x + deskPlayer2.weight + 2 * radius) {
                        x = tempX
                        y = tempY
                        if (dy < 0) dy = -dy
                    }
                }
                v = 1.0
            }

        }
        if (x - radius < deskPlayer1.weight + deskPlayer1.x)
            println("x = $x dx = $dx")
/*        if (x - radius in deskPlayer1.x-2*radius..deskPlayer1.x + deskPlayer1.weight && dx<0)
            if ((y + radius)in  deskPlayer1.y .. deskPlayer1.y+5) {
            if (dy> 0 ) dy = -dy
            dx = -dx
                println("dfffffffffdddfffffffffffq")
        }
        else if (dx<0) inDesk(deskPlayer1)*/
        /*  x += dx// добовление до deskPlayer1.x + deskPlayer1.weight
        y += dy
*/

        /*  if (x + radius in deskPlayer2.x..deskPlayer2.x + deskPlayer2.weight){
              inDesk(deskPlayer2)
              v=1.0
          }*/
        if (y >= limit.y - radius) {// не знаю почему 4 radius
            y = limit.y - radius
            dy = -dy
        }
        if (y < radius) {
            y = radius
            dy = -dy
        }

        return when {
            x < radius -> 1
            x >= limit.x - radius -> -1
            else -> 0
        }
    }

    fun inDesk(desk: Desk) {// private
        if (y > (desk.y - radius) && y < (desk.y + desk.length + radius)) {
            val center = desk.y + desk.length / 2.0
            dx = -dx
            dy = if (abs(dx) > 2) (center - y) / 25
            else (center - y)*dx / 50

            //  println("$dy $dx center = $center)")
            speed()
        }
    }

    fun speed() {//private
        if (dx > 0) dx += 0.5
        else dx -= 0.5
    }

/*    private fun inEdge(tempX:Double,tempY:Double, desk: Desk) {

}*/
}

data class Point(var x: Double, var y: Double)