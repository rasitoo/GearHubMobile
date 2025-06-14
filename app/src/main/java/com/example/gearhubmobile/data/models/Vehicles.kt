package com.example.gearhubmobile.data.models

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
data class VehicleUser(
    val id: String,
    val userId: String,
    val name: String,
    val surname: String
)

data class VehicleUserModifications(
    val name: String,
    val surname: String,
    val dni: String
)

data class VehicleUserDetail(
    val id: String,
    val userId: String,
    val name: String,
    val surname: String,
    val dni: String
)

data class VehicleUserPost(
    val name: String,
    val surName: String,
    val dni: String
)

data class VehicleDetail(
    val vin: String,
    val brand: String,
    val model: String,
    val year: Int,
    val id: String,
    val license: String,
    val userId: String
)
data class VehiclePost(
    val vin: String,
    val brand: String,
    val model: String,
    val year: Int,
    val license: String
)
data class Vehicle(
    val id: String,
    val license: String,
    val userId: String
)