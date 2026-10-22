package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryBlue
import com.example.ui.viewmodel.FilterState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    currentFilter: FilterState,
    onApplyFilter: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedType by remember { mutableStateOf(currentFilter.propertyType) }
    var selectedRooms by remember { mutableStateOf(currentFilter.roomsFilter) }
    var maxRent by remember { mutableFloatStateOf(currentFilter.maxRent.toFloat()) }
    var selectedFurnishing by remember { mutableStateOf(currentFilter.furnishing) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "फ़िल्टर करें (Filters)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Property Type
            Text(
                text = "प्रॉपर्टी का प्रकार (Property Type)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val propertyTypes = listOf(
                "ALL" to "सभी प्रकार",
                "HOUSE" to "🏠 मकान (House)",
                "FLAT" to "🏢 फ्लैट (Flat)",
                "ROOM" to "🚪 कमरा (Room)",
                "FLOOR" to "🏬 स्वतंत्र मंजिल",
                "PG" to "🛏️ पीजी / हॉस्टल"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                propertyTypes.forEach { (code, label) ->
                    FilterChip(
                        selected = selectedType == code,
                        onClick = { selectedType = code },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Rooms Count / BHK
            Text(
                text = "कमरों की संख्या (Rooms / BHK)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val roomOptions = listOf(
                "ALL" to "सभी",
                "1 Room" to "1 कमरा (Single)",
                "1 BHK" to "1 BHK",
                "2 BHK" to "2 BHK",
                "3 BHK" to "3 BHK",
                "4+" to "4+ BHK"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                roomOptions.forEach { (code, label) ->
                    FilterChip(
                        selected = selectedRooms == code,
                        onClick = { selectedRooms = code },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Max Budget Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "अधिकतम किराया (Budget)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "₹${maxRent.toInt()} / माह तक",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
            Slider(
                value = maxRent,
                onValueChange = { maxRent = it },
                valueRange = 3000f..50000f,
                steps = 18
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Furnishing
            Text(
                text = "फर्नीचर (Furnishing)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val furnishingOptions = listOf(
                "ALL" to "सभी",
                "Unfurnished" to "बिना फर्नीचर",
                "Semi-Furnished" to "सेमी-फर्निश्ड",
                "Fully Furnished" to "फुल फर्निश्ड"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                furnishingOptions.forEach { (code, label) ->
                    FilterChip(
                        selected = selectedFurnishing == code,
                        onClick = { selectedFurnishing = code },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedType = "ALL"
                        selectedRooms = "ALL"
                        maxRent = 50000f
                        selectedFurnishing = "ALL"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("रीसेट करें")
                }

                Button(
                    onClick = {
                        onApplyFilter(
                            currentFilter.copy(
                                propertyType = selectedType,
                                roomsFilter = selectedRooms,
                                maxRent = maxRent.toInt(),
                                furnishing = selectedFurnishing
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier.weight(1.5f)
                ) {
                    Text("फ़िल्टर लागू करें")
                }
            }
        }
    }
}
