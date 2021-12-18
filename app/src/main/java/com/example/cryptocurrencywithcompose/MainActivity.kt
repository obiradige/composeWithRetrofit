package com.example.cryptocurrencywithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
                    it.forEach {
                        println(it)
                    }
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })
    Scaffold(topBar = {AppBar()}) {
        
    }
}

@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(12.dp)) {
        Text(text = "First Compose Exercies")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoCurrencyWithComposeTheme {
        MainScreen()
    }
}