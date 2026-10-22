package com.example.data.local

import com.example.data.model.PropertyListing
import org.json.JSONArray

object SampleData {
    fun getInitialProperties(): List<PropertyListing> {
        val sample1Photos = listOf(
            "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1080&q=80",
            "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=1080&q=80",
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=1080&q=80",
            "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1080&q=80",
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=1080&q=80"
        )
        val sample1Videos = listOf(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        )
        val sample1Amenities = listOf("24x7 पानी", "कार पार्किंग", "बालकनी", "लिफ्ट", "मॉड्यूलर किचन", "सिक्योरिटी गार्ड")

        val sample2Photos = listOf(
            "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=1080&q=80",
            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1080&q=80",
            "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?w=1080&q=80",
            "https://images.unsplash.com/photo-1600573472550-8090b5e0745e?w=1080&q=80"
        )
        val sample2Videos = listOf(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )
        val sample2Amenities = listOf("अलग बिजली मीटर", "बाइक पार्किंग", "खुली छत", "24x7 पानी", "शांत माहौल")

        val sample3Photos = listOf(
            "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=1080&q=80",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=1080&q=80",
            "https://images.unsplash.com/photo-1554995207-c18c203602cb?w=1080&q=80"
        )
        val sample3Videos = emptyList<String>()
        val sample3Amenities = listOf("अटैच्ड लेट-बाथ", "कूलर/पंखा", "RO पेयजल", "अलग मीटर", "विद्यार्थी व जॉब हेतु")

        val sample4Photos = listOf(
            "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?w=1080&q=80",
            "https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=1080&q=80",
            "https://images.unsplash.com/photo-1613977257363-707ba9348227?w=1080&q=80",
            "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=1080&q=80"
        )
        val sample4Videos = listOf(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        )
        val sample4Amenities = listOf("कार पार्किंग", "गैस पाइपलाइन", "क्लब हाउस", "पार्क व्यू", "2 बालकनी", "24x7 सिक्योरिटी")

        fun toJson(list: List<String>): String {
            val arr = JSONArray()
            list.forEach { arr.put(it) }
            return arr.toString()
        }

        return listOf(
            PropertyListing(
                id = 1,
                title = "शानदार 2 BHK फ्लैट - मालवीय नगर मुख्य मार्ग के पास",
                propertyType = "FLAT",
                roomsCount = "2 BHK",
                floorNumber = "2nd Floor (दूसरी मंजिल)",
                totalFloors = "4",
                rentAmount = 14500,
                securityDeposit = 25000,
                maintenanceAmount = 800,
                furnishing = "Semi-Furnished",
                city = "जयपुर",
                areaColony = "मालवीय नगर, सेक्टर 3",
                address = "मकान नं. 45, गिरधर मार्ग, मालवीय नगर",
                landmark = "वर्ल्ड ट्रेड पार्क के नजदीक",
                ownerName = "राजेश शर्मा (मकान मालिक)",
                contactNumber = "9829012345",
                alternateNumber = "9829054321",
                whatsappAvailable = true,
                tenantPreference = "Family (परिवार)",
                description = "हवादार और रोशनी से भरपूर 2 BHK फ्लैट, जिसमें 2 बेडरूम, बड़ा हॉल, 2 बालकनी और मॉड्यूलर किचन उपलब्ध है। सिर्फ अच्छे परिवार के लिए।",
                photosJson = toJson(sample1Photos),
                videosJson = toJson(sample1Videos),
                amenitiesJson = toJson(sample1Amenities),
                status = "APPROVED",
                isFeatured = true,
                isFavorite = false,
                createdAt = System.currentTimeMillis() - 86400000L * 2
            ),
            PropertyListing(
                id = 2,
                title = "स्वतंत्र 3 BHK बड़ा मकान (पूरा ग्राउंड फ्लोर)",
                propertyType = "HOUSE",
                roomsCount = "3 BHK",
                floorNumber = "Ground Floor (ग्राउंड फ्लोर)",
                totalFloors = "2",
                rentAmount = 18000,
                securityDeposit = 30000,
                maintenanceAmount = 0,
                furnishing = "Unfurnished",
                city = "जयपुर",
                areaColony = "वैशाली नगर",
                address = "प्लॉट नं. 12, आम्रपाली सर्कल के पास",
                landmark = "नेशनल हैंडलूम के पीछे",
                ownerName = "महेंद्र सिंह राठौड़",
                contactNumber = "9414098765",
                whatsappAvailable = true,
                tenantPreference = "Any (परिवार या सरकारी कर्मचारी)",
                description = "स्वतंत्र 3 कमरों का मकान, बड़ी कार पार्किंग, आगे व पीछे खुला आंगन, 24 घंटे मीठा पानी। किसी भी प्रकार की कोई रोक-टोक नहीं।",
                photosJson = toJson(sample2Photos),
                videosJson = toJson(sample2Videos),
                amenitiesJson = toJson(sample2Amenities),
                status = "APPROVED",
                isFeatured = true,
                isFavorite = true,
                createdAt = System.currentTimeMillis() - 86400000L * 3
            ),
            PropertyListing(
                id = 3,
                title = "1 बड़ा कमरा + किचन + अटैच लेट-बाथ (स्टूडेंट्स/जॉब हेतु)",
                propertyType = "ROOM",
                roomsCount = "1 Room (सिंगल कमरा)",
                floorNumber = "1st Floor (पहली मंजिल)",
                totalFloors = "3",
                rentAmount = 5500,
                securityDeposit = 5500,
                maintenanceAmount = 200,
                furnishing = "Semi-Furnished",
                city = "जयपुर",
                areaColony = "गोपालपुरा बाईपास",
                address = "गली नं. 4, त्रिवेणी नगर",
                landmark = "कोचिंग हब के निकट",
                ownerName = "सुरेश कुमार अग्रवाल",
                contactNumber = "9782011223",
                whatsappAvailable = true,
                tenantPreference = "Bachelors (विद्यार्थी / जॉब पेशा)",
                description = "शांत वातावरण में 1 कमरा, अटैच वॉशरूम, स्लैब किचन, बेड और पंखा लगा हुआ। कोचिंग संस्थानों के बिल्कुल पास।",
                photosJson = toJson(sample3Photos),
                videosJson = toJson(sample3Videos),
                amenitiesJson = toJson(sample3Amenities),
                status = "APPROVED",
                isFeatured = false,
                isFavorite = false,
                createdAt = System.currentTimeMillis() - 86400000L * 1
            ),
            PropertyListing(
                id = 4,
                title = "लक्जरी 2 BHK फ्लैट - गेटेड सोसाइटी विथ स्विमिंग पूल",
                propertyType = "FLAT",
                roomsCount = "2 BHK",
                floorNumber = "3rd Floor (तीसरी मंजिल)",
                totalFloors = "7",
                rentAmount = 16000,
                securityDeposit = 32000,
                maintenanceAmount = 1200,
                furnishing = "Fully Furnished",
                city = "जयपुर",
                areaColony = "जगतपुरा",
                address = "टॉवर B, सनराइज अपार्टमेंट्स, जगतपुरा",
                landmark = "अक्षय पात्र मंदिर के सामने",
                ownerName = "अनिल वर्मा (मकान मालिक)",
                contactNumber = "9887766554",
                whatsappAvailable = true,
                tenantPreference = "Family (सभ्य परिवार)",
                description = "पूरी तरह से फर्निश्ड 2 BHK फ्लैट, जिसमें सोफा, डाइनिंग, फ्रिज, एसी और आरओ लगा है। 24 घंटे सुरक्षा और पावर बैकअप।",
                photosJson = toJson(sample4Photos),
                videosJson = toJson(sample4Videos),
                amenitiesJson = toJson(sample4Amenities),
                status = "APPROVED",
                isFeatured = true,
                isFavorite = false,
                createdAt = System.currentTimeMillis() - 86400000L * 4
            )
        )
    }
}
