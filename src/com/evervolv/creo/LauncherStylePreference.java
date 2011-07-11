/*
 * Copyright (C) 2011, The Evervolv Project.
 * Portions Copyright (C) 2008 The Android Open Source Project
 *  * Portions Copyright (C) 2008, T-Mobile USA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evervolv.creo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import com.evervolv.creo.R;

public class LauncherStylePreference extends Activity implements OnDoubleTapListener, OnGestureListener, OnClickListener {

    private static final String TAG = "LauncherStylePreference";
    public static final String LAUNCHER_STYLE = "pref_key_launcher_style";
    
    private Gallery mGallery;
	private Button applyButton;
    
    private SharedPreferences mSharedPrefs;
	private GestureDetector gesturedetector = null;
	private ProgressDialog mProgressDialog;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        inflateActivity();
    }

    private void inflateActivity() {
        setContentView(R.layout.styles_main);
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");

        gesturedetector = new GestureDetector(this, this);
        gesturedetector.setOnDoubleTapListener(this);
        
        mGallery = (Gallery) findViewById(R.id.gallery);
        mGallery.setAdapter(new StylesAdapter(this));
        mGallery.setSelection(Integer.valueOf(mSharedPrefs.getString(LauncherPreferences.LAUNCHER_STYLE, "1")));
        mGallery.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				savePreference();
				return true;
			}
        });

        applyButton = (Button)findViewById(R.id.apply);
        applyButton.setOnClickListener(this);
    }
	
    private void savePreference() {
        mSharedPrefs.edit().putString(LauncherPreferences.LAUNCHER_STYLE , 
        		Integer.toString(mGallery.getSelectedItemPosition())).commit();
        mProgressDialog.show();
        new Handler().postDelayed(
        		new Runnable() {
        			@Override public void run() {
        				mProgressDialog.dismiss();
        				LauncherStylePreference.this.finish();
        				}
        			}, 550);
        /*
         * Generally, 100 to 200ms is the threshold beyond which users will perceive lag in an application.
         */
    }

	@Override
	public void onClick(View v) {
		
		if (v == applyButton) {
			savePreference();
		}
		
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
            return gesturedetector.onTouchEvent(event);
    }

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		savePreference();
		return false;
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.hold, R.anim.drawer_fade_out);
	}
	
	/* Land of the forgotten classes */
	
	public boolean onDoubleTapEvent(MotionEvent e) {return false;}
	public boolean onSingleTapConfirmed(MotionEvent e) {return false;}
	public boolean onDown(MotionEvent e) {return false;}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return false;}
	public void onLongPress(MotionEvent e) {}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
	public void onShowPress(MotionEvent e) {}
	public boolean onSingleTapUp(MotionEvent e) {return false;}
    
}
