package com.example.yamicomputer.screen

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yamicomputer.data.DealerNames
import com.example.yamicomputer.data.ProductData
import com.example.yamicomputer.data.ProductStatus
import com.example.yamicomputer.data.stringToDealerName
import com.example.yamicomputer.data.stringToProductStatus
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.ui.theme.BrightRed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current

    val smsPermissionState = rememberPermissionState(
        permission = Manifest.permission.SEND_SMS
    )

    var smsPermissionClicked by remember {
        mutableStateOf(false)
    }

    //define permission in composable fun
    val getPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
//        if (isGranted) {
//            //permission accepted do something
//            Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show()
//        } else {
//            //permission not accepted show message
//            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
//        }
    }

    SideEffect {
        getPermission.launch(Manifest.permission.SEND_SMS)
    }

//    val permissionResult = (
//        permissionState = smsPermissionState
//    )
    LaunchedEffect(smsPermissionState) {
        getPermission.launch(Manifest.permission.SEND_SMS)
    }

    val id by sharedViewModel.productID

    /** we are checking if id is empty or not, here if the id is empty
    then this screen will be used to create new Complaint and
    if id is not empty then this screen will be used to update an existing complaint.
    This variable gets the ID of the existing complaint. **/
    var idNotEmpty by remember {
        mutableStateOf(id.isNotEmpty())
    }

    idNotEmpty = id.isNotEmpty()

    val productData by sharedViewModel.productData

    var name by rememberSaveable {
        mutableStateOf(productData.name)
    }

    var mobileNo by rememberSaveable {
        mutableStateOf(productData.mobileNo)
    }

    var date by rememberSaveable {
        mutableStateOf(productData.date)
    }

    var product by rememberSaveable {
        mutableStateOf(productData.product)
    }

    var description by rememberSaveable {
        mutableStateOf(productData.description)
    }

    var productStatus by rememberSaveable {
        mutableStateOf(productData.status.stringToProductStatus())
    }

    var productStatusClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var price by rememberSaveable {
        mutableStateOf(productData.price)
    }

    var photo by rememberSaveable {
        mutableStateOf(productData.photo)
    }

    var collectorName by rememberSaveable {
        mutableStateOf(productData.collectorName)
    }

    var collectionDate by rememberSaveable {
        mutableStateOf(productData.collectionDate)
    }

    var dealerName by rememberSaveable {
        mutableStateOf(productData.dealerName.stringToDealerName())
    }

    var dealerNameClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var addComplaintClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var deleteClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var imageUri by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        imageUri = getImageUrlFromFirebaseStorage("images/$photo")
    }

    // on below line creating variable for freebase database
    // and database reference.
