package com.cdek.courier.ui.base.task.robocall

import com.cdek.courier.utils.enums.RobocallTypes

interface DialogTimeListener {

    fun pickRobocall(
        type: RobocallTypes
    )

    fun pickRobocallChangeTime(
        hourStart: Int,
        minuteStart: Int,
        hourEnd: Int,
        minuteEnd: Int
    )
}