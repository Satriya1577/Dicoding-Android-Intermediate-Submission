package com.example.mystoryapp.ui.maps

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMapsBinding
import com.example.mystoryapp.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val data = ArrayList<ListStoryItem>()
        viewModel.getStoriesWithLocation().observe(this@MapsActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        result.data.forEach {
                            data.add(it)
                        }
                        addManyMarker(data)
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.failed_title)
                            setMessage(result.error)
                            setPositiveButton(R.string.positive_reply) { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }

    private fun addManyMarker(data: ArrayList<ListStoryItem>) {
        data.forEach { storyItem ->
            val lat = storyItem.lat
            val lon = storyItem.lon

            if (lat != null && lon != null) {
                val position = LatLng(lat, lon)
                Log.d("MapsActivity", "lat: $lat, lon: $lon")

                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(storyItem.name)
                        .snippet(storyItem.description)
                )
                boundsBuilder.include(position)
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}