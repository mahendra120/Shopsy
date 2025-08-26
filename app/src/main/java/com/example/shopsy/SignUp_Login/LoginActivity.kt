package com.example.shopsy.SignUp_Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.shopsy.Category
import com.example.shopsy.R
import com.example.shopsy.RoomDatabase.AppDatabase
import com.example.shopsy.ui.theme.quicksand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {
    val sp by lazy {
        getSharedPreferences("lecture", MODE_PRIVATE)
    }
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val emailcheck = sp.getString("email", "") ?: ""
        val passwordcheck = sp.getString("password", "") ?: ""

        Log.d("90909090", "onCreate: $emailcheck $passwordcheck")
        if (emailcheck.isNotEmpty() && passwordcheck.isNotEmpty()) {
            val intent = Intent(this@LoginActivity, Category::class.java)
            startActivity(intent)
        }
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                LoginScrenn()
            }
        }
    }

    @Composable
    @Preview(showSystemUi = true)
    fun LoginScrenn() {
        Image(
            painter = painterResource(R.drawable.purpalcolorbackgrung),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Login",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(253, 253, 253),
                fontFamily = quicksand, modifier = Modifier.padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 35.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "email", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF341539),
                    cursorColor = Color(253, 221, 94, 250),
                    unfocusedContainerColor = Color(0, 0, 0),
                    focusedTextColor = Color(253, 253, 253),
                    unfocusedTextColor = Color(253, 253, 253),
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "password", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF341539),
                    cursorColor = Color(253, 221, 94, 250),
                    unfocusedContainerColor = Color(0, 0, 0),
                    focusedTextColor = Color(253, 253, 253),
                    unfocusedTextColor = Color(253, 253, 253),
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 27.dp))
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val user = AppDatabase.getInstance(this@LoginActivity)
                                .userDao()
                                .isUserFound(email, password)
                            sp?.edit()?.apply {
                                putString("email", email)
                                putString("password", password)
                                apply()
                            }
                            withContext(Dispatchers.Main) {
                                if (user != null) {
                                    val intent = Intent(this@LoginActivity, Category::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this@LoginActivity, "Login", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "enter valid email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "User not found!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.width(220.dp),
                border = BorderStroke(1.dp, color = Color.White),
                colors = ButtonDefaults.buttonColors(containerColor = Color(73, 4, 86, 255))
            ) {
                Text(
                    "Login",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = quicksand
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            TextButton(onClick = {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }) {
                Text(
                    buildAnnotatedString {
                        append("Haven't Acoun? ")
                        withStyle(style = SpanStyle(color = Color(253, 221, 94, 250))) {
                            append("  Register")
                        }
                    }, fontFamily = quicksand, color = Color.White
                )
            }
        }
    }
}