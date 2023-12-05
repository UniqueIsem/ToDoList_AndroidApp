package com.example.proyecto_todolist

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Crea una notificación
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.notificaciones)
            .setContentTitle("¡Alarma activada!")
            .setContentText("Es hora de hacer algo.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Muestra la notificación
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())

        // Aquí puedes realizar acciones adicionales cuando la alarma se activa.
    }
}