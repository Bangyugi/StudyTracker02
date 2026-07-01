package com.bangvan.studytracker.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
){

    fun fetchAndActivate(onComplete: (Boolean) -> Unit = {}){
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    fun getString(key: String): String = remoteConfig.getString(key)

    fun getLong(key: String): Long = remoteConfig.getLong(key)

    fun getDouble(key: String): Double = remoteConfig.getDouble(key)

}