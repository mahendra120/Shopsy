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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
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
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

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
            Scaffold(
                modifier = Modifier.Companion.fillMaxSize(),
                topBar = { TopAppbar() }, bottomBar = {
                    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                doPurchase(100)
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0, 0, 139))
                        )
                        {
                            Text(
                                "Buy Now",
                                fontSize = 22.sp,
                                fontFamily = font4,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                })
            { innerPadding ->
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
        var expanded by remember { mutableStateOf(false) }

        Log.d("============", "Product: $product")
        LazyColumn {
            item {
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
                            .size(400.dp)
                            .padding(10.dp)
                            .clip(MaterialTheme.shapes.large),
                    )
                }
                Text(
                    "Product : $name",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 10.dp, start = 9.dp)
                )
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Row(modifier = Modifier) {
                    Text(
                        "Brand : ${product.brand} | ",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp, start = 9.dp)
                    )
                    Text(
                        "⭐️ ${product.rating} Rating",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp, start = 2.dp)
                    )
                }
                Row(modifier = Modifier.padding(start = 10.dp, top = 5.dp)) {
                    Text(
                        "Category : ${product.discountPercentage}% | ",
                        fontSize = 18.sp,
                        color = Color(0, 100, 0),
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp, start = 0.dp)
                    )

                    Text(
                        "Price : $ ${product.price}",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp, start = 0.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Row(modifier = Modifier.padding()) {
                    Text(
                        "Quantity : ${product.stock}",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp, start = 9.dp)
                    )
                    Icon(
                        Icons.Default.ShoppingCartCheckout,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 7.dp, top = 3.dp),
                        tint = Color.DarkGray.copy(.6f)
                    )
                    Text(
                        "${product.availabilityStatus}",
                        fontSize = 15.sp,
                        color = Color(0, 0, 180),
                        fontFamily = font4,
                        modifier = Modifier.padding(top = 4.dp, start = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Row(modifier = Modifier.padding()) {
                    Text(
                        "Shipping : ${product.shippingInformation}",
                        fontSize = 18.sp,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Icon(
                        Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 7.dp, top = 3.dp),
                        tint = Color.DarkGray.copy(.6f)
                    )
                }
                Text(
                    "Warranty : ${product.warrantyInformation}", fontSize = 18.sp,
                    color = Color.Black,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp, start = 9.dp)
                )
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
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (expanded) {
                                Icon(
                                    Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(
                                "${product.description}",
                                fontSize = 15.sp,
                                fontFamily = font4,
                                color = Color.DarkGray,
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
                    modifier = Modifier.padding(top = 0.dp, start = 9.dp)
                )
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

    private fun doPurchase(amount: Int) {
        val amount = Math.round(amount.toFloat() * 100)
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_CKxIMtjjMGCpJo")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", "First Payment")
            jsonObject.put("description", "Test payment compose Integration")
            jsonObject.put("currency", "INR")
            jsonObject.put("amount", amount)
            jsonObject.put("prefill.contact", "9106597990")
            jsonObject.put("prefill.email", "test@gmail.com")

            Log.d("900904", "doPurchase: $amount")
            // open razorpay to checkout activity
            checkout.open(this@ProductViwePage, jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this@ProductViwePage, "onPaymentSuccess", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this@ProductViwePage, "onPaymentError", Toast.LENGTH_SHORT).show()
    }
}