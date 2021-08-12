package com.example.voilaregistration.NetworkResponse

import com.example.voilaregistration.Model.RequiredDoc

data class GetAllRequiredDocsResponse(
        val message: String,
        val requiredDocs: List<RequiredDoc>,
        val result: Boolean
)