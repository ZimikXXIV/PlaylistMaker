package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useNewAction: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useNewAction) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useNewAction) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}