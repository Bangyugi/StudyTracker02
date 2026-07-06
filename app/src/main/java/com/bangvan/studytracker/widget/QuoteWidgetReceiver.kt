package com.bangvan.studytracker.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.bangvan.studytracker.data.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuoteWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    override val glanceAppWidget: GlanceAppWidget = QuoteGlanceWidget()

    companion object {
        const val ACTION_REFRESH = "com.bangvan.studytracker.widget.GLANCE_REFRESH_QUOTE"
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        fetchNewQuote(context)
    }
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {
            fetchNewQuote(context)
        }
    }

    private fun fetchNewQuote(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val quotes = taskRepository.getRandomQuote()
                val quote = quotes.firstOrNull()
                if (quote != null) {
                    updateWidgetState(context, quote.quote, quote.author)
                }
            } catch (e: Exception) {
                updateWidgetState(context, "Không thể tải danh ngôn. Hãy thử lại!", "Lỗi kết nối")
            }
        }
    }

    private suspend fun updateWidgetState(context: Context, quote: String, author: String) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(QuoteGlanceWidget::class.java)

        for (glanceId in glanceIds) {

            updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[QuoteGlanceWidget.KEY_QUOTE] = quote
                    this[QuoteGlanceWidget.KEY_AUTHOR] = author
                }
            }
            QuoteGlanceWidget().update(context, glanceId)
        }
    }
}