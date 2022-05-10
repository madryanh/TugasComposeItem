package com.adl.tugascomposeitem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.adl.tugascomposeitem.data.Product
import com.adl.tugascomposeitem.repo.OnFailure
import com.adl.tugascomposeitem.repo.OnSuccess
import com.adl.tugascomposeitem.repo.ProductRepo
import com.adl.tugascomposeitem.ui.theme.TugasComposeItemTheme
import com.adl.tugascomposeitem.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {

    val productViewModel by viewModels<ProductViewModel>(factoryProducer = {ProductViewModelFactory(ProductRepo())})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TugasComposeItemTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProductItem(productViewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductItem(productViewModel: ProductViewModel){

    when(val productList = productViewModel.productStateFlow.asStateFlow().collectAsState().value){
        is OnFailure ->{

        }
        is OnSuccess ->{
            val listOfProduct = productList.querySnapshot?.toObjects(Product::class.java)
            listOfProduct?.let {
                Column {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){
                        items(listOfProduct){
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                shape = RoundedCornerShape(16.dp), elevation = 5.dp) {
                                ProductItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    var showProductFullDesciption by remember { mutableStateOf(false) }

    Column {
        Row(modifier = Modifier.padding(12.dp)) {

            AsyncImage(
                model = product.gambar,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)) {
                Text(
                    text = product.productName,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
                Text(
                    text = product.category,
                    style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                )
                Text(
                    text = product.description,
                    style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                )
            }

            Column(modifier = Modifier.padding(80.dp, 0.dp, 0.dp, 0.dp)) {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/61/61147.png",
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                        .clickable { showProductFullDesciption = showProductFullDesciption.not() },
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = product.price,
                    style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                )
            }
        }

        AnimatedVisibility(visible = showProductFullDesciption) {
            Text(
                text = product.fullDescription, style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic, fontSize = 11.sp
                ),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            )
        }
    }
}
    class ProductViewModelFactory (val booksRepo: ProductRepo): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
                return ProductViewModel(booksRepo) as T
            }
            throw IllegalAccessException()
        }
}

