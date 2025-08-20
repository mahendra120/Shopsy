package com.example.shopsy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.shopsy.ui.theme.font4
import com.google.gson.Gson


class Category : ComponentActivity() {
    //    val productList = ArrayList<Products>()
//    var categoryList = mutableStateListOf<String>()


    val productList = mutableStateListOf<Products>()
    var mainProductList = ArrayList<Products>(0)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchData()
        setContent {
            Scaffold(
                topBar = {
                    topbar()
                }) { innerPadding ->
                mySearchBar()
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
                            var intent = Intent(this@Category, ProductListPage::class.java)
                            intent.putExtra("productList", list)
                            intent.putExtra("name", product.category)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                                    Text(
                                        product.category,
                                        fontFamily = font4,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                                    )
                                    Text(
                                        "${product.discountPercentage}% Discount",
                                        fontFamily = font4,
                                        color = Color(0, 100, 0),
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
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
                    fontFamily = font4,
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
            StringRequest(Request.Method.GET, url, { response ->
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

            }, {
                Log.d("=====", "fetchData: That didn't work!")
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topbar() {
        TopAppBar(
            title = {
                Text("Shopsy")
            },
            actions = {},
        )
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @Composable
    fun mySearchBar() {
        var searchQuery by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }
        val filteredItems =
            productList.filter { it.title.contains(searchQuery, ignoreCase = true) }
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier
//                .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            placeholder = { Text("Search", fontFamily = font4) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            trailingIcon = {
                if (active)
                    IconButton(onClick = {
                        searchQuery = ""
                        active = false
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null
                        )
                    }
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            tonalElevation = 0.dp,
        ) {
            LazyColumn {
                item {
                    filteredItems.forEach { index ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = {
                                val product = index
                                var list =
                                    ArrayList(mainProductList.filter { it.category == product.category })
                                Log.d("=====", "categoryScreen: $list")
                                var intent = Intent(this@Category, ProductListPage::class.java)
                                intent.putExtra("productList", list)
                                intent.putExtra("name", product.category)
                                startActivity(intent)
                            }
                        ) {
                            Text(
                                index.title,
                                fontSize = 15.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                            )
                            Text(
                                index.category,
                                fontSize = 12.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
