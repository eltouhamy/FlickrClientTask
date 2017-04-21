package com.example.android.flickrclient;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mohamed on 19/04/17.
 */

public class MainFragment  extends Fragment{
    GridView photos_gridView;
    SwipeRefreshLayout swipeRefreshLayout;
    int Lastposition=0;
    ArrayList<Photo>mPhotos;
    ArrayList<Photo> TwentyPhoto=new ArrayList<Photo>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View root=inflater.inflate(R.layout.fragment_main,container,false);

        // execute the asyTask in background thread

        GetPhotoList asyObj=new GetPhotoList();
        asyObj.execute();


        photos_gridView=(GridView) root.findViewById(R.id.photos_gridview);



        photos_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get photo object from mphotos ArrayList in i position(selected position)

                Photo photo=mPhotos.get(i);

                //set photo url for selected photo.

                String url="https://farm"+mPhotos.get(i).getFarm()
                        +".staticflickr.com/"+mPhotos.get(i).getServer()
                        +"/"+mPhotos.get(i).getId()+"_"
                        +mPhotos.get(i).getSecret()+"_h.jpg";

                 // send aphoto url to the FullScreenActivity

                Intent intent=new Intent(getActivity(),FullScreenActivity.class).putExtra("photo_url",url);
                startActivity(intent);
            }
        });

        swipeRefreshLayout=(SwipeRefreshLayout) root.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               GetPhotoList obj=new GetPhotoList();
                obj.execute();
                Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        photos_gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {

                int LastVisableItem=firstVisibleItem+visibleItemCount;

                 //if we reach to the end of the gridview --> Load more
                if(LastVisableItem==Lastposition){

                    //Load another 20 photo from mphots arrylist that returned from AsyTask

                    Toast.makeText(getActivity(),"Loading",Toast.LENGTH_SHORT).show();

                    for(int i=Lastposition;i<Lastposition+20;i++){

                        if(mPhotos!=null && i<mPhotos.size()){

                            TwentyPhoto.add(mPhotos.get(i));
                        }
                    }


                    // update the Last position

                    Lastposition=Lastposition+20;

                    if(TwentyPhoto!=null) {

                        photos_gridView.setAdapter(new ImageAdapter(getActivity(), TwentyPhoto));
                        photos_gridView.setSelection(firstVisibleItem);
                    }

                }


            }
        });






        return root;
    }

    private class GetPhotoList extends AsyncTask<Void,Void,ArrayList<Photo>>{




        @Override
        protected ArrayList<Photo> doInBackground(Void... voids) {



            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            InputStream inputStream=null;
            StringBuffer buffer=new StringBuffer();
            String mjson="";
            ArrayList<Photo>result=new ArrayList<Photo>();

          //  My API KEY IS 04c0972fc03c1324ad81eccb17030872;


                try {
                    URL url=new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&" +
                            "api_key=04c0972fc03c1324ad81eccb17030872&" +
                            "tags=trending%2Cpopular&per_page=500&format=json&nojsoncallback=1");

                    urlConnection=(HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    inputStream=urlConnection.getInputStream();

                    if (inputStream == null) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {

                        return null;
                    }
                    mjson=buffer.toString();
                    Log.v("ReturnedJson-->",mjson);
                    try {

                        result=getListOfPhotos(mjson);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            super.onPostExecute(photos);
             TwentyPhoto=new ArrayList<Photo>();

            // get only the first 20 photo and put it in Twenty photo ArrayList
            // and the set Twenty photo as a parametar for the ImageAdapter

            for(int i=0;i<20;i++){
                TwentyPhoto.add(photos.get(i));

            }
            // if i need to load more image from mphotos i will start from this LastPosition
            // so i need to  update it after each Load
            Lastposition=20;

            photos_gridView.setAdapter(new ImageAdapter(getActivity(),TwentyPhoto));
            mPhotos=photos;

        }

    }
    public ArrayList<Photo> getListOfPhotos(String json) throws JSONException {

        String id,secret,server,farm;
        JSONObject resultForOnePhoto=new JSONObject();
        ArrayList<Photo>result=new ArrayList<Photo>();


        JSONObject returnedJsonObject=new JSONObject(json);
        JSONObject photosJsonObject=returnedJsonObject.getJSONObject("photos");
        JSONArray photosArray=photosJsonObject.getJSONArray("photo");

        for(int i=0;i<photosArray.length();i++){

            resultForOnePhoto=photosArray.getJSONObject(i);

            id=resultForOnePhoto.getString("id").toString();
            secret=resultForOnePhoto.getString("secret").toString();
            server=resultForOnePhoto.getString("server").toString();
            farm=resultForOnePhoto.getString("farm").toString();

            Photo photo=new Photo(secret,id,server,farm);

            result.add(photo);


        }

        return result;
    }
}
