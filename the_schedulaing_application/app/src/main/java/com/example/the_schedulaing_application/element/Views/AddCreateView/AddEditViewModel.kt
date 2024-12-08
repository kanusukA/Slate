package com.example.the_schedulaing_application.element.Views.AddCreateView

import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.SharedViews.AddEditSharedEvent
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.Cases.CaseRepeatableType
import com.example.the_schedulaing_application.domain.Cases.CaseType
import com.example.the_schedulaing_application.domain.Cases.SlateEvent
import com.example.the_schedulaing_application.domain.Cases.caseDailyTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseDurationTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseMonthlyTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseSingletonTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseWeeklyTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseYearlyEventTimeDiff
import com.example.the_schedulaing_application.domain.Cases.caseYearlyTimeDiff
import com.example.the_schedulaing_application.domain.Cases.getDateStringFromKTime
import com.example.the_schedulaing_application.domain.Klinder
import com.example.the_schedulaing_application.element.components.caseTypeComponents.CaseDurationComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    val addEditSharedEvent: AddEditSharedEvent
) : ViewModel() {

    private var _timeLeft: MutableStateFlow<String> =
        MutableStateFlow("Select Value")
    val timeLeft: StateFlow<String> = _timeLeft

    fun setEventName(name: String) {
        addEditSharedEvent.setName(name)
    }

    fun setEventDescription(description: String) {
        addEditSharedEvent.setDescription(description)
    }

    fun setEventCaseType(caseType: CaseType) {
        eventTimeDiff(caseType)
        addEditSharedEvent.setCaseType(caseType)
    }

    fun setEventCaseType(int: Int) {
        _timeLeft.update { "Select Value" }
        when (int) {
            0 -> {
                addEditSharedEvent.setCaseType(CaseType.CaseSingleton(System.currentTimeMillis()))
            }

            1 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseDuration(
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                    )
                )

            }

            2 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                )

            }

            else -> {
                addEditSharedEvent.setCaseType(CaseType.CaseSingleton(System.currentTimeMillis()))
            }
        }

    }

    fun setEventCaseRepeatableType(int: Int) {
        _timeLeft.update { "Select Value" }

        when (int) {
            0 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                )
            }

            1 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Weekly(emptyList())
                    )
                )
            }

            2 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Monthly(emptyList())
                    )
                )
            }

            3 -> {
               addEditSharedEvent.setCaseType(
                   CaseType.CaseRepeatable(
                       CaseRepeatableType.Yearly(emptyList(), emptyList())
                   )
               )
            }

            4 -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.YearlyEvent(1, 1)
                    )
                )

            }

            else -> {
                addEditSharedEvent.setCaseType(
                    CaseType.CaseRepeatable(
                        CaseRepeatableType.Daily(Klinder.getInstance().getTimeOfDayLong())
                    )
                )

            }
        }

    }

    fun resetValues() {
        addEditSharedEvent.setName("")
        addEditSharedEvent.setDescription("")
        addEditSharedEvent.setCaseType(CaseType.CaseSingleton(System.currentTimeMillis()))
    }

    private fun eventTimeDiff(caseType: CaseType) {
        when (caseType) {
            is CaseType.CaseDuration -> {
                if (caseType.fromEpoch > System.currentTimeMillis()) {
                    _timeLeft.update {
                        getDateStringFromKTime(
                            caseDurationTimeDiff(caseType.fromEpoch, caseType.toEpoch)
                        ) + " to Begin"
                    }
                } else {
                    _timeLeft.update {
                        getDateStringFromKTime(
                            caseDurationTimeDiff(caseType.fromEpoch, caseType.toEpoch)
                        ) + " to End"
                    }
                }

            }

            is CaseType.CaseRepeatable -> {
                when (caseType.caseRepeatableType) {
                    is CaseRepeatableType.Daily -> {
                        _timeLeft.update {
                            getDateStringFromKTime(
                                caseDailyTimeDiff(caseType.caseRepeatableType.timeOfDay),
                                showHourMin = true
                            ) + " left"
                        }
                    }

                    is CaseRepeatableType.Monthly -> {
                        if (caseType.caseRepeatableType.selectDates.isNotEmpty()) {
                            _timeLeft.update {
                                getDateStringFromKTime(
                                    caseMonthlyTimeDiff(caseType.caseRepeatableType.selectDates),
                                    showHourMin = true
                                ) + " left"
                            }
                        } else {
                            _timeLeft.update { "Select Value" }
                        }
                    }

                    is CaseRepeatableType.Weekly -> {
                        if (caseType.caseRepeatableType.selectWeeks.isNotEmpty()) {
                            _timeLeft.update {
                                getDateStringFromKTime(
                                    caseWeeklyTimeDiff(caseType.caseRepeatableType.selectWeeks),
                                    showHourMin = false
                                ) + " left"
                            }
                        } else {
                            _timeLeft.update { "Select Value" }
                        }
                    }

                    is CaseRepeatableType.Yearly -> {
                        if (caseType.caseRepeatableType.date.isNotEmpty() &&
                            caseType.caseRepeatableType.selectMonths.isNotEmpty()
                        ) {
                            _timeLeft.update {
                                getDateStringFromKTime(
                                    caseYearlyTimeDiff(
                                        caseType.caseRepeatableType.date,
                                        caseType.caseRepeatableType.selectMonths
                                    )
                                )
                            }
                        } else {
                            _timeLeft.update { "Select Value" }
                        }
                    }

                    is CaseRepeatableType.YearlyEvent -> {
                        _timeLeft.update {
                            getDateStringFromKTime(
                                caseYearlyEventTimeDiff(
                                    caseType.caseRepeatableType.date,
                                    caseType.caseRepeatableType.month
                                )
                            )
                        }
                    }
                }
            }

            is CaseType.CaseSingleton -> {
                _timeLeft.update {
                    getDateStringFromKTime(
                        caseSingletonTimeDiff(caseType.epochTimeMilli)
                    ) + " left"
                }
            }
        }
    }

}