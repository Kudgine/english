package com.deadely.itgenglish.utils


class RequestField {
    companion object {
        const val EMAIL = "Email"
        const val PASSWORD = "Password"

        @JvmStatic
        fun getLoginParams(email: String, password: String): LinkedHashMap<String, String> {
            val params: LinkedHashMap<String, String> = LinkedHashMap(2)
            params[EMAIL] = email
            params[PASSWORD] = password
            return params
        }
    }


}