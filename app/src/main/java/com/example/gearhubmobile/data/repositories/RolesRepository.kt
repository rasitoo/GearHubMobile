package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.RolesApi
import com.example.gearhubmobile.data.models.AssignRoleRequest
import com.example.gearhubmobile.data.models.RemoveRoleRequest
import com.example.gearhubmobile.data.models.RoleDto
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 29 mayo, 2025
 */
class RolesRepository @Inject constructor(private val api: RolesApi) {

    suspend fun assignRole(id: String, roleId: String, communityId: String): Response<Unit> {
        val roleRequest = AssignRoleRequest(id, roleId, communityId)

        return api.assignRole(roleRequest)
    }

    suspend fun createRole(
        id: String,
        name: String,
        list: List<String>,
        communityId: String,
    ): Response<Unit> {
        val roleRequest = RoleDto(id, name, list, communityId)

        return api.createRole(roleRequest)
    }

    suspend fun removeRole(id: String, roleId: String): Response<Unit> {
        return api.removeRole(id, roleId)
    }

    suspend fun getRolesByCommunity(communityId: String): List<RoleDto> {
        return api.getRolesByCommunity(communityId)
    }

    suspend fun getRoleInCommunity(id: String, communityId: String): RoleDto {
        return api.getRoleInCommunity(id, communityId)
    }

    suspend fun removeRoleGeneral(id: String, roleId: String, communityId: String): Response<Unit> {
        val roleRequest = RemoveRoleRequest(id, roleId, communityId)

        return api.removeRoleGeneral(roleRequest)
    }

    suspend fun updateRolesOfCommunity(id: String, list: List<RoleDto>): Response<Unit> {
        return api.updateRolesOfCommunity(id, list)
    }

}