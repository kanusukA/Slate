package com.example.the_schedulaing_application.data.objects

import android.net.Uri
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmUser : RealmObject {
    @PrimaryKey var id = 0
    var username = ""
}

class User(
    val id: Int = 0,
    val username: String = "",
    val profilePicture: Uri = Uri.EMPTY
)