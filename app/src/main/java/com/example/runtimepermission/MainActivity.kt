package com.example.runtimepermission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.runtimepermission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val REQCODE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.android.setOnClickListener {
            if (checkPermission()) {
                Toast.makeText(this, "Permission granted for location", Toast.LENGTH_SHORT).show()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                ACCESS_FINE_LOCATION,
               ACCESS_COARSE_LOCATION
            ), REQCODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQCODE) {
            if (grantResults.isNotEmpty()) {
                 var fineloc  = grantResults[0]
                 var coarseloc  = grantResults[1]
                if (fineloc == PackageManager.PERMISSION_GRANTED && coarseloc == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Location permission is required to use this feature.")
            .setCancelable(false)
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestLocationPermission()
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                // Handle permission denial, e.g., show a message or close the app.
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Permission Required")
        alert.show()
    }
}
