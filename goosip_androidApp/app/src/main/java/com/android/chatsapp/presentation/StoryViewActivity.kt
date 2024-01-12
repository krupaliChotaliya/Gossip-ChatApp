package com.android.chatsapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.chatsapp.databinding.ActivityStoryViewBinding
import com.android.chatsapp.model.Story
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView

class StoryViewActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var binding: ActivityStoryViewBinding
    private var counter: Int = 0
    private var TAG: String = "StoryViewActivity"
    private var pressTime: Long = 0
    private val limit: Long = 500
    private lateinit var storiesList:ArrayList<Story>
    private var username: String? =null
    private var profileImg:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stories.setStoriesListener(this)

        try {
            storiesList = intent.getParcelableArrayListExtra("story")!!
            username=intent.getStringExtra("username")
            profileImg=intent.getStringExtra("profileImg")

        }catch (e:Exception){
            Log.i(TAG,e.toString())
        }


        //prev move
        binding.prev.setOnClickListener {
            binding.stories.reverse()
        }
        binding.prev.setOnTouchListener(onTouchListener)

        //next move
        binding.next.setOnClickListener {
            binding.stories.skip()
        }
        binding.next.setOnTouchListener(onTouchListener)


        // showing the back button in action bar
        var actionBar = this.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        initializeStories()


    }



    private fun initializeStories() {
        if (storiesList.isNotEmpty()) {
            binding.stories.setStoriesCount(storiesList.size)
            Picasso.get().load(profileImg).into(binding.profileImage)
            binding.username.text=username
            binding.storyTime.text=formatTimestamp(storiesList[0].timestamp)
            binding.stories.setStoryDuration(3000L)

            Picasso.get().load(storiesList[0].story).into(binding.image, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.i(TAG, "success")
                    binding.stories.startStories(counter)
                }
                override fun onError(e: Exception?) {
                    Log.i(TAG, "Error" + e.toString())
                }
            })
        }
    }

    override fun onNext() {
        Log.i(TAG, "onNext")
        if (counter < storiesList.size - 1) {
            counter++
            Picasso.get().load(storiesList[counter].story).into(binding.image)
            binding.storyTime.text=formatTimestamp(storiesList[counter].timestamp)
        } else {
            onComplete()
        }
    }

    override fun onPrev() {
        Log.i(TAG, "onPrev")
        if (counter > 0) {
            counter--
            Picasso.get().load(storiesList[counter].story).into(binding.image)
            binding.storyTime.text=formatTimestamp(storiesList[counter].timestamp)
        }
    }

    override fun onComplete() {
        counter = 0
        finish()
    }

    private val onTouchListener = View.OnTouchListener { _, event ->
        Log.i(TAG, "touch")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                binding.stories.pause()
                true
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                binding.stories.resume()
                if (limit < now - pressTime) {
                    onNext() // Call onNext if the touch duration is long enough
                }
                true
            }
            else -> false
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - timestamp

        val seconds = timeDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}hr ago"
            minutes > 0 -> "${minutes}min ago"
            else -> "${seconds}s ago"
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
