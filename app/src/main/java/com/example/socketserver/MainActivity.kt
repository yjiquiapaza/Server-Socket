@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socketserver

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.socketserver.ui.theme.SocketServerTheme
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocketServerTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { MyTopBar() }
    ) { innerPading ->
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPading),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyContent(innerPading)
                SearchButton()
            }
        }
    }
}

@Composable
fun SearchButton() {
    Button(onClick = { }, colors = ButtonDefaults.buttonColors()) {
        Icon(
            painter = painterResource(R.drawable.baseline_search_24),
            contentDescription = "Find all Bluetooth devices"
        )
        Text("Button")
    }
}


@Composable
fun MyTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = LocalResources.current.getText(R.string.top_bar).toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    )
}

@Composable
fun MyContent(innerPading: PaddingValues) {
    val context = LocalContext.current
    val devices = getConnectedBluetoothDevices(context)
    BluetoothDevicesList(devices)
}

@Composable
fun BluetoothDevicesList(devices: List<BluetoothDevice>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (devices.isNotEmpty()) {
            items(devices) { devices ->
                DeviceItem(devices)
            }
        } else {
            item { Item() }
        }
    }
}

@Composable
fun Item(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column() {
            Text(text = "Test", style = MaterialTheme.typography.titleMedium)
            Text(text = "Test", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun DeviceItem(device: BluetoothDevice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val name = try {
                device.name ?: "Unknow"
            } catch (e: SecurityException) {
                "Without Permissions"
            }
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Text(text = device.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun getConnectedBluetoothDevices(context: Context): List<BluetoothDevice> {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetooAdapter = bluetoothManager.adapter

    if (bluetooAdapter == null || !bluetooAdapter.isEnabled) {
        return emptyList()
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return emptyList()
    }
    return bluetooAdapter.bondedDevices.toList()
}


@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun GreetingPreview() {
    SocketServerTheme {
        MyApp(Modifier.fillMaxSize())
    }
}