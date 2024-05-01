package com.example.yamicomputer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.R
import com.example.yamicomputer.data.ActivityCardData
import com.example.yamicomputer.data.ActivityCardItems
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.ProductActions
import com.example.yamicomputer.data.ProfileActions
import com.example.yamicomputer.data.ProfileData
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.navigation.Routes
import com.example.yamicomputer.ui.theme.Dark
import com.example.yamicomputer.ui.theme.DarkBlue
import com.example.yamicomputer.ui.theme.DarkYellow
import com.example.yamicomputer.ui.theme.Green
import com.example.yamicomputer.ui.theme.Orange
import com.example.yamicomputer.ui.theme.OrangeDark
import com.example.yamicomputer.ui.theme.Pink40
import com.example.yamicomputer.ui.theme.Pink80
import com.example.yamicomputer.ui.theme.Salmon
import com.example.yamicomputer.ui.theme.SalmonDark
import com.example.yamicomputer.ui.theme.UIBlue
import com.example.yamicomputer.ui.theme.Yellow
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val userId: FirebaseUser = mAuth.currentUser!!

    val database = Firebase.database

    val myRef = database.getReference("customer-profile").child(userId.uid)
    var dateProfile by remember {
        mutableStateOf(ProfileData("", "", ""))
    }

    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // Data has changed
            dateProfile = snapshot.getValue(ProfileData::class.java)!!
        }

        override fun onCancelled(error: DatabaseError) {
            // Error occurred
        }
    })

    val complaintDataList = remember {
        mutableStateListOf<ComplaintData>()
    }
    val firebaseDatabase = Firebase.database
    val databaseReference = firebaseDatabase.getReference("customer-complaints")

    LaunchedEffect(key1 = Unit) {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot, previousChildName: String?
            ) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our  list
                complaintDataList.add(snapshot.getValue(ComplaintData::class.java)!!)
            }

            override fun onChildChanged(
                snapshot: DataSnapshot, previousChildName: String?
            ) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be called
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
            }

            override fun onChildMoved(
                snapshot: DataSnapshot, previousChildName: String?
            ) {
                // this method is called when we move our
                // child in our database.
                // in our code we are not moving any child.
            }

            override fun onCancelled(error: DatabaseError) {
                // this method is called when we get any
                // error from Firebase with error.
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "YAMI Computer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    IconButton(onClick = {
                        sharedViewModel.profileAction.value = ProfileActions.UPDATE_PROFILE
                        navController.navigate(Routes.ProfileCreateScreen.id)
                    }) {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_account_circle_24),
                            contentDescription = "account",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )

                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue))
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp)
            ) {

                // Buttons
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 18.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    // Total
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "Total Complaints",
                            icon = complaintDataList.filterNot { it.status == ComplaintStatus.COMPLETE.name || it.status == ComplaintStatus.OUTWARDS.name }.size,
                            bgColor = Dark,
                            cardColor = UIBlue,
                            key = ""
                        ),
                        onClick = {
                            sharedViewModel.compliantStatus.value = ComplaintStatus.NOTHING
                            navController.navigate(Routes.TotalComplaintScreen.id)
                        }
                    )
                    // Pending
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "Pending Complaints",
                            icon = complaintDataList.filter { it.status == ComplaintStatus.PENDING.name }.size,
                            bgColor = Dark,
                            cardColor = Green,
                            key = ""
                        ),
                        onClick = {
                            sharedViewModel.compliantStatus.value = ComplaintStatus.PENDING
                            navController.navigate(Routes.TotalComplaintScreen.id)
                        }
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 18.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    // ONGOING
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "Ongoing Complaints",
                            icon = complaintDataList.filter { it.status == ComplaintStatus.ONGOING.name }.size,
                            bgColor = SalmonDark,
                            cardColor = Salmon,
                            key = ""
                        ),
                        onClick = {
                            sharedViewModel.compliantStatus.value = ComplaintStatus.ONGOING
                            navController.navigate(Routes.TotalComplaintScreen.id)
                        }
                    )
                    // COMPLETE
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "Complete Complaints",
                            icon = complaintDataList.filter { it.status == ComplaintStatus.COMPLETE.name }.size,
                            bgColor = DarkYellow,
                            cardColor = Yellow,
                            key = ""
                        ),
                        onClick = {
                            sharedViewModel.compliantStatus.value = ComplaintStatus.COMPLETE
                            sharedViewModel.profileAction.value = ProfileActions.OUTWARD_COMPLAINT
                            navController.navigate(Routes.TotalComplaintScreen.id)
                        }
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 18.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    // OUTWARDS
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "Outwards Complaints",
                            icon = complaintDataList.filter { it.status == ComplaintStatus.OUTWARDS.name }.size,
                            bgColor = Pink40,
                            cardColor = Pink80,
                            key = ""
                        ),
                        onClick = {
                            sharedViewModel.compliantStatus.value = ComplaintStatus.OUTWARDS
                            sharedViewModel.profileAction.value = ProfileActions.OUTWARD_COMPLAINT
                            navController.navigate(Routes.TotalComplaintScreen.id)
                        }
                    )
                    // All Products
                    ActivityCardItems(
                        activityCardData = ActivityCardData(
                            text = "All Products",
                            icon = 0,
                            bgColor = OrangeDark,
                            cardColor = Orange,
                            key = "",
                        ),
                        enableIcon = false,
                        onClick = {
                            sharedViewModel.productAction.value = ProductActions.CREATE
                            navController.navigate(Routes.AllProductsScreen.id)
                        }
                    )
                }

