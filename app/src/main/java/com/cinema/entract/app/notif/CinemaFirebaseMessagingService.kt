/*
 * Copyright 2018 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.app.notif

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.core.ext.secondaryColor
import com.cinema.entract.data.interactor.NotifUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CinemaFirebaseMessagingService : FirebaseMessagingService(), CoroutineScope {

    private val useCase by inject<NotifUseCase>()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onNewToken(token: String) {
        Timber.d("New token is $token")
        launch(context = coroutineContext) { useCase.registerNotifications(token) }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.body?.let { manageNotification(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun manageNotification(message: String) {
        val intent = Intent(this, CinemaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_black_24dp)
            .setColor(secondaryColor())
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(0, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "Entract Grenade Cinema"
    }
}