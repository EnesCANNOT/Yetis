package com.helloworldstudios.yetis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.helloworldstudios.yetis.databinding.ActivityLoginBinding
import com.helloworldstudios.yetis.databinding.ActivityNewAccountBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class NewAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    lateinit var userName: String
    lateinit var fullName: String
    lateinit var phoneNo: String
    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    fun register(view: View){
        userName = binding.newAccountUserNameEditText.text.toString().trim()
        fullName = binding.newAccountFullNameEditText.text.toString().trim()
        phoneNo = binding.newAccountPhoneNoEditText.text.toString().trim()
        email = binding.newAccountEmailEditText.text.toString().trim()
        password = binding.newAccountPasswordEditText.text.toString().trim()

        checkFields(userName, fullName, phoneNo, email, password)
    }

    private fun checkFields(userName: String, fullName: String, phoneNo: String, email: String, password: String){
        if (checkValidUserName(userName)){
            if (checkValidFullName(fullName)){
                if (checkValidPhoneNo(phoneNo)){
                    if (checkValidEmail(email)){
                        if (checkValidPassword(password)){
                            register()
                        } else{
                            binding.newAccountPasswordEditText.setError("Invalid Password")
                        }
                    } else{
                        binding.newAccountEmailEditText.setError("Invalid Email")
                    }

                } else{
                    binding.newAccountPhoneNoEditText.setError("Invalid Phone No")
                }
            } else{
                binding.newAccountFullNameEditText.setError("Invalid Full Name")
            }
        } else{
            binding.newAccountUserNameEditText.setError("Invalid User Name")
        }
    }

    private fun checkValidUserName(userName: String) : Boolean{

        val userNameLength = 6

        if (userName.isEmpty()){
            binding.newAccountUserNameEditText.requestFocus()
            println("Logcat | UserName : Empty")
            Toast.makeText(this, "Username can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            if (userName.length < userNameLength){
                binding.newAccountUserNameEditText.requestFocus()
                println("Logcat | UserName : !(userNameLength >= 6)")
                Toast.makeText(this, "Username size can not be smaller than 6", Toast.LENGTH_LONG).show()
                return false
            } else{
                println("Logcat | Username : Matched")
                return true
            }
        }
    }

    private fun checkValidFullName(fullName: String) : Boolean{
        if (fullName.isEmpty()){
            binding.newAccountFullNameEditText.requestFocus()
            println("Logcat | FullName : Empty")
            Toast.makeText(this, "Fullname can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            println("Logcat | Fullname : Matched")
            return true
        }
    }

    private fun checkValidPhoneNo (phoneNo: String) : Boolean{
        if (phoneNo.isEmpty()){
            binding.newAccountPhoneNoEditText.requestFocus()
            println("Logcat | PhoneNo : Empty")
            Toast.makeText(this, "Phone Number can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            var pattern : Pattern = Pattern.compile("(05|5)[0-9][0-9][1-9]([0-9]){6}")
            var matcher : Matcher = pattern.matcher(phoneNo)

            if (!matcher.matches()){
                println("Logcat | PhoneNo : Not matched")
                Toast.makeText(this, "Invalid Phone No Type!\nExample Phone No : 05XXXXXXXXX", Toast.LENGTH_LONG).show()
                return false
            } else{
                println("Logcat | PhoneNo : Matched")
                return true
            }
        }
    }

    private fun checkValidEmail(email: String) : Boolean{
        if(email.isEmpty()){
            binding.newAccountEmailEditText.requestFocus()
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

    private fun checkValidPassword(password: String) : Boolean{
        if (password.isEmpty()){
            binding.newAccountPasswordEditText.requestFocus()
            println("Logcat | Password : Empty")
            Toast.makeText(this, "Password can not be empty!", Toast.LENGTH_LONG).show()
            return false
        } else{
            val passwordLength = 6
            if (password.length < passwordLength){
                binding.newAccountPasswordEditText.requestFocus()
                println("Logcat | Password : !(passwordLength >= 6)")
                Toast.makeText(this, "Password size can not be smaller than 6", Toast.LENGTH_LONG).show()
                return false
            } else{
                return true
            }
        }
    }

    private fun register(){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() {
            if(it.isSuccessful){
                saveUserFieldsToStorage(userName, fullName, phoneNo, email, password)
                print("Logcat | Verification : Successful")
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener(OnSuccessListener {
                        println("Logcat | Verification : Successful")
                        val availableServicesActivity: Intent = Intent(this, AvailableServicesActivity::class.java)
                        startActivity(availableServicesActivity)
                        finish()
                    })
                    ?.addOnFailureListener(OnFailureListener(){

                        println("Logcat | Verification : Failed\n" + it.localizedMessage)
                    })
            } else{
                print("Logcat | Verification : Failed")
            }
        }
    }

    private fun saveUserFieldsToStorage(userName: String, fullName: String, phoneNo: String, email: String, password: String){

        val userMap = hashMapOf<String, Any>()

        userMap.put("userName", userName)
        userMap.put("fullName", fullName)
        userMap.put("phoneNo", phoneNo)
        userMap.put("email", email)
        userMap.put("password", password)

        firestore.collection("Users").document(email).set(userMap).addOnSuccessListener {
            println("User successfully registered")

        }.addOnFailureListener {
            println("User registration failed")
            //Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}