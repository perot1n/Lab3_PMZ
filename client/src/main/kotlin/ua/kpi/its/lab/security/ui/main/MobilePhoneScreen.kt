package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import ua.kpi.its.lab.security.dto.FileRequestDto
import ua.kpi.its.lab.security.dto.FileResponseDto
import ua.kpi.its.lab.security.dto.MobilePhoneRequestDto
import ua.kpi.its.lab.security.dto.MobilePhoneResponseDto

@Composable
fun MobilePhoneScreen(
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    snackbarHostState: SnackbarHostState
) {
    var phones by remember { mutableStateOf<List<MobilePhoneResponseDto>>(listOf()) }
    var loading by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf<MobilePhoneResponseDto?>(null) }

    LaunchedEffect(token) {
        loading = true
        delay(1000)
        phones = withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://localhost:8080/api/phones") {
                    bearerAuth(token)
                }
                loading = false
                response.body()
            }
            catch (e: Exception) {
                val msg = e.toString()
                snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                phones
            }
        }
    }

    if (loading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedPhone = null
                    openDialog = true
                },
                content = {
                    Icon(Icons.Filled.Add, "add mobile phone")
                }
            )
        }
    ) {
        if (phones.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No mobile phones to show", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(phones) { phone ->
                    MobilePhoneItem(
                        phone = phone,
                        onEdit = {
                            selectedPhone = phone
                            openDialog = true
                        },
                        onRemove = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.delete("http://localhost:8080/api/phones/${phone.id}") {
                                            bearerAuth(token)
                                        }
                                        require(response.status.isSuccess())
                                    }
                                    catch(e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                    }
                                }

                                loading = true

                                phones = withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.get("http://localhost:8080/api/phones") {
                                            bearerAuth(token)
                                        }
                                        loading = false
                                        response.body()
                                    }
                                    catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                        phones
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (openDialog) {
            MobilePhoneDialog(
                phone = selectedPhone,
                token = token,
                scope = scope,
                client = client,
                onDismiss = {
                    openDialog = false
                },
                onError = {
                    scope.launch {
                        snackbarHostState.showSnackbar(it, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                    }
                },
                onConfirm = {
                    openDialog = false
                    loading = true
                    scope.launch {
                        phones = withContext(Dispatchers.IO) {
                            try {
                                val response = client.get("http://localhost:8080/api/phones") {
                                    bearerAuth(token)
                                }
                                loading = false
                                response.body()
                            }
                            catch (e: Exception) {
                                loading = false
                                phones
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MobilePhoneDialog(
    phone: MobilePhoneResponseDto?,
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    onDismiss: () -> Unit,
    onError: (String) -> Unit,
    onConfirm: () -> Unit,
) {

    var brand by remember { mutableStateOf(phone?.brand ?: "") }
    var manufacturer by remember { mutableStateOf(phone?.manufacturer ?: "") }
    var model by remember { mutableStateOf(phone?.model ?: "") }
    var memorySize by remember { mutableStateOf(phone?.memorySize?.toString() ?: "") }
    var price by remember { mutableStateOf(phone?.price?.toString() ?: "") }
    var releaseDate by remember { mutableStateOf(phone?.releaseDate ?: "") }
    var dualSimSupport by remember { mutableStateOf(phone?.dualSimSupport ?: false) }

    // Поля для файлу
    var fileName by remember { mutableStateOf("") }
    var fileExtension by remember { mutableStateOf("") }
    var fileSize by remember { mutableStateOf("") }
    var fileCreationDate by remember { mutableStateOf("") }
    var fileIsPhoto by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp).wrapContentSize()) {
            Column(
                modifier = Modifier.padding(16.dp, 8.dp).width(IntrinsicSize.Max).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (phone == null) {
                    Text("Create mobile phone")
                } else {
                    Text("Update mobile phone")
                }

                HorizontalDivider()
                Text("Mobile Phone info")
                TextField(brand, { brand = it }, label = { Text("Brand") })
                TextField(manufacturer, { manufacturer = it }, label = { Text("Manufacturer") })
                TextField(model, { model = it }, label = { Text("Model") })
                TextField(memorySize, { memorySize = it }, label = { Text("Memory Size") })
                TextField(price, { price = it }, label = { Text("Price") })
                TextField(releaseDate, { releaseDate = it }, label = { Text("Release Date") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(dualSimSupport, { dualSimSupport = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Dual SIM Support")
                }

                HorizontalDivider()
                Text("File info")
                TextField(fileName, { fileName = it }, label = { Text("Name") })
                TextField(fileExtension, { fileExtension = it }, label = { Text("Extension") })
                TextField(fileSize, { fileSize = it }, label = { Text("Size") })
                TextField(fileCreationDate, { fileCreationDate = it }, label = { Text("Creation Date") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(fileIsPhoto, { fileIsPhoto = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Photo")
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                try {
                                    val request = MobilePhoneRequestDto(
                                        brand, manufacturer, model, memorySize.toDouble(), price.toDouble(), releaseDate, dualSimSupport
                                    )
                                    val response = if (phone == null) {
                                        client.post("http://localhost:8080/api/phones") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    } else {
                                        client.put("http://localhost:8080/api/phones/${phone.id}") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    }
                                    require(response.status.isSuccess())
                                    onConfirm()
                                } catch (e: Exception) {
                                    val msg = e.toString()
                                    onError(msg)
                                }
                            }
                        }
                    ) {
                        if (phone == null) {
                            Text("Create")
                        } else {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MobilePhoneItem(phone: MobilePhoneResponseDto, onEdit: () -> Unit, onRemove: () -> Unit) {
    Card(shape = CardDefaults.elevatedShape, elevation = CardDefaults.elevatedCardElevation()) {
        ListItem(
            overlineContent = {
                Text(phone.brand)
            },
            headlineContent = {
                Text(phone.model)
            },
            supportingContent = {
                Text("$${phone.price}")
            },
            trailingContent = {
                Row(modifier = Modifier.padding(0.dp, 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onEdit)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onRemove)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}