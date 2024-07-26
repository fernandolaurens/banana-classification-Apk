package com.laurens.klasifikasibanana.view.Deteksi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.laurens.klasifikasibanana.R
import com.laurens.klasifikasibanana.database.AnalysisResult
import com.laurens.klasifikasibanana.database.AppDatabase
import com.laurens.klasifikasibanana.databinding.ActivityResultBinding
import com.laurens.klasifikasibanana.utils.GPSTracker
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var BananaDescriptions: Map<String, String>
    private lateinit var gpsTracker: GPSTracker

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                gpsTracker = GPSTracker(this)
                showLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        BananaDescriptions()
        resultview()

        gpsTracker = GPSTracker(this)
        // Check and request location permission if not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            // Permission already granted, show location
            showLocation()
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLocation() {
        val locationName = gpsTracker.fetchLocationName()
        binding.tvLocation.text = "Your Location: $locationName"

        // Format date and time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a", java.util.Locale.getDefault())
        val dateTime = dateFormat.format(Date())
        binding.tvDateTime.text = "Time: $dateTime"
    }

    private fun BananaDescriptions() {
        val inputStream = resources.openRawResource(R.raw.banana_descriptions)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = bufferedReader.use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        BananaDescriptions =
            jsonObject.keys().asSequence().associateWith { jsonObject.getString(it) }
    }

    private fun setupListeners() {
        binding.apply {
            tvLabel.setOnClickListener { searchOnGoogle() }
            btnSaveAnalysis.setOnClickListener { saveCurrentAnalysis() }
        }
    }

    private fun searchOnGoogle() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/search?q=${binding.tvLabel.text}")
        )
        startActivity(intent)
    }

    private fun resultview() {
        val label = intent.getStringExtra("label")
        val confidence = intent.getFloatExtra("confidence", 0.0f)
        val imageUri = intent.getStringExtra("imageUri")

        binding.tvLabel.text = getString(R.string.analysis_type, label)
        binding.ada.text = getString(R.string.moreInfo)
        binding.tvScore.text = getString(
            R.string.analysis_score,
            NumberFormat.getPercentInstance().format(confidence).toString()
        )
        binding.imageView.load(imageUri)

        // Display the bird description if available
        val description = BananaDescriptions[label] ?: "Deskripsi tidak tersedia untuk burung ini."
        binding.additionalInfoTextView.text = description
    }

    private fun saveCurrentAnalysis() {
        val label = intent.getStringExtra("label") ?: return
        val score = intent.getFloatExtra("confidence", 0.0f)
        val imageUri = intent.getStringExtra("imageUri") ?: return
        val location = binding.tvLocation.text.toString().removePrefix("Your Location: ")
        val dateTime = binding.tvDateTime.text.toString().removePrefix("Time: ")
        saveAnalysisResult(label, score, imageUri, dateTime, location)
    }

    private fun saveAnalysisResult(
        label: String,
        score: Float,
        imageUri: String,
        date: String,
        location: String
    ) {
        val analysisResult = AnalysisResult(
            label = label,
            score = score,
            imageUri = imageUri,
            date = date,
            location = location
        )
        val db = AppDatabase.getDatabase(this)
        val dao = db.analysisResultDao()

        lifecycleScope.launch {
            dao.insert(analysisResult)
            runOnUiThread {
                // Display a toast message to inform the user
                Toast.makeText(
                    this@ResultActivity,
                    "Analysis saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}