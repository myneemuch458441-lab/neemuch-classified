package com.example.data.repository

import com.example.data.local.PropertyDao
import com.example.data.local.SampleData
import com.example.data.model.InquiryMessage
import com.example.data.model.PropertyListing
import kotlinx.coroutines.flow.Flow

class PropertyRepository(private val propertyDao: PropertyDao) {

    val approvedListings: Flow<List<PropertyListing>> = propertyDao.getAllApprovedListings()
    val allListingsAdmin: Flow<List<PropertyListing>> = propertyDao.getAllListingsAdmin()
    val pendingListings: Flow<List<PropertyListing>> = propertyDao.getPendingListings()
    val favoriteListings: Flow<List<PropertyListing>> = propertyDao.getFavoriteListings()
    val allInquiries: Flow<List<InquiryMessage>> = propertyDao.getAllInquiries()

    suspend fun checkAndSeedInitialData() {
        val count = propertyDao.getCount()
        if (count == 0) {
            propertyDao.insertListings(SampleData.getInitialProperties())
        }
    }

    fun getListingById(id: Long): Flow<PropertyListing?> = propertyDao.getListingById(id)

    suspend fun getListingByIdOnce(id: Long): PropertyListing? = propertyDao.getListingByIdOnce(id)

    suspend fun postListing(listing: PropertyListing): Long {
        return propertyDao.insertListing(listing)
    }

    suspend fun updateListing(listing: PropertyListing) {
        propertyDao.updateListing(listing)
    }

    suspend fun updateStatus(id: Long, status: String) {
        propertyDao.updateStatus(id, status)
    }

    suspend fun toggleFavorite(id: Long, current: Boolean) {
        propertyDao.updateFavorite(id, !current)
    }

    suspend fun toggleFeatured(id: Long, current: Boolean) {
        propertyDao.updateFeatured(id, !current)
    }

    suspend fun deleteListing(id: Long) {
        propertyDao.deleteListingById(id)
    }

    suspend fun submitInquiry(inquiry: InquiryMessage): Long {
        return propertyDao.insertInquiry(inquiry)
    }
}
