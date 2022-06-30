package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    var flag: Boolean = false

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var firestore: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference

    private lateinit var userName: String
    private lateinit var fullName: String
    private lateinit var enteredCurrentPassword: String
    private lateinit var newPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.firestore
        firestore = Firebase.firestore
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        auth = Firebase.auth
        println(auth.currentUser!!.email)

        firestore.collection("Users")
            .whereEqualTo("email", auth.currentUser!!.email)
            .addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                } else{
                    if (value != null){
                        if (!value.isEmpty){
                            val user = value.documents
                            for (field in user){
                                binding.profileActivityUserNameEditText.setText(field.get("userName").toString())
                                binding.profileActivityFullNameEditText.setText(field.get("fullName").toString())
                                binding.profileActivityEmailEditText.setText(field.get("email").toString())
                                //binding.profileActivityCurrentPasswordEditText.setText(field.get("password").toString())
                                println(field.get("userName"))
                            }
                        } else{
                            println("Value is empty")
                        }
                    } else{
                        println("Value is null")
                    }

                }
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.my_services, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.myServices -> {
                val intentForMyServices = Intent(this, MyServicesActivity::class.java)
                startActivity(intentForMyServices)
            }
        }
        return super.onOptionsItemSelected(item)
    }
//
//    fun saveChanges(view: View){
//        userName = binding.profileActivityUserNameEditText.text.toString()
//        fullName = binding.profileActivityFullNameEditText.text.toString()
//        enteredCurrentPassword = binding.profileActivityCurrentPasswordEditText.text.toString()
//        newPassword = binding.profileActivityNewPasswordEditText.text.toString()
//
//        if (getCurrentPassword(enteredCurrentPassword) == enteredCurrentPassword){
//            println("hello")
//        } else{
//            println("hi")
//        }
//    }
//
//    private fun getCurrentPassword(enteredCurrentPassword: String) : String{
//        var currentPassword = ""
//
//        firestore.collection("Users")
//            .whereEqualTo("email", auth.currentUser!!.email)
//            .addSnapshotListener { value, error ->
//                if (error != null){
//                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
//                } else{
//                    if (value != null){
//                        if (!value.isEmpty){
//                            val user = value.documents
//                            for (field in user){
//                                currentPassword = field.get("password").toString()
//                                println("Current password : $currentPassword")
//                                println("Entered Current Password : $enteredCurrentPassword")
//
//                            }
//                        } else{
//                            println("Value is empty")
//                        }
//                    } else{
//                        println("Value is null")
//                    }
//
//                }
//            }
//
//        return currentPassword
//    }
}