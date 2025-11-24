package com.example.shopsy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shopsy.Data.Product
import com.example.shopsy.Data.Products
import com.example.shopsy.totalscreen.CardScreen
import com.example.shopsy.totalscreen.ParsonScreen
import com.example.shopsy.totalscreen.bannerImages
import com.example.shopsy.ui.theme.ShopsyTheme
import com.example.shopsy.ui.theme.font4
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import kotlinx.coroutines.delay

class Category : ComponentActivity() {
    val productList = mutableStateListOf<Products>()
    var mainProductList = ArrayList<Products>(0)
    var selecetedIcon by mutableStateOf(0)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchData()
        AdManager.init(this)

        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            ShopsyTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { topbar(scrollBehavior) },
                    bottomBar = { bottomber() }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selecetedIcon) {
                            0 -> CategoryScreen(modifier = Modifier)
                            1 -> CardScreen(modifier = Modifier)
                            2 -> ParsonScreen(modifier = Modifier, this@Category)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CategoryScreen(modifier: Modifier) {
        if (productList.isNotEmpty()) {
            Column {
                mySearchBar()
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    item(span = { GridItemSpan(2) }) {
                        mybaaner()
                    }
                    items(productList.size) { index ->
                        val product = productList[index]
                        Card(
                            onClick = {
                                val list =
                                    ArrayList(mainProductList.filter { it.category == product.category })
                                val kok = product.category
                                AdManager.showInterstitialAd(this@Category) {
                                    val intent = Intent(this@Category, ProductListPage::class.java)
                                    intent.putExtra("productList", list)
                                    intent.putExtra("name", product)
                                    startActivity(intent)
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .border(
                                    1.dp, Color(0xFF4F46E5), RoundedCornerShape(12.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GlideImage(
                                        model = product.thumbnail,
                                        modifier = Modifier.padding(0.dp),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    product.category,
                                    fontFamily = font4,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = Color(0, 150, 0))) {
                                            append("Discount ")
                                        }
                                        append("${product.discountPercentage}%")
                                    }, fontFamily = font4
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "loading...",
                        fontFamily = font4,
                        fontSize = 35.sp,
                        modifier = Modifier.padding(top = 9.dp, end = 0.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun mybaaner() {
        val pagerState = rememberPagerState()
        Column(modifier = Modifier.fillMaxWidth()) {

            LaunchedEffect(pagerState) {
                while (true) {
                    delay(3000) // 3 seconds
                    val nextPage = (pagerState.currentPage + 1) % bannerImages.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }

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
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                activeColor = Color(0xFF4F46E5),
                inactiveColor = Color.LightGray
            )
        }
    }


    fun fetchData() {
        val url = "https://dummyjson.com/products?limit=194"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val productMode = Gson().fromJson(response, Product::class.java)
            productList.clear()
            mainProductList = productMode.products
            productList.addAll(mainProductList.distinctBy { it.category })
        }, {
            Log.d("=====", "fetchData: That didn't work!")
        })
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topbar(scrollBehavior: TopAppBarScrollBehavior) {
        TopAppBar(
            title = {
                Image(
                    painter = painterResource(R.drawable.shopsy),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 20.dp)
                )
            },
            scrollBehavior = scrollBehavior,
            actions = {},
        )
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @Composable
    fun mySearchBar() {
        var searchQuery by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }
        val filteredItems = productList.filter { it.title.contains(searchQuery, ignoreCase = true) }
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { active = false },
            shape = RoundedCornerShape(12.dp),
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            windowInsets = WindowInsets(left = 0.dp, right = 0.dp, top = 0.dp, bottom = 0.dp),
            placeholder = { Text("Search", fontFamily = font4) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color(0xFF4F46E5)
                )
            },
            trailingIcon = {
                if (active) IconButton(onClick = {
                    searchQuery = ""
                    active = false
                }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            tonalElevation = 0.dp,
        ) {
            LazyColumn {
                item {
                    filteredItems.forEach { product ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), onClick = {
                                val list =
                                    ArrayList(mainProductList.filter { it.category == product.category })
                                val intent = Intent(this@Category, ProductListPage::class.java)
                                intent.putExtra("productList", list)
                                intent.putExtra("name", product.category)
                                startActivity(intent)
                            }) {
                            Row {
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(70.dp)
                                ) {
                                    GlideImage(
                                        product.thumbnail,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(start = 12.dp, top = 20.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        product.title,
                                        fontSize = 19.sp,
                                        fontFamily = font4,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(0.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        product.category,
                                        fontSize = 15.sp,
                                        fontFamily = font4,
                                        modifier = Modifier.padding(start = 0.dp, bottom = 10.dp),
                                        color = Color(0xFF57BAFA),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Card", Icons.Default.ShoppingCart),
        NavItem("Person", Icons.Default.Person)
    )

    @Composable
    fun bottomber() {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            navItemList.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == selecetedIcon,
                    onClick = { selecetedIcon = index },
                    icon = {
                        if (index == selecetedIcon) {
                            Icon(
                                item.icon, contentDescription = null, tint = Color(0xFF4F46E5)
                            )
                        } else Icon(item.icon, contentDescription = null)
                    },
                    label = {
                        if (index == selecetedIcon) {
                            Text(item.label)
                        } else {
                            Text(item.label)
                        }
                    })
            }
        }
    }
    data class NavItem(
        val label: String, val icon: ImageVector
    )
}
