package com.example.shopsy

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
                                doPurchase("${product.price}")
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 15.dp, top = 15.dp),
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
        // jo ama khali show karvanu chhe dhyan rakhje
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
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 10.dp, start = 9.dp)
                )
                Spacer(modifier = Modifier.padding(top = 0.dp))
                Text(
                    "Brand : ${product.brand}",
                    fontSize = 19.sp,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 0.dp, start = 9.dp)
                )
                Row(modifier = Modifier.padding(top = 0.dp)) {
                    Text(
                        "⭐️ ${product.rating} Very Good",
                        fontSize = 17.sp,
                        color = Color.Black,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 5.dp, start = 9.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 0.dp))
                    Text(
                        "${product.availabilityStatus}",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp, start = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = 0.dp))
                Row(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        "Category : ${product.discountPercentage}%",
                        fontSize = 20.sp,
                        color = Color(0, 100, 0),
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 5.dp, start = 0.dp)
                    )
                    Spacer(Modifier.padding(7.dp))
                    Text(
                        "Price : $ ${product.price}",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = font4,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 5.dp, start = 0.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = 3.dp))
                Text(
                    "Quantity : ${product.minimumOrderQuantity}",
                    fontSize = 21.sp,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 0.dp, start = 9.dp)
                )
                Spacer(modifier = Modifier.padding(top = 3.dp))
                Text(
                    "Shipping : ${product.shippingInformation}",
                    fontSize = 19.sp,
                    fontFamily = font4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Text(
                    "Description : ${product.description}",
                    fontSize = 19.sp,
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
                Text(
                    "Brand : ${product.brand}",
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

    private fun doPurchase(amount: String) {

        val amount = Math.round(amount.toFloat() * 100)

        val checkout = Checkout()

        checkout.setKeyID("rzp_test_CKxIMtjjMGCpJo")

        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", "First Payment")
            jsonObject.put("description", "Test payment compose Integration")
            jsonObject.put("theme.color", "")
            jsonObject.put("currency", "INR")
            jsonObject.put("amount", amount)
            jsonObject.put("prefill.contact", "9106597990")
            jsonObject.put("prefill.email", "test@gmail.com")

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