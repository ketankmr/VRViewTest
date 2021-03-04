package com.example.vrviewtest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_image360.*

class Image360Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image360)
        video_view.initialize()

        val target = object :Target{
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Toast.makeText(this@Image360Activity,"onPrepareLoad",Toast.LENGTH_LONG).show()
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                Toast.makeText(this@Image360Activity,"onBitmapFailed",Toast.LENGTH_LONG).show()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                Toast.makeText(this@Image360Activity,"onBitmapLoaded",Toast.LENGTH_LONG).show()
                video_view.loadMedia(bitmap)
            }
        }

        PicassoUtils
            .getPicasso()
            .load("http://sensara-static-files.sensara.tv/uploads/android/71557812e5974be6bab55a1c08ecd0b2/interior_360.jpeg")
            .into(target)
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