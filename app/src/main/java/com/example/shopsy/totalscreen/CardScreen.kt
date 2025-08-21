package com.example.shopsy.totalscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CardScreen(modifier: Modifier.Companion) {
    Box(modifier = modifier.fillMaxSize())
    {
        Text("Card Screen", fontSize = 44.sp, modifier = modifier)
    }
}