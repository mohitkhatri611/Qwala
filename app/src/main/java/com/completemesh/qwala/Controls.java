package com.completemesh.qwala;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

import static com.completemesh.qwala.TimerFragment.DATE_DIALOG_ID;

/**
 * Created by Mohit on 4/4/2018.
 */

public class Controls extends Fragment {
    private static final String TAG = "Controls";
    ToggleButton toogleBtnLight, toogleBtnFan, toogleBtnDoor, toogleBtnMusic;
    Button btnPrevious, btnNext, btnStop,btnNews,btnWeather,btnAlarm,btnFace;
    TextView textView;
    private Button btnTEST;
    EditText txtAddress,editFace;

    private int mHour,mMinute,totalTime;
    public static String CMD = "";
    final MainActivity mainActivity = new MainActivity();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_controls, container, false);


        // Inflate the layout for this fragment

        toogleBtnLight = (ToggleButton) view.findViewById(R.id.toogleBtnLight);
        toogleBtnFan = (ToggleButton) view.findViewById(R.id.toogleBtnFan);
        toogleBtnDoor = (ToggleButton) view.findViewById(R.id.toogleBtndoor);
        toogleBtnMusic = (ToggleButton) view.findViewById(R.id.toogleBtnPlay);

        btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnStop = (Button) view.findViewById(R.id.btnStop);
        btnNews = (Button) view.findViewById(R.id.btnNews);
        btnWeather = (Button) view.findViewById(R.id.btnWeather);
        btnAlarm = (Button) view.findViewById(R.id.btnAlarm);
        btnFace = (Button) view.findViewById(R.id.btnFace);

        txtAddress = (EditText) view.findViewById(R.id.ipAddress);
        editFace = (EditText) view.findViewById(R.id.editFace);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "play previous song";
                mainActivity.fn_insert(getContext(),CMD);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "play next song";
                mainActivity.fn_insert(getContext(),CMD);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "stop music";
                mainActivity.fn_insert(getContext(),CMD);
            }
        });
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.fn_insert(getContext(),"news");
            }
        });
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "weather";
                mainActivity.fn_insert(getContext(),"weather");
            }
        });
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFancyDateTimePicker(DATE_DIALOG_ID).show();
            }
        });

        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String CMD= editFace.getText().toString();
               mainActivity.fn_insert(getContext(),"addface "+ CMD);
               editFace.setText("");
            }
        });

        toogleBtnLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    CMD = "light on";
                    mainActivity.fn_insert(getContext(),CMD);
                } else {
                    CMD = "light off";
                    mainActivity.fn_insert(getContext(),CMD);
                }
            }
        });
        toogleBtnFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CMD = "fan on";
                    mainActivity.fn_insert(getContext(),CMD);
                } else {
                    // The toggle is disabled
                    CMD = "fan off";
                    mainActivity.fn_insert(getContext(),CMD);
                }
            }
        });
        toogleBtnDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CMD = "door open";
                    mainActivity.fn_insert(getContext(),CMD);
                } else {
                    // The toggle is disabled
                    CMD = "door close";
                    mainActivity.fn_insert(getContext(),CMD);
                }
            }
        });
        toogleBtnMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CMD = "play music";
                    mainActivity.fn_insert(getContext(),CMD);
                } else {
                    CMD = "stop music";
                    mainActivity.fn_insert(getContext(),CMD);
                }
            }
        });
        return view;
    }

    protected Dialog createFancyDateTimePicker(int id) {
        if (id==DATE_DIALOG_ID)
            return new TimePickerDialog(getActivity(),
                    mTimeSetListener, mHour, mMinute, false);
        return null;
    }


    // Timepicker dialog generation
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    totalTime=(mHour*60)+minute;
                    mainActivity.fn_insert(getContext(),"alarm "+ mHour +" "+ mMinute);
                    Toast.makeText(getContext(),"Timer set for-> "+ mHour+" Hour and " +mMinute+" Minutes", Toast.LENGTH_SHORT).show();
                }
            };

//          button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StringBuffer result = new StringBuffer();
//                result.append(("START Condition - ")).append(toggleButton2.getText());
//                result.append("\n ON Condition - ").append(toggleButton.getText());
//
//                Toast.makeText(getActivity(),result.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });


//    @Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        if(switch1.isChecked()){
//            textView.setText("Swich On");
//        }else{
//            textView.setText("Swich Off");
//        }
//    }
}
