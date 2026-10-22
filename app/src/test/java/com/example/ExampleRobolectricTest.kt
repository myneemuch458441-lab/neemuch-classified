package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.PropertyListing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("किराया साथी", appName)
  }

  @Test
  fun `verify property listing model attributes`() {
    val listing = PropertyListing(
      id = 1,
      title = "2 BHK Flat",
      propertyType = "FLAT",
      roomsCount = "2 BHK",
      floorNumber = "2nd Floor",
      rentAmount = 12000,
      securityDeposit = 24000,
      areaColony = "Malviya Nagar",
      address = "Street 1",
      ownerName = "Rajesh Sharma",
      contactNumber = "9829012345"
    )

    assertEquals(12000, listing.rentAmount)
    assertEquals(24000, listing.securityDeposit)
    assertEquals("2 BHK", listing.roomsCount)
    assertEquals("फ्लैट / अपार्टमेंट", listing.localizedPropertyType)
    assertNotNull(listing.createdAt)
  }
}
