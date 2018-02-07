package com.github.rixspi.simplecompass.ui.main

enum class MainPosition(val position: Int) {
    Compass(1);

    companion object {
        fun from(position: Int): MainPosition = MainPosition.values().firstOrNull { it.position == position } ?: Compass
    }
}