package com.teamxdevelopers.teamx.utils

fun <T> List<T>.getFive():List<T>{
    if(size > 5)
        return subList(size-6,size-1)
    return this
}
