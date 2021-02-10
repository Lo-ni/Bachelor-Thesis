package de.asta.hochschule.trier.verleih.rental.model

class RentalObject() {
	
	var name: String? = null
	var description: String? = null
	var picture_name: String? = null
	var quantity: Int? = null
	var components: Map<String, Map<String, Any>>? = null
	
	constructor(
		name: String?,
		description: String?,
		picture_name: String?,
		quantity: Int?,
		components: Map<String, Map<String, Any>>?
	) : this() {
		this.name = name
		this.description = description
		this.picture_name = picture_name
		this.quantity = quantity
		this.components = components
	}
}