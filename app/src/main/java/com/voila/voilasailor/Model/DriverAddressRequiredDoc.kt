package com.voila.voilasailor.Model

data class DriverAddressRequiredDoc(
    val id: Int,
    val required_docs_name: String,
    val required_docs_type: String,
    val status: Int
)