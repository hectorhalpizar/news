package me.hectorhalpizar.android.news.framework

import android.content.pm.PackageInfo
import android.os.Build

fun versionCode(appPackageInfo: PackageInfo) : Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        appPackageInfo.longVersionCode.toInt()
    } else {
        appPackageInfo.versionCode
    }

fun versionName(appPackageInfo: PackageInfo) : String = appPackageInfo.versionName
