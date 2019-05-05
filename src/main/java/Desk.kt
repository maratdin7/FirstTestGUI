class Desk(val x:Int, var y:Int, val length:Int, val weight: Int){
    fun step (limitY:Int,radious:Int, ddy:Int) {
        y+=ddy
        val limitMin= 2*(radious+1)
        val limitMax = limitY - length - limitMin
        if (y<=limitMin) y = limitMin
        if (y>=limitMax) y = limitMax
    }
}