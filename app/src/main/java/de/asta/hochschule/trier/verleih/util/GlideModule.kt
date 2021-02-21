package de.asta.hochschule.trier.verleih.util

import android.content.Context
import com.bumptech.glide.*
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream

@GlideModule
class GlideModule : AppGlideModule() {
	
	override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
		// register FirebaseImageLoader to handle StorageReference
		registry.append(
			StorageReference::class.java, InputStream::class.java,
			FirebaseImageLoader.Factory()
		)
	}
	
}