package com.github.guqt178.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


//UI thread
internal val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

//适合计算密集型
internal val cpuScope = CoroutineScope(Dispatchers.Default)

//文件读写 / 网络访问
internal val ioScope = CoroutineScope(Dispatchers.IO)


suspend fun <T> Deferred<T>.await(): T = await()

//代理 Coroutine  --->    delay
suspend fun delayAction(timeMillis: Long) {
    delay(timeMillis)
}

//代理 CoroutineScope.async
fun <T> CoroutineScope.doAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T): Deferred<T> = async(context, start, block)


//代理 CoroutineScope.launch
fun CoroutineScope.doLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit): Job = launch(context, start, block)

fun main(action: suspend CoroutineScope.() -> Unit) {
    mainScope.launch {
        action.invoke(this)
    }
}

fun io(action: suspend CoroutineScope.() -> Unit) {
    ioScope.launch {
        action.invoke(this)
    }
}

fun compute(action: suspend CoroutineScope.() -> Unit) {
    cpuScope.launch {
        action.invoke(this)
    }
}