package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
import com.example.gearhubmobile.data.models.AssignRoleRequest
import com.example.gearhubmobile.data.models.RemoveRoleRequest
import com.example.gearhubmobile.data.models.RoleDto
import retrofit2.Response
import retrofit2.http.*

interface RolesApi {

    @GET("api/Roles/Comunidad/{idCommunity}/Rol/{idRol}")
    suspend fun getRoleInCommunity(
        @Path("idCommunity") communityId: String,
        @Path("idRol") roleId: String
    ): RoleDto

    @GET("api/Roles/RolesComunidad/{idCommunity}")
    suspend fun getRolesByCommunity(@Path("idCommunity") communityId: String): List<RoleDto>

    @POST("api/Roles/assignRole")
    suspend fun assignRole(@Body request: AssignRoleRequest) : Response<Unit>

    @DELETE("api/Roles/community/{communityId}/Rol{rolId}")
    suspend fun removeRole(
        @Path("communityId") communityId: String,
        @Path("rolId") roleId: String
    ) : Response<Unit>

    @PUT("api/Roles/community/{communityId}")
    suspend fun updateRolesOfCommunity(
        @Path("communityId") communityId: String,
        @Body updatedRoles: List<RoleDto>
    ) : Response<Unit>

    @POST("api/Roles/createRole")
    suspend fun createRole(@Body role: RoleDto) : Response<Unit>

    @DELETE("api/Roles/removeRole")
    suspend fun removeRoleGeneral(@Body role: RemoveRoleRequest) : Response<Unit>
}
