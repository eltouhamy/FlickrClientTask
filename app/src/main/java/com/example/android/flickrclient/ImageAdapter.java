package com.example.android.flickrclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mohamed on 20/04/17.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Photo> mPhotosArrayList;

    public ImageAdapter(Context mContext, ArrayList<Photo> mPhotosArrayList) {
        this.mContext = mContext;
        this.mPhotosArrayList = mPhotosArrayList;
    }

    @Override
    public int getCount() {
        return mPhotosArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPhotosArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        String url="https://farm"+mPhotosArrayList.get(i).getFarm()
                +".staticflickr.com/"+mPhotosArrayList.get(i).getServer()
                +"/"+mPhotosArrayList.get(i).getId()+"_"
                +mPhotosArrayList.get(i).getSecret()+"_h.jpg";

        String id,farm,server,secret;



        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);

        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);

        Picasso.with(mContext).load(url).into(imageView);


         return convertView;

    }
}
