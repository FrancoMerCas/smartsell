package com.sinaptix.smartsell

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform