package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    lateinit var password: String
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.show()

        auth = Firebase.auth
        currentUser = auth.currentUser

        if (currentUser != null){
            println(currentUser!!.email)
            println("Logcat Main Activity | Sign In : Successful")

            val intent = Intent(this, AvailableServicesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signIn(view: View){

        email = binding.mainActivityEmailEditText.text.toString().trim()
        password = binding.mainActivityPasswordEditText.text.toString().trim()

        if (checkValidEmail(email) && checkValidPassword(password)){
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this, AvailableServicesActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun newAccount(view: View){
        val newAccIntent: Intent = Intent(this, NewAccountActivity::class.java)
        startActivity(newAccIntent)
        //finish()
    }

    fun forgotPassword(view: View){
        val forgotPasswordIntent: Intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(forgotPasswordIntent)
        //finish()
    }

    private fun checkValidEmail(email: String) : Boolean{
        if(email.isEmpty()){
            binding.mainActivityEmailEditText.requestFocus()
            println("Logcat Main Activity | Email : Empty")
            Toast.makeText(this, "Email can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                println("Logcat | Email : Not matched")
                Toast.makeText(this, "Invalid Email Type!\nExample Email : yetis@gmail.com", Toast.LENGTH_LONG).show()
                return false
            } else{
                println("Logcat Main Activity | Email : Matched")
                return true
            }
        }
    }

    private fun checkValidPassword(password: String) : Boolean{
        if (password.isEmpty()){
            binding.mainActivityPasswordEditText.requestFocus()
            println("Logcat Main Activity | Password : Empty")
            Toast.makeText(this, "Password can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            val passwordLength = 6
            if (password.length < passwordLength){
                binding.mainActivityPasswordEditText.requestFocus()
                println("Logcat Main Activity | Password : !(passwordLength >= 6)")
                Toast.makeText(this, "Password size can not be smaller than 6", Toast.LENGTH_LONG).show()
                return false
            } else{
                return true
            }
        }
    }
}