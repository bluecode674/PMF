package com.example.pmf.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import java.io.InputStream

data class Recipe(val id: Int, val name: String, val ingredients: List<String>, val instructions: String)

class RecipeDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "recipe.db"
        private const val DATABASE_VERSION = 3

        private const val TABLE_RECIPES = "Recipes"
        private const val TABLE_INGREDIENTS = "Ingredients"
        private const val TABLE_RECIPE_INGREDIENTS = "RecipeIngredients"

        private const val COLUMN_RECIPE_ID = "id"
        private const val COLUMN_RECIPE_NAME = "name"
        private const val COLUMN_RECIPE_INSTRUCTIONS = "instructions"

        private const val COLUMN_INGREDIENT_ID = "id"
        private const val COLUMN_INGREDIENT_NAME = "name"

        private const val COLUMN_RECIPE_INGREDIENT_RECIPE_ID = "recipe_id"
        private const val COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID = "ingredient_id"
    }

    private val appContext = context.applicationContext

    override fun onCreate(db: SQLiteDatabase) {
        val createRecipesTable = """
            CREATE TABLE $TABLE_RECIPES (
                $COLUMN_RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_RECIPE_NAME TEXT,
                $COLUMN_RECIPE_INSTRUCTIONS TEXT
            )
        """
        db.execSQL(createRecipesTable)

        val createIngredientsTable = """
            CREATE TABLE $TABLE_INGREDIENTS (
                $COLUMN_INGREDIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INGREDIENT_NAME TEXT
            )
        """
        db.execSQL(createIngredientsTable)

        val createRecipeIngredientsTable = """
            CREATE TABLE $TABLE_RECIPE_INGREDIENTS (
                $COLUMN_RECIPE_INGREDIENT_RECIPE_ID INTEGER,
                $COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID INTEGER,
                FOREIGN KEY ($COLUMN_RECIPE_INGREDIENT_RECIPE_ID) REFERENCES $TABLE_RECIPES($COLUMN_RECIPE_ID),
                FOREIGN KEY ($COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID) REFERENCES $TABLE_INGREDIENTS($COLUMN_INGREDIENT_ID)
            )
        """
        db.execSQL(createRecipeIngredientsTable)

        insertInitialData(db, appContext)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPE_INGREDIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INGREDIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPES")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase, context: Context) {
        val jsonString = loadJSONFromAsset(context, "recipes.json")
        if (jsonString != null) {
            val recipesArray = JSONArray(jsonString)  // JSON 객체가 아닌 JSON 배열로 파싱

            for (i in 0 until recipesArray.length()) {
                val recipe = recipesArray.getJSONObject(i)
                val recipeName = recipe.getString("name")
                val recipeInstructions = recipe.getString("instructions")
                val recipeIngredients = recipe.getJSONArray("ingredients")

                val recipeValues = ContentValues().apply {
                    put(COLUMN_RECIPE_NAME, recipeName)
                    put(COLUMN_RECIPE_INSTRUCTIONS, recipeInstructions)
                }
                val recipeId = db.insert(TABLE_RECIPES, null, recipeValues)

                for (j in 0 until recipeIngredients.length()) {
                    val ingredientName = recipeIngredients.getString(j)
                    var ingredientId: Long

                    val cursor = db.query(
                        TABLE_INGREDIENTS,
                        arrayOf(COLUMN_INGREDIENT_ID),
                        "$COLUMN_INGREDIENT_NAME = ?",
                        arrayOf(ingredientName),
                        null,
                        null,
                        null
                    )

                    if (cursor.moveToFirst()) {
                        ingredientId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_ID))
                    } else {
                        val ingredientValues = ContentValues().apply {
                            put(COLUMN_INGREDIENT_NAME, ingredientName)
                        }
                        ingredientId = db.insert(TABLE_INGREDIENTS, null, ingredientValues)
                    }
                    cursor.close()

                    val recipeIngredientValues = ContentValues().apply {
                        put(COLUMN_RECIPE_INGREDIENT_RECIPE_ID, recipeId)
                        put(COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID, ingredientId)
                    }
                    db.insert(TABLE_RECIPE_INGREDIENTS, null, recipeIngredientValues)
                }
            }
        }
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        val db = this.readableDatabase
        var recipe: Recipe? = null
        val recipeCursor = db.query(
            TABLE_RECIPES,
            arrayOf(COLUMN_RECIPE_NAME, COLUMN_RECIPE_INSTRUCTIONS),
            "$COLUMN_RECIPE_ID = ?",
            arrayOf(recipeId.toString()),
            null,
            null,
            null
        )

        if (recipeCursor.moveToFirst()) {
            val recipeName = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME))
            val recipeInstructions = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS))

            val ingredientsCursor = db.query(
                TABLE_RECIPE_INGREDIENTS + " INNER JOIN " + TABLE_INGREDIENTS + " ON " +
                        TABLE_RECIPE_INGREDIENTS + "." + COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID + " = " + TABLE_INGREDIENTS + "." + COLUMN_INGREDIENT_ID,
                arrayOf(COLUMN_INGREDIENT_NAME),
                "$COLUMN_RECIPE_INGREDIENT_RECIPE_ID = ?",
                arrayOf(recipeId.toString()),
                null,
                null,
                null
            )

            val ingredients = mutableListOf<String>()
            if (ingredientsCursor.moveToFirst()) {
                do {
                    ingredients.add(ingredientsCursor.getString(ingredientsCursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)))
                } while (ingredientsCursor.moveToNext())
            }
            ingredientsCursor.close()
            recipe = Recipe(recipeId, recipeName, ingredients, recipeInstructions)
        }
        recipeCursor.close()
        return recipe
    }

    fun getAllRecipes(): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME))
                val instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS))
                val ingredients = getIngredientsForRecipe(id)
                recipes.add(Recipe(id, name, ingredients, instructions))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recipes
    }

    private fun getIngredientsForRecipe(recipeId: Int): List<String> {
        val ingredients = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_INGREDIENT_NAME FROM $TABLE_INGREDIENTS WHERE $COLUMN_INGREDIENT_ID IN (SELECT $COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID FROM $TABLE_RECIPE_INGREDIENTS WHERE $COLUMN_RECIPE_INGREDIENT_RECIPE_ID = ?)", arrayOf(recipeId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME))
                ingredients.add(ingredientName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ingredients
    }

    fun searchRecipes(query: String): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES WHERE $COLUMN_RECIPE_NAME LIKE ?", arrayOf("%$query%"))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME))
                val instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS))

                val ingredientsCursor = db.query(
                    TABLE_RECIPE_INGREDIENTS + " INNER JOIN " + TABLE_INGREDIENTS + " ON " +
                            TABLE_RECIPE_INGREDIENTS + "." + COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID + " = " + TABLE_INGREDIENTS + "." + COLUMN_INGREDIENT_ID,
                    arrayOf(COLUMN_INGREDIENT_NAME),
                    "$COLUMN_RECIPE_INGREDIENT_RECIPE_ID = ?",
                    arrayOf(id.toString()),
                    null,
                    null,
                    null
                )

                val ingredients = mutableListOf<String>()
                if (ingredientsCursor.moveToFirst()) {
                    do {
                        ingredients.add(ingredientsCursor.getString(ingredientsCursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)))
                    } while (ingredientsCursor.moveToNext())
                }
                ingredientsCursor.close()

                recipes.add(Recipe(id, name, ingredients, instructions))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recipes
    }

    fun getRecipesByIngredients(ingredients: List<String>): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = this.readableDatabase

        val ingredientsPlaceholders = ingredients.joinToString(",") { "?" }
        val query = """
            SELECT DISTINCT r.* FROM $TABLE_RECIPES r
            INNER JOIN $TABLE_RECIPE_INGREDIENTS ri ON r.$COLUMN_RECIPE_ID = ri.$COLUMN_RECIPE_INGREDIENT_RECIPE_ID
            INNER JOIN $TABLE_INGREDIENTS i ON ri.$COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID = i.$COLUMN_INGREDIENT_ID
            WHERE i.$COLUMN_INGREDIENT_NAME IN ($ingredientsPlaceholders)
            GROUP BY r.$COLUMN_RECIPE_ID
            HAVING COUNT(DISTINCT i.$COLUMN_INGREDIENT_NAME) = ?
        """

        val args = ingredients.toTypedArray() + arrayOf(ingredients.size.toString())
        val cursor = db.rawQuery(query, args)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME))
                val instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS))
                val ingredientsList = getIngredientsForRecipe(id)
                recipes.add(Recipe(id, name, ingredientsList, instructions))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recipes
    }

    fun getRecipesByUserIngredients(userIngredients: List<Ingredient>): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = this.readableDatabase

        val ingredientsPlaceholders = userIngredients.joinToString(",") { "?" }
        val query = """
            SELECT r.*, COUNT(*) as ingredient_count FROM $TABLE_RECIPES r
            INNER JOIN $TABLE_RECIPE_INGREDIENTS ri ON r.$COLUMN_RECIPE_ID = ri.$COLUMN_RECIPE_INGREDIENT_RECIPE_ID
            INNER JOIN $TABLE_INGREDIENTS i ON ri.$COLUMN_RECIPE_INGREDIENT_INGREDIENT_ID = i.$COLUMN_INGREDIENT_ID
            WHERE i.$COLUMN_INGREDIENT_NAME IN ($ingredientsPlaceholders)
            GROUP BY r.$COLUMN_RECIPE_ID
            ORDER BY ingredient_count DESC
        """

        val args = userIngredients.map { it.name }.toTypedArray()
        val cursor = db.rawQuery(query, args)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME))
                val instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS))
                val ingredientsList = getIngredientsForRecipe(id)
                recipes.add(Recipe(id, name, ingredientsList, instructions))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recipes
    }
}
