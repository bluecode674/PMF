package com.example.pmf.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

data class BasicIngredient(val id: Int, val name: String)

class BasicIngredientsDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "basic_ingredients.db"
        private const val DATABASE_VERSION = 2
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
            BasicIngredient(1, "가지"),
            BasicIngredient(2, "간장"),
            BasicIngredient(3, "감자"),
            BasicIngredient(4, "고구마"),
            BasicIngredient(5, "고추"),
            BasicIngredient(6, "고춧가루"),
            BasicIngredient(7, "김치"),
            BasicIngredient(8, "계란"),
            BasicIngredient(9, "된장"),
            BasicIngredient(10, "두부"),
            BasicIngredient(11, "다진마늘"),
            BasicIngredient(12, "라면"),
            BasicIngredient(13, "마늘"),
            BasicIngredient(14, "마요네즈"),
            BasicIngredient(15, "멸치"),
            BasicIngredient(16, "미역"),
            BasicIngredient(17, "버터"),
            BasicIngredient(18, "배추"),
            BasicIngredient(19, "베이컨"),
            BasicIngredient(20, "빵"),
            BasicIngredient(21, "사과"),
            BasicIngredient(22, "상추"),
            BasicIngredient(23, "소금"),
            BasicIngredient(24, "소세지"),
            BasicIngredient(25, "스팸"),
            BasicIngredient(26, "스파게티"),
            BasicIngredient(27, "설탕"),
            BasicIngredient(28, "시금치"),
            BasicIngredient(29, "식초"),
            BasicIngredient(30, "쌀"),
            BasicIngredient(31, "참외"),
            BasicIngredient(32, "참기름"),
            BasicIngredient(33, "참치캔"),
            BasicIngredient(34, "치즈"),
            BasicIngredient(35, "토마토"),
            BasicIngredient(36, "우유"),
            BasicIngredient(37, "연어"),
            BasicIngredient(38, "양배추"),
            BasicIngredient(39, "양파"),
            BasicIngredient(40, "오이"),
            BasicIngredient(41, "감자"),
            BasicIngredient(42, "케첩"),
            BasicIngredient(43, "파"),
            BasicIngredient(44, "후추"),
            BasicIngredient(45, "햄"),
            BasicIngredient(46, "버섯"),
            BasicIngredient(47, "닭가슴살"),
            BasicIngredient(48, "대파"),
            BasicIngredient(49, "고추장"),
            BasicIngredient(50, "고춧가루")
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
