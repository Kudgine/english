package com.deadely.itgenglish.model


data class Text(
    var isUser: Boolean,
    var text: String
)

fun toUserSpeech(text: String): String {
    return "You:\n \n    - $text"
}

fun toManSpeech(text: String): String {
    return "Man:\n \n    - $text"
}

fun toFemaleSpeech(text: String): String {
    return "Woman:\n \n    - $text"
}

fun toGirlSpeech(text: String): String {
    return "Girl:\n \n    - $text"
}

fun toBoySpeech(text: String): String {
    return "Boy:\n \n    - $text"
}