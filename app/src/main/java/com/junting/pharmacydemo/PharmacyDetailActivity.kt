package com.junting.pharmacydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.junting.pharmacydemo.databinding.ActivityPharmacyDetailBinding
import com.junting.pharmacydemo.data.Feature


class PharmacyDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val data by lazy { intent.getSerializableExtra("data") as? Feature }

    private val name by lazy { data?.properties?.name }
    private val maskAdultAmount by lazy { data?.properties?.mask_adult }
    private val maskChildAmount by lazy { data?.properties?.mask_child }
    private val phone by lazy { data?.properties?.phone }
    private val address by lazy { data?.properties?.address }
    private val latitude by lazy { data?.geometry?.coordinates?.get(1) ?: "取緯失敗" }
    private val longitude by lazy { data?.geometry?.coordinates?.get(0) ?: "取經失敗" }

    private lateinit var binding: ActivityPharmacyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pharmacy_detail)

        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initView()

    }

    private fun initView() {
        binding.tvName.text = name ?: "資料發生錯誤"
        binding.tvAdultAmount.text = maskAdultAmount.toString()
        binding.tvChildAmount.text = maskChildAmount.toString()
        binding.tvPhone.text = phone
        binding.tvAddress.text = address
    }

    override fun onMapReady(googleMap: GoogleMap) {

        // Add a marker in Sydney and move the camera
        val location = LatLng(latitude as Double, longitude as Double)
        Log.d("Junting",location.toString())
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location, 15f
            )
        )
        googleMap.addMarker(MarkerOptions().position(location).title(name ?: "資料發生錯誤")).showInfoWindow()

    }
}