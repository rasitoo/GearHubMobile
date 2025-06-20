package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Community(
    val id: String,
    val comName: String,
    val comPicture: String?,
    val comBanner: String?,
    val comDescription: String?,
    val creatorId: String,
    val subscriptions: Int?
)
data class CreateCommunityResponse(val id: String)
data class CommunityDto(
    val id: String,
    val comName: String,
    val comPicture: String?
)

data class CommunityCreateDTO(
    val id: String,
    val comName: String?,
    val comPicture: String?,
    val comBanner: String?
)

data class CommunityUpdateDTO(
    val id: String,
    val comName: String?,
    val comDesc: String?,
    val comPicture: String?,
    val comBanner: String?
)

data class CommunityResponse(
    val data: List<CommunityDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val totalPages: Int
)