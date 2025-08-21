package com.example.shopsy

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Products
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

// 👇 Import your theme + font
import com.example.shopsy.ui.theme.ShopsyTheme
import com.example.shopsy.ui.theme.font4

class ProductViwePage : ComponentActivity(), PaymentResultListener {
    lateinit var product: Products
    var name = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        product = intent.getSerializableExtra("product", Products::class.java) ?: Products()
        name = intent.getStringExtra("name") ?: ""

        setContent {
            // 👇 Wrap with your Theme
            ShopsyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppbar() },
                    bottomBar = {
                        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { doPurchase(100) },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    "Buy Now",
                                    fontSize = 22.sp,
                                    fontFamily = font4,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Product()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Preview(showSystemUi = true)
    @Composable
    fun Product() {
        var expanded by remember { mutableStateOf(false) }
        Log.d("============", "Product: $product")

        LazyColumn {
            item {
                // Product Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp,color = MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.TopCenter
                ) {
                    GlideImage(
                        model = product.thumbnail,
                        contentDescription = product.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(400.dp)
                            .padding(10.dp)
                            .clip(MaterialTheme.shapes.large),
                    )
                }

                // Title
                Text(
                    "Product : $name",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 10.dp, start = 9.dp)
                )

                Spacer(modifier = Modifier.padding(top = 5.dp))

                // Brand + Rating
                Row {
                    Text(
                        "Brand : ${product.brand} | ",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 9.dp)
                    )
                    Text(
                        "⭐️ ${product.rating} Rating",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Discount + Price
                Row(modifier = Modifier.padding(start = 10.dp, top = 5.dp)) {
                    Text(
                        "Category : ${product.discountPercentage}% | ",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Price : $ ${product.price}",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quantity + Availability
                Row(modifier = Modifier.padding(top = 5.dp, start = 9.dp)) {
                    Text(
                        "Quantity : ${product.stock}",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        Icons.Default.ShoppingCartCheckout,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 7.dp, top = 3.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(.6f)
                    )
                    Text(
                        product.availabilityStatus,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = font4,
                        modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                    )
                }

                // Shipping
                Row(modifier = Modifier.padding(top = 5.dp, start = 10.dp)) {
                    Text(
                        "Shipping : ${product.shippingInformation}",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 7.dp, top = 3.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(.6f)
                    )
                }

                // Warranty
                Text(
                    "Warranty : ${product.warrantyInformation}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, start = 9.dp)
                )

                // Expandable Description
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clickable { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text(
                                text = "Description",
                                fontSize = 18.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (expanded) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = null)
                            } else {
                                Icon(Icons.Default.ArrowDownward, contentDescription = null)
                            }
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(
                                product.description,
                                fontSize = 15.sp,
                                fontFamily = font4,
                                color = MaterialTheme.colorScheme.onSurface.copy(.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 5.dp, start = 9.dp)
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
                    " ${product.brand}",
                    fontSize = 25.sp,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
        )
    }

    private fun doPurchase(amount: Int) {
        val amountPaise = Math.round(amount.toFloat() * 100)
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_CKxIMtjjMGCpJo")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", "First Payment")
            jsonObject.put("description", "Test payment compose Integration")
            jsonObject.put("currency", "INR")
            jsonObject.put("amount", amountPaise)
            jsonObject.put("prefill.contact", "9106597990")
            jsonObject.put("prefill.email", "test@gmail.com")

            Log.d("900904", "doPurchase: $amountPaise")
            checkout.open(this@ProductViwePage, jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success ✅", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed ❌", Toast.LENGTH_SHORT).show()
    }
}
