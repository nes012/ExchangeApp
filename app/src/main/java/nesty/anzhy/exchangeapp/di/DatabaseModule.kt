package nesty.anzhy.exchangeapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nesty.anzhy.exchangeapp.data.database.ExchangeDatabase
import nesty.anzhy.exchangeapp.utils.Constants.Companion.EXCHANGE_DB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
    ExchangeDatabase::class.java,
        EXCHANGE_DB).build()

    @Singleton
    @Provides
    fun provideDao(database: ExchangeDatabase) = database.exchangeDao()
}