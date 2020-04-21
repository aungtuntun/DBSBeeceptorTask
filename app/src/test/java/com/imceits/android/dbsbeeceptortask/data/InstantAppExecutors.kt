package com.imceits.android.dbsbeeceptortask.data

import java.util.concurrent.Executor

class InstantAppExecutors: AppExecutors(instant, instant, instant) {

    companion object {
        private val instant : Executor = Executor { command -> command.run() }
    }
}