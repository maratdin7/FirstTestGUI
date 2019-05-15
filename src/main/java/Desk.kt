class Desk(val x: Double, var y: Double, val length: Int, val weight: Int) {
    fun step(limitY: Double, radious: Double, ddy: Int) {
        y += ddy
        val limitMin = 2 * (radious + 1)
        val limitMax = limitY - length - limitMin
        if (y <= limitMin) y = limitMin
        if (y >= limitMax) y = limitMax
    }
}