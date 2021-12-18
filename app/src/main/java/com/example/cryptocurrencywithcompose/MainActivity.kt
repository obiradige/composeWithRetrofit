package com.example.cryptocurrencywithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencywithcompose.model.CryptoModel
import com.example.cryptocurrencywithcompose.service.CryptoApi
import com.example.cryptocurrencywithcompose.ui.theme.CryptoCurrencyWithComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoCurrencyWithComposeTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){

    var cryptoModel = remember {
        mutableStateListOf<CryptoModel>()
    }
    val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoApi::class.java)

    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    cryptoModel.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })
    Scaffold(topBar = {AppBar()}) {
        CryptoList(cryptos = cryptoModel)
    }
}

@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(12.dp)) {
        Text(text = "First Compose Exercies")
    }
}

@Composable
fun CryptoList(cryptos : List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(12.dp) ){
        items(cryptos) {crypto ->
            CryptoRow(crypto = crypto)

        }
    }
}

@Composable
fun CryptoRow(crypto : CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency, style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(2.dp),
        fontWeight = FontWeight.Bold)
        Text(text = crypto.price, style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold)
        
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoCurrencyWithComposeTheme {
        MainScreen()
    }
}