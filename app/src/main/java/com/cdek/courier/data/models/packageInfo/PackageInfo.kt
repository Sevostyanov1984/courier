package com.cdek.courier.data.models.packageInfo

import com.cdek.courier.data.models.entities.task.Place
import com.cdek.courier.utils.DEFAULT_STATE

class PackageInfo(places: List<Place>?) {
    val placesCount: Int
    val totalWeight: Double
    val description: String

    init {
        var totalWeight: Double = DEFAULT_STATE.toDouble()
        var count: Int = DEFAULT_STATE
        var description = "Многоместный"
        if (places != null && !places.isEmpty()) {
            count = places.size
            try {
                for (place in places) {
                    if (place.weight != null)
                        totalWeight += place.weight!!.toDouble()
                }
            } catch (e: Exception) {
                totalWeight = DEFAULT_STATE.toDouble()
            }
            if (count == 1) {
                if (places[0].description != null) {
                    description = places[0].description!!
                }
            }
        }
        this.totalWeight = totalWeight
        placesCount = count
        this.description = description
    }
}