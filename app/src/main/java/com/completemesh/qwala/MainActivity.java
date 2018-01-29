package com.completemesh.qwala;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.completemesh.qwala.translation_engine.TranslatorFactory;
import com.completemesh.qwala.translation_engine.utils.ConversionCallaback;
import com.completemesh.qwala.translation_engine.utils.TranslatorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ConversionCallaback {
//..................Main speak variables start.............
    private static final int TTS = 0;
    private static final int STT = 1;
    private static int CURRENT_MODE = -1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private EditText ttsInput;
    private TextView sttOutput;
    private TextView erroConsole;
   private FloatingActionButton speechToText;
    private FloatingActionButton textToSpeech;

    //...............................Main speak Varaibles TTS-STT END................


    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1000;
  //  private EditText metTextHint, txtMessagequery;
    private ListView mlvTextMatches;
    private Spinner msTextmatches;
    private Button mbtnSpeak, mbuttonSendMessage;

    //private TextView txtViewOutput;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static  TextView txtViewOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            // Pain in A$$ Marshmallow+ Permission APIs
            requestForPermission();
        } else {
            // Pre-Marshmallow
            if (!isConnected(MainActivity.this)) {
                buildDialog(MainActivity.this).show();
                return;
            } else {
                setContentView(R.layout.activity_main);
                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                setUpView();
            }

        }


        //   metTextHint = (EditText) findViewById(R.id.etTextHint);
        // mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
        //msTextmatches = (Spinner) findViewById(R.id.sNOfMatches);


        txtViewOutput = (TextView)findViewById(R.id.txtOutput);
        mbtnSpeak = (Button) findViewById(R.id.btSpeak);
//            txtMessagequery = (EditText) findViewById(R.id.txtquery);
        mbuttonSendMessage = (Button) findViewById(R.id.btnSendMessage);

//don't set mbutton like this if you are using fragment activity.......................
//            mbuttonSendMessage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String query = txtMessagequery.getText().toString();
//                    fn_insert(query);
//                }
//            });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

        /**
         * Set up listeners on View
         */
    private void setUpView() {

        //Text to speech Input
     //   ttsInput = (EditText) findViewById(R.id.tts_input);
        //Speech to text output
      //  sttOutput = (TextView) findViewById(R.id.stt_output);
        //Failure message
      //  erroConsole = (TextView) findViewById(R.id.error_output);

        //Button to trigger TTS
       // textToSpeech = (FloatingActionButton) findViewById(R.id.talk);

        //Button to trigger STT
        speechToText = (FloatingActionButton) findViewById(R.id.start_listening);

        //Listen and convert convert speech to text
        speechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Listening", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Ask translator factory to start speech tpo text convertion
                //Hello There is optional
                TranslatorFactory.getInstance().
                        getTranslator(TranslatorFactory.TRANSLATOR_TYPE.SPEECH_TO_TEXT, MainActivity.this).
                        initialize("Hello There", MainActivity.this);



                CURRENT_MODE = STT;
            }
        });


        //Read texts and convert them into speech
//        textToSpeech.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Talking", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                //Ask translator factory to start speech tpo text convertion
//                //Read edittext if it is empty say invalid input
//                TranslatorFactory.getInstance().
//                        getTranslator(TranslatorFactory.TRANSLATOR_TYPE.TEXT_TO_SPEECH, MainActivity.this)
//                        .initialize((null != ttsInput.getText().toString() && !ttsInput.getText().toString().isEmpty() ? ttsInput.getText().toString() : "Invalid Input"), MainActivity.this);
//
//                CURRENT_MODE = TTS;
//            }
//        });


        //share whatever is enterted by STT (Speech to text)
