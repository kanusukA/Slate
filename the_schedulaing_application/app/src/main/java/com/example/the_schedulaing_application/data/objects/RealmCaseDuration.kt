package com.example.the_schedulaing_application.data.objects

import io.realm.kotlin.types.EmbeddedRealmObject

class RealmCaseDuration: EmbeddedRealmObject {
    var fromEpoch: Long = 0L
    var toEpoch: Long = 0L
    var event: Event? = null
}