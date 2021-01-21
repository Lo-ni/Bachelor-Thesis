package de.asta.hochschule.trier.verleih.app.component

import android.content.Context
import android.util.*
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import de.asta.hochschule.trier.verleih.R
import de.hdodenhof.circleimageview.CircleImageView

class AppBarContent @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
	
	var titleTextView: MaterialTextView
	var profileImageView: CircleImageView
	
	init {
		val styledAttributes = context.obtainStyledAttributes(
			attrs,
			R.styleable.AppBarContent, 0, 0
		)
		val title = styledAttributes.getString(R.styleable.AppBarContent_title)
		val image =
			styledAttributes.getResourceId(R.styleable.AppBarContent_profileImage, R.drawable.chick)
		styledAttributes.recycle()
		
		LayoutInflater.from(context).inflate(R.layout.appbar_content, this, true)
		
		titleTextView = (getChildAt(0) as ViewGroup).getChildAt(1) as MaterialTextView
		titleTextView.text = title
		
		profileImageView = (getChildAt(0) as ViewGroup).getChildAt(0) as CircleImageView
		profileImageView.setImageDrawable(ContextCompat.getDrawable(context, image))
		profileImageView.setOnClickListener { Log.d("AppBarContent", "onClick") }
	}
	
	fun setTitle(title: String) {
		titleTextView.text = title
	}
	
	fun setProfileImage(drawableId: Int) {
		profileImageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
	}
	
	fun setProfileImageClick(listener: OnClickListener) {
		profileImageView.setOnClickListener(listener)
	}
	
}