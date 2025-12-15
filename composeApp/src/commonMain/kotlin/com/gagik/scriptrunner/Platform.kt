package com.gagik.scriptrunner

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform