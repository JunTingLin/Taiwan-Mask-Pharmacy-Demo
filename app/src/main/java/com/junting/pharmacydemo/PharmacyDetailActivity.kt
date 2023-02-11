package com.junting.pharmacydemo

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.junting.pharmacydemo.data.Feature
import com.junting.pharmacydemo.databinding.ActivityPharmacyDetailBinding

class PharmacyDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityPharmacyDetailBinding

    //獲取
    private val data by lazy { intent.getSerializableExtra("data") as Feature }

    private val name by lazy { data?.properties?.name }
    private val maskAdultAmount by lazy { data?.properties?.mask_adult }
    private val maskChildAmount by lazy { data?.properties?.mask_child }
    private val phone by lazy { data?.properties?.phone }
    private val address by lazy { data?.properties?.address }


    private lateinit var mContext: Context

    private lateinit var mFusedLocationProviderClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pharmacy_detail)
        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

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
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}