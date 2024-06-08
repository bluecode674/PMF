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
            BasicIngredient(41, "케첩"),
            BasicIngredient(42, "파"),
            BasicIngredient(43, "후추"),
            BasicIngredient(44, "햄"),
            BasicIngredient(45, "버섯"),
            BasicIngredient(46, "닭가슴살"),
            BasicIngredient(47, "대파"),
            BasicIngredient(48, "고추장"),
            BasicIngredient(49, "깨"),
            BasicIngredient(50, "어묵"),
            BasicIngredient(51, "옥수수"),
            BasicIngredient(52, "계란말이용김"),
            BasicIngredient(53, "콩나물"),
            BasicIngredient(54, "고등어"),
            BasicIngredient(55, "호박"),
            BasicIngredient(56, "두유"),
            BasicIngredient(57, "딸기"),
            BasicIngredient(58, "바나나"),
            BasicIngredient(59, "냉동만두"),
            BasicIngredient(60, "감자칩"),
            BasicIngredient(61, "누룽지"),
            BasicIngredient(62, "김"),
            BasicIngredient(63, "잡곡"),
            BasicIngredient(64, "햇반"),
            BasicIngredient(65, "조미김"),
            BasicIngredient(66, "오징어채"),
            BasicIngredient(67, "다시다"),
            BasicIngredient(68, "꿀"),
            BasicIngredient(69, "옥수수콘"),
            BasicIngredient(70, "간마늘"),
            BasicIngredient(71, "동치미"),
            BasicIngredient(72, "생강"),
            BasicIngredient(73, "멸치액젓"),
            BasicIngredient(74, "고추냉이"),
            BasicIngredient(75, "된장찌개용된장"),
            BasicIngredient(76, "애호박"),
            BasicIngredient(77, "부추"),
            BasicIngredient(78, "된장국용된장"),
            BasicIngredient(79, "당근"),
            BasicIngredient(80, "청경채"),
            BasicIngredient(81, "시금치나물"),
            BasicIngredient(82, "열무김치"),
            BasicIngredient(83, "떡국떡"),
            BasicIngredient(84, "우엉"),
            BasicIngredient(85, "표고버섯"),
            BasicIngredient(86, "가지나물"),
            BasicIngredient(87, "숙주나물"),
            BasicIngredient(88, "팽이버섯"),
            BasicIngredient(89, "브로콜리"),
            BasicIngredient(90, "피망"),
            BasicIngredient(91, "청양고추"),
            BasicIngredient(92, "김치찌개용김치"),
            BasicIngredient(93, "청국장"),
            BasicIngredient(94, "간장게장"),
            BasicIngredient(95, "게맛살"),
            BasicIngredient(96, "다시마"),
            BasicIngredient(97, "양배추찜"),
            BasicIngredient(98, "콩"),
            BasicIngredient(99, "땅콩"),
            BasicIngredient(100, "파프리카")
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
