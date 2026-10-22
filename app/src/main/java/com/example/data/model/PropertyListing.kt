package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.json.JSONArray

@Entity(tableName = "property_listings")
data class PropertyListing(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val propertyType: String, // HOUSE, FLAT, ROOM, FLOOR, PG
    val roomsCount: String, // "1 Room", "1 RK", "1 BHK", "2 BHK", "3 BHK", "4+ BHK"
    val floorNumber: String, // "Ground Floor", "1st Floor", "2nd Floor", etc.
    val totalFloors: String = "3",
    val rentAmount: Int, // ₹ per month
    val securityDeposit: Int, // ₹ security deposit
    val maintenanceAmount: Int = 0, // ₹ per month
    val furnishing: String = "Semi-Furnished", // Unfurnished, Semi-Furnished, Fully Furnished
    val city: String = "जयपुर",
    val areaColony: String,
    val address: String,
    val landmark: String = "",
    val ownerName: String,
    val contactNumber: String,
    val alternateNumber: String = "",
    val whatsappAvailable: Boolean = true,
    val tenantPreference: String = "Any", // Family, Bachelors, Any, Girls
    val description: String = "",
    val photosJson: String = "[]", // JSON array of up to 20 photo URIs / URLs
    val videosJson: String = "[]", // JSON array of up to 5 video URIs / URLs
    val amenitiesJson: String = "[]", // JSON array of amenity tags
    val status: String = "APPROVED", // PENDING, APPROVED, REJECTED, RENTED
    val isFeatured: Boolean = false,
    val isFavorite: Boolean = false,
    val postedByAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getPhotos(): List<String> {
        return try {
            val jsonArray = JSONArray(photosJson)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getVideos(): List<String> {
        return try {
            val jsonArray = JSONArray(videosJson)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAmenities(): List<String> {
        return try {
            val jsonArray = JSONArray(amenitiesJson)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    val localizedPropertyType: String
        get() = when (propertyType.uppercase()) {
            "HOUSE" -> "मकान (House)"
            "FLAT" -> "फ्लैट / अपार्टमेंट"
            "ROOM" -> "कमरा (Single Room)"
            "FLOOR" -> "स्वतंत्र मंजिल (Floor)"
            "PG" -> "पीजी / हॉस्टल (PG)"
            else -> propertyType
        }

    val localizedFurnishing: String
        get() = when (furnishing.lowercase()) {
            "fully furnished", "fully" -> "फुल फर्निश्ड"
            "semi-furnished", "semi" -> "सेमी फर्निश्ड"
            "unfurnished" -> "बिना फर्नीचर"
            else -> furnishing
        }

    val localizedStatus: String
        get() = when (status) {
            "PENDING" -> "समीक्षा जारी (Pending)"
            "APPROVED" -> "सक्रिय विज्ञापन (Active)"
            "RENTED" -> "किराए पर लग गया (Rented)"
            "REJECTED" -> "अस्वीकृत (Rejected)"
            else -> status
        }
}

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        val array = JSONArray()
        list.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data == null || data.isBlank()) return emptyList()
        return try {
            val jsonArray = JSONArray(data)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
