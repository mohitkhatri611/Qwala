package com.completemesh.qwala;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mohit on 4/4/2018.
 */

public class Home extends Fragment {


    private static final String TAG = "Home";
    final MainActivity mainActivity = new MainActivity();

    private Button btnTEST,mbuttonSendMessage;
    TextView txtViewOutput;
    Context context;
    public Home(){}
    private AdapterView.OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        mbuttonSendMessage  = (Button) view.findViewById(R.id.btnSendMessage);
        Button mbtnSpeak = (Button) view.findViewById(R.id.btSpeak);

        final EditText txtMessagequery;
        txtMessagequery = (EditText) view.findViewById(R.id.txtquery);
        txtViewOutput = (TextView) view.findViewById(R.id.txtOutput);

        mbtnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proptSpeechInput();


            }
        });
final GlobalVariable globalVariable = new GlobalVariable();
        final String[] query = new String[1];
        mbuttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                query[0] = txtMessagequery.getText().toString();
                mainActivity.fn_insert(getActivity(),query[0]);
                //Toast.makeText(getActivity(), query[0], Toast.LENGTH_SHORT).show();
               // String  size=((GlobalVariable)getActivity().getApplicationContext()).getNetworkState();

               // txtViewOutput.setText(query[0]);
            }
        });

        return view;
    }
    public void proptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "SPEAK");


        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {


        }
    }
            @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {

                case 100:
                    if (resultCode == RESULT_OK && data != null) {

                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.fn_insert(getContext(), result.get(0));
                        Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
                        txtViewOutput.setText(result.get(0));
                        break;
                    }

            }
        }

    // Store the listener (activity) that will have events fired once the fragment is attached

}
