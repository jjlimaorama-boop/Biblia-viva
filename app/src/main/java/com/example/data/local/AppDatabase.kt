package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BiblicalCharacter
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.BibleVersion
import com.example.data.model.CrossReference
import com.example.data.model.Devotional
import com.example.data.model.Favorite
import com.example.data.model.Highlight
import com.example.data.model.Note
import com.example.data.model.PrayerRequest
import com.example.data.model.ReadingPlanProgress
import com.example.data.model.Sermon
import com.example.data.model.StudyTopic
import com.example.data.model.UserSettings

@Database(
    entities = [
        BibleVersion::class,
        BibleBook::class,
        BibleVerse::class,
        CrossReference::class,
        Note::class,
        Highlight::class,
        Favorite::class,
        PrayerRequest::class,
        ReadingPlanProgress::class,
        Sermon::class,
        StudyTopic::class,
        BiblicalCharacter::class,
        Devotional::class,
        UserSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bibleDao(): BibleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biblia_viva.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
