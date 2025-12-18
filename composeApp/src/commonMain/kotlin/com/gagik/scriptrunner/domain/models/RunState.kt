package com.gagik.scriptrunner.domain.models

import com.gagik.scriptrunner.domain.models.RunState.IDLE
import com.gagik.scriptrunner.domain.models.RunState.RUNNING
import com.gagik.scriptrunner.domain.models.RunState.STOPPING


/**
 * Represents the execution state of a script.
 *
 * @property IDLE The default state.
 * @property RUNNING The script is currently running.
 * @property STOPPING The script is currently waiting to be stopped.
 */
enum class RunState {
    IDLE,
    RUNNING,
    STOPPING,
}