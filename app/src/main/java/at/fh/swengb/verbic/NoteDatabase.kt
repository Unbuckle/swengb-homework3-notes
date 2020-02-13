package at.fh.swengb.verbic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val NoteDao: NoteDao

    companion object {
        private var INSTANCE: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(
                context,
                NoteDatabase::class.java, "note-db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

        }
    }
}