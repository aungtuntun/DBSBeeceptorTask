package com.imceits.android.dbsbeeceptortask

import com.imceits.android.dbsbeeceptortask.data.Resource
import com.imceits.android.dbsbeeceptortask.data.Status
import org.mockito.ArgumentMatcher

class Error404NotFoundMatcher<T>: ArgumentMatcher<Resource<T>> {
    override fun matches(argument: Resource<T>): Boolean {
        return argument.status == Status.ERROR
    }
}