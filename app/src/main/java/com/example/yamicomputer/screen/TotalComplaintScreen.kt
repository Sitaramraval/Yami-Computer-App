package com.example.yamicomputer.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yamicomputer.data.ComplaintData
import com.example.yamicomputer.data.ComplaintStatus
import com.example.yamicomputer.data.ProfileActions
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.navigation.Routes.AddComplaintScreen
import com.example.yamicomputer.ui.theme.DarkBlue
import com.example.yamicomputer.ui.theme.LightBlue
import com.example.yamicomputer.ui.theme.Purple40
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalComplaintScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val complaintStatus by sharedViewModel.compliantStatus
    val id by sharedViewModel.id
    val deleteComplaint by sharedViewModel.deleteCompliant

    val complaintDataList = remember {
        mutableStateListOf<ComplaintData>()
    }
    val firebaseDatabase = Firebase.database
    val databaseReference = firebaseDatabase.getReference("customer-complaints")

    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {

        if (deleteComplaint) {
            databaseReference.child(id).removeValue().addOnSuccessListener {
                Toast.makeText(context, "Successfully deleted Complaint!", Toast.LENGTH_SHORT)
                    .show()
                sharedViewModel.deleteCompliant.value = false
            }.addOnCanceledListener {
                Toast.makeText(context, "Error while deleting Complaint!", Toast.LENGTH_SHORT)
                    .show()
                sharedViewModel.deleteCompliant.value = false
            }
        }

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
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

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
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "${
                            if (complaintStatus == ComplaintStatus.NOTHING) "Total" else complaintStatus.name.lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        } Complaint",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue))
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 30.dp),
                onClick = {
                    sharedViewModel.id.value = ""
                    sharedViewModel.complaintData.value = ComplaintData()
                    navController.navigate(AddComplaintScreen.id)
                },
                containerColor = Purple40
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add complaint",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {

            ComplaintListUI(
                complaintDataList,
                complaintStatus,
                sharedViewModel,
                navController,
                profileActions = if (complaintStatus == ComplaintStatus.COMPLETE || complaintStatus == ComplaintStatus.OUTWARDS) ProfileActions.OUTWARD_COMPLAINT else ProfileActions.UPDATE_PROFILE
            )

        }
    }

}

@Composable
fun ComplaintListUI(
    list: SnapshotStateList<ComplaintData>,
    complaintStatus: ComplaintStatus,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    profileActions: ProfileActions = ProfileActions.UPDATE_PROFILE
) {

    var search by rememberSaveable {
        mutableStateOf("")
    }

    Row {
//        TextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = search, onValueChange = { s ->
//                search = s
//            }, trailingIcon = {
//                Icon(
//                    imageVector = Icons.Rounded.Search, contentDescription = "search"
//                )
//            }
//        )
    }

    LazyColumn {

        items(list.reversed().filterComplaintStatus(complaintStatus)) {

            var imageUri by remember {
                mutableStateOf<String?>(null)
            }

            LaunchedEffect(key1 = Unit) {
                imageUri = getImageUrlFromFirebaseStorage("images/${it.photo}")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable {
                        sharedViewModel.id.value = it.complaintId
                        sharedViewModel.complaintData.value = it
                        sharedViewModel.profileAction.value = profileActions
                        navController.navigate(AddComplaintScreen.id)
                    },
                colors = CardDefaults.cardColors(containerColor = LightBlue),
            ) {

                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(if (it.photo.isNotEmpty()) 0.6f else 1f)
                    ) {

                        Text(text = "Name: ${it.name}")
                        Text(text = "Date: ${it.date}")
                        Text(text = "Item: ${it.item}")
                        Text(text = "Problem: ${it.problem}")
                        Text(text = "Status: ${it.status}")

                    }

                    if (it.photo.isNotEmpty()) {
                        imageUri?.let { uri ->
                            val painter = rememberImagePainter(uri)
                            Card(
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(8.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Image(
                                    painter = painter,
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }

            }
        }
    }

}

fun List<ComplaintData>.filterComplaintStatus(complaintStatus: ComplaintStatus): List<ComplaintData> {

    return if (complaintStatus != ComplaintStatus.NOTHING) this.filter { it.status == complaintStatus.name } else this.filterNot { it.status == ComplaintStatus.COMPLETE.name || it.status == ComplaintStatus.OUTWARDS.name }

}