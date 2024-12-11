package com.example.the_schedulaing_application

import android.app.Application
import android.content.ComponentName
import android.os.Bundle
import com.example.the_schedulaing_application.data.objects.Event
import com.example.the_schedulaing_application.data.objects.RealmCaseDaily
import com.example.the_schedulaing_application.data.objects.RealmCaseDuration
import com.example.the_schedulaing_application.data.objects.RealmCaseMonthly
import com.example.the_schedulaing_application.data.objects.RealmCaseSingleton
import com.example.the_schedulaing_application.data.objects.RealmCaseWeekly
import com.example.the_schedulaing_application.data.objects.RealmCaseYearly
import com.example.the_schedulaing_application.data.objects.RealmCaseYearlyEvent
import com.example.the_schedulaing_application.data.objects.RealmUser
import com.example.the_schedulaing_application.data.objects.User
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp
class SlateApplication : Application() {

    companion object{
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    RealmCaseSingleton::class,
                    RealmCaseDuration::class,
                    RealmCaseDaily::class,
                    RealmCaseWeekly::class,
                    RealmCaseMonthly::class,
                    RealmCaseYearly::class,
                    RealmCaseYearlyEvent::class,
                    Event::class,
                    RealmUser::class
                )
            )
        )
    }

}




