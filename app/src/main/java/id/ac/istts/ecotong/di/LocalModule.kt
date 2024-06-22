package id.ac.istts.ecotong.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.ac.istts.ecotong.data.local.datastore.SessionManager
import id.ac.istts.ecotong.data.local.room.EcotongDatabase
import id.ac.istts.ecotong.data.local.room.PostDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): SessionManager =
        SessionManager(context)

    @Singleton
    @Provides
    fun provideEcotongDatabase(@ApplicationContext context: Context): EcotongDatabase {
        return Room.databaseBuilder(
            context.applicationContext, EcotongDatabase::class.java, "ecotong.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePostDao(database: EcotongDatabase): PostDao {
        return database.postDao()
    }
}