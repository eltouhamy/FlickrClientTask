package com.example.android.flickrclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by mohamed on 21/04/17.
 */

public class FullScreenFragment extends Fragment {
    Intent intent;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_full_screen, container, false);
        intent=getActivity().getIntent();
        String photo_Url=intent.getStringExtra("photo_url");
        imageView=(ImageView) root.findViewById(R.id.fullscreen_image);

        Picasso.with(getActivity()).load(photo_Url).into(imageView);



        return root;

    }
}
