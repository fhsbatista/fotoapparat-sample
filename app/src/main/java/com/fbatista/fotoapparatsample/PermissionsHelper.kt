package com.fbatista.fotoapparatsample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHelper {

    const val REQUEST_CODE_CAMERA = 100

    fun isCameraPermissionsGranted(context: Context): Boolean
            = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
    }



}