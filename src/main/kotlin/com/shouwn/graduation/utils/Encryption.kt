package com.shouwn.graduation.utils

import java.security.MessageDigest

object Encryption {
    const val SHA256 = "SHA-256"
    const val MD5 = "MD5"

    fun encrypt(s: String, messageDigest: String): String =
            MessageDigest.getInstance(messageDigest).apply { reset() }.digest(s.toByteArray())
                    .map { Integer.toHexString(0xff and it.toInt()) }
                    .toString()
}