package com.example.the_schedulaing_application.data

import com.example.the_schedulaing_application.SharedViews.AddEditSharedEvent
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import com.example.the_schedulaing_application.domain.loginErrorHandling.LoginErrorHandler
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun mainViewModel():MainRealmViewModel{
        return MainRealmViewModel()
    }

    @Provides
    @Singleton
    fun addEditSharedEvent(): AddEditSharedEvent{
        return AddEditSharedEvent()
    }


    @Provides
    @Singleton
    fun navConductorViewModel(): NavConductorViewModel{
        return NavConductorViewModel()
    }

    @Provides
    @Singleton
    fun loginErrorHandler(): LoginErrorHandler{
        return LoginErrorHandler()
    }

}
