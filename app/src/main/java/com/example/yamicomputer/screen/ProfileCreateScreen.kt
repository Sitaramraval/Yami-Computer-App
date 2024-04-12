package com.example.yamicomputer.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.data.ProfileActions
import com.example.yamicomputer.data.ProfileData
import com.example.yamicomputer.navigation.Routes
import com.example.yamicomputer.ui.theme.DarkBlue
import com.example.yamicomputer.logic.SharedViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current


    // for firebase auth and callback
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val userId: FirebaseUser = mAuth.currentUser!!

    // on below line creating variable for freebase database
    // and database reference.
//    val firebaseDatabase = FirebaseDatabase.database
    val database = Firebase.database
    val databaseReference = database.getReference("customer-profile")

    val profileAction by sharedViewModel.profileAction

    /** This boolean variable stores value which states is this screen used to create a new profile or to update an existing profile **/
    val isProfileNotCreated = profileAction == ProfileActions.CREATE_PROFILE

    var name by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var city by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        if (!isProfileNotCreated) {
            databaseReference.child(userId.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the data as a User object
                    val data = dataSnapshot.getValue<ProfileData>()!!
                    name = data.name
                    address = data.address
                    city = data.city
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(
                        context,
                        "Error while fetching data!, please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (!isProfileNotCreated) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "back",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )

                        }
                    }

                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "${
                            if (isProfileNotCreated) "Create" else "Update"
                        } Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Name
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Full Name",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // Address
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Address",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = address,
                    onValueChange = {
                        address = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // City
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "City",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                TextField(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(10.dp), color = Color.Gray)
                        .fillMaxWidth(),
                    value = city,
                    onValueChange = {
                        city = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 100.dp)
                    .width(200.dp),
                onClick = {

                    // on below line we are adding data.
                    val profileData = ProfileData(name, address, city)

                    val newRef = databaseReference.child(userId.uid)
                    newRef.setValue(profileData)
                    Log.d("error-firebase-database", "onCancelled:")
                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Data has changed
                            Toast.makeText(
                                context,
                                "Profile Created!",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (isProfileNotCreated) {
                                navController.popBackStack()
                                navController.navigate(Routes.HomeScreen.id)
                            } else {
                                navController.popBackStack()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Error occurred
                            Toast.makeText(
                                context,
                                "Fail to add data $error",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d("error-firebase-database", "onCancelled: $error")
                        }
                    })

                },
                shape = RoundedCornerShape(10.dp),

                ) {

                Text(
                    text = if (isProfileNotCreated) "Continue" else "Update",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

            }

        }
    }
}