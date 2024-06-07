package com.example.pmf.ui.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pmf.DB.DBHelper
import com.example.pmf.R
import java.text.SimpleDateFormat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val dbHelper = DBHelper(context)
        val ingredients = try {
            dbHelper.getAllItems()
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error getting items from database", e)
            return
        }

        val sharedPref = context.getSharedPreferences("com.example.pmf", Context.MODE_PRIVATE)
        val daysBefore = sharedPref.getInt("notification_days_before", 1)
        val today = Calendar.getInstance()

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (ingredients.isNullOrEmpty()) {
            Log.e("NotificationReceiver", "No ingredients found in the database")
            return
        }

        ingredients.forEach { ingredient ->
            try {
                val expiryDate = Calendar.getInstance().apply {
                    time = sdf.parse(ingredient.expiryDate) ?: throw NullPointerException("Expiry date parsing failed")
                }
                expiryDate.add(Calendar.DAY_OF_YEAR, -daysBefore)
                if (today.after(expiryDate)) {
                    sendNotification(context, ingredient.name, sdf.format(expiryDate.time))
                }
            } catch (e: Exception) {
                Log.e("NotificationReceiver", "Error processing ingredient: ${ingredient.name}", e)
            }
        }
    }

    private fun sendNotification(context: Context, ingredientName: String, expiryDate: String) {
        val notificationId = ingredientName.hashCode()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("expiry_channel", "Expiry Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notifications for items nearing expiry"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 빌더 설정
        val builder = NotificationCompat.Builder(context, "expiry_channel")
            .setSmallIcon(R.drawable.ic_setting_black_24dp) // 아이콘 설정
            .setContentTitle("소비기한 알림")
            .setContentText("$ingredientName 가 $expiryDate 까지 소비해야 합니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 알림 보내기
        try {
            notificationManager.notify(notificationId, builder.build())
            Log.d("NotificationReceiver", "Notification sent for $ingredientName")
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Failed to send notification: ${e.message}", e)
        }
    }
}
