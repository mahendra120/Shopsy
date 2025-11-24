package com.example.shopsy.SignUp_Login


import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.shopsy.R
import com.example.shopsy.ui.theme.quicksand
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignupActivity : ComponentActivity() {
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var email by mutableStateOf("")
    var Password by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                SignUp()
            }
        }
    }

    @Composable
    @Preview(showSystemUi = true)
    fun SignUp() {
        Image(
            painter = painterResource(R.drawable.purpalcolorbackgrung),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Signup",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(253, 253, 253),
                fontFamily = quicksand,
                modifier = Modifier.padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it }, leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "name", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp), // bigger radius
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
                value = surname, onValueChange = { surname = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "surname", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF341539),
                    cursorColor = Color(253, 221, 94, 250),
                    unfocusedContainerColor = Color(0, 0, 0),
                    focusedTextColor = Color(253, 253, 253),
                    unfocusedTextColor = Color(253, 253, 253),
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
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
                }, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF341539),
                    cursorColor = Color(253, 221, 94, 250),
                    unfocusedContainerColor = Color(0, 0, 0),
                    focusedTextColor = Color(253, 253, 253),
                    unfocusedTextColor = Color(253, 253, 253),
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            OutlinedTextField(
                value = Password, onValueChange = { Password = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "password", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF341539),
                    cursorColor = Color(253, 221, 94, 250),
                    unfocusedContainerColor = Color(0, 0, 0),
                    focusedTextColor = Color(253, 253, 253),
                    unfocusedTextColor = Color(253, 253, 253),
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 25.dp))
            Button(
                onClick = {
                    SignUp1()
                },
                modifier = Modifier.width(220.dp),
                border = BorderStroke(1.dp, color = Color.White),
                colors = ButtonDefaults.buttonColors(containerColor = Color(73, 4, 86, 255))
            ) {
                Text(
                    "Singup", fontSize = 23.sp, fontWeight = FontWeight.Bold, fontFamily = quicksand
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            TextButton(
                onClick = {
                    finish()
                },
                modifier = Modifier,
            ) {
                Text(
                    buildAnnotatedString {
                        append("Have Account? ")
                        withStyle(style = SpanStyle(color = Color(253, 221, 94, 250))) {
                            append("  Login")
                        }
                    }, fontFamily = quicksand, color = Color.White
                )
            }
        }
    }

    fun SignUp1() {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, Password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("======", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("======", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}
