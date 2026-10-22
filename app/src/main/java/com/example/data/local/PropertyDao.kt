package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.InquiryMessage
import com.example.data.model.PropertyListing
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM property_listings WHERE status = 'APPROVED' ORDER BY isFeatured DESC, createdAt DESC")
    fun getAllApprovedListings(): Flow<List<PropertyListing>>

    @Query("SELECT * FROM property_listings ORDER BY createdAt DESC")
    fun getAllListingsAdmin(): Flow<List<PropertyListing>>

    @Query("SELECT * FROM property_listings WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingListings(): Flow<List<PropertyListing>>

    @Query("SELECT * FROM property_listings WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteListings(): Flow<List<PropertyListing>>

    @Query("SELECT * FROM property_listings WHERE id = :id")
    fun getListingById(id: Long): Flow<PropertyListing?>

    @Query("SELECT * FROM property_listings WHERE id = :id")
    suspend fun getListingByIdOnce(id: Long): PropertyListing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: PropertyListing): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<PropertyListing>)

    @Update
    suspend fun updateListing(listing: PropertyListing)

    @Query("UPDATE property_listings SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE property_listings SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE property_listings SET isFeatured = :isFeatured WHERE id = :id")
    suspend fun updateFeatured(id: Long, isFeatured: Boolean)

    @Query("DELETE FROM property_listings WHERE id = :id")
    suspend fun deleteListingById(id: Long)

    @Query("SELECT COUNT(*) FROM property_listings")
    suspend fun getCount(): Int

    // Inquiries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: InquiryMessage): Long

    @Query("SELECT * FROM inquiry_messages ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<InquiryMessage>>

    @Query("SELECT * FROM inquiry_messages WHERE propertyId = :propertyId ORDER BY timestamp DESC")
    fun getInquiriesForProperty(propertyId: Long): Flow<List<InquiryMessage>>
}
