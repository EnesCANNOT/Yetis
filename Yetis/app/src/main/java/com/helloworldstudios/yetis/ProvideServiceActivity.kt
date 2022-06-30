package com.helloworldstudios.yetis

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityProvideServiceBinding

class ProvideServiceActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityProvideServiceBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
//    private lateinit var sharedPreferences: SharedPreferences
//    private var trackBoolean: Boolean? = null
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

    private lateinit var serviceTitle: String
    private lateinit var serviceDescription: String
    private var serviceLatitude: Double = 0.0
    private var serviceLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProvideServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        auth = Firebase.auth
        firestore = Firebase.firestore

        registerLauncher()

//        sharedPreferences = this.getSharedPreferences("com.helloworldstudios.yetis", MODE_PRIVATE)
//        trackBoolean = false

        selectedLatitude = 0.0
        selectedLongitude = 0.0

        binding.buttonSaveService.setOnClickListener {
            serviceTitle = binding.editTextTextServiceTitle.text.toString()
            serviceDescription = binding.editTextTextServiceDescription.text.toString()
            serviceLatitude = selectedLatitude!!
            serviceLongitude = selectedLongitude!!

            saveService(auth.currentUser?.email!!, serviceTitle, serviceDescription, serviceLatitude, serviceLongitude)
            val intentForAvailableServices = Intent(this, AvailableServicesActivity::class.java)
            startActivity(intentForAvailableServices)
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)

        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {

//                trackBoolean = sharedPreferences.getBoolean("trackBoolean", false)
//
//                if (!trackBoolean!!){
//                    val userLocation = LatLng(location.latitude, location.longitude)
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
//                    sharedPreferences.edit().putBoolean("trackBoolean", true).apply()
//                }

            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.root, "Permission needed for location", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //request location permission
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            } else{
                //request location permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            mMap.isMyLocationEnabled = true
        }

    }

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))

        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude
    }

    private fun registerLauncher(){

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                //location permission granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                    mMap.isMyLocationEnabled = true
                }
            } else{
                //location permission denied
                Toast.makeText(this, "Permission needed", Toast.LENGTH_LONG).show()

            }
        }

    }

    private fun saveService(userEmail: String, serviceTitle: String, serviceDescription: String, serviceLatitude: Double, serviceLongitude: Double){
        val userService = hashMapOf<String, Any>()

        userService.put("userEmail", userEmail)
        userService.put("serviceTitle", serviceTitle)
        userService.put("serviceDescription", serviceDescription)
        userService.put("serviceLatitude", serviceLatitude)
        userService.put("serviceLongitude", serviceLongitude)
        userService.put("serviceUploadDate", Timestamp.now())

        firestore.collection("Services").add(userService).addOnSuccessListener {
            println("User successfully registered")

        }.addOnFailureListener {
            println("User registration failed")
            //Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}