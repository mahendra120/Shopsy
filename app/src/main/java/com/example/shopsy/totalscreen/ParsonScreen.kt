package com.example.shopsy.totalscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.shopsy.Category
import com.example.shopsy.RoomDatabase.AppDatabase
import com.example.shopsy.RoomDatabase.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val list = mutableStateListOf<User>()

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ParsonScreen(modifier: Modifier.Companion, category: Category) {
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize())
    {
        GlobalScope.launch {
            val userList = AppDatabase.getInstance(category)
                .userDao()
                .getAllUser()
            list.addAll(userList)

            list.forEach {
                Log.d("90909087887", "onCreate: $it")
            }
        }
    }
}