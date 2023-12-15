package com.jnu.student;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.student.data.BookLocation;
import com.jnu.student.data.DataDownload;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;


public class mapFragment extends Fragment {


    public mapFragment() {
        // Required empty public constructor
    }


    public static mapFragment newInstance(String param1, String param2) {
        mapFragment fragment = new mapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public class DataDownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return new DataDownload().download(urls[0]);
        }
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            if (responseData != null) {
                ArrayList<BookLocation> shopLocations= new DataDownload().parseJsonObjects(responseData);
                TencentMap tencentMap = mapView.getMap();
                for (BookLocation shopLocation : shopLocations) {
                    LatLng point1 = new LatLng(shopLocation.getLatitude(), shopLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions(point1)
                            .title(shopLocation.getName());
                    Marker marker = tencentMap.addMarker(markerOptions);


                }
            }
        }
    }


    private com.tencent.tencentmap.mapsdk.maps.TextureMapView mapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        mapView = rootView.findViewById(R.id.mapView);

        new DataDownloadTask().execute("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        TencentMap tencentMap = mapView.getMap();

        LatLng point1 = new LatLng(22.255453, 113.54145);
        tencentMap.moveCamera(CameraUpdateFactory.newLatLng(point1));

        // 创建一个Marker对象
        MarkerOptions markerOptions = new MarkerOptions(point1)
                .title("标记标题");

        // 添加标记到地图上
        Marker marker = tencentMap.addMarker(markerOptions);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}