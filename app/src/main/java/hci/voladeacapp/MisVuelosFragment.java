package hci.voladeacapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;

public class MisVuelosFragment extends Fragment {

    public final static String INSTANCE_TAG = "hci.voladeacapp.MisVuelos.INSTANCE_TAG";
    public final static int GET_FLIGHT = 1;

    private ListView flightsListView;

    ArrayList<Flight> flight_details;
    FlightListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_misvuelos, parent, false);
        flightsListView = (ListView) rootView.findViewById(R.id.text_mis_vuelos);

        populateList();

        flightsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //ACA A LO QUE PASA CUANDO HACE CLICK
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = flightsListView.getItemAtPosition(position);
                Flight flightData = (Flight) o;

                Intent detailIntent = new Intent(getActivity(), FlightDetails.class);
                detailIntent.putExtra("Flight",flightData);

                startActivity(detailIntent);

            }
        });

        FloatingActionButton addButton = (FloatingActionButton)rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getActivity(),"Aca se deberia agregar un vuelo", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(v.getContext(), AddFlightActivity.class), GET_FLIGHT);
            }
        });

        return rootView;
    }

    private void populateList() {
        flight_details = getListData();
        adapter = new FlightListAdapter(getActivity(),flight_details);
        flightsListView.setAdapter(adapter);
    }


    protected void addToList(Flight f){
        flight_details.add(f);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getActivity(), "Recibi resultado", Toast.LENGTH_SHORT)
                    .show();

            FlightStatusGson resultado = (FlightStatusGson)data.getSerializableExtra("RESPONSE");
            if(requestCode == GET_FLIGHT){
                addToList(new Flight(resultado));

            }
        }
    }

    /* PROBANDO UNA ARRAYLIST CUALQUIERA */
    public ArrayList getListData() {
        ArrayList<Flight> results = new ArrayList<Flight>();
        Flight flight1 = new Flight();
        flight1.setArrivalCity("BUENOS AIRES");
        flight1.setNumber("12345");
        flight1.setDepartureCity("NUEVA YORK");
        flight1.setState("EXPLOTADO");
        results.add(flight1);

        Flight flight2 = new Flight();
        flight2.setArrivalCity("EL INFINITO");
        flight2.setNumber("00000");
        flight2.setDepartureCity("MAS ALLA");
        flight2.setState("PERDIDO");
        results.add(flight2);

        Flight flight3 = new Flight();
        flight3.setArrivalCity("MADRID");
        flight3.setNumber("00002");
        flight3.setDepartureCity("BARILOCHE");
        flight3.setState("RESTRASADO");
        results.add(flight3);

        Flight flight4 = new Flight();
        flight4.setArrivalCity("LALA");
        flight4.setNumber("000123");
        flight4.setDepartureCity("ERWER");
        flight4.setState("RESTRASADO");
        results.add(flight4);


        return results;
    }
}
