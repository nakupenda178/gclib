package com.github.guqt178.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

open class Event<out T>(private val content: T? = null) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T? = content
}

class VoidEvent {
    private var hasBeenHandled = false

    fun hasBeenHandled(): Boolean = if (hasBeenHandled) {
        true
    } else {
        hasBeenHandled = true
        false
    }
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, onEventUnhandled: (T) -> Unit) {
    observe(owner, EventObserver(onEventUnhandled))
}

fun LiveData<VoidEvent>.observeEvent(owner: LifecycleOwner, onEventUnhandled: () -> Unit) {
    observe(owner, Observer { if (it?.hasBeenHandled() == false) onEventUnhandled() })
}

inline fun <T> LiveData<Event<T>>.observe(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}