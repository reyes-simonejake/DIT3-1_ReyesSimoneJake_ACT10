package com.example.crossplatformtodolistapp

data class Task(
    val id: Long = System.currentTimeMillis(),
    val text: String
)
