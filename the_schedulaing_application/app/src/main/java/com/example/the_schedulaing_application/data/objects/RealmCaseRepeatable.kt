package com.example.the_schedulaing_application.data.objects

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class RealmCaseDaily: EmbeddedRealmObject {
    var timeOfDay: Int = 0
    var event: Event? = null
}

class RealmCaseWeekly: EmbeddedRealmObject{
    var weeks: RealmList<Int> = realmListOf()
    var event: Event? = null
}

class RealmCaseMonthly: EmbeddedRealmObject{
    var days: RealmList<Int> = realmListOf()
    var event: Event? = null
}

class RealmCaseYearly: EmbeddedRealmObject{
    var days: RealmList<Int> = realmListOf()
    var months: RealmList<Int> = realmListOf()
    var event: Event? = null
}

class RealmCaseYearlyEvent: EmbeddedRealmObject{
    var date: Int = 1
    var month: Int = 1
    var event: Event? = null
}