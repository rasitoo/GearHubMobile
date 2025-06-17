package com.example.gearhubmobile.ui.navigation

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val LOGOUT = "logout"
    const val REGISTER = "register"
    const val RECOVER = "recover"
    const val CREATE_USER = "create_user"
    const val MAIN = "main"

    const val HOME = "home"

    const val COMMUNITIES = "communities"
    const val COMMUNITY_DETAIL = "communityDetail/{communityId}"
    const val COMMUNITY_DETAIL_BASE = "communityDetail"
    const val CREATE_COMMUNITY = "community_create"

    const val POST = "post"
    const val POST_DETAIL = "post_detail/{threadId}"
    const val POST_DETAIL_BASE = "post_detail"

    const val CHAT_DETAIL = "chatDetail/{chatId}"
    const val CHAT_DETAIL_BASE = "chatDetail"
    const val CHAT_INFO= "chatInfo/{chatId}"
    const val CHAT_INFO_BASE = "chatInfo"
    const val CHATS = "chats"

    const val USER_DETAIL = "userDetail/{userId}"
    const val USER_DETAIL_BASE = "userDetail"
    const val USERS = "users"

    const val CREATE_CHAT = "createChat"
    const val EDIT_CHAT = "editChat/{chatId}"
    const val EDIT_CHAT_BASE = "editChat"
    const val SELECT_USERS = "usersList"

    const val VEHICLES = "vehicles"
    const val VEHICLES_DETAIL = "vehicles/{userId}"
    const val ADD_VEHICLE = "add_vehicle"
    const val ADD_VEHICLE_EXTENDED = "add_vehicle/{vehicleId}"

    const val REVIEWS = "reviews"
    const val REVIEWS_DETAIL = "reviews/{userId}"
    const val ADD_REVIEW = "add_review"
}
