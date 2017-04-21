package com.example.android.flickrclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by mohamed on 21/04/17.
 */

public class FullScreenActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);


        if(savedInstanceState==null){

            getFragmentManager().beginTransaction().add(R.id.fullScreen_fragment_container,new FullScreenFragment()).commit();
        }

    }
}
