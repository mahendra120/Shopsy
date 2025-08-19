package com.example.shopsy

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.shopsy.Data.Products

class ProductViwePage : ComponentActivity() {
    lateinit var product: Products

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        product = intent.getSerializableExtra("product", Products::class.java) ?: Products()
        setContent {
            Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
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

    @Preview(showSystemUi = true)
    @Composable
    fun Product() {
        // jo ama khali show karvanu chhe dhyan rakhje
        Log.d("============", "Product: $product")

    }
}