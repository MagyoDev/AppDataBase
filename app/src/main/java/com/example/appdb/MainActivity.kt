package com.example.appdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.appdb.roomDB.Pessoa
import com.example.appdb.roomDB.PessoaDataBase
import com.example.appdb.ui.theme.AppDBTheme
import com.example.appdb.viewModel.PessoaViewModel
import com.example.appdb.viewModel.Repository

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDataBase::class.java,
            "pessoa.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppDBTheme {
                App(viewModel, this)
            }
        }
    }
}

@Composable
fun App(viewModel: PessoaViewModel, mainActivity: MainActivity) {
    var nome by remember {
        mutableStateOf("")
    }

    var telefone by remember {
        mutableStateOf("")
    }

    val pessoa = Pessoa(
        nome,
        telefone
    )

    var pessoaList by remember {
        mutableStateOf(listOf<Pessoa>())
    }

    Column(
        Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "App Database",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        Button(
            onClick = {
                viewModel.upsertPessoa(pessoa)
                nome = ""
                telefone = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Text("Cadastrar")
        }

        Divider(color = Color.Gray, thickness = 1.dp)

        LazyColumn {
            items(pessoaList) { pessoa ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    Arrangement.SpaceBetween
                ) {
                    Text(
                        text = pessoa.nome,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = pessoa.telefone,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp)

            }
        }
    }
}
