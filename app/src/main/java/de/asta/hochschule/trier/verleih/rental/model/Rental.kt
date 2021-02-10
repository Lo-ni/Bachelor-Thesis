package de.asta.hochschule.trier.verleih.rental.model

class Rental() {
	var timestamp: String? = null
	var pickupdate: String? = null
	var returndate: String? = null
	var eventname: String? = null
	var status: String? = null
	var objects: MutableMap<String, MutableMap<String, Int>?>? = null
	
	constructor(
		timestamp: String?,
		pickupdate: String?,
		returndate: String?,
		eventname: String?,
		status: String?,
		objects: MutableMap<String, MutableMap<String, Int>?>?
	) : this() {
		this.timestamp = timestamp
		this.pickupdate = pickupdate
		this.returndate = returndate
		this.eventname = eventname
		this.status = status
		this.objects = objects
	}
	
}