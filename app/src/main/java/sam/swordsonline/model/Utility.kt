package sam.swordsonline.model

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun CalculatePairFromPosition(pos: Int): Pair<Int, Int> {
    val column = Math.ceil((pos.toDouble() + 1) / 10).toInt()
    val row = column * 10 - pos
    val mycol = Math.abs(column - 11)
    val myrow = Math.abs(row - 11)
    return Pair(myrow, mycol)
}

fun CalculatePositionFromPair(pair: Pair<Int, Int>): Int {
    when (pair.first) {
        1 -> return 100 - (pair.second * 10)
        2 -> return 101 - (pair.second * 10)
        3 -> return 102 - (pair.second * 10)
        4 -> return 103 - (pair.second * 10)
        5 -> return 104 - (pair.second * 10)
        6 -> return 105 - (pair.second * 10)
        7 -> return 106 - (pair.second * 10)
        8 -> return 107 - (pair.second * 10)
        9 -> return 108 - (pair.second * 10)
        10 -> return 109 - (pair.second * 10)
    }
    return 0
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
}