package com.example.shopsy

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Products
import com.example.shopsy.ui.theme.font4

class ProductViwePage : ComponentActivity() {
    lateinit var product: Products
    var name = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        product = intent.getSerializableExtra("product", Products::class.java) ?: Products()
        name = intent.getStringExtra("name") ?: ""
        setContent {
            Scaffold(
                modifier = Modifier.Companion.fillMaxSize(),
                topBar = { TopAppbar() }) { innerPadding ->
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                {
                    Product()
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Preview(showSystemUi = true)
    @Composable
    fun Product() {
        // jo ama khali show karvanu chhe dhyan rakhje
        Log.d("============", "Product: $product")
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.TopCenter
            ) {
                GlideImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(300.dp)
                        .padding(10.dp)
                        .clip(MaterialTheme.shapes.large),
                )
            }
            Text(
                "Product : $name",
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = font4,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 10.dp, start = 9.dp)
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Text(
                "⭐️ ${product.rating} Very Good",
                fontSize = 17.sp,
                color = Color.Black,
                fontFamily = font4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp, start = 9.dp)
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Row(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    "Category : ${product.discountPercentage}%",
                    fontSize = 19.sp,
                    color = Color(0, 100, 0),
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp, start = 9.dp)
                )
                Text(
                    "Price : $ ${product.price}",
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp, start = 9.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppbar() {
        TopAppBar(
            title = {
            },
            navigationIcon = {
                IconButton(onClick = {
                    finish()
                }, modifier = Modifier.padding(start = 5.dp))
                {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {},
        )
    }
}