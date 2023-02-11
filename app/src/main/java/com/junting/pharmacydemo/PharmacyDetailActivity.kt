package com.junting.pharmacydemo

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.junting.pharmacydemo.data.Feature
import com.junting.pharmacydemo.databinding.ActivityPharmacyDetailBinding

class PharmacyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPharmacyDetailBinding

    //獲取
    private val data by lazy { intent.getSerializableExtra("data") as Feature }

    private val name by lazy { data?.properties?.name }
    private val maskAdultAmount by lazy { data?.properties?.mask_adult }
    private val maskChildAmount by lazy { data?.properties?.mask_child }
    private val phone by lazy { data?.properties?.phone }
    private val address by lazy { data?.properties?.address }

    private var locationPermissionGranted = false

    private lateinit var mContext: Context

    private lateinit var mLocationProviderClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_detail)

        binding = ActivityPharmacyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initView()
        getLocationPermission()

    }

    private fun getLocationPermission() {
        //檢查權限
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //已獲取到權限
            Toast.makeText(this, "已獲取到位置權限，可以準備開始獲取經緯度", Toast.LENGTH_SHORT).show()
            locationPermissionGranted = true
            checkGPSState()
        } else {
            //詢問要求獲取權限
            requestLocationPermission()
        }
    }

    private fun checkGPSState() {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(mContext)
                .setTitle("GPS 尚未開啟")
                .setMessage("使用此功能需要開啟 GSP 定位功能")
                .setPositiveButton("前往開啟",
                    DialogInterface.OnClickListener { _, _ ->
                        startActivityForResult(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_ENABLE_GPS
                        )
                    })
                .setNegativeButton("取消", null)
                .show()
        } else {
            getDeviceLocation()
            Toast.makeText(this, "已獲取到位置權限且GPS已開啟，可以準備開始獲取經緯度", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted
            ) {
                val locationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                //更新頻率
                locationRequest.interval = 1000
                //更新次數，若沒設定，會持續更新
                //locationRequest.numUpdates = 1
                mLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationResult ?: return
                            Log.d(
                                "junting",
                                "緯度:${locationResult.lastLocation?.latitude} , 經度:${locationResult.lastLocation?.longitude} "
                            )
                        }
                    },
                    null
                )

            } else {
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setMessage("此應用程式，需要位置權限才能正常使用")
                .setPositiveButton("確定") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
                .setNegativeButton("取消") { _, _ -> requestLocationPermission() }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        }
    }


    private fun initView() {
        binding.tvName.text = name ?: "資料發生錯誤"
        binding.tvAdultAmount.text = maskAdultAmount.toString()
        binding.tvChildAmount.text = maskChildAmount.toString()
        binding.tvPhone.text = phone
        binding.tvAddress.text = address
    }
}