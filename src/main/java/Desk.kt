class Desk(val x: Double, var y: Double, val length: Double, val width: Double, private val speedDesk: Int = 2) {

    private fun step(limitY: Double, radious: Double, ddy: Int) {
        y += ddy
        val limitMin = 2 * (radious + 1)
        val limitMax = limitY - length - limitMin
        if (y <= limitMin) y = limitMin
        if (y >= limitMax) y = limitMax
    }

    fun stepDesk(limitY: Double, radius: Double, stepDesk: StepDesk) = when (stepDesk) {
        StepDesk.UP -> step(limitY, radius, -speedDesk)
        StepDesk.Down -> step(limitY, radius, speedDesk)
        else -> {
        }
    }
}

enum class StepDesk {
    UP,
    Down,
    Nothing
}