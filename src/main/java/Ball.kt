import kotlin.math.abs
import kotlin.math.sign

enum class Player {
    One,
    Two
}

class Ball(var x: Double, var y: Double, var dx: Double, var dy: Double, var nowDesk: Player, val radius: Double) {

    fun step(width: Double, heigh: Double, deskPlayer1: Desk, deskPlayer2: Desk): Int {
        x += dx
        y += dy

        if (x - radius <= deskPlayer1.weight + deskPlayer1.x && nowDesk == Player.One) {
            val tg = dy / dx
            val b = y - tg * x
            var tempX = deskPlayer1.x + deskPlayer1.weight + radius
            var tempY = tempX * tg + b
            val lasty = y
            x = tempX + 0.5
            y = tempY
            if (tempY in deskPlayer1.y - radius + 1..deskPlayer1.y + deskPlayer1.length + radius) {

                inDesk(deskPlayer1)
                nowDesk = Player.Two
            } else {

                if (lasty < deskPlayer1.y + radius) {
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
                nowDesk = Player.Two
            }

        }
        if (x + radius >= deskPlayer2.x && nowDesk == Player.Two) {
            val tg = dy / dx
            val b = y - tg * x
            var tempX = deskPlayer2.x - radius
            var tempY = tempX * tg + b
            val lasty = y
            x = tempX - 0.5
            y = tempY
            if (tempY in deskPlayer2.y - radius + 1..deskPlayer2.y + deskPlayer2.length + radius) {
                inDesk(deskPlayer2)
                nowDesk = Player.One
            } else {
                if (lasty < deskPlayer2.y + radius) {
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
                nowDesk = Player.One
            }
        }

        if (y >= heigh - radius) {
            y = heigh - radius
            dy = -dy
        }
        if (y < radius) {
            y = radius
            dy = -dy
        }

        return when {
            x < radius -> 1
            x >= width - radius -> -1
            else -> 0
        }
    }

    fun inDesk(desk: Desk) {
        if (y > (desk.y - radius) && y < (desk.y + desk.length + radius)) {
            val center = desk.y + desk.length / 2.0
            dx = -dx
            val temp = dy.sign * abs(center - y) / 25
            dy = if (abs(dx) > 2) temp
            else temp * abs(dx) / 2
            speed()
        }
    }

    private fun speed() {
        if (dx > 0) dx += 0.5
        else dx -= 0.5
    }
}