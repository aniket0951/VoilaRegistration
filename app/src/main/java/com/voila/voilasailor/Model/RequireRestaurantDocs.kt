package com.voila.voilasailor.Model

data class RequireRestaurantDocs(
        val restaurant_owner_required_docs : List<RestaurantOwnerRequiredDoc>,
        val restaurant_details_required_docs : List<RestaurantDetailsRequiredDoc>
)
