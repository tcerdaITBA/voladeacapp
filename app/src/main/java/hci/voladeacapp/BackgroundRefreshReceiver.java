package hci.voladeacapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

import static hci.voladeacapp.ApiService.DATA_FLIGHT_GSON;
import static hci.voladeacapp.MisVuelosFragment.ACTION_GET_REFRESH;


public class BackgroundRefreshReceiver extends BroadcastReceiver {

    public static final String TIME_TO_PULL = "hci.voladeacapp.TIME_TO_PULL";

    public BackgroundRefreshReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getBooleanExtra(ApiService.API_REQUEST_ERROR, false)){
            //Error de conexion, no puedo hacer nada :(
            return;
        }

        ArrayList<Flight> flight_details = StorageHelper.getFlights(context.getApplicationContext());

        if(intent.getAction().equals(ACTION_GET_REFRESH)){
                FlightStatusGson updatedGson = (FlightStatusGson)intent.getSerializableExtra(DATA_FLIGHT_GSON);
                if(updatedGson == null)
                     return;
                 int idx = flight_details.indexOf(new Flight(updatedGson));
                 if(idx == -1){
                    return;
                 }
                Flight toUpdate = flight_details.get(idx);
                List<NotificationCategory> changes = toUpdate.update(updatedGson);
                FlightSettings flightSettings = StorageHelper.getSettings(context, toUpdate.getIdentifier());
                for(NotificationCategory change: changes){
                    if(flightSettings.notificationsActive() && flightSettings.isActive(change)) {
                        System.out.println("UPDATING FLIGHT " + toUpdate.getIdentifier() + " " + change);
                        NotificationCreator.createNotification(context, toUpdate, change);
                    } else{
                        System.out.println("FLIGHT " + toUpdate.getIdentifier() + " has " + change + "turned off");
                    }
                }

                StorageHelper.saveFlight(context, toUpdate);

                if(changes.isEmpty()){
                    System.out.println("Nothing changed for" + toUpdate.toString());
                }else{
                    System.out.println("Sending refresh for " + toUpdate.toString());
                    context.sendBroadcast(new Intent(MisVuelosFragment.FLIGHTS_REFRESHED));
                }


        }

    }


}





