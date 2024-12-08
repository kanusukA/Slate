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
        name = slateEvent.eventName
        description = slateEvent.eventDescription
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

fun fromRealmEventToCasedSlateEvent(realmEvent: Event, caseType: CaseType): SlateEvent?{
    println("event - ${realmEvent.name}")
    return when(caseType){
        is CaseType.CaseDuration -> {
            if(realmEvent.caseType == 1){
                SlateEvent(
                    realmEvent.name,
                    realmEvent.description,
                    CaseType.CaseDuration(
                        fromEpoch = realmEvent.caseDuration?.fromEpoch ?: System.currentTimeMillis(),
                        toEpoch = realmEvent.caseDuration?.toEpoch ?: System.currentTimeMillis()
                    )
                ).apply { id = realmEvent.id }
            }else{
                null
            }
        }
        is CaseType.CaseRepeatable -> {
            when(caseType.caseRepeatableType){
                is CaseRepeatableType.Daily -> {
                    if(realmEvent.caseType == 2){
                        SlateEvent(
                            realmEvent.name,
                            realmEvent.description,
                            CaseType.CaseRepeatable(CaseRepeatableType.Daily(
                                realmEvent.caseDaily?.timeOfDay?.toLong() ?: 0L
                            ))
                        ).apply { id = realmEvent.id }
                    }else{
                        null
                    }
                }
                is CaseRepeatableType.Monthly -> {
                    if(realmEvent.caseType == 4){
                        SlateEvent(
                            realmEvent.name,
                            realmEvent.description,
                            CaseType.CaseRepeatable(CaseRepeatableType.Monthly(
                                realmEvent.caseMonthly?.days?.toList() ?: emptyList()
                            ))
                        ).apply { id = realmEvent.id }
                    }else{
                        null
                    }
                }
                is CaseRepeatableType.Weekly -> {
                    if(realmEvent.caseType == 3){
                        SlateEvent(
                            realmEvent.name,
                            realmEvent.description,
                            CaseType.CaseRepeatable(CaseRepeatableType.Weekly(
                                realmEvent.caseWeekly?.weeks?.map { SlateWeeks.entries[it] } ?: emptyList()
                            ))
                        ).apply { id = realmEvent.id }
                    }else{
                        null
                    }
                }
                is CaseRepeatableType.Yearly -> {
                    if(realmEvent.caseType == 5){
                        SlateEvent(
                            realmEvent.name,
                            realmEvent.description,
                            CaseType.CaseRepeatable(CaseRepeatableType.Yearly(
                                date = realmEvent.caseYearly?.days?.toList() ?: emptyList(),
                                selectMonths = realmEvent.caseYearly?.months?.toList() ?: emptyList()
                            ))
                        ).apply { id = realmEvent.id }
                    }else{
                        null
                    }
                }
                is CaseRepeatableType.YearlyEvent -> {
                    if(realmEvent.caseType == 6){
                        SlateEvent(
                            realmEvent.name,
                            realmEvent.description,
                            CaseType.CaseRepeatable(CaseRepeatableType.YearlyEvent(
                                date = realmEvent.caseYearlyEvent?.date ?: 1,
                                month = realmEvent.caseYearlyEvent?.month ?: 1
                            ))
                        ).apply { id = realmEvent.id }
                    }else{
                        null
                    }
                }
            }
        }
        is CaseType.CaseSingleton -> {
            if(realmEvent.caseType == 0){
                SlateEvent(
                    realmEvent.name,
                    realmEvent.description,
                    CaseType.CaseSingleton(realmEvent.caseSingleton?.epochTimeMilli ?: System.currentTimeMillis())
                ).apply { id = realmEvent.id }
            }else{
                null
            }
        }
    }
}

fun fromRealmEventToSlateEvent(realmEvents: List<Event>): List<SlateEvent>{
    return realmEvents.map { fromRealmEventToSlateEvent(it) }
}

fun fromRealmEventToSlateEvent(realmEvent: Event): SlateEvent{

    return when(realmEvent.caseType){
        0 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseSingleton(realmEvent.caseSingleton?.epochTimeMilli ?: System.currentTimeMillis())
            ).apply { id = realmEvent.id }
        }
        1 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseDuration(
                    fromEpoch = realmEvent.caseDuration?.fromEpoch ?: System.currentTimeMillis(),
                    toEpoch = realmEvent.caseDuration?.toEpoch ?: System.currentTimeMillis()
                )
            ).apply { id = realmEvent.id }
        }
        2 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Daily(
                    realmEvent.caseDaily?.timeOfDay?.toLong() ?: 0L
                ))
            ).apply { id = realmEvent.id }
        }
        3 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Weekly(
                    realmEvent.caseWeekly?.weeks?.map { SlateWeeks.entries[it] } ?: emptyList()
                ))
            ).apply { id = realmEvent.id }
        }
        4 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Monthly(
                    realmEvent.caseMonthly?.days?.toList() ?: emptyList()
                ))
            ).apply { id = realmEvent.id }
        }
        5 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.Yearly(
                    date = realmEvent.caseYearly?.days?.toList() ?: emptyList(),
                    selectMonths = realmEvent.caseYearly?.months?.toList() ?: emptyList()
                ))
            ).apply { id = realmEvent.id }
        }
        6 -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseRepeatable(CaseRepeatableType.YearlyEvent(
                    date = realmEvent.caseYearlyEvent?.date ?: 1,
                    month = realmEvent.caseYearlyEvent?.month ?: 1
                ))
            ).apply { id = realmEvent.id }
        }
        else -> {
            SlateEvent(
                realmEvent.name,
                realmEvent.description,
                CaseType.CaseSingleton(System.currentTimeMillis())
            ).apply { id = realmEvent.id }
        }
    }
}

