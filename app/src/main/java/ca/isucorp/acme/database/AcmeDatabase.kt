package ca.isucorp.acme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.isucorp.acme.database.dao.UserDao
import ca.isucorp.acme.database.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AcmeDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: AcmeDatabase? = null

        /**
         * Helper function to get the database.
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getDatabase(context: Context): AcmeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            AcmeDatabase::class.java,
                            "acme_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun closeDatabase() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
            }
        }
    }
}
