package com.example.shravanapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    ToggleButton connection, fms;
    RaspberryIntercom ConnectivityTask;
    RaspberryIntercom2 FmsTask;
    private ProgressDialog pDialog;
    boolean ConnectivityTask_running;
    String c1_num,c2_num,c3_num,c4_num,REGEX_REMOVE_WHITESPACE_HYPHENS;
    //public String HOST_NAME="192.168.29.108";
    public String HOST_NAME="192.168.0.106";





    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestCheckPermission();

        connection =(ToggleButton)view.findViewById(R.id.connection_tglBtn);
        fms =(ToggleButton)view.findViewById(R.id.ring_tglBtn);
        Log.d("AAYAN:","flag1 from OnViewCreated");

        REGEX_REMOVE_WHITESPACE_HYPHENS = "[\\-\\s]";



        // Shravan Connectivity button functionality
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection.isChecked())
                {
                    Toast.makeText(getView().getContext(),"Initiating Connection",Toast.LENGTH_SHORT).show();
                    // call asynctask for socket1
                    ConnectivityTask = (RaspberryIntercom) new RaspberryIntercom(getActivity()).execute();
                    ConnectivityTask_running = true;
                }
                else
                {
                    Toast.makeText(getActivity(),"Disconnecting Shravan",Toast.LENGTH_SHORT).show();
                    // call onCancelled for socket1
                    if (ConnectivityTask_running && ConnectivityTask!=null)
                    {
                        ConnectivityTask.onPostExecute(null);
                        ConnectivityTask_running = false;
                        Log.d("AAYAN:","Cancellation request for Socket");
                    }
                    else{
                        connection.setChecked(false);
                    }
                }
            }
        });


        // Find my Shravan button functionality
        fms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fms.isChecked())
                {
                    fms.setChecked(true);
                    Toast.makeText(getView().getContext(),"Asking Device to Ring",Toast.LENGTH_SHORT).show();
                    // calling ring async for socket2
                    try {
                        //set time in mili
                        fms.setChecked(true);
                        fms.setClickable(false);
                        FmsTask = (RaspberryIntercom2) new RaspberryIntercom2().execute();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("AAYAN:","out of delay");
                    fms.setClickable(true);
                }
                else{

                }

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("AAYAN:","flag1 from onDestroy");
    }


    @Override
    public void onDetach() {
        super.onDetach();

        Log.d("AAYAN:","flag1 from onDetach");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("AAYAN:","flag1 from OnCreate");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("AAYAN:","flag1 from OnCreateView");
                // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    //permission checker

    private void requestCheckPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "Internet permission already granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.INTERNET }, 100);
        }
        else {
            Toast.makeText(getActivity(), "Internet permission already granted", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "SMS Permission not granted!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.SEND_SMS }, 100);
        }
        else {
            Toast.makeText(getActivity(), "SMS Permission already granted!", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "COARSE permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
            Toast.makeText(getActivity(), "COARSE permission granted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "COARSE permission granted", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "FINE permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 100);
        }
        else {
            Toast.makeText(getActivity(), "FINE permission already granted", Toast.LENGTH_SHORT).show();
        }



    }

    // AsyncTask for Connectivity - message, sos

    class RaspberryIntercom extends AsyncTask<String,Void,Void>
    {
        Socket s;
        PrintWriter pw;
        DataOutputStream dout;
        BufferedReader in;
        PrintWriter out;
        public String action, whom, location_string;
        public Integer flag;
        SmsManager smsManager;
        private LocationRequest locationRequest;
        public String latitude, longitude;
        Context mContext;


        public RaspberryIntercom(Context context) {
            mContext = context;
        }

        @SuppressLint({"MissingPermission", "WrongThread"})
        @Override
        protected Void doInBackground(String... strings) {

            try{
                // Socket Initialization

                s= new Socket(HOST_NAME,6969);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(),true);


                // Cache Invoking to retrieve Contacts

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.example.shravan.sos_contacts", getActivity().MODE_PRIVATE);
                c1_num = sharedPrefs.getString("c1_num","Number");
                c2_num = sharedPrefs.getString("c2_num","Number");
                c3_num = sharedPrefs.getString("c3_num","Number");
                c4_num = sharedPrefs.getString("c4_num","Number");

                c1_num = c1_num.replaceAll(REGEX_REMOVE_WHITESPACE_HYPHENS, "");
                c2_num = c2_num.replaceAll(REGEX_REMOVE_WHITESPACE_HYPHENS, "");
                c3_num = c3_num.replaceAll(REGEX_REMOVE_WHITESPACE_HYPHENS, "");
                c4_num = c4_num.replaceAll(REGEX_REMOVE_WHITESPACE_HYPHENS, "");


                while (true)
                {

                    String result = in.readLine();
                    JSONObject jsonObject= new JSONObject(result);
                    action = (String) jsonObject.get("action");
                    System.out.println("AAYAN: "+" Value of Action: "+action);



                    if (action.equals("sos"))
                    {
                        whom = jsonObject.getJSONObject("options").getString("op1");
                        System.out.println("DEBUG TEST"+" Value of to every1: "+whom);
                        Log.d("DEBUG TEST","REACHES 1");
                        if (whom.equals("everyone"))
                        {

                            sendSOSMessage();

                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(750);
                                        final Toast toast = Toast.makeText(mContext, "SOS sent out to "+whom, Toast.LENGTH_SHORT);
                                        toast.show();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            //Toast.makeText(mContext, "SOS sent out to "+whom, Toast.LENGTH_SHORT).show();

                            jsonObject = null;
                            Runtime.getRuntime().gc();

                            Log.d("AAYAN:","Out of inner loop");

                            }
                        }
                        else if (action.equals("call"))
                            {
                                whom = jsonObject.getJSONObject("options").getString("op1");
                                System.out.println("AAYAN: "+" Value of to every1: "+whom);
                                Log.d("AAYAN: ","call :"+whom);

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mContext, "Calling "+whom, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            Toast.makeText(mContext, "Calling "+whom, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(),"Invoking call to "+whom,Toast.LENGTH_SHORT).show();
                            jsonObject = null;
                            Runtime.getRuntime().gc();
                            continue;

                        }
                        else if (action.equals("dc"))
                        {
                            Log.d("AAYAN: ","socket ended");
                            jsonObject = null;
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    final Toast toast = Toast.makeText(mContext,"Disconnecting socket"+whom,Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            Runtime.getRuntime().gc();
                            onPostExecute(null);
                        }
                        else if (action.equals("check"))
                        {
                            Log.d("AAYAN:","check function invoked");
                            out.println("okay");
                        }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("AAYAN:","Exception: "+e.toString());

            }

            return null;
        }

        @SuppressLint("MissingPermission")
        private void sendSOSMessage() {

            Log.d("DEBUG TEST","REACHES 2");

            // Retrieving Location - Block of code runs in parallel if enclosed in a method (DO NOT TOUCH)

            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Log.d("DEBUG TEST","REACHES 3");
                    Location location = task.getResult();
                    if (location != null) {
                        Log.d("DEBUG TEST","REACHES 4");
                        try
                        {

                            Log.d("DEBUG TEST","REACHES 5");

                            // Initialize geoCoder
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                            // Initialize address list
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                // Set longitude and Latitude

                            latitude = String.valueOf(addresses.get(0).getLatitude());
                            longitude = String.valueOf(addresses.get(0).getLongitude());
                            Log.d("DEBUG TEST","latitude: "+latitude);
                            Log.d("DEBUG TEST","longitude: "+longitude);
                            Log.d("DEBUG TEST","After getLocation() completes");


                            Log.d("DEBUG TEST","From METHOD:   latitude: "+latitude);
                            Log.d("DEBUG TEST","From METHOD:   longitude: "+longitude);

                // Preparing SOS Message String
                            location_string = "Hello, it's Shravan and I need your assistance. \n\nHere's my location: https://www.google.com/maps/place/"+latitude+","+longitude+"";

                // Sending SOS Messages
                            sendSMS(c1_num,location_string);
                            //sendSMS(c2_num,location_string);
                            //sendSMS(c3_num,location_string);
                            //sendSMS(c4_num,location_string);

                            Log.d("DEBUG TEST","Sent Dynamic location to everyone!");




                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


        }


        private void sendSMS(String contactNo,String message) {

            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contactNo, null, message, null, null);

        }

        @Override
        protected void onPostExecute(Void unused) {

            super.onPostExecute(unused);

            ConnectivityTask_running = false;
            connection.setChecked(false);

            Log.d("AAYAN:","exiting Async from onPostExecute");

            try {
                s.close();
                Log.d("AAYAN:","Exiting Socket");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled(Void unused) {
            ConnectivityTask_running = false;
            connection.setChecked(false);

            Log.d("AAYAN:","exiting Async from onPostExecute");

            try {
                s.close();
                Log.d("AAYAN:","Exiting Socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onCancelled(unused);

        }


        @SuppressLint("MissingPermission")
        private void getLocation() {


        }

    }




    // AsyncTask for FMS, Image Upload

    class RaspberryIntercom2 extends AsyncTask<String,Void,Void>
    {

        Socket s2;
        BufferedReader in;
        PrintWriter out;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getView().getContext());
            pDialog.setMessage("Ringing your Shravan... Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            try
            {

                //Socket Initialization
                s2= new Socket(HOST_NAME,6667);
                in = new BufferedReader(new InputStreamReader(s2.getInputStream()));
                out = new PrintWriter(s2.getOutputStream(),true);

                out.println("fms");

                s2.close();

                Thread.sleep(5000);

            }
            catch (Exception e) {
                Log.d("AAYAN:","ERROR",e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            pDialog.dismiss();

        }
    }

}