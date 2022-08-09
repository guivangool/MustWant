package com.example.musthave.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musthave.Dao.ConfigurationDao
import com.example.musthave.Dao.GoalProgressDao
import com.example.musthave.Dao.InspirationDao
import com.example.musthave.Dao.ObstacleDao
import com.example.musthave.DataEntities.ConfigurationEntity
import com.example.musthave.DataEntities.GoalProgressEntity
import com.example.musthave.DataEntities.InspirationEntity
import com.example.musthave.DataEntities.ObstacleEntity
import com.example.musthave.TypeConverters.DateConverter

@Database(
    entities = [
        ConfigurationEntity::class,
        GoalProgressEntity::class,
        InspirationEntity::class,
        ObstacleEntity::class],
        version = 18
)
@TypeConverters(DateConverter::class)

abstract class MustWantDatabase : RoomDatabase() {
    abstract fun configurationDao(): ConfigurationDao
    abstract fun goalProgressDao(): GoalProgressDao
    abstract fun inspitationDao(): InspirationDao
    abstract fun obstacleDao(): ObstacleDao

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