package com.example.vrviewtest

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewOptions = VrPanoramaView.Options()
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO
        pano_view.setPureTouchTracking(true)
        pano_view.setFullscreenButtonEnabled(false)
        pano_view.setInfoButtonEnabled(false)
        pano_view.setStereoModeButtonEnabled(false)
        pano_view.loadImageFromBitmap(BitmapFactory.decodeResource(resources,R.drawable.interior),viewOptions)
    }

    private var posX: Float = 500.0f
    private var posY: Float = 500.0f

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        var action: Int = -1

        if(event?.keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
            if(event.action==KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posX -= 10.0f
            }
            else if(event.action==KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        else if(event?.keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
            if(event.action==KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posX += 10.0f
            }
            else if(event.action==KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        if(event?.keyCode==KeyEvent.KEYCODE_DPAD_UP){
            if(event.action==KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posY += 10.0f
            }
            else if(event.action==KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }
        else if(event?.keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
            if(event.action==KeyEvent.ACTION_DOWN){
                action = MotionEvent.ACTION_MOVE
                posY -= 10.0f
            }
            else if(event.action==KeyEvent.ACTION_UP){
                action = MotionEvent.ACTION_UP
            }
        }

        if(event?.keyCode==KeyEvent.KEYCODE_DPAD_RIGHT
            || event?.keyCode==KeyEvent.KEYCODE_DPAD_LEFT
            || event?.keyCode==KeyEvent.KEYCODE_DPAD_UP
            || event?.keyCode==KeyEvent.KEYCODE_DPAD_DOWN){

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

            //pano_view.renderingView?.dispatchTouchEvent(motionEvent)
            return true
        }

        return super.dispatchKeyEvent(event)
    }
}