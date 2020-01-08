package com.fbatista.fotoapparatsample

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import io.fotoapparat.Fotoapparat
import io.fotoapparat.characteristic.LensPosition
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.LensPositionSelector
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class CameraLens(val lensPosition: LensPositionSelector) {
        FRONT(front()),
        BACK(back())
    }


    private lateinit var fotoapparat: Fotoapparat
    private var currentCameraLens = CameraLens.FRONT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCamera()
        setListeners()
    }

    private fun setupCamera() {
        if (PermissionsHelper.isCameraPermissionsGranted(this)) {
            setFotoApparat()
        } else {
            PermissionsHelper.requestCameraPermission(this)
        }
    }

    private fun setListeners() {
        turnCameraBtn.setOnClickListener {
            turnCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionsHelper.REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setFotoApparat()
                } else {
                    showToast("Vá para as configurações para dar as permissões quando vc precisar da foto")
                }
            }
        }
    }

    private fun setFotoApparat() {
        fotoapparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = currentCameraLens.lensPosition,
            cameraErrorCallback = { showToast("Deu Pau") }
        )
    }

    private fun turnCamera() {
        when (currentCameraLens) {
            CameraLens.FRONT -> {
                fotoapparat.switchTo(CameraLens.BACK.lensPosition, CameraConfiguration.default())
                currentCameraLens = CameraLens.BACK
            }
            CameraLens.BACK -> {
                fotoapparat.switchTo(CameraLens.FRONT.lensPosition, CameraConfiguration.default())
                currentCameraLens = CameraLens.FRONT
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        fotoapparat.stop()
    }
}
