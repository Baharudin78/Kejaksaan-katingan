package com.baharudin.pengaduanapp.di

import com.baharudin.pengaduanapp.data.remote.ReportApi
import com.baharudin.pengaduanapp.data.respository.ReportRepositoryImpl
import com.baharudin.pengaduanapp.domain.repository.ReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
class ReportModule {
    @Provides
    @Singleton
    fun provideLetterApi(retrofit : Retrofit) : ReportApi{
        return retrofit.create(ReportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLetterRepository(letterApi : ReportApi) : ReportRepository{
        return ReportRepositoryImpl(letterApi)
    }
}