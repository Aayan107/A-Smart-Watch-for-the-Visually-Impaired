package com.example.shravanapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SosFragment extends Fragment {


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userlist;
    Adapter adapter;
    Button btnAddButton, btnRefereshContacts, btnDeleteContacts;
    String contactnameS, contactnumberS;
    ArrayList <String> contactNameList, contactNumList;
    Integer contactCountFlag =0;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SosFragment newInstance(String param1, String param2) {
        SosFragment fragment = new SosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactNameList = new ArrayList<>();
        contactNumList = new ArrayList<>();
        initData(contactNameList, contactNumList,true);
        initRecyclerView();

        btnAddButton = getView().findViewById(R.id.btnAddContacts);
        btnRefereshContacts = getView().findViewById(R.id.btnRefereshContacts);
        btnDeleteContacts = getView().findViewById(R.id.btnDeleteContacts);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(), "FINE permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_CONTACTS }, 100);
        }
        else {
            /*Toast.makeText(getActivity(), "FINE permission already granted", Toast.LENGTH_SHORT).show();*/
        }



        btnAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactNameList.clear();
                contactNumList.clear();

                Intent in1 = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                Intent in2 = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                Intent in3 = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                Intent in4 = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in1,1);
                startActivityForResult(in2,1);
                startActivityForResult(in3,1);
                startActivityForResult(in4,1);



            }
        });

        btnRefereshContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.example.shravan.sos_contacts", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed;
                    ed = sharedPrefs.edit();

                    //Set Contact names in Cache
                    ed.putString("c1_name", contactNameList.get(0));
                    ed.putString("c2_name", contactNameList.get(1));
                    ed.putString("c3_name", contactNameList.get(2));
                    ed.putString("c4_name", contactNameList.get(3));


                    //Set Contact number in Cache
                    ed.putString("c1_num", contactNumList.get(0));
                    ed.putString("c2_num", contactNumList.get(1));
                    ed.putString("c3_num", contactNumList.get(2));
                    ed.putString("c4_num", contactNumList.get(3));

                    // Status of Contacts in Cache set to true
                    ed.putBoolean("initialized", true);

                    ed.commit();

                    PrintRecyler();
                    Toast.makeText(getActivity(),"Contacts Updated",Toast.LENGTH_SHORT).show();



            }
        });

        btnDeleteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.example.shravan.sos_contacts", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed = sharedPrefs.edit();

                ed.clear();
                ed.apply();
                ed.commit();

                SharedPreferences sharedPrefs1 = getActivity().getSharedPreferences("com.example.shravan.sos_contacts", getActivity().MODE_PRIVATE);

                contactNameList.clear();
                contactNumList.clear();


                contactNameList.add(sharedPrefs1.getString("c1_name","Contact 1"));
                contactNameList.add(sharedPrefs1.getString("c2_name","Contact 2"));
                contactNameList.add(sharedPrefs1.getString("c3_name","Contact 3"));
                contactNameList.add(sharedPrefs1.getString("c4_name","Contact 4"));

                contactNumList.add(sharedPrefs1.getString("c1_num","Number"));
                contactNumList.add(sharedPrefs1.getString("c2_num","Number"));
                contactNumList.add(sharedPrefs1.getString("c3_num","Number"));
                contactNumList.add(sharedPrefs1.getString("c4_num","Number"));


                initData(contactNameList, contactNumList,true);
                initRecyclerView();

                Toast.makeText(getActivity(),"Contacts Deleted",Toast.LENGTH_SHORT).show();



            }
        });



    }

    private void PrintRecyler()
    {
        Log.d("AAYAN:","Now printing");

        initData(contactNameList, contactNumList,false);
        initRecyclerView();
    }

/*    private void Contact2Pick(Intent data)
    {
        startActivityForResult(data,1);
    }

    private void Contact3Pick(Intent data)
    {
        startActivityForResult(data,1);
    }

    private void Contact4Pick(Intent data)
    {
        startActivityForResult(data,1);

    }*/





                    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            switch (requestCode) {
                case 1:
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Failed to pick contact", Toast.LENGTH_SHORT).show();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void contactPicked(Intent data) {

        Cursor cursor = null;

        try{
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            contactnumberS = cursor.getString(phoneIndex);

            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            contactnameS = cursor.getString(nameIndex);

            Log.d("AAYAN:", "FIRST CONTACT");


            if(contactNameList.size()<=4)
            {
                Log.d("AAYAN:", "CONTACT");
                contactNameList.add(contactnameS);
                contactNumList.add(contactnumberS);
            }
            else
            {
                contactCountFlag = 1;
            }


        }
        catch (Exception e)
        {
            Log.d("AAYAN:","Stack Error",e);
            e.printStackTrace();
        }


    }


    private void initData(ArrayList<String> contactNameListLocal, ArrayList<String> contactNumListLocal, Boolean First) {

        userlist = new ArrayList<>();

        if (First== true)
        {
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.example.shravan.sos_contacts", getActivity().MODE_PRIVATE);

            contactNameList.clear();
            contactNameList.clear();

            contactNameList.add(sharedPrefs.getString("c1_name","Contact 1"));
            contactNameList.add(sharedPrefs.getString("c2_name","Contact 2"));
            contactNameList.add(sharedPrefs.getString("c3_name","Contact 3"));
            contactNameList.add(sharedPrefs.getString("c4_name","Contact 4"));

            contactNumList.add(sharedPrefs.getString("c1_num","Number"));
            contactNumList.add(sharedPrefs.getString("c2_num","Number"));
            contactNumList.add(sharedPrefs.getString("c3_num","Number"));
            contactNumList.add(sharedPrefs.getString("c4_num","Number"));

        }

        try {
            if (contactNumList.size() == 0) {

                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_one_24, "Emergency Contact 1", "Number"));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_two_24, "Emergency Contact 2", "Number"));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_3_24, "Emergency Contact 3", "Number"));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_4_24, "Emergency Contact 4", "Number"));
            }
            else
            {
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_one_24, contactNameList.get(0), contactNumList.get(0)));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_two_24, contactNameList.get(1), contactNumList.get(1)));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_3_24, contactNameList.get(2), contactNumList.get(2)));
                userlist.add(new ModelClass(R.drawable.ic_baseline_looks_4_24, contactNameList.get(3), contactNumList.get(3)));
            }

        }

        catch (Exception e)
        {
            Log.d("AAYAN:","ERROR in block 1",e);
        }
    }

    private void initRecyclerView() {

        recyclerView = getView().findViewById(R.id.reyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(userlist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




    }
}