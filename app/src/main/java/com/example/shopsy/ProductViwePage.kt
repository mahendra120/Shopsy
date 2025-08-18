package com.example.shopsy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Products
import com.example.shopsy.ui.theme.font2
import com.example.shopsy.ui.theme.font3
import java.io.Serializable

class ProductViwePage : ComponentActivity() {
    var productList: ArrayList<Products> = arrayListOf()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        productList = intent.getSerializableExtra(
            "productList",
            ArrayList::class.java
        ) as? ArrayList<Products> ?: arrayListOf()

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProductListUI() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(productList) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
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
                                fontSize = 21.sp,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 2,
                                fontFamily = font2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (product.brand.isNotEmpty()) {
                                Text(
                                    "Brand: ${product.brand}",
                                    fontSize = 17.sp,
                                    fontFamily = font2,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                "Discount: ${product.discountPercentage}%", fontSize = 17.sp,
                                color =
                                    Color(0, 120, 0),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Price: $${product.price}",
                                fontSize = 15.sp,
                                color = Color(0, 120, 0),
                            )
                            Text(
                                "⭐ ${product.rating ?: 0.0}",
                                color = Color.Black, fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
