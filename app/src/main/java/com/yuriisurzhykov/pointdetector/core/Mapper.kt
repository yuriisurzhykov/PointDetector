package com.yuriisurzhykov.pointdetector.core

interface Mapper<T, S> {

    fun map(from: T): S

    abstract class UnitMapper<T> : Mapper<T, Unit>

}