//                // total complaints
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.compliantStatus.value = ComplaintStatus.NOTHING
//                            navController.navigate(Routes.TotalComplaintScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "Total Complaints: ${complaintDataList.filterNot { it.status == ComplaintStatus.COMPLETE.name || it.status == ComplaintStatus.OUTWARDS.name }.size}")
//
//                    }
//
//                }
//                // pending complaints
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.compliantStatus.value = ComplaintStatus.PENDING
//                            navController.navigate(Routes.TotalComplaintScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "Pending Complaints: ${complaintDataList.filter { it.status == ComplaintStatus.PENDING.name }.size}")
//
//                    }
//
//                }

//                // ongoing complaints
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.compliantStatus.value = ComplaintStatus.ONGOING
//                            navController.navigate(Routes.TotalComplaintScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "Ongoing Complaints: ${complaintDataList.filter { it.status == ComplaintStatus.ONGOING.name }.size}")
//
//                    }
//
//                }
//
//                // complete complaints
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.compliantStatus.value = ComplaintStatus.COMPLETE
//                            sharedViewModel.profileAction.value = ProfileActions.OUTWARD_COMPLAINT
//                            navController.navigate(Routes.TotalComplaintScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "Completed Complaints: ${complaintDataList.filter { it.status == ComplaintStatus.COMPLETE.name }.size}")
//
//                    }
//
//                }

//                // outwards complaints
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.compliantStatus.value = ComplaintStatus.OUTWARDS
//                            sharedViewModel.profileAction.value = ProfileActions.OUTWARD_COMPLAINT
//                            navController.navigate(Routes.TotalComplaintScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "Outwards Complaints: ${complaintDataList.filter { it.status == ComplaintStatus.OUTWARDS.name }.size}")
//
//                    }
//
//                }

//                // All products
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 10.dp)
//                        .fillMaxWidth()
//                        .clickable {
//                            sharedViewModel.productAction.value = ProductActions.CREATE
//                            navController.navigate(Routes.AllProductsScreen.id)
//                        },
//                    colors = CardDefaults.cardColors(containerColor = Purple80)
//                ) {
//
//                    Row(modifier = Modifier.padding(20.dp)) {
//
//                        Text(text = "All Products")
//
//                    }
//
//                }
            }


        }

    }

}