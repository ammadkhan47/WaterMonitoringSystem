package com.example.monitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
/**/
public class Map extends AppCompatActivity implements OnMapReadyCallback  {





    String query;
    GoogleMap mMap;
    DatabaseReference db;
    List<Marker> mMarkers = new ArrayList<Marker>();
    TextView tv;
    public int n;





    LatLngBounds.Builder bld;
    LatLng ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


       SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);



    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;




        db= FirebaseDatabase.getInstance().getReference().child("Gis");


        ////////----------------------









            db= FirebaseDatabase.getInstance().getReference().child("Gis");


            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                        String turbl= Snapshot.child("Turb").getValue().toString() ;
                        String phl= Snapshot.child("PH").getValue().toString() ;
                        String quality="none";


                        if(turbl.length()>4) {
                            turbl = turbl.substring(0, 4);
                        }


                        double turb=    Double.valueOf(turbl);
                        double ph=   Double.valueOf(phl);
                        if( ( (turb>1.30)&&(turb<1.60) )&&( (ph>=5.00)&&(ph<9)) ) {
                            quality="Good";

                        }

                        if(((turb>0.00)&&(turb<1.30))||(((ph>=0.00)&&(ph<5))||(ph>8))){
                            quality="Bad";

                        }



                        int i=0;

                        ll = new LatLng(Double.parseDouble(Snapshot.child("Lat").getValue().toString()), Double.parseDouble(Snapshot.child("Long").getValue().toString()));

                        db= FirebaseDatabase.getInstance().getReference().child("Gis");


                        mMarkers.add(mMap.addMarker(new MarkerOptions().position(ll).title("Water Quality: " + quality).snippet("PH:"+phl+" Turb:"+turbl )));

                        mMarkers.get(i).setTag(0);
                        i++;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // Add some markers to the map, and add a data object to each marker.


            // Set a listener for marker click.
            //mMap.setOnMarkerClickListener(OnMapReadyCallback this);





      }



    //--------------------------------------------------------------





/*

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the
        // contents and look of the
        // info window.
        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events. See the bottom of this class for
        // their behavior.
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        mMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.google_map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    bld = new LatLngBounds.Builder();
                    for (int i = 0; i < n; i++) {


                        query=String.valueOf(i+1);
                        db = db.child(query);


                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                 ll = new LatLng(Double.parseDouble(dataSnapshot.child("Lat").getValue().toString()), Double.parseDouble(dataSnapshot.child("Long").getValue().toString()));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        bld.include(ll);
                    }
                    LatLngBounds bounds = bld.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));


                }
            });
        }
    }


    private void addMarkersToMap() {
        mMap.clear();


        for (int i = 0; i < n; i++) {


            query=String.valueOf(i+1);
            db = db.child(query);


            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             ll = new LatLng(Double.parseDouble(dataSnapshot.child("Lat").getValue().toString()), Double.parseDouble(dataSnapshot.child("Long").getValue().toString()));

                    mMarkers.add(mMap.addMarker(new MarkerOptions().position(ll).title("Name")));

                    //Log.i(TAG,"Car number "+i+"  was added " +mMarkers.get(mMarkers.size()-1).getId());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            To set the color of marker-------------------------------------



            BitmapDescriptor bitmapMarker;

            switch (Cars.get(i).getState()) {
                case 0:
                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    Log.i(TAG, "RED");
                    break;
                case 1:
                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    Log.i(TAG, "GREEN");
                    break;
                case 2:
                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                    Log.i(TAG, "ORANGE");
                    break;
                default:
                    bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    Log.i(TAG, "DEFAULT");
                    break;
            }
            ------------------------------------




        }
    } */

}




