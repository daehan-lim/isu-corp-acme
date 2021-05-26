package ca.isucorp.acme.database.model

import androidx.room.*


@Entity(tableName = "user_table")
data class User (
        @ColumnInfo(name = "user_name")
        var userName: String = "",

        @ColumnInfo(name = "password")
        var password: String = "",

        @PrimaryKey(autoGenerate = true)
        var id: Long? = null,
)