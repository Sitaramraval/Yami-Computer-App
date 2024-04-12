package com.example.yamicomputer.logic

import android.net.Uri
import androidx.annotation.Keep
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.ProductActions
import com.example.yamicomputer.data.ProductData
import com.example.yamicomputer.data.ProfileActions
import com.example.yamicomputer.data.ProfileData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import kotlinx.coroutines.delay
import java.util.UUID

@Keep
class SharedViewModel : ViewModel() {

    var id: MutableState<String> = mutableStateOf("")
    var productID: MutableState<String> = mutableStateOf("")
    var productData: MutableState<ProductData> = mutableStateOf(ProductData())

    var complaintData: MutableState<ComplaintData> = mutableStateOf(ComplaintData())
    var compliantStatus: MutableState<ComplaintStatus> = mutableStateOf(ComplaintStatus.NOTHING)

    var deleteCompliant: MutableState<Boolean> = mutableStateOf(false)
    var deleteProduct: MutableState<Boolean> = mutableStateOf(false)

    // for firebase auth and callback
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database
    private val profileReference = database.getReference("customer-profile")

    fun isUserLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }

    private suspend fun isProfileCreated(): ProfileData {

        val dataSet: MutableState<ProfileData> = mutableStateOf(ProfileData())

        try {
            val userId: FirebaseUser = mAuth.currentUser!!
            profileReference.child(userId.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the data as a User object
                    dataSet.value = dataSnapshot.getValue<ProfileData>()!!
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })


        } catch (_: Exception) {

        }

        delay(1000)
        return dataSet.value
    }

    suspend fun isProfileComplete(): Boolean {

        val validation: MutableState<Boolean> = mutableStateOf(isUserLoggedIn() && isProfileCreated().name.isNotEmpty())
        
        return validation.value
    }

    /** This variable stores the enum value for use of ProfileCreateScreen as update profile or create profile **/
    var profileAction: MutableState<ProfileActions> = mutableStateOf(ProfileActions.CREATE_PROFILE)
    var productAction: MutableState<ProductActions> = mutableStateOf(ProductActions.CREATE)


    // firebase
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    fun uploadPhoto(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val imageName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("images/$imageName")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                onSuccess(downloadUri.toString())
            } else {
                onFailure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }
}