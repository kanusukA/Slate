package com.example.the_schedulaing_application.data.objects

import com.example.the_schedulaing_application.domain.Cases.CaseType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Event : RealmObject{
    @PrimaryKey var id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""

    // 0 -> Singleton
    // 1 -> Duration
    // 2 -> Daily
    // 3 -> Weekly
    // 4 -> Monthly
    // 5 -> Yearly
    // 6 -> YearlyEvent
    var caseType: Int = 0

    var caseSingleton: RealmCaseSingleton? = null
    var caseDuration: RealmCaseDuration? = null
    var caseDaily: RealmCaseDaily? = null
    var caseWeekly: RealmCaseWeekly? = null
    var caseMonthly: RealmCaseMonthly? = null
    var caseYearly: RealmCaseYearly? = null
    var caseYearlyEvent: RealmCaseYearlyEvent? = null

}