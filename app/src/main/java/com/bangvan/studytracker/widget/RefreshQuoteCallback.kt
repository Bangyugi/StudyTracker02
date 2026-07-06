package com.bangvan.studytracker.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class RefreshQuoteCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, QuoteWidgetReceiver::class.java).apply {
            action = QuoteWidgetReceiver.ACTION_REFRESH
        }
        context.sendBroadcast(intent)
    }
}