package com.bangvan.studytracker.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

import com.bangvan.studytracker.MainActivity

class QuoteGlanceWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object{
        val KEY_QUOTE = stringPreferencesKey("widget_quote")
        val KEY_AUTHOR = stringPreferencesKey("widget_author")
    }

    @SuppressLint("RestrictedApi")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val quoteText = prefs[KEY_QUOTE] ?: "Chưa có danh ngôn nào. Hãy nhấn làm mới!"
            val authorText = prefs[KEY_AUTHOR] ?: ""

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color(0xE61A1B2F))
                    .padding(16.dp)
                    .clickable(actionStartActivity<MainActivity>())
            ) {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.End

                    ){

                        Button(
                            text = "Làm mới",
                            onClick = actionRunCallback<RefreshQuoteCallback>()
                        )
                    }
                    Box(
                        modifier = GlanceModifier.defaultWeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = quoteText,
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = 15.sp,
                                fontStyle = androidx.glance.text.FontStyle.Italic
                            ),
                            maxLines = 3
                        )
                    }

                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = if (authorText.isNotEmpty()) "- $authorText" else "",
                            style = TextStyle(
                                color = ColorProvider(Color(0xB3FFFFFF)),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
        }


    }
}