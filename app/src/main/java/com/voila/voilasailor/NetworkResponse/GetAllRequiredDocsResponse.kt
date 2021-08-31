package com.voila.voilasailor.NetworkResponse

import com.voila.voilasailor.Model.RequiredDoc

data class GetAllRequiredDocsResponse(
        val message: String,
        val requiredDocs: List<RequiredDoc>,
        val result: Boolean
)