//        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Sharing", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                if (null == sttOutput.getText().toString() && sttOutput.getText().toString().isEmpty()) {
//                    Snackbar.make(view, "Error empty spech to text output ", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//                TranslatorUtil.share(sttOutput.getText().toString(), MainActivity.this);
//
//            }
//        });
    }

    /**
     * On success is common of both translator so if you had triggered Speech to text show converted text on textview
     *
     * @param result
     */
    @Override
    public void onSuccess(String result) {

        Toast.makeText(this, "Result " + result, Toast.LENGTH_SHORT).show();

        switch (CURRENT_MODE) {
            case STT:
               txtViewOutput.setText(result);
        }
    }

    @Override
    public void onCompletion() {
        Toast.makeText(this, "Done ", Toast.LENGTH_SHORT).show();

    }

    /**
     * If translator failed error message will be come in this callback
     *
     * @param errorMessage
     */
    @Override
    public void onErrorOccured(String errorMessage) {

        erroConsole.setText(errorMessage);
    }


    /**
     * Request Permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestForPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!isPermissionGranted(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Require for Speech to text");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);

                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);
                }

                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //add listeners on view
        setUpView();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted(List<String> permissionsList, String permission) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted

                    //add listeners on view
                    setUpView();

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permissions are Denied Exiting App", Toast.LENGTH_SHORT)
                            .show();

                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {



        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                Button mbuttonSendMessage = (Button) rootView.findViewById(R.id.btnSendMessage);
                Button mbtnSpeak = (Button) rootView.findViewById(R.id.btSpeak);
               final EditText txtMessagequery;
                txtMessagequery = (EditText) rootView.findViewById(R.id.txtquery);


                txtViewOutput = (TextView) rootView.findViewById(R.id.txtOutput);

                final String[] query = new String[1];
                mbuttonSendMessage.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        MainActivity mainActivity= new MainActivity();
                        query[0] =txtMessagequery.getText().toString();
                        mainActivity.fn_insert(query[0]);
                        Toast.makeText(getActivity(), query[0], Toast.LENGTH_SHORT).show();
                        txtViewOutput.setText(query[0]);
                    }
                });


                mbtnSpeak.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                       proptSpeechInput();


 //.....................code for ArrayAdapter storing for 10 using spinner.........................
//                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
//                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText().toString());
//                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//                        if (msTextmatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
//
//                            Toast.makeText(getActivity(), "Please select No. of Matches from Spinner", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        int noOfMatches = Integer.parseInt(msTextmatches.getSelectedItem().toString());
//                        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
//                        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
                    }
                });
                return rootView;
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                View rootView = inflater.inflate(R.layout.fragment_controls, container, false);
                return rootView;
            }
            else{
                View rootView = inflater.inflate(R.layout.fragment_plus_one, container, false);
                return rootView;

            }

        }

        public void proptSpeechInput() {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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
                       MainActivity mainActivity= new MainActivity();
                        mainActivity.fn_insert(result.get(0));
                        Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
                        txtViewOutput.setText(result.get(0));
                        break;
                    }

            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Controls";
                case 2:
                    return "No Use";
            }
            return null;
        }
    }


    //code Started for google speech Prompt.....................................prompt


    //    @Override
//    protected void onResume() {
//        if (!isConnected(VoiceRecognitionActivity.this)) {
//            buildDialog(VoiceRecognitionActivity.this).show();
//
//        } else {
//            setContentView(R.layout.activity_voice_recognition);
//            Toast.makeText(VoiceRecognitionActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//            super.onResume();
//        }
//
//    }

    public void CheckVoiceRecognition() {

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            mbtnSpeak.setEnabled(false);
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();

        }
    }
    public void speak(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
      //  intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText().toString());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        if (msTextmatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {

            Toast.makeText(this, "Please select No. of Matches from Spinner", Toast.LENGTH_SHORT).show();
            return;
        }

        int noOfMatches = Integer.parseInt(msTextmatches.getSelectedItem().toString());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            ArrayList<String> textMatchlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//            if (!textMatchlist.isEmpty()) {
//                if (textMatchlist.get(0).contains("search")) {
//                    String searchQuery = textMatchlist.get(0).replace("search", " ");
//                    Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
//                    search.putExtra(SearchManager.QUERY, searchQuery);
//
//                    startActivity(search);
//
//                } else {
//                    String sample = textMatchlist.get(0);
//                    fn_insert(sample);
//                    Toast.makeText(this, sample, Toast.LENGTH_SHORT).show();
//
//                    mlvTextMatches.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, textMatchlist));
//
//                }
//            }
//
//        } else if (requestCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
//            showToastMessage("Audio Error");
//
//        } else if (requestCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
//            showToastMessage("Client Error");
//
//        } else if (requestCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
//            showToastMessage("Network Error");
//
//        } else if (requestCode == RecognizerIntent.RESULT_NO_MATCH) {
//            showToastMessage("No Match");
//
//
//        } else if (requestCode == RecognizerIntent.RESULT_SERVER_ERROR) {
//            showToastMessage("Server Error");
//
//        }
//        super.onActivityResult(resultCode, resultCode, data);
//
//    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void fn_insert(String message) {

        BackgroundTasks bg_login = new BackgroundTasks();
        try {
            String query = "insert into searchdb(input) values('" + message + "');";
            //showToastMessage(message);
            GlobalVariable globalVariable= new GlobalVariable();
            String link = "http://" + globalVariable.getHost()+ "/qwala/inserttabledata.php";

            //String link="http://mypatshala.com/qwala/inserttabledata.php";
            Log.v("error", query);
            String str = bg_login.execute(query, link).get();
            String msg = bg_login.insertuser(str);
            Log.v("error1", msg);

            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
           // Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_SHORT).show();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
