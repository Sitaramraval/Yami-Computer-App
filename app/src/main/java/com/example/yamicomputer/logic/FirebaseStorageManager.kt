package com.example.yamicomputer.logic

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID

object FirebaseStorageManager {

    private val storage = FirebaseStorage.getInstance()

    fun uploadPhoto(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        // Create a storage reference
        val storageRef = storage.reference

        // Create a reference to the file that will be uploaded
        val photoRef = storageRef.child("images/${imageUri.lastPathSegment}")

        // Upload file to Firebase Storage
        val uploadTask = photoRef.putFile(imageUri)

        // Register observers to listen for upload progress or errors
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            // Get the public download URL
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString()) // Pass the download URL to the callback
            }.addOnFailureListener { e ->
                onFailure(e) // Handle any error getting the download URL
            }
        }.addOnFailureListener { e ->
            // Handle unsuccessful uploads
            onFailure(e)
        }
    }
}

class UploadPhotoViewModel : ViewModel() {

}