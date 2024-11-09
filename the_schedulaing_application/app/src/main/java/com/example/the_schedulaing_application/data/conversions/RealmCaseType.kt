package com.example.the_schedulaing_application.data.conversions

import com.example.the_schedulaing_application.data.objects.Event
import com.example.the_schedulaing_application.data.objects.RealmCaseDaily
import com.example.the_schedulaing_application.data.objects.RealmCaseDuration
import com.example.the_schedulaing_application.data.objects.RealmCaseMonthly
import com.example.the_schedulaing_application.data.objects.RealmCaseSingleton
import com.example.the_schedulaing_application.data.objects.RealmCaseWeekly
import com.example.the_schedulaing_application.data.objects.RealmCaseYearly
import com.example.the_schedulaing_application.data.objects.RealmCaseYearlyEvent
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import io.realm.kotlin.ext.toRealmList

fun fromSlateEventToRealmEvent(slateEvent: SlateEvent, slateCaseType: CaseType): Event{

    val event = Event().apply {
        name = slateEvent.eventName.value
        description = slateEvent.eventDescription.value
    }

    return when(slateCaseType){
        is CaseType.CaseSingleton -> {
            event.apply {
                caseType = 0
                caseSingleton = RealmCaseSingleton().apply { epochTimeMilli = slateCaseType.epochTimeMilli }
            }
        }
        is CaseType.CaseDuration -> {
            event.apply {
                caseType = 1
                caseDuration = RealmCaseDuration().apply {
                    fromEpoch = slateCaseType.fromEpoch
                    toEpoch = slateCaseType.toEpoch
                }
            }
        }
        is CaseType.CaseRepeatable -> {
            when(slateCaseType.caseRepeatableType){
                is CaseRepeatableType.Daily -> {
                    event.apply {
                        caseType = 2
                        caseDaily = RealmCaseDaily().apply {
                            timeOfDay = slateCaseType.caseRepeatableType.timeOfDay.toInt()
                        }
                    }
                }
                is CaseRepeatableType.Weekly -> {
                    event.apply {
                        caseType = 3
                        caseWeekly = RealmCaseWeekly().apply {
                            weeks = slateCaseType.caseRepeatableType.selectWeeks.map { it.ordinal }.toRealmList()
                        }
                    }
                }
                is CaseRepeatableType.Monthly -> {
                    event.apply {
                        caseType = 4
                        caseMonthly = RealmCaseMonthly().apply {
                            days = slateCaseType.caseRepeatableType.selectDates.toRealmList()
                        }
                    }
                }
                is CaseRepeatableType.Yearly -> {
                    event.apply {
                        caseType = 5
                        caseYearly = RealmCaseYearly().apply {
                            days = slateCaseType.caseRepeatableType.date.toRealmList()
                            months = slateCaseType.caseRepeatableType.selectMonths.toRealmList()
                        }
                    }
                }
                is CaseRepeatableType.YearlyEvent -> {
                    event.apply {
                        caseType = 6
                        caseYearlyEvent = RealmCaseYearlyEvent().apply {
                            date = slateCaseType.caseRepeatableType.date
                            month = slateCaseType.caseRepeatableType.month
                        }
                    }
                }
            }
        }
    }

}

fun fromRealmEventToSlateEvent(realmEvent: Event): SlateEvent{

    return when(realmEvent.caseType){
        0 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseSingleton(realmEvent.caseSingleton?.epochTimeMilli ?: System.currentTimeMillis())
            )
        }
        1 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseDuration(
                    fromEpoch = realmEvent.caseDuration?.fromEpoch ?: System.currentTimeMillis(),
                    toEpoch = realmEvent.caseDuration?.toEpoch ?: System.currentTimeMillis()
                )
            )
        }
        2 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Daily(
                    realmEvent.caseDaily?.timeOfDay?.toLong() ?: 0L
                ))
            )
        }
        3 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Weekly(
                    realmEvent.caseWeekly?.weeks?.map { SlateWeeks.entries[it] } ?: emptyList()
                ))
            )
        }
        4 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Monthly(
                    realmEvent.caseMonthly?.days?.toList() ?: emptyList()
                ))
            )
        }
        5 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Yearly(
                    date = realmEvent.caseYearly?.days?.toList() ?: emptyList(),
                    selectMonths = realmEvent.caseYearly?.months?.toList() ?: emptyList()
                ))
            )
        }
        6 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.YearlyEvent(
                    date = realmEvent.caseYearlyEvent?.date ?: 1,
                    month = realmEvent.caseYearlyEvent?.month ?: 1
                ))
            )
        }
        else -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseSingleton(System.currentTimeMillis())
            )
        }
    }
}

