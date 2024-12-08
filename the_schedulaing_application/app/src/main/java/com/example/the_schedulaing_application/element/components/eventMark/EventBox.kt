package com.example.the_schedulaing_application.element.components.eventMark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.SlateWeeks
import com.example.the_schedulaing_application.domain.Cases.getDateStringFromKTime
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.domain.getKClock
import com.example.the_schedulaing_application.element.components.caseTypeComponents.ClockViewModel
import com.example.the_schedulaing_application.element.components.caseTypeComponents.RunningClockType

@Composable
fun EventBox(
    event: SlateEvent,
    onDeleteEvent: () -> Unit,
    onEditEvent: () -> Unit
){
    val case = event.caseType
    val completed by event.completed.collectAsStateWithLifecycle()
    when(case){
        is CaseType.CaseDuration -> {
            EventBoxDuration(
                title = event.eventName,
                description = event.eventDescription,
                eventIconId = event.getEventIcon(),
                from = Klinder.getInstance().getKTime(case.fromEpoch),
                to = Klinder.getInstance().getKTime(case.toEpoch),
                timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                completed = completed,
                onDelete = { onDeleteEvent()},
                onEdit = {onEditEvent()}
            )
        }
        is CaseType.CaseRepeatable -> {
            when(case.caseRepeatableType){
                is CaseRepeatableType.Daily -> {
                    EventBoxDaily(
                        title = event.eventName,
                        description = event.eventDescription,
                        eventIconId = event.getEventIcon(),
                        initClock = Klinder.getInstance()
                            .getKClock(case.caseRepeatableType.timeOfDay),
                        timeLeft = getKClock(event.getTimeLeft()),
                        timeLeftStr = getDateStringFromKTime(event.getTimeLeft()),
                        onDelete = { onDeleteEvent()},
                        onEdit = {onEditEvent()}
                    )
                }
                is CaseRepeatableType.Monthly -> {
                    EventBoxMonthly(
                        title = event.eventName,
                        eventIconId = event.getEventIcon(),
                        dates = case.caseRepeatableType.selectDates,
                        nextDate = Klinder.getInstance()
                            .getTimeAdd(add = event.getTimeLeft()),
                        timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                        onDelete = { onDeleteEvent()},
                        onEdit = {onEditEvent()}
                    )
                }
                is CaseRepeatableType.Weekly -> {
                    EventBoxWeekly(
                        title = event.eventName,
                        description = event.eventDescription,
                        eventIconId = event.getEventIcon(),
                        weeks = case.caseRepeatableType.selectWeeks,
                        nextWeek = SlateWeeks.entries[event.getTimeLeft().day],
                        timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                        onDelete = { onDeleteEvent()},
                        onEdit = {onEditEvent()}
                    )
                }
                is CaseRepeatableType.Yearly -> {
                    EventBoxYearly(
                        title = event.eventName,
                        description = event.eventDescription,
                        eventIconId = event.getEventIcon(),
                        dates = case.caseRepeatableType.date,
                        months = case.caseRepeatableType.selectMonths,
                        nextTime = Klinder.getInstance()
                            .getTimeAdd(add = event.getTimeLeft()),
                        timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                        onDelete = { onDeleteEvent()},
                        onEdit = {onEditEvent()}
                    )
                }
                is CaseRepeatableType.YearlyEvent -> {
                    EventBoxYearlyEvent(
                        title = event.eventName,
                        description = event.eventDescription,
                        eventIconId = event.getEventIcon(),
                        nextTime = Klinder.getInstance()
                            .getTimeAdd(add = event.getTimeLeft()),
                        timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                        onDelete = { onDeleteEvent()},
                        onEdit = {onEditEvent()}
                    )
                }
            }
        }
        is CaseType.CaseSingleton -> {
            EventBoxSingleton(
                title = event.eventName,
                description = event.eventDescription,
                eventIconId = event.getEventIcon(),
                initTime = Klinder.getInstance()
                    .getKTime(case.epochTimeMilli),
                timeLeft = getDateStringFromKTime(event.getTimeLeft()),
                completed = completed,
                onDelete = { onDeleteEvent()},
                onEdit = {onEditEvent()}
            )
        }
    }
}