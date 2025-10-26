package com.example.birthday

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.room.Room
import com.example.birthday.data.AppDatabase
import com.example.birthday.data.Person
import com.example.birthday.data.PersonDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// Mulighet for å delete data med andre applikasjoner

class PersonContentProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "com.example.birthday.PersonContentProvider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/persons")
    }

    private lateinit var personDao: PersonDao

    override fun onCreate(): Boolean {
        val db = Room.databaseBuilder(
            context!!.applicationContext,
            AppDatabase::class.java,
            "person_db"
        ).build()
        personDao = db.personDao()
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values ?: return null

        val person = Person(
            name = values.getAsString("name") ?: "",
            dob = values.getAsString("dob") ?: "",
            phoneNumber = values.getAsString("phoneNumber") ?: ""
        )

        runBlocking { personDao.insert(person) }

        return Uri.withAppendedPath(CONTENT_URI, person.phoneNumber)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("name", "dob", "phoneNumber"))
        runBlocking {
            val persons: List<Person> = personDao.getAllPersons().first()
            for (p in persons) {
                cursor.addRow(arrayOf(p.name, p.dob, p.phoneNumber))
            }
        }
        return cursor
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        values ?: return 0
        runBlocking {
            val name = values.getAsString("name") ?: return@runBlocking
            val dob = values.getAsString("dob") ?: return@runBlocking
            val phoneNumber = values.getAsString("phoneNumber") ?: return@runBlocking
            personDao.update(name, dob, phoneNumber)
        }
        return 1
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val phone = selectionArgs?.firstOrNull() ?: return 0
        runBlocking { personDao.delete(phone) }
        return 1
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.$AUTHORITY.persons"
    }
}