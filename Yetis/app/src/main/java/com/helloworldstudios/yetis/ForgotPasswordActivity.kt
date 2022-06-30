package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.helloworldstudios.yetis.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
    }

    fun send(view: View){
        val emailAddress: String = binding.forgotPasswordEmailEditText.text.trim().toString()

        if (checkValidEmail(emailAddress)){
            auth.sendPasswordResetEmail(emailAddress)
                .addOnSuccessListener {
                    Toast.makeText(this, "Password reset link has been sent to your email", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    println("Logcat | Forgot Password Link Sending : Failed\n" + it.localizedMessage)
                }
        }
    }

    fun newAccount(view: View){
        val newAccIntent: Intent = Intent(this, NewAccountActivity::class.java)
        startActivity(newAccIntent)
        finish()
    }

    private fun checkValidEmail(email: String) : Boolean{
        if(email.isEmpty()){
            binding.forgotPasswordEmailEditText.requestFocus()
            println("Logcat | Email : Empty")
            Toast.makeText(this, "Email can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                println("Logcat | Email : Not matched")
                Toast.makeText(this, "Invalid Email Type!\nExample Email : yetis@gmail.com", Toast.LENGTH_LONG).show()
                return false
            } else{
                println("Logcat | Email : Matched")
                return true
            }
        }
    }
}