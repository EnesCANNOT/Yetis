package com.helloworldstudios.yetis

data class Service(
    val userEmail: String,
    val serviceTitle: String,
    val serviceDescription: String,
    val serviceLatitude: Double,
    val serviceLongitude: Double)
{
}