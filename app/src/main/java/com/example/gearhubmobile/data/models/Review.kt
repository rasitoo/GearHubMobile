package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Review(
    val id: String,
    val rating: Int,
    val comment: String,
    val userId: String,
    val response: Response
    )
