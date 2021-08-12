package com.example.voilaregistration.Model

data class DriverKycRequiredDoc(
    val id: Int,
    val required_docs_name: String,
    val required_docs_type: String,
    val status: Int
)