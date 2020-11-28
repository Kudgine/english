package com.deadely.itgenglish.utils

import android.widget.TextView
import java.util.*

object Utils {

    fun printText(word: Word, textView: TextView) {
        if (word.currentCharacterIndex == 0) {
            textView.text = ""
        }
        val random = Random(System.currentTimeMillis())
        val currentRandOffset: Int = random.nextInt(word.offset)
        val addOrSubtract: Boolean = random.nextBoolean()
        var finalDelay =
            if (addOrSubtract) word.delayBetweenSymbols + currentRandOffset else word.delayBetweenSymbols - currentRandOffset
        if (finalDelay < 0) finalDelay = 0
        textView.postDelayed(Runnable {
            val charAt = word.word[word.currentCharacterIndex].toString()
            ++word.currentCharacterIndex
            textView.text = textView.text.toString() + charAt
            if (word.currentCharacterIndex >= word.word.length) {
                return@Runnable
            }
            printText(word, textView)
        }, finalDelay)
    }
}

class Word(delayBetweenSymbols: Long, word: String) {
    val delayBetweenSymbols: Long
    val word: String
    var offset = 0
    var currentCharacterIndex = 0

    init {
        require(delayBetweenSymbols >= 0) { "Delay can't be < 0" }
        this.delayBetweenSymbols = delayBetweenSymbols
        this.word = word
    }
}