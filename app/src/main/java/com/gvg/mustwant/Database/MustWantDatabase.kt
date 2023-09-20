package com.gvg.mustwant.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gvg.mustwant.Dao.ConfigurationDao
import com.gvg.mustwant.Dao.GoalProgressDao
import com.gvg.mustwant.Dao.InspirationDao
import com.gvg.mustwant.Dao.ObstacleDao
import com.gvg.mustwant.DataEntities.*
import com.gvg.mustwant.TypeConverters.DateConverter

@Database(
    entities = [
        GoalProgressEntity::class,
        InspirationEntity::class,
        ObstacleEntity::class,
        GoalEntity::class],
        version = 35
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