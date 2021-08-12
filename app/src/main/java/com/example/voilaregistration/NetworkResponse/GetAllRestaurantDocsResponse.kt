package com.example.voilaregistration.NetworkResponse

import com.example.voilaregistration.Model.RequireRestaurantDocs
import com.example.voilaregistration.Model.RequiredDoc

data class GetAllRestaurantDocsResponse(
        val message: String,
        val requiredDocs: List<RequireRestaurantDocs>,
        val result: Boolean
 )
