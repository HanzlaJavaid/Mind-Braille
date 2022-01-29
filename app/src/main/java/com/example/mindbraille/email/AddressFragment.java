package com.example.mindbraille.email;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.mindbraille.R;
import com.example.mindbraille.models.AppConstants;
import com.example.mindbraille.models.AuthInfo;
import com.example.mindbraille.models.ContactModel;
import com.example.mindbraille.models.EmailModel;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressFragment extends Fragment {


    private static final int RESULT_OK = 999;
    AuthInfo userauthInfo;
    EditText r_address;
    Button buttontoemailbody;
    Button buttontocontacts;
    ArrayList<ContactModel> contactModelArrayList;



    ArrayList<ContactModel> fakeContacts;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == AppConstants.FRAGMENT_CODE){
                ContactModel selectedContect = (ContactModel) data.getSerializableExtra("selectedContact");
                String prevText = r_address.getText().toString()+';';
                r_address.setText(prevText + selectedContect.getEmail());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.address_fragment,container,false);
        userauthInfo = (AuthInfo) getArguments().getSerializable("userauthInfo");
        contactModelArrayList = new ArrayList<ContactModel>();
        fakeContacts = new ArrayList<ContactModel>();
        r_address = v.findViewById(R.id.recipientmailfield);
        buttontoemailbody = v.findViewById(R.id.buttontoemailbody);
        buttontocontacts = v.findViewById(R.id.buttontocontactlistemail);



        buttontocontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutlookContactsFragment fragment = new OutlookContactsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactList",contactModelArrayList);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                fragment.setTargetFragment(AddressFragment.this, AppConstants.FRAGMENT_CODE);
                ft.addToBackStack(AddressFragment.class.getName());
                ft.add(R.id.frag_cont,fragment,"TAG");
                ft.commit();
            }
        });

        buttontoemailbody.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                List<String> addresses = Arrays.asList(r_address.getText().toString().substring(1).split(";"));
                EmailModel.recipients = addresses;

                Bundle bundle = new Bundle();
                bundle.putString("r_address",r_address.getText().toString());
                bundle.putSerializable("userauthInfo",userauthInfo);
                if(!r_address.getText().toString().equals("")){
                CCFragment fragment = new CCFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(AddressFragment.class.getName());
                ft.add(R.id.frag_cont,fragment,"TAG");
                ft.commit();}
            }
        });

        AuthInfo.graphClient.me().contacts().buildRequest().select("emailAddresses,displayName").top(5).get(new ICallback<IContactCollectionPage>() {
            @Override
            public void success(IContactCollectionPage iContactCollectionPage) {
                for( int i = 0; i < iContactCollectionPage.getCurrentPage().size();i++){
                    contactModelArrayList.add(
                            new ContactModel(
                                    iContactCollectionPage.getCurrentPage().get(i).displayName,
                                    iContactCollectionPage.getCurrentPage().get(i).emailAddresses.get(0).address
                            )

                    );
                }
                Log.d("CONTACTS",contactModelArrayList.toString());
            }

            @Override
            public void failure(ClientException ex) {

            }
        });

        return v;
    }



}