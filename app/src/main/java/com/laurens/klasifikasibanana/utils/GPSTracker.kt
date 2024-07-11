package com.laurens.klasifikasibanana.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.laurens.klasifikasibanana.view.Deteksi.ResultActivity
import com.laurens.klasifikasibanana.view.Deteksi.ScanFragment
import java.io.IOException
import java.util.*

class GPSTracker(private val context: Context) : Service(), LocationListener {

    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var isGPSTrackingEnabled = false
    private var location: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var geocoderMaxResults = 1

    private var locationManager: LocationManager? = null
    private var providerInfo: String? = null

    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled || isNetworkEnabled) {
                isGPSTrackingEnabled = true

                // Determine provider to use
                providerInfo = if (isGPSEnabled) {
                    Log.d(TAG, "Using GPS Provider")
                    LocationManager.GPS_PROVIDER
                } else {
                    Log.d(TAG, "Using Network Provider")
                    LocationManager.NETWORK_PROVIDER
                }

                // Request location updates
                locationManager!!.requestLocationUpdates(
                    providerInfo!!,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )

                // Get last known location
                location = locationManager!!.getLastKnownLocation(providerInfo!!)
                updateGPSCoordinates()
            } else {
                showSettingsAlert()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting location", e)
        }
    }

    private fun updateGPSCoordinates() {
        location?.let {
            latitude = it.latitude
            longitude = it.longitude
        }
    }

    fun fetchLatitude(): Double {
        return latitude
    }

    fun fetchLongitude(): Double {
        return longitude
    }
    fun fetchLocationName(): String? {
        return getAddressLine(context)
    }

    fun stopUsingGPS() {
        locationManager?.removeUpdates(this)
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("GPS Settings")
        alertDialog.setMessage("GPS is not enabled. Do you want to go to the settings menu?")

        alertDialog.setPositiveButton("Settings") { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    private fun getGeocoderAddress(): List<Address>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            geocoder.getFromLocation(latitude, longitude, geocoderMaxResults)
        } catch (e: IOException) {
            Log.e(TAG, "Error getting geocoder address", e)
            null
        }
    }

    private fun getAddressLine(context: Context): String? {
        val addresses = getGeocoderAddress()
        return addresses?.getOrNull(0)?.getAddressLine(0)
    }

    fun getLocality(): String? {
        val addresses = getGeocoderAddress()
        return addresses?.getOrNull(0)?.locality
    }

    fun getPostalCode(): String? {
        val addresses = getGeocoderAddress()
        return addresses?.getOrNull(0)?.postalCode
    }

    fun getCountryName(): String? {
        val addresses = getGeocoderAddress()
        return addresses?.getOrNull(0)?.countryName
    }

    override fun onLocationChanged(location: Location) {
        this.location = location
        updateGPSCoordinates()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "GPSTracker"
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong() // 1 minute
    }

    init {
        getLocation()
    }
}
