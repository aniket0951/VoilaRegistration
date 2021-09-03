package com.voila.voilasailor.NetworkResponse

import com.voila.voilasailor.Model.RequireRestaurantDocs

data class GetAllRestaurantDocsResponse(
        val message: String,
        val requiredDocs: List<RequireRestaurantDocs>,
        val result: Boolean,
        var processComplete : String
 )
