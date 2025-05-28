package com.example.gearhubmobile.data.models

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
data class RoleDto(
    val id: String,
    val name: String,
    val permissions: List<String>,
    val communityId: String
)

data class AssignRoleRequest(
    val userId: String,
    val roleId: String,
    val communityId: String
)

data class RemoveRoleRequest(
    val userId: String,
    val roleId: String,
    val communityId: String
)

