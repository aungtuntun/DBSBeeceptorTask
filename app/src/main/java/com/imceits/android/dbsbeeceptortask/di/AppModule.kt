package com.imceits.android.dbsbeeceptortask.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imceits.android.dbsbeeceptortask.data.APIService
import com.imceits.android.dbsbeeceptortask.data.ArcDao
import com.imceits.android.dbsbeeceptortask.data.ArcDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton @Provides
    fun provideAPIService(gson: Gson, okHttpClient: OkHttpClient): APIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://task.free.beeceptor.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(APIService::class.java)
    }

    @Singleton @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Singleton @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton @Provides
    fun provideArcDao(db: ArcDatabase): ArcDao {
        return db.arcDao()
    }

    @Singleton @Provides
    fun provideArcDatabase(app: Application): ArcDatabase {
        return Room.databaseBuilder(app.applicationContext, ArcDatabase::class.java, "Article.db")
            .allowMainThreadQueries()
            .build()
    }
}