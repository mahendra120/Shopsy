package com.example.shopsy.totalscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.shopsy.Category


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ParsonScreen(modifier: Modifier.Companion, category: Category) {
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize())
    {

    }
}