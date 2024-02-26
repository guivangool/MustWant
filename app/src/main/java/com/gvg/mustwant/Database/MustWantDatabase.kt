package com.gvg.mustwant.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gvg.mustwant.Dao.CreditCardDao
import com.gvg.mustwant.Dao.IncomeDao
import com.gvg.mustwant.Dao.PaymentDao
import com.gvg.mustwant.Dao.UserDao
import com.gvg.mustwant.DataEntities.*
import com.gvg.mustwant.TypeConverters.DateConverter

@Database(
    entities = [
        User::class,
        CreditCard::class,
        Income::class,
        Payment::class],
        version = 48
)
@TypeConverters(DateConverter::class)

abstract class MustWantDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDao

    abstract fun incomeDao(): IncomeDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: MustWantDatabase? = null

        fun getInstance(context: Context): MustWantDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MustWantDatabase::class.java,
                        "mustwant_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}