package com.example.gearhubmobile.ui.navigation

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val RECOVER = "recover"
    const val CREATE_USER = "create_user"
    const val MAIN = "main"

    const val HOME = "home"

    const val COMMUNITIES = "communities"
    const val COMMUNITY_DETAIL = "communityDetail/{communityId}"
    const val COMMUNITY_DETAIL_BASE = "communityDetail"

    const val POST = "post"

    const val CHAT_DETAIL = "chatDetail/{chatId}"
    const val CHAT_DETAIL_BASE = "chatDetail"
    const val CHATS = "chats"

    const val USER_DETAIL = "userDetail?userId={userId}"
    const val USER_DETAIL_BASE = "userDetail"

    const val CREATE_CHAT = "createChat"
    const val SELECT_USERS = "usersList"

    const val VEHICLES = "vehicles"
    const val ADD_VEHICLE = "add_vehicle"
}
