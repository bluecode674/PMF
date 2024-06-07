package com.example.pmf.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Ingredient(
    val name: String,
    val purchaseDate: String,
    val expiryDate: String,
    val storageLocation: String,
    val quantity: Int
) {
    fun getRemainingDays(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expiry = sdf.parse(expiryDate)
        val today = Date()
        val diff = expiry.time - today.time
        val days = diff / (1000 * 60 * 60 * 24)
        return if (days >= 0) {
            "D-$days"
        } else {
            "D+${-days}"
        }
    }
}

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ingredients.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "ingre"
        const val COLUMN_NAME = "name"
        const val COLUMN_PURCHASE_DATE = "purchase_date"
        const val COLUMN_EXPIRY_DATE = "expiry_date"
        const val COLUMN_STORAGE_LOCATION = "storage_location"
        const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_NAME CHAR(50),
                $COLUMN_PURCHASE_DATE DATE,
                $COLUMN_EXPIRY_DATE DATE,
                $COLUMN_STORAGE_LOCATION CHAR(50),
                $COLUMN_QUANTITY INTEGER,
                PRIMARY KEY ($COLUMN_NAME, $COLUMN_PURCHASE_DATE)
            )
        """
        db.execSQL(createTable)
        // insertInitialData(db)
    }
    /*
    private fun insertInitialData(db: SQLiteDatabase) {
        // 초기 데이터 삽입 예제
        val initialData = listOf(
            Ingredient("carrot", "2024-01-01", "2024-01-10", "냉장고", 10),
            Ingredient("당근", "2024-01-02", "2024-06-03", "냉동고", 5),
            Ingredient("우유", "2024-01-03", "2024-01-05", "냉장고", 2)
        )

        initialData.forEach {
            val values = ContentValues().apply {
                put(COLUMN_NAME, it.name)
                put(COLUMN_PURCHASE_DATE, it.purchaseDate)
                put(COLUMN_EXPIRY_DATE, it.expiryDate)
                put(COLUMN_STORAGE_LOCATION, it.storageLocation)
                put(COLUMN_QUANTITY, it.quantity)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }
    */

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(name: String, purchaseDate: String, expiryDate: String, storageLocation: String, quantity: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PURCHASE_DATE, purchaseDate)
            put(COLUMN_EXPIRY_DATE, expiryDate)
            put(COLUMN_STORAGE_LOCATION, storageLocation)
            put(COLUMN_QUANTITY, quantity)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateItem(name: String, purchaseDate: String, expiryDate: String, storageLocation: String, quantity: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EXPIRY_DATE, expiryDate)
            put(COLUMN_STORAGE_LOCATION, storageLocation)
            put(COLUMN_QUANTITY, quantity)
        }
        db.update(TABLE_NAME, values, "$COLUMN_NAME = ? AND $COLUMN_PURCHASE_DATE = ?", arrayOf(name, purchaseDate))
        db.close()
    }

    fun deleteItem(name: String, purchaseDate: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ? AND $COLUMN_PURCHASE_DATE = ?", arrayOf(name, purchaseDate))
        db.close()
    }

    fun getItem(name: String, purchaseDate: String): Ingredient? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME, COLUMN_PURCHASE_DATE, COLUMN_EXPIRY_DATE, COLUMN_STORAGE_LOCATION, COLUMN_QUANTITY), "$COLUMN_NAME = ? AND $COLUMN_PURCHASE_DATE = ?", arrayOf(name, purchaseDate), null, null, null)
        cursor?.moveToFirst()
        val item = if (cursor.count > 0) {
            Ingredient(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PURCHASE_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORAGE_LOCATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return item
    }

    fun getAllItems(): List<Ingredient> {
        val itemList = mutableListOf<Ingredient>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val purchaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PURCHASE_DATE))
                val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE))
                val storageLocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORAGE_LOCATION))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                itemList.add(Ingredient(name, purchaseDate, expiryDate, storageLocation, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }

    fun searchItems(query: String): List<Ingredient> {
        val itemList = mutableListOf<Ingredient>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME LIKE ?", arrayOf("%$query%"))
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val purchaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PURCHASE_DATE))
                val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE))
                val storageLocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORAGE_LOCATION))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                itemList.add(Ingredient(name, purchaseDate, expiryDate, storageLocation, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }



    fun searchItemsByStorageLocation(location: String): List<Ingredient> {
        val itemList = mutableListOf<Ingredient>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STORAGE_LOCATION = ?", arrayOf(location))
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val purchaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PURCHASE_DATE))
                val expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE))
                val storageLocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORAGE_LOCATION))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                itemList.add(Ingredient(name, purchaseDate, expiryDate, storageLocation, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }
}
