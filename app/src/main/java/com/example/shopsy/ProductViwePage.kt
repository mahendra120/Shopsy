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
import androidx.compose.material.icons.filled.AllInbox
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import kotlin.math.roundToInt

class ProductViwePage : ComponentActivity(), PaymentResultListener {
    lateinit var product: Products
    var name = ""

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        product = intent.getSerializableExtra("product", Products::class.java) ?: Products()
        name = intent.getStringExtra("name") ?: "unknown"

        setContent {
            ShopsyTheme {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { TopAppbar(scrollBehavior) },
                    bottomBar = {
                        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { doPurchase(product.price!!.toFloat() * 100) },
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            color = Color(0xFF4F46E5),
                            shape = RoundedCornerShape(12.dp)
                        ),
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
                Column(modifier = Modifier.padding(start = 2.dp)) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFF4F46E5))) {
                                append("Product : ")
                            }
                            append(name)
                        },
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 10.dp, start = 9.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    Row {
                        if (product.brand != "") {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle()) {
                                        append("Brand : ")
                                    }
                                    append(if (product.brand != "") product.brand else "Unknown")
                                },
                                fontSize = 17.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 9.dp)
                            )
                        } else {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle()) {
                                        append("Brand : ")
                                    }
                                    append("Unknown")
                                },
                                fontSize = 17.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 9.dp)
                            )
                        }
                        Text(
                            " | ⭐️ ${product.rating} Rating",
                            fontSize = 17.sp,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(modifier = Modifier.padding(start = 10.dp, top = 5.dp)) {
                        Text(
                            buildAnnotatedString {
                                append("Discount : ")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append("${product.discountPercentage}% ")
                                }
                            },
                            fontSize = 17.sp,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            buildAnnotatedString {
                                append("| Price : ")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append("$ ${product.price} ")
                                }
                            },
                            fontSize = 17.sp,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Quantity + Availability
                    Row(modifier = Modifier.padding(top = 5.dp, start = 9.dp)) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle()) {
                                    append("Quantity : ")
                                }
                                append("${product.stock}")
                            },
                            fontSize = 17.sp,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            Icons.Default.ShoppingCartCheckout,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(.6f)
                        )
                        Text(
                            product.availabilityStatus,
                            fontSize = 15.sp,
                            color = Color(197, 175, 24, 255),
                            fontFamily = font4,
                            modifier = Modifier.padding(start = 5.dp, top = 1.dp)
                        )
                        Text(
                            "| weight: ${product.weight}g",
                            fontSize = 17.sp,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    // Shipping
                    Row(modifier = Modifier.padding(top = 5.dp, start = 10.dp)) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle()) {
                                    append("Shipping : ")
                                }
                                append(product.shippingInformation)
                            },
                            fontSize = 17.sp,
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
                        buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("Warranty : ")
                            }
                            append(product.warrantyInformation)
                        },
                        fontSize = 17.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 3.dp, start = 9.dp)
                    )
                    Row {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle()) {
                                    append("MinimumOrderQuantity : ")
                                }
                                append("${product.minimumOrderQuantity}")
                            },
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = font4,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, start = 9.dp)
                        )
                        Icon(
                            Icons.Default.AllInbox,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 10.dp),
                            tint = Color.Gray
                        )
                    }
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("ReturnPolicy : ")
                            }
                            append(product.returnPolicy)
                        },
                        fontSize = 14.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 3.dp, start = 9.dp)
                    )
                }
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
                Spacer(modifier = Modifier.padding(top = 15.dp))
                // Reviews Header
                Text(
                    text = "Customer Reviews",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp, top = 15.dp)
                )
                product.reviews.forEach { review ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = review.reviewerName,
                                    fontFamily = font4,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "⭐ ${review.rating}", fontSize = 14.sp)
                            }
                            Text(
                                text = review.comment,
                                fontSize = 15.sp,
                                fontFamily = font4,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = review.date.take(10),
                                fontSize = 12.sp,
                                fontFamily = font4,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Text(
                    "More Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Tags: ${product.tags.joinToString()}")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppbar(scrollBehavior: TopAppBarScrollBehavior) {
        TopAppBar(
            title = {
                if (product.brand != "") {
                    Text(
                        " ${product.brand}",
                        fontSize = 30.sp,
                        color = Color(0xFF4F46E5),
                        fontFamily = font4,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                } else {
                    Text(
                        "Unknown",
                        fontSize = 30.sp,
                        color = Color(0xFF4F46E5),
                        fontFamily = font4,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

    private fun doPurchase(amount: Float) {
        val amountPaise = (amount * 100)
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_Dd5eud3a91mnti")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", "First Payment")
            jsonObject.put("description", "Test payment compose Integration")
            jsonObject.put("currency", "USD")
            jsonObject.put("amount", amount)
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
        Log.d("=====", "onPaymentError: p0 :: $p0")
        Log.d("=====", "onPaymentError: p1 :: $p1")
        Toast.makeText(this, "Payment Failed ❌", Toast.LENGTH_SHORT).show()
    }
}
