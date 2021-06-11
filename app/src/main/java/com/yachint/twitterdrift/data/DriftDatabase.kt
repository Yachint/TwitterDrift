package com.yachint.twitterdrift.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yachint.twitterdrift.data.dao.TrendsDao
import com.yachint.twitterdrift.data.model.trends.Trend

@Database(entities = [Trend::class], version = 1)
abstract class DriftDatabase: RoomDatabase() {

    abstract fun trendsDao(): TrendsDao

    companion object {

        @Volatile private var instance: DriftDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DriftDatabase::class.java, "drift_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }

        private val roomCallback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}