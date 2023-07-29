package com.example.memesharingapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMemeStringRequest(){
        // val textView = findViewById<TextView>(R.id.text)
// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        // val url = "https://www.google.com"
        val url = "https://meme-api.com/gimme"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                // textView.text = "Response is: ${response.substring(0, 500)}"
                // Log.d("Success Request", "Yes it worked")
                Log.d("Success Request", "Response is: ${response.substring(0, 500)}")
            },
            Response.ErrorListener {
                Log.d("Failure Request", "No it did not work")
            }
        )

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun loadMemeStandardRequest(){
        // Instantiate the RequestQueue.
        // val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"
        val imageView = findViewById<ImageView>(R.id.imageView1)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                //textView.text = "Response: %s".format(response.toString())
                val memeUrl = response.getString("url")
                currentImageURL = memeUrl
                Log.d("Success API Request",memeUrl)

               // Glide.with(this).load(memeUrl).into(imageView)

                 Glide.with(this).load(memeUrl).listener(object:RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean
                     ): Boolean {

                         progressBar.visibility = View.GONE
                         return false
                     }

                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {

                         progressBar.visibility = View.GONE
                         return false
                     }

                 }).into(imageView)
            },
            Response.ErrorListener { error ->

                Log.d("Failed API request","API request failed")
            }
        )

        // queue.add(jsonObjectRequest)
        // Access the RequestQueue through your singleton class.
      MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }
    private fun loadMeme(){
        // loadMemeStringRequest()
        loadMemeStandardRequest()


    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        // intent.putExtra(Intent.EXTRA_TEXT, "A meme from Reddit : $currentImageURL")
         intent.putExtra(Intent.EXTRA_TEXT, currentImageURL)
        val chooser = Intent.createChooser(intent,"Share this Reddit Meme using...")
       startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}