//    val firebaseDatabase = FirebaseDatabase.database
    val database = Firebase.database
    val databaseReference = database.getReference("all-products")

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "back",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )

                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (!smsPermissionState.status.isGranted) {
                Button(onClick = {
                    smsPermissionClicked = true
                }) {
                    Text(text = "Request SMS Permission")
                }
            }

            if (!idNotEmpty) {
                ImagePickerScreen(selectedImageUriListener = {
                    selectedImageUri = it
                })
            } else {

                imageUri?.let { uri ->
                    val painter = rememberImagePainter(uri)
                    Image(
                        painter = painter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                }
            }

            // Name
            RegularTextField(
                textFieldName = "Name",
                value = name,
                onTextChange = {
                    name = it
                }
            )

            // Mobile Number
            RegularTextField(
                textFieldName = "Mobile Number",
                value = mobileNo,
                onTextChange = {
                    mobileNo = it
                },
                keyboardType = KeyboardType.Number
            )

            // date
            RegularTextField(
                textFieldName = "Date",
                value = date,
                onTextChange = {
                    date = it
                }
            )

            // item
            RegularTextField(
                textFieldName = "Product",
                value = product,
                onTextChange = {
                    product = it
                }
            )

            // problem
            RegularTextField(
                textFieldName = "Description",
                value = description,
                onTextChange = {
                    description = it
                }
            )


            // complaint Status
            RegularTextField(
                textFieldName = "Product Status",
                value = productStatus.name,
                onTextChange = {

                },
                enable = false,
                onClick = {
                    productStatusClicked = !productStatusClicked
                }
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    expanded = productStatusClicked,
                    onDismissRequest = {
                        productStatusClicked = false
                    }) {
                    ProductStatus.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.name, color = Color.Black)
                            },
                            onClick = {
                                productStatus = it
                                productStatusClicked = false
                            },
                        )
                    }
                }
            }

            // charge
            RegularTextField(
                textFieldName = "Prize",
                value = price,
                onTextChange = {
                    price = it
                }
            )

            // collector name
            RegularTextField(
                textFieldName = "Collector Name",
                value = collectorName,
                onTextChange = {
                    collectorName = it
                }
            )

            // collection date
            RegularTextField(
                textFieldName = "Collection Date",
                value = collectionDate,
                onTextChange = {
                    collectionDate = it
                }
            )

            // Dealer Name
            RegularTextField(
                textFieldName = "Dealer Name",
                value = dealerName.name,
                onTextChange = {

                },
                enable = false,
                onClick = {
                    dealerNameClicked = !dealerNameClicked
                }
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    expanded = dealerNameClicked,
                    onDismissRequest = {
                        dealerNameClicked = false
                    }) {
                    DealerNames.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.name, color = Color.Black)
                            },
                            onClick = {
                                dealerName = it
                                dealerNameClicked = false
                            },
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(
                        top = 30.dp,
                        start = 30.dp,
                        end = 30.dp,
                        bottom = if (idNotEmpty) 0.dp else 100.dp
                    )
                    .width(270.dp),
                onClick = {
                    addComplaintClicked = true
                },
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "${if (idNotEmpty) "Update" else "Add"} Product",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

            }

            if (idNotEmpty) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 100.dp, top = 20.dp)
                        .clickable {
                            deleteClicked = true
                        },
                    text = "Delete Product",
                    color = BrightRed,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }

    if (deleteClicked) {
        AlertDialog(
            onDismissRequest = {
                deleteClicked = false
            },
            confirmButton = {
                Button(onClick = {
                    sharedViewModel.id.value = id
                    sharedViewModel.deleteCompliant.value = true
                    navController.popBackStack()
                    deleteClicked = false
                }) {
                    Text(text = "Confirm")

                }
            },
            dismissButton = {
                Button(onClick = { deleteClicked = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Delete the Product!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }, text = {
                Text(text = "Are you sure to delete the Product? Once deleted the action can not be reversed, resulting permanent deletion of the Product. \nTo delete click confirm")
            }
        )
    }

    if (addComplaintClicked) {
        LaunchedEffect(key1 = Unit) {

            if (smsPermissionState.status.isGranted) {


                val newRef =
                    if (idNotEmpty) databaseReference.child(id) else databaseReference.push()

                selectedImageUri?.let {
                    uploadPhotoToFirebase(
                        context = context,
                        imageUri = it,
                        onImageUploadSuccessListener = { photoPath ->

                            // on below line we are adding data.
                            val complaintDataEdited = ProductData(
                                name = name,
                                date = date,
                                product = product,
                                description = description,
                                price = price,
                                photo = photoPath,
                                status = productStatus.name,
                                productID = newRef.key ?: "error while adding product!",
                                mobileNo = mobileNo,
                                collectorName = collectorName,
                                collectionDate = collectionDate,
                                dealerName = dealerName.name
                            )

                            newRef.setValue(complaintDataEdited)
                            Log.d("error-firebase-database", "onCancelled:")
                            databaseReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    // Data has changed
                                    Toast.makeText(
                                        context,
                                        "Product ${if (idNotEmpty) "Updated" else "Created"}!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    if (!idNotEmpty) {
                                        name = ""
                                        date = ""
                                        product = ""
                                        description = ""
                                        price = ""
                                        photo = ""
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

                        }
                    )}

                addComplaintClicked = false
            } else {
                Toast.makeText(context, "SMS Permission not granted!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> uri?.let { onImageSelected(it) } }
    )

    Button(
        onClick = { launcher.launch("image/*") }
    ) {
        Text("Select Image")
    }
}