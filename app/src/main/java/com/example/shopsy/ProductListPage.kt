package com.example.shopsy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Products
import com.example.shopsy.ui.theme.ShopsyTheme
import com.example.shopsy.ui.theme.font4

class ProductListPage : ComponentActivity() {
    var productList: ArrayList<Products> = arrayListOf()
    var name = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        productList = intent.getSerializableExtra(
            "productList", ArrayList::class.java
        ) as? ArrayList<Products> ?: arrayListOf()

        name = intent.getStringExtra("name") ?: ""
        setContent {
            ShopsyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppbar()
                    }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ProductListUI()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProductListUI() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)
        ) {
            items(productList) { product ->
                Card(
                    onClick = {
                        val intent = Intent(this@ProductListPage, ProductViwePage::class.java)
                        intent.putExtra("product", product)
                        intent.putExtra("name", product.title)
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            model = product.thumbnail,
                            contentDescription = product.title,
                            modifier = Modifier.size(90.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = product.title,
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 2,
                                fontFamily = font4,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (product.brand.isNotEmpty()) {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground,)) {
                                            append("Brand: ")
                                        }
                                        append("${product.brand}")
                                    },
                                    fontFamily = font4,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(253,253,0)
                                )
                            }
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                        append("Discount: ")
                                    }
                                    append("${product.discountPercentage}%")
                                },
                                fontFamily = font4,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                        append("Price: ")
                                    }
                                    append("$${product.price}")
                                },
                                fontFamily = font4,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "⭐ ${product.rating ?: 0.0}",
                                color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppbar() {
        TopAppBar(
            title = {
                Text(
                    name,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = font4,
                    fontWeight = FontWeight.ExtraLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    finish()
                }, modifier = Modifier.padding(start = 5.dp)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {},
        )
    }
}


