package com.example.the_schedulaing_application.data.objects

import io.realm.kotlin.types.EmbeddedRealmObject

class RealmCaseSingleton: EmbeddedRealmObject {
    var epochTimeMilli: Long = System.currentTimeMillis()
    var event: Event? = null
}