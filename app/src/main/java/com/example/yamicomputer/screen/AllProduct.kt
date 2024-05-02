package com.example.yamicomputer.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.yamicomputer.data.ProductActions
import com.example.yamicomputer.data.ProductData
import com.example.yamicomputer.data.ProductStatus
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.navigation.Routes
import com.example.yamicomputer.ui.theme.DarkBlue
import com.example.yamicomputer.ui.theme.LightBlue
import com.example.yamicomputer.ui.theme.Purple40
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    val id by sharedViewModel.productID
    val deleteProduct by sharedViewModel.deleteProduct

    val productDataList = remember {
        mutableStateListOf<ProductData>()
    }
    val firebaseDatabase = Firebase.database
    val databaseReference = firebaseDatabase.getReference("all-products")

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {

        // delete product
        if (deleteProduct) {
            databaseReference.child(id).removeValue().addOnSuccessListener {
                Toast.makeText(context, "Successfully deleted Complaint!", Toast.LENGTH_SHORT)
                    .show()
                sharedViewModel.deleteProduct.value = false
            }.addOnCanceledListener {
                Toast.makeText(context, "Error while deleting Complaint!", Toast.LENGTH_SHORT)
                    .show()
                sharedViewModel.deleteProduct.value = false
            }
        }

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot, previousChildName: String?
            ) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our  list
                val data = snapshot.getValue(ProductData::class.java)!!
                productDataList.add(data)
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


    var selectedProductStatus by remember {
        mutableStateOf(ProductStatus.NOTHING)
    }

    var selectedProductStatusExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                text = "All Products",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )

                        }

                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 30.dp),
                onClick = {
                    sharedViewModel.productID.value = ""
                    sharedViewModel.productData.value = ProductData()
                    sharedViewModel.productAction.value = ProductActions.CREATE
                    navController.navigate(Routes.AddProductScreen.id)
                },
                containerColor = Purple40
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add product",
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

            Column {
                Text(
                    text = "Sort By: ${selectedProductStatus.name}",
                    modifier = Modifier
                        .clickable {
                            selectedProductStatusExpanded = true
                        }
                        .padding(top = 10.dp, start = 15.dp),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                DropdownMenu(expanded = selectedProductStatusExpanded, onDismissRequest = {
                    selectedProductStatusExpanded = false
                }) {

                    ProductStatus.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                selectedProductStatus = it
                                selectedProductStatusExpanded = false
                            })
                    }

                }
            }

            ProductListUI(
                list = productDataList,
                sharedViewModel = sharedViewModel,
                navController = navController,
                productStatus = selectedProductStatus
            )

        }
    }

}

@Composable
fun ProductListUI(
    list: SnapshotStateList<ProductData>,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    productStatus: ProductStatus
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        list.reversed().filterProductStatus(productStatus).forEach {

            var imageUri by remember {
                mutableStateOf<String?>(null)
            }

            LaunchedEffect(key1 = Unit) {
                if (it.photo.isNotEmpty()) {
                    imageUri = getImageUrlFromFirebaseStorage("images/${it.photo}")
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable {
                        sharedViewModel.productID.value = it.productID
                        sharedViewModel.productData.value = it
                        Log.d("product-data", it.toString())
                        sharedViewModel.productAction.value = ProductActions.UPDATE
                        navController.navigate(Routes.AddProductScreen.id)
                    },
                colors = CardDefaults.cardColors(containerColor = LightBlue),
            ) {


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (it.photo.isNotEmpty()) {
                        imageUri?.let { uri ->
                            val painter = rememberImagePainter(uri)
                            Card(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(170.dp)
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

                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                    ) {

                        Text(text = "Name: ${it.name}")
                        Text(text = "Date: ${it.date}")
                        Text(text = "Product: ${it.product}")
                        Text(text = "Description: ${it.description}")
                        Text(text = "Status: ${it.status}")
                    }

                }

            }
        }
    }
}


fun List<ProductData>.filterProductStatus(productStatus: ProductStatus): List<ProductData> {

    return if (productStatus != ProductStatus.NOTHING) this.filter { it.status == productStatus.name } else this

}
