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
            BasicIngredient(2, "carrot"),
            BasicIngredient(3, "우유"),
            BasicIngredient(4, "계란"),
            BasicIngredient(5, "빵"),
            BasicIngredient(6, "참외"),
            BasicIngredient(7, "양파"),
            BasicIngredient(8, "감자"),
            BasicIngredient(9, "고구마"),
            BasicIngredient(10, "사과"),
            BasicIngredient(11, "바나나"),
            BasicIngredient(12, "상추"),
            BasicIngredient(13, "고추"),
            BasicIngredient(14, "마늘"),
            BasicIngredient(15, "파"),
            BasicIngredient(16, "시금치"),
            BasicIngredient(17, "오이"),
            BasicIngredient(18, "가지"),
            BasicIngredient(19, "버섯"),
            BasicIngredient(20, "양배추"),
            BasicIngredient(21, "배추"),
            BasicIngredient(22, "콩나물"),
            BasicIngredient(23, "두부"),
            BasicIngredient(24, "참치캔"),
            BasicIngredient(25, "스팸"),
            BasicIngredient(26, "김치"),
            BasicIngredient(27, "된장"),
            BasicIngredient(28, "고추장"),
            BasicIngredient(29, "간장"),
            BasicIngredient(30, "참기름"),
            BasicIngredient(31, "식초"),
            BasicIngredient(32, "설탕"),
            BasicIngredient(33, "소금"),
            BasicIngredient(34, "후추"),
            BasicIngredient(35, "고춧가루"),
            BasicIngredient(36, "다진마늘"),
            BasicIngredient(37, "미역"),
            BasicIngredient(38, "멸치"),
            BasicIngredient(39, "쌀"),
            BasicIngredient(40, "라면"),
            BasicIngredient(41, "스파게티"),
            BasicIngredient(42, "케첩"),
            BasicIngredient(43, "마요네즈"),
            BasicIngredient(44, "버터"),
            BasicIngredient(45, "치즈"),
            BasicIngredient(46, "햄"),
            BasicIngredient(47, "베이컨"),
            BasicIngredient(48, "소세지"),
            BasicIngredient(49, "닭가슴살"),
            BasicIngredient(50, "연어")
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
