package com.example.shopsy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Product
import com.example.shopsy.Data.Products
import com.example.shopsy.ui.theme.font2
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    var apiResponse by mutableStateOf("")
    var productList = mutableStateListOf<Products>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchData()
        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(), containerColor = Color.White
            ) { innerPadding ->
                if (productList.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color.White)
                    ) {
                        UI()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "loading...",
                            fontSize = 35.sp,
                            fontFamily = font2,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding()
                        )
                    }
                }
            }
        }
    }

    fun fetchData() {
        val url = "https://dummyjson.com/products"
        val stringRequest =
            object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                Log.d("65689", "fetchData: $response")
                apiResponse = response
                var userMode: Product = Gson().fromJson(apiResponse, Product::class.java)
                productList.addAll(userMode.products)
                Log.d("89562132", "fetchData: $productList")
            }, Response.ErrorListener {
                Log.d("=====", "error: ${it.localizedMessage}")
                apiResponse = "That didn't work!"
            }) {}
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Preview(showSystemUi = true)
    @Composable
    fun UI() {
        LazyColumn {
            items(productList.size) { index ->
                var post = productList[index]
                Card(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        GlideImage(
                            model = post.thumbnail,
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp)
                        ) {
                            // Title
                            Text(
                                text = post.title,
                                fontSize = 19.sp,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            // Ratings Row
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "⭐ ${post.rating}",
                                    color = Color.Black, fontSize = 15.sp
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("(3,13,748)")
                                Spacer(Modifier.width(4.dp))
                                Text("Assured", color = Color.Blue)
                            }
                            Spacer(Modifier.height(4.dp))
                            // Price Row
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${post.discountPercentage}%",
                                    color = Color(0, 120, 0),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "SALE",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0, 120, 0),
                                )
                                Log.d("9090==", "UI: ${post.price}")
                            }
                            Spacer(Modifier.height(3.dp))
                            Text("₹ ${post.price}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }
}
