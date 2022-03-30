package me

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun main(args: Array<String>) = runBlocking {
    val dates = flow<LocalDateTime> {
        repeat(10) {
            delay(1000)
            emit(LocalDateTime.now())
        }
    }
    dates.collect { value -> println(value) }
}


fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}
