package com.example.memedex

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

//An App with memes which the users can share with their dear ones.
//
//An ImageView for the Meme
//A Share Button
//A Next Button
//Add ProgressBar
//
//Things to Learn:
//Volley Library [For network Requests]
//Volley Singleton Pattern
//Glide Library [For Image Handling]
//Share/Send Intent

class MainActivity : AppCompatActivity() {
    var currentMemeURL : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emptyStateTextView.visibility = View.INVISIBLE
        loadMeme()
    }

    fun loadMeme(){
//        var requestQueue = Volley.newRequestQueue(this)
//        It is recommended to create the requestQueue as a Singleton [Recommended by Google]
        var requestQueue = MySingleton.getInstance(this)
        val currentURL = "https://meme-api.herokuapp.com/gimme"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentURL, null,
            Response.Listener { response ->
                currentMemeURL = response.getString("url")
                Glide.with(this).load(currentMemeURL).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        memeProgressBar.visibility = View.GONE
                        emptyStateTextView.visibility = View.VISIBLE
//                        Toast.makeText(this@MainActivity, "Check your Internet Connection", Toast.LENGTH_LONG)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        memeProgressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        requestQueue.addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Checkout this cool meme which I found in MemeDex from Srivathsan!!! $currentMemeURL")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    fun nextMeme(view: View) {
        loadMeme()
    }
}