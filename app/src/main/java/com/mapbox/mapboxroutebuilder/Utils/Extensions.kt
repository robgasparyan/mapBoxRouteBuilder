package com.mapbox.mapboxroutebuilder.Utils

import kotlin.random.Random


fun <T> List<T>.getRandomItemFromList(): T {
    return this[Random.nextInt(this.size - 1)]
}

