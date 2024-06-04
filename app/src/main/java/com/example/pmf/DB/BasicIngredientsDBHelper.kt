package com.example.pmf.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

data class BasicIngredient(val id: Int, val name: String)

class BasicIngredientsDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "basic_ingredients.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "basic_ingredients"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT
            )
        """
        db.execSQL(createTable)
        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val initialData = listOf(
            BasicIngredient(1, "토마토"),
            BasicIngredient(2, "당근"),
            BasicIngredient(3, "우유"),
            BasicIngredient(4, "계란"),
            BasicIngredient(5, "빵"),
            BasicIngredient(6, "참외")
        )

        initialData.forEach {
            val values = ContentValues().apply {
                put(COLUMN_ID, it.id)
                put(COLUMN_NAME, it.name)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllItems(): List<BasicIngredient> {
        val itemList = mutableListOf<BasicIngredient>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                itemList.add(BasicIngredient(id, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }
}
