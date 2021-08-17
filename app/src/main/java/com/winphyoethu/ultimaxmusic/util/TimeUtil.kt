package com.winphyoethu.ultimaxmusic.util

import android.util.Log

object TimeUtil {

    fun timestampToDuration(timestampInMilli: Long): String {
        val timestamp = timestampInMilli / 1000

        Log.i("h:m:s", ":: timestamp :: " + timestamp)
        val hours = timestamp / 3600

        Log.i("h:m:s", ":: hours :: " + hours + " :: " + (timestamp % 3600))
        val minutes = timestamp % 3600 / 60

        Log.i("h:m:s", ":: minutes :: " + minutes + " :: " + (timestamp % 3600 % 60))
        val second = timestamp % 3600 % 60

        Log.i("h:m:s", ":: seconds :: " + second + " :: ")

        Log.i("h:m:s", hours.toString() + " :" + minutes + ":" + second)

        var duration = ""

        duration = if (hours > 0) {
            if (hours > 9) {
                "$hours:"
            } else {
                "0$hours:"
            }
        } else {
            ""
        }

        duration += if (minutes > 0) {
            if (minutes > 9) {
                "$minutes:"
            } else {
                "0$minutes:"
            }
        } else {
            "00:"
        }

        duration += if (second > 0) {
            if (duration.isEmpty()) {
                if (second > 9) {
                    "$second"
                } else {
                    "0$second"
                }
            } else {
                if (second > 9) {
                    "$second"
                } else {
                    "0$second"
                }
            }
        } else {
            "00"
        }

        return duration
    }

}