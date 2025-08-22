package com.example.shopsy.Retrofir

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.example.shopsy.totalscreen.bannerImages
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


class RetrofitActivity : ComponentActivity() {

    val list = mutableStateListOf<Todos>()
    lateinit var maintodoList: ArrayList<Todos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        fetchData()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    mybaaner()
//                    MyUI()
                }
            }
        }
    }

    @Composable
    fun mybaaner() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            val pagerState = rememberPagerState()

            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalPager(
                    count = bannerImages.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) { page ->
                    Image(
                        painter = painterResource(id = bannerImages[page]),
                        contentDescription = "Banner Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
            }
        }
    }
}


//@Composable
//fun MyUI() {
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn {
//            items(list.size) { index ->
//                val todo = list[index]
//                Log.d("0909090", "MyUI: $todo")
//                Text(todo.todo ?: "No task", fontSize = 20.sp, color = Color.Black)
//                Spacer(modifier = Modifier.padding(10.dp))
//            }
//        }
//    }
//}
//
//fun fetchData() {
//    val apiService = RetroFitHelper.getInstance()
//    val call = apiService.getTodo()
//    call.enqueue(object : Callback<Tododata> {
//        override fun onResponse(call: Call<Tododata>, response: Response<Tododata>) {
//            val productMode = response.body()
//            productMode?.let {
//                list.clear()
//                list.addAll(it.todos)
////                    maintodoList = it.todos
//            }
//
//        }
//
//        override fun onFailure(call: Call<Tododata>, t: Throwable) {
//            Log.d("=====", "onFailure: ${t.localizedMessage}")
//        }
//    })
//}
//}

