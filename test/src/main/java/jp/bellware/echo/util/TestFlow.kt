package jp.bellware.echo.util

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@InternalCoroutinesApi
fun <T> testFlow(value: T): Flow<T> {
    return object : Flow<T> {
        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<T>) {
            collector.emit(value)
        }
    }
}