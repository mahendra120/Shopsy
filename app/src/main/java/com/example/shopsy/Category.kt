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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.shopsy.ui.theme.font3
import com.google.gson.Gson

class Category : ComponentActivity() {
    //    val productList = ArrayList<Products>()
//    var categoryList = mutableStateListOf<String>()

    val productList = mutableStateListOf<Products>()
    var mainProductList = ArrayList<Products>(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchData()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    categoryScreen(modifier = Modifier)
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun categoryScreen(modifier: Modifier) {
        if (productList.isNotEmpty()) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(productList.size) { index ->
                    val product = productList[index]
                    Card(
                        onClick = {
                            var list =
                                ArrayList(mainProductList.filter { it.category == product.category })
                            Log.d("=====", "categoryScreen: $list")
                            var intent = Intent(this@Category, ProductViwePage::class.java)
                            intent.putExtra("productList", list)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        Column(modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                GlideImage(product.thumbnail, contentDescription = null)
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(product.category, fontFamily = font3, fontSize = 19.sp)
                                    Text(
                                        "${product.discountPercentage}% Discount",
                                        fontFamily = font2,
                                        color = Color(0, 100, 0),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
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

    fun fetchData() {
        val url = "https://dummyjson.com/products?limit=194"
        val stringRequest =
            StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                var apiResponse = response
                val productMode = Gson().fromJson(response.toString(), Product::class.java)
                productList.clear()
//                productList.addAll(productMode.products)
//                    way - 1
//                productList.forEach {
//                    if (!categoryList.contains(it.category)) {
//                        categoryList.add(it.category)
//                    }
//                }
                //way - 2
//                productList.addAll(mainProductList.filter {
//                    productList.find { p -> p.category == it.category } == null
//                })
                //way - 3
                mainProductList = productMode.products
                productList.addAll(mainProductList.distinctBy { it.category })

            }, Response.ErrorListener {
                Log.d("=====", "fetchData: That didn't work!")
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}
