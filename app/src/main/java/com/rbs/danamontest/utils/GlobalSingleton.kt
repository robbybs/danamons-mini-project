package com.rbs.danamontest.utils

object GlobalSingleton {

    // a reference to the listener of the Activity is kept as long as
    // unregister isn't called
    private val listeners = mutableListOf<GlobalSingletonListener>()

    fun register(listener: GlobalSingletonListener) {
        listeners.add(listener)
    }

    fun unregister(listener: GlobalSingletonListener) {
        listeners.remove(listener)
    }
}

interface GlobalSingletonListener {
    fun onEvent()
}