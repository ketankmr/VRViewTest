package com.example.vrviewtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_image360.*
import java.lang.Exception

class Image360Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image360)
        video_view.initialize()

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val path = intent?.getStringExtra("url")
                path?.let {
                    loadImage(it)
                }
            }
        }, IntentFilter("co.sensara.vrviewtest.LoadImage"))
    }

    private fun loadImage (imagePath:String){

        Glide.with(this)
                .asBitmap()
                .load(imagePath)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        video_view.loadMedia(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
    }

    private var posX: Float = 0.0f
    private var posY: Float = 0.0f

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        var action: Int = -1

        if(event?.keyCode== KeyEvent.KEYCODE_DPAD_RIGHT){
            if(event.action== KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posX += 10.0f
            }
            else if(event.action== KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        else if(event?.keyCode== KeyEvent.KEYCODE_DPAD_LEFT){
            if(event.action== KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posX -= 10.0f
            }
            else if(event.action== KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        if(event?.keyCode== KeyEvent.KEYCODE_DPAD_UP){
            if(event.action== KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posY += 10.0f
            }
            else if(event.action== KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        else if(event?.keyCode== KeyEvent.KEYCODE_DPAD_DOWN){
            if(event.action== KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posY -= 10.0f
            }
            else if(event.action== KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }

        if(event?.keyCode== KeyEvent.KEYCODE_DPAD_RIGHT
            || event?.keyCode== KeyEvent.KEYCODE_DPAD_LEFT
            || event?.keyCode== KeyEvent.KEYCODE_DPAD_UP
            || event?.keyCode== KeyEvent.KEYCODE_DPAD_DOWN){

            val downTime : Long = System.currentTimeMillis()
            val eventTime : Long = System.currentTimeMillis()
            val metaState : Int = 0
            val motionEvent : MotionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                action,
                posX,
                posY,
                metaState
            )

            video_view.dispatchTouchEvent(motionEvent)
            return true
        }

        return super.dispatchKeyEvent(event)
    }
}