package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryAmber
import com.example.ui.theme.TertiaryGreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostAdScreen(
    viewModel: PropertyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BackHandler {
        viewModel.navigateBack()
    }

    val photos by viewModel.postAdPhotos.collectAsStateWithLifecycle()
    val videos by viewModel.postAdVideos.collectAsStateWithLifecycle()
    val selectedAmenities by viewModel.postAdAmenities.collectAsStateWithLifecycle()

    // Form inputs
    var title by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("FLAT") }
    var roomsCount by remember { mutableStateOf("2 BHK") }
    var floorNumber by remember { mutableStateOf("1st Floor (पहली मंजिल)") }
    var totalFloors by remember { mutableStateOf("3") }
    var rentAmountText by remember { mutableStateOf("") }
    var securityDepositText by remember { mutableStateOf("") }
    var maintenanceText by remember { mutableStateOf("") }
    var furnishing by remember { mutableStateOf("Semi-Furnished") }
    var city by remember { mutableStateOf("जयपुर") }
    var areaColony by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var alternateNumber by remember { mutableStateOf("") }
    var whatsappAvailable by remember { mutableStateOf(true) }
    var tenantPreference by remember { mutableStateOf("Family (परिवार)") }
    var description by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }

    // Photo picker launcher (up to 20 photos)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 20)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val uriStrings = uris.map { it.toString() }
            viewModel.addPhotos(uriStrings)
            Toast.makeText(context, "${uris.size} फोटो जोड़े गए!", Toast.LENGTH_SHORT).show()
        }
    }

    // Video picker launcher (up to 5 videos)
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val uriStrings = uris.map { it.toString() }
            viewModel.addVideos(uriStrings)
            Toast.makeText(context, "${uris.size} वीडियो जोड़े गए!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "नया विज्ञापन पोस्ट करें",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "मकान मालिक द्वारा सीधा विज्ञापन",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("post_ad_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Intro Card
            Surface(
                color = PrimaryBlue.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🏡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "मकान मालिक सीधे अपने मोबाइल से विज्ञापन डालें",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "अधिकतम 20 फोटो और 5 वीडियो अपलोड कर सकते हैं।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 1: PHOTO & VIDEO UPLOAD
            SectionTitle(title = "1. मकान / कमरों की फोटो व वीडियो अपलोड करें")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Upload buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_photos_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Add Photo",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "फोटो जोड़ें (${photos.size}/20)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_videos_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = "Add Video",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "वीडियो जोड़ें (${videos.size}/5)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Demo Media button (convenient for immediate testing)
                    OutlinedButton(
                        onClick = {
                            viewModel.addSamplePropertyMedia()
                            Toast.makeText(context, "डेमो फोटो व वीडियो जोड़े गए!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("demo_media_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "⭐ टेस्ट हेतु डेमो फोटो व वीडियो जोड़ें",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Display Selected Photos
                    if (photos.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "चयनित फोटो (${photos.size} / अधिकतम 20):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(photos) { index, photoUri ->
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                ) {
                                    AsyncImage(
                                        model = photoUri,
                                        contentDescription = "Photo $index",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // Remove button
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.7f),
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(22.dp)
                                            .clickable { viewModel.removePhoto(index) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove photo",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Display Selected Videos
                    if (videos.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "चयनित वीडियो (${videos.size} / अधिकतम 5):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(videos) { index, videoUri ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier
                                        .size(width = 110.dp, height = 75.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Default.PlayCircle,
                                                contentDescription = "Video",
                                                tint = Color.White,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Text(
                                                text = "वीडियो #${index + 1}",
                                                color = Color.White,
                                                fontSize = 10.sp
                                            )
                                        }

                                        // Remove video
                                        Surface(
                                            color = Color.Red.copy(alpha = 0.8f),
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(20.dp)
                                                .clickable { viewModel.removeVideo(index) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove video",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 2: PROPERTY DETAILS
            SectionTitle(title = "2. मकान की मुख्य जानकारी")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("विज्ञापन का शीर्षक * (Title)") },
                        placeholder = { Text("जैसे: 2 BHK हवादार फ्लैट - मालवीय नगर") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_title"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Property Type
                    Text(text = "प्रॉपर्टी का प्रकार * (Property Type):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val types = listOf(
                        "HOUSE" to "🏠 मकान (House)",
                        "FLAT" to "🏢 फ्लैट (Flat)",
                        "ROOM" to "🚪 कमरा (Room)",
                        "FLOOR" to "🏬 स्वतंत्र मंजिल",
                        "PG" to "🛏️ पीजी"
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        types.forEach { (typeCode, typeLabel) ->
                            FilterChip(
                                selected = propertyType == typeCode,
                                onClick = { propertyType = typeCode },
                                label = { Text(typeLabel, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Rooms Count (कमरों की संख्या)
                    Text(text = "मकान में कितने कमरे हैं? * (Rooms / BHK):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val roomOptions = listOf("1 Room (सिंगल कमरा)", "1 RK", "1 BHK", "2 BHK", "3 BHK", "4+ BHK")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        roomOptions.forEach { opt ->
                            FilterChip(
                                selected = roomsCount == opt,
                                onClick = { roomsCount = opt },
                                label = { Text(opt, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Floor Number (कौनसे फ्लोर पर है)
                    Text(text = "मकान कौनसे फ्लोर पर है? * (Which Floor):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val floorOptions = listOf(
                        "Ground Floor (ग्राउंड फ्लोर)",
                        "1st Floor (पहली मंजिल)",
                        "2nd Floor (दूसरी मंजिल)",
                        "3rd Floor (तीसरी मंजिल)",
                        "4th Floor+",
                        "पूरा मकान (Full House)"
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        floorOptions.forEach { opt ->
                            FilterChip(
                                selected = floorNumber == opt,
                                onClick = { floorNumber = opt },
                                label = { Text(opt, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Furnishing
                    Text(text = "फर्नीचर की स्थिति (Furnishing):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val furnishingOptions = listOf(
                        "Unfurnished" to "बिना फर्नीचर",
                        "Semi-Furnished" to "सेमी-फर्निश्ड",
                        "Fully Furnished" to "फुल फर्निश्ड"
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        furnishingOptions.forEach { (code, label) ->
                            FilterChip(
                                selected = furnishing == code,
                                onClick = { furnishing = code },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 3: RENT & SECURITY DEPOSIT
            SectionTitle(title = "3. किराया और सिक्युरिटी डिपॉजिट की जानकारी")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Monthly Rent
                    OutlinedTextField(
                        value = rentAmountText,
                        onValueChange = { rentAmountText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("मासिक किराया ₹ * (Monthly Rent)") },
                        placeholder = { Text("जैसे: 8500") },
                        prefix = { Text("₹ ", fontWeight = FontWeight.Bold) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_rent"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Security Deposit
                    OutlinedTextField(
                        value = securityDepositText,
                        onValueChange = { securityDepositText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("सिक्योरिटी डिपॉजिट ₹ (Security Deposit)") },
                        placeholder = { Text("जैसे: 15000 (यदि कोई हो)") },
                        prefix = { Text("₹ ", fontWeight = FontWeight.Bold) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_security"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Maintenance charges
                    OutlinedTextField(
                        value = maintenanceText,
                        onValueChange = { maintenanceText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("मेंटेनेंस चार्ज ₹ (Maintenance - यदि लागू हो)") },
                        placeholder = { Text("जैसे: 500 (शून्य छोड़ सकते हैं)") },
                        prefix = { Text("₹ ", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 4: LOCATION & ADDRESS
            SectionTitle(title = "4. मकान का स्थान व पता")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("शहर (City) *") },
                            placeholder = { Text("जयपुर") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_city"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = areaColony,
                            onValueChange = { areaColony = it },
                            label = { Text("कॉलोनी / इलाका *") },
                            placeholder = { Text("मालवीय नगर") },
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("input_colony"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("मकान / फ्लैट नंबर व पता") },
                        placeholder = { Text("मकान नं. 45, गली नं. 2") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = landmark,
                        onValueChange = { landmark = it },
                        label = { Text("पहचान / लैंडमार्क (Landmark)") },
                        placeholder = { Text("मेट्रो स्टेशन / मंदिर के पास") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 5: LANDLORD CONTACT DETAILS
            SectionTitle(title = "5. मकान मालिक का कांटेक्ट नंबर व जानकारी")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        label = { Text("मकान मालिक का नाम * (Owner Name)") },
                        placeholder = { Text("जैसे: राम शर्मा") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_owner_name"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = contactNumber,
                        onValueChange = { contactNumber = it.filter { ch -> ch.isDigit() } },
                        label = { Text("मोबाइल कांटेक्ट नंबर * (Phone Number)") },
                        placeholder = { Text("98290XXXXX (10 अंक)") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = "Phone")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_phone"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { whatsappAvailable = !whatsappAvailable }
                    ) {
                        Checkbox(
                            checked = whatsappAvailable,
                            onCheckedChange = { whatsappAvailable = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "इस नंबर पर व्हाट्सएप (WhatsApp) चालू है",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = alternateNumber,
                        onValueChange = { alternateNumber = it.filter { ch -> ch.isDigit() } },
                        label = { Text("वैकल्पिक नंबर (Alternate Number - ऐच्छिक)") },
                        placeholder = { Text("दूसरा नंबर यदि कोई हो") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 6: AMENITIES & PREFERENCES
            SectionTitle(title = "6. सुविधाएं व किरायेदार की पसंद")

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Tenant Preference
                    Text(text = "पसंदीदा किरायेदार (Preferred Tenant):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val preferences = listOf("Family (परिवार)", "Bachelors (विद्यार्थी/जॉब)", "Any (कोई भी)", "Girls Only (केवल छात्राएं/महिलाएं)")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        preferences.forEach { pref ->
                            FilterChip(
                                selected = tenantPreference == pref,
                                onClick = { tenantPreference = pref },
                                label = { Text(pref, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Amenities Checkboxes
                    Text(text = "उपलब्ध सुविधाएं (Amenities):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val amenitiesList = listOf(
                        "24x7 मीठा पानी",
                        "कार पार्किंग",
                        "बाइक पार्किंग",
                        "अलग बिजली मीटर",
                        "बालकनी",
                        "लिफ्ट",
                        "मॉड्यूलर किचन",
                        "सिक्योरिटी गार्ड",
                        "खुली छत",
                        "अटैच्ड लेट-बाथ",
                        "वाई-फाई"
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        amenitiesList.forEach { amenity ->
                            val isChecked = selectedAmenities.contains(amenity)
                            FilterChip(
                                selected = isChecked,
                                onClick = { viewModel.toggleAmenity(amenity) },
                                label = { Text(amenity, fontSize = 12.sp) },
                                leadingIcon = if (isChecked) {
                                    {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            modifier = Modifier.size(16.dp),
                                            tint = TertiaryGreen
                                        )
                                    }
                                } else null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("अतिरिक्त विवरण (Description)") },
                        placeholder = { Text("जैसे: मकान हवादार है, मुख्य मार्ग के नजदीक है...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // SUBMIT BUTTON
            Button(
                onClick = {
                    val rent = rentAmountText.toIntOrNull() ?: 0
                    val security = securityDepositText.toIntOrNull() ?: 0
                    val maintenance = maintenanceText.toIntOrNull() ?: 0

                    isSubmitting = true
                    viewModel.submitNewListing(
                        title = title,
                        propertyType = propertyType,
                        roomsCount = roomsCount,
                        floorNumber = floorNumber,
                        totalFloors = totalFloors,
                        rentAmount = rent,
                        securityDeposit = security,
                        maintenanceAmount = maintenance,
                        furnishing = furnishing,
                        city = city,
                        areaColony = areaColony,
                        address = address,
                        landmark = landmark,
                        ownerName = ownerName,
                        contactNumber = contactNumber,
                        alternateNumber = alternateNumber,
                        whatsappAvailable = whatsappAvailable,
                        tenantPreference = tenantPreference,
                        description = description,
                        onSuccess = { newId ->
                            isSubmitting = false
                            Toast.makeText(
                                context,
                                "बधाई! आपका विज्ञापन सफलतापूर्वक पोस्ट हो गया है।",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.navigateTo(AppScreen.DETAIL, newId)
                        },
                        onError = { errMsg ->
                            isSubmitting = false
                            Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_ad_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "🚀 विज्ञापन पोस्ट करें (Publish Ad)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
