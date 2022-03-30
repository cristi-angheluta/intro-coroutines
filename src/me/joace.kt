package me
// 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
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlin.system.measureTimeMillis

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main() = runBlocking {
    val job = launch(CoroutineName("mainJob-1")) {
        log("In coroutine ctx = `${this.coroutineContext}`")

        try {
            repeat(1000) {
                log("job: I'm sleeping ${it + 1} ...")
                delay(500)
            }
        } finally {
            withContext(NonCancellable) {
                log("In finally block")
                delay(2000)
                log("Done with finally")
            }
        }
    }
    log("Ready?")
    delay(3000)
    log("Cancelling and waiting to finish job: $job")
    job.cancelAndJoin()
    log("Done")

    val time = measureTimeMillis {
        val one = async(start = LAZY) { doSomethingUsefulOne() }
        val two = async(start = LAZY) { doSomethingUsefulTwo() }

        one.start()
        two.start()

        println("The answer is ${one.await() + two.await()}")
    }
    println("Concurrent Sum: ${concurrentSum()}")
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}
