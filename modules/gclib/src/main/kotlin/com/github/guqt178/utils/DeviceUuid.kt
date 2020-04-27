package com.github.guqt178.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import java.io.UnsupportedEncodingException
import java.util.*

/**
 * Describe：获取Android设备唯一id
 */
@SuppressLint("MissingPermission")
class DeviceUuid private constructor(context: Context) {
    private var deviceUuid: UUID? = null

    companion object {
        private var INSTANCE: DeviceUuid? = null
        private const val PREFS_FILE = "device_id.xml"
        private const val PREFS_DEVICE_ID = "device_id"

        fun getDeviceUuid(context: Context) = INSTANCE?.deviceUuid
                ?: DeviceUuid(context).deviceUuid

    }

    init {
        if (deviceUuid == null) {
            synchronized(DeviceUuid::class.java) {
                if (deviceUuid == null) {
                    val prefs = context.getSharedPreferences(PREFS_FILE, 0)
                    val id = prefs.getString(PREFS_DEVICE_ID, null)
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        deviceUuid = UUID.fromString(id)
                    } else {
                        @SuppressLint("HardwareIds") val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if ("9774d56d682e549c" != androidId) {
                                deviceUuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                            } else {
                                @SuppressLint("HardwareIds") val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                                deviceUuid = if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))) else UUID.randomUUID()
                            }
                        } catch (e: UnsupportedEncodingException) {
                            throw RuntimeException(e)
                        }
                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, deviceUuid.toString()).apply()
                    }
                }
            }
        }
    }
}