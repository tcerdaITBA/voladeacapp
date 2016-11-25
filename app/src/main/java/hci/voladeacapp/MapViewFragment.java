package hci.voladeacapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MapViewFragment extends Fragment {
    MapView mMapView;
    private GoogleMap GMap;

    private String fromCity;
    private ArrayList<DealGson> deals;
    private Map<Marker, DealGson> markerDeals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Creating map fragment");
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        getActivity().findViewById(R.id.promo_card_list).setVisibility(View.GONE);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                GMap = mMap;
                //TODO: se podría mover la camara a la ciudad de salida
//              CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//              GMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
                System.out.println("CITY: " + fromCity);

                GMap.setOnInfoWindowClickListener(new mapPromoDetailsListener());
            }
        });

        return rootView;
    }

    public static MapViewFragment newInstance(ArrayList<DealGson> deals, String fromCity)
    {
        MapViewFragment newFragment = new MapViewFragment();
        newFragment.deals = deals;
        newFragment.fromCity = fromCity;
        newFragment.markerDeals = new HashMap<>();

        return newFragment;
    }

    public class FillMapTask extends AsyncTask<ArrayList<DealGson>, Void, Void> {
        private final Double LOW_OFFER = 200.0;
        private final Double MID_OFFER = 500.0;

        @Override
        protected Void doInBackground(ArrayList<DealGson>... dealsArrays) {
            clearMap();
            for (DealGson d: dealsArrays[0]) {
                addNewMarker(d);
            }
            return null;
        }

        private void addNewMarker(final DealGson deal) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String cityName = deal.city.name;
                    LatLng pos = new LatLng(deal.city.latitude, deal.city.longitude);
                    MarkerOptions marker = new MarkerOptions().position(pos)
                                                            .title(cityName.split(",")[0])
                                                            .snippet("U$D " + deal.price)
                            .icon(BitmapDescriptorFactory.defaultMarker(getColor(deal.price)))
                            ;
                    Marker m = GMap.addMarker(marker);
                    markerDeals.put(m, deal);
                    GMap.addMarker(marker);
                }
            });
        }

        private void clearMap() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GMap.clear();
                }
            });
        }

        private float getColor(Double price) {
            if(price < LOW_OFFER){
                return BitmapDescriptorFactory.HUE_GREEN;
            }
            else if(price < MID_OFFER){
                return BitmapDescriptorFactory.HUE_YELLOW;
            }

            return BitmapDescriptorFactory.HUE_RED;
        }


    }


    public void updateMap(ArrayList<DealGson> deals, String fromCity) {
        this.deals = deals;
        this.fromCity = fromCity;
        System.out.println("UPDATING MAP");
        if (GMap == null)
            return;

        new FillMapTask().execute(deals);
    }

    private class mapPromoDetailsListener implements GoogleMap.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick(Marker marker) {
            //TODO
            System.out.println("CLICKED INFO WINDOW! " + markerDeals.get(marker).city + " " + markerDeals.get(marker).price);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}