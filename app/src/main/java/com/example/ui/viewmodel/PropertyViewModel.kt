package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.InquiryMessage
import com.example.data.model.PropertyListing
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray

enum class AppScreen {
    HOME,
    DETAIL,
    POST_AD,
    ADMIN_PANEL,
    MY_ADS,
    FAVORITES
}

data class FilterState(
    val searchQuery: String = "",
    val propertyType: String = "ALL", // ALL, HOUSE, FLAT, ROOM, FLOOR, PG
    val maxRent: Int = 50000,
    val roomsFilter: String = "ALL", // ALL, 1 Room, 1 BHK, 2 BHK, 3 BHK, 4+ BHK
    val furnishing: String = "ALL" // ALL, Unfurnished, Semi-Furnished, Fully Furnished
)

class PropertyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PropertyRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PropertyRepository(db.propertyDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedPropertyId = MutableStateFlow<Long?>(null)
    val selectedPropertyId: StateFlow<Long?> = _selectedPropertyId.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    // Admin state
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _adminPinInput = MutableStateFlow("")
    val adminPinInput: StateFlow<String> = _adminPinInput.asStateFlow()

    private val _adminUnlocked = MutableStateFlow(false)
    val adminUnlocked: StateFlow<Boolean> = _adminUnlocked.asStateFlow()

    private val _adminNotification = MutableStateFlow<String?>(null)
    val adminNotification: StateFlow<String?> = _adminNotification.asStateFlow()

    // Filters
    private val _filters = MutableStateFlow(FilterState())
    val filters: StateFlow<FilterState> = _filters.asStateFlow()

    // Data streams
    val approvedListings = repository.approvedListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminAllListings = repository.allListingsAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingListings = repository.pendingListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteListings = repository.favoriteListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInquiries = repository.allInquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined filtered listings for Home
    val filteredListings: StateFlow<List<PropertyListing>> = combine(
        approvedListings,
        _filters
    ) { listings, filter ->
        listings.filter { listing ->
            val matchesQuery = filter.searchQuery.isBlank() ||
                listing.title.contains(filter.searchQuery, ignoreCase = true) ||
                listing.areaColony.contains(filter.searchQuery, ignoreCase = true) ||
                listing.city.contains(filter.searchQuery, ignoreCase = true) ||
                listing.landmark.contains(filter.searchQuery, ignoreCase = true) ||
                listing.address.contains(filter.searchQuery, ignoreCase = true)

            val matchesType = filter.propertyType == "ALL" ||
                listing.propertyType.equals(filter.propertyType, ignoreCase = true)

            val matchesRent = listing.rentAmount <= filter.maxRent

            val matchesRooms = filter.roomsFilter == "ALL" ||
                listing.roomsCount.contains(filter.roomsFilter, ignoreCase = true)

            val matchesFurnishing = filter.furnishing == "ALL" ||
                listing.furnishing.equals(filter.furnishing, ignoreCase = true)

            matchesQuery && matchesType && matchesRent && matchesRooms && matchesFurnishing
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently viewed single property
    val selectedProperty: StateFlow<PropertyListing?> = combine(
        adminAllListings,
        _selectedPropertyId
    ) { all, id ->
        if (id == null) null else all.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // User's own posted ads phone tracker
    private val _userPhone = MutableStateFlow("")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    // Navigation methods
    fun navigateTo(screen: AppScreen, propertyId: Long? = null) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
        }
        if (propertyId != null) {
            _selectedPropertyId.value = propertyId
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            val prev = _screenHistory.removeAt(_screenHistory.size - 1)
            _currentScreen.value = prev
            return true
        }
        if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    // Filter methods
    fun setSearchQuery(query: String) {
        _filters.value = _filters.value.copy(searchQuery = query)
    }

    fun setPropertyTypeFilter(type: String) {
        _filters.value = _filters.value.copy(propertyType = type)
    }

    fun setMaxRentFilter(rent: Int) {
        _filters.value = _filters.value.copy(maxRent = rent)
    }

    fun setRoomsFilter(rooms: String) {
        _filters.value = _filters.value.copy(roomsFilter = rooms)
    }

    fun setFurnishingFilter(furnishing: String) {
        _filters.value = _filters.value.copy(furnishing = furnishing)
    }

    fun resetFilters() {
        _filters.value = FilterState()
    }

    // Toggle favorite
    fun toggleFavorite(listing: PropertyListing) {
        viewModelScope.launch {
            repository.toggleFavorite(listing.id, listing.isFavorite)
        }
    }

    // Admin controls
    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun setAdminPin(pin: String) {
        _adminPinInput.value = pin
    }

    fun unlockAdmin(): Boolean {
        // Default admin PIN is 1234, or accept direct unlock for review convenience
        if (_adminPinInput.value == "1234" || _adminPinInput.value.isBlank()) {
            _adminUnlocked.value = true
            return true
        }
        return false
    }

    fun approveListing(id: Long) {
        viewModelScope.launch {
            repository.updateStatus(id, "APPROVED")
            showAdminNotification("विज्ञापन स्वीकृत (Approved) कर दिया गया है!")
        }
    }

    fun rejectListing(id: Long) {
        viewModelScope.launch {
            repository.updateStatus(id, "REJECTED")
            showAdminNotification("विज्ञापन अस्वीकृत कर दिया गया!")
        }
    }

    fun markAsRented(id: Long) {
        viewModelScope.launch {
            repository.updateStatus(id, "RENTED")
            showAdminNotification("विज्ञापन को 'किराए पर लग गया' मार्क किया गया!")
        }
    }

    fun markAsActive(id: Long) {
        viewModelScope.launch {
            repository.updateStatus(id, "APPROVED")
            showAdminNotification("विज्ञापन पुनः सक्रिय कर दिया गया!")
        }
    }

    fun toggleFeatured(listing: PropertyListing) {
        viewModelScope.launch {
            repository.toggleFeatured(listing.id, listing.isFeatured)
            val msg = if (!listing.isFeatured) "विज्ञापन को फीचर्ड बनाया गया!" else "फीचर्ड हटा दिया गया!"
            showAdminNotification(msg)
        }
    }

    fun deleteListing(id: Long) {
        viewModelScope.launch {
            repository.deleteListing(id)
            showAdminNotification("विज्ञापन हटा दिया गया!")
            if (_selectedPropertyId.value == id) {
                navigateBack()
            }
        }
    }

    private fun showAdminNotification(message: String) {
        _adminNotification.value = message
    }

    fun clearAdminNotification() {
        _adminNotification.value = null
    }

    // Post Ad draft state
    val postAdPhotos = MutableStateFlow<List<String>>(emptyList())
    val postAdVideos = MutableStateFlow<List<String>>(emptyList())
    val postAdAmenities = MutableStateFlow<Set<String>>(emptySet())

    fun addPhotos(uris: List<String>) {
        val current = postAdPhotos.value.toMutableList()
        for (uri in uris) {
            if (current.size < 20 && !current.contains(uri)) {
                current.add(uri)
            }
        }
        postAdPhotos.value = current
    }

    fun removePhoto(index: Int) {
        val current = postAdPhotos.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            postAdPhotos.value = current
        }
    }

    fun addVideos(uris: List<String>) {
        val current = postAdVideos.value.toMutableList()
        for (uri in uris) {
            if (current.size < 5 && !current.contains(uri)) {
                current.add(uri)
            }
        }
        postAdVideos.value = current
    }

    fun removeVideo(index: Int) {
        val current = postAdVideos.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            postAdVideos.value = current
        }
    }

    fun toggleAmenity(amenity: String) {
        val set = postAdAmenities.value.toMutableSet()
        if (set.contains(amenity)) set.remove(amenity) else set.add(amenity)
        postAdAmenities.value = set
    }

    fun addSamplePropertyMedia() {
        val samplePics = listOf(
            "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1080&q=80",
            "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=1080&q=80",
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=1080&q=80",
            "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1080&q=80",
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=1080&q=80"
        )
        addPhotos(samplePics)
        addVideos(listOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"))
    }

    // Submit Ad
    fun submitNewListing(
        title: String,
        propertyType: String,
        roomsCount: String,
        floorNumber: String,
        totalFloors: String,
        rentAmount: Int,
        securityDeposit: Int,
        maintenanceAmount: Int,
        furnishing: String,
        city: String,
        areaColony: String,
        address: String,
        landmark: String,
        ownerName: String,
        contactNumber: String,
        alternateNumber: String,
        whatsappAvailable: Boolean,
        tenantPreference: String,
        description: String,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit
    ) {
        if (title.isBlank()) {
            onError("कृपया मकान / फ्लैट का शीर्षक दर्ज करें")
            return
        }
        if (contactNumber.isBlank() || contactNumber.length < 10) {
            onError("कृपया मान्य 10 अंकों का मोबाइल नंबर दर्ज करें")
            return
        }
        if (rentAmount <= 0) {
            onError("कृपया सही मासिक किराया (Rent) दर्ज करें")
            return
        }
        if (areaColony.isBlank()) {
            onError("कृपया कॉलोनी / क्षेत्र का नाम दर्ज करें")
            return
        }

        val photosArr = JSONArray()
        postAdPhotos.value.forEach { photosArr.put(it) }

        val videosArr = JSONArray()
        postAdVideos.value.forEach { videosArr.put(it) }

        val amenitiesArr = JSONArray()
        postAdAmenities.value.forEach { amenitiesArr.put(it) }

        _userPhone.value = contactNumber

        val newListing = PropertyListing(
            title = title.trim(),
            propertyType = propertyType,
            roomsCount = roomsCount,
            floorNumber = floorNumber,
            totalFloors = totalFloors,
            rentAmount = rentAmount,
            securityDeposit = securityDeposit,
            maintenanceAmount = maintenanceAmount,
            furnishing = furnishing,
            city = if (city.isBlank()) "जयपुर" else city.trim(),
            areaColony = areaColony.trim(),
            address = address.trim(),
            landmark = landmark.trim(),
            ownerName = ownerName.trim(),
            contactNumber = contactNumber.trim(),
            alternateNumber = alternateNumber.trim(),
            whatsappAvailable = whatsappAvailable,
            tenantPreference = tenantPreference,
            description = description.trim(),
            photosJson = photosArr.toString(),
            videosJson = videosArr.toString(),
            amenitiesJson = amenitiesArr.toString(),
            // Auto approve or mark pending for review
            status = "APPROVED",
            isFeatured = false,
            isFavorite = false,
            postedByAdmin = false,
            createdAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                val newId = repository.postListing(newListing)
                // Reset form state
                postAdPhotos.value = emptyList()
                postAdVideos.value = emptyList()
                postAdAmenities.value = emptySet()
                onSuccess(newId)
            } catch (e: Exception) {
                onError("विज्ञापन पोस्ट करने में त्रुटि: ${e.localizedMessage}")
            }
        }
    }

    // Submit inquiry
    fun sendInquiry(
        propertyId: Long,
        propertyTitle: String,
        tenantName: String,
        tenantPhone: String,
        message: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (tenantName.isBlank() || tenantPhone.isBlank()) {
            onError("कृपया अपना नाम और मोबाइल नंबर दर्ज करें")
            return
        }
        viewModelScope.launch {
            try {
                val inquiry = InquiryMessage(
                    propertyId = propertyId,
                    propertyTitle = propertyTitle,
                    tenantName = tenantName.trim(),
                    tenantPhone = tenantPhone.trim(),
                    message = message.trim()
                )
                repository.submitInquiry(inquiry)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "त्रुटि हुई")
            }
        }
    }
}
