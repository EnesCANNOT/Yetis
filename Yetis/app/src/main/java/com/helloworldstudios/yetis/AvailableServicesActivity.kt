package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityAvailableServicesBinding

class AvailableServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableServicesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var serviceArrayList: ArrayList<Service>
    private lateinit var serviceAdapter: ServiceRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        serviceArrayList = ArrayList<Service>()
        getData()
        binding.availableServicesRecyclerView.layoutManager = LinearLayoutManager(this)
        serviceAdapter = ServiceRecyclerAdapter(this@AvailableServicesActivity, serviceArrayList)
        binding.availableServicesRecyclerView.adapter = serviceAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.home_page_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.availableServices -> {
                val intentForAvailableServices = Intent(this, AvailableServicesActivity::class.java)
                startActivity(intentForAvailableServices)
                finish()
            }

            R.id.profile ->{
                val intentForProfile = Intent(this, ProfileActivity::class.java)
                startActivity(intentForProfile)
                //finish()
            }

            R.id.provideService -> {
                val intentForProvideService = Intent(this, ProvideServiceActivity::class.java)
                startActivity(intentForProvideService)
            }

            R.id.logout -> {
                val intentForLogout = Intent(this, LoginActivity::class.java)
                val auth: FirebaseAuth = Firebase.auth
                auth.signOut()
                startActivity(intentForLogout)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getData(){
        db.collection("Services")
            .orderBy("serviceUploadDate", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                } else{
                    if (value != null){
                        if (!value.isEmpty){
                            val services = value.documents
                            serviceArrayList.clear()

                            for (service in services){


                                val serviceSupplier = service.get("userEmail") as? String
                                val serviceTitle = service.get("serviceTitle") as? String
                                val serviceDescription = service.get("serviceDescription") as? String
                                val serviceLatitude = service.get("serviceLatitude") as? Double
                                val serviceLongitude = service.get("serviceLongitude") as? Double

//                                println(serviceSupplier)
//                                println(serviceTitle)
//                                println(serviceDescription)
//                                println(serviceLatitude)
//                                println(serviceLongitude)


                                val service = Service(serviceSupplier!!, serviceTitle!!, serviceDescription!!, serviceLatitude!!, serviceLongitude!!)
                                serviceArrayList.add(service)
                            }

                            serviceAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }
}