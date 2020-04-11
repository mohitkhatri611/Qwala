package com.completemesh.qwala;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


/**
 * Created by Mohit on 4/4/2018.
 */

public class TimerFragment extends Fragment {
    private static final String TAG = "Home";
    private Button btnSwitch1,btnCancel1,btnSwitch2,btnCancel2,btnSwitch3,btnCancel3,btnShutDown,btnReboot;
    private static final String FORMAT = "%02d:%02d:%02d";

    private int totalTime,switchNo;

 static final int DATE_DIALOG_ID=0;
    Button button;
    int mHour,mMinute;
    CountDownTimer countDownTimer1,countDownTimer2,countDownTimer3=null;
    TextView textView1,textView2,textView3;
   final MainActivity mainActivity= new MainActivity();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer,container,false);
        btnSwitch1 = (Button) view.findViewById(R.id.btnSwitch1);
        btnSwitch2 = (Button) view.findViewById(R.id.btnSwitch2);
        btnSwitch3 = (Button) view.findViewById(R.id.btnSwitch3);

        btnCancel1 = (Button) view.findViewById(R.id.btnSw1Cancel);
        btnCancel2 = (Button) view.findViewById(R.id.btnSw2Cancel);
        btnCancel3 = (Button) view.findViewById(R.id.btnSw3Cancel);

        textView1=(TextView)view.findViewById(R.id.txtSwitch1);
        textView2=(TextView)view.findViewById(R.id.txtSwitch2);
        textView3=(TextView)view.findViewById(R.id.txtSwitch3);

        btnShutDown = (Button) view.findViewById(R.id.btnShutDown);
        btnReboot = (Button) view.findViewById(R.id.btnReboot);

        btnSwitch1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             textView1.setText(null);
              switchNo = 1;
             createFancyDateTimePicker(DATE_DIALOG_ID).show();
             if(countDownTimer1!=null){
                countDownTimer1.cancel();
             }
            }
        });
        btnSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setText(null);
                switchNo = 2;
                createFancyDateTimePicker(DATE_DIALOG_ID).show();
                if(countDownTimer2!=null){
                    countDownTimer2.cancel();
                }
            }
        });
        btnSwitch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView3.setText(null);
                switchNo = 3;
                createFancyDateTimePicker(DATE_DIALOG_ID).show();
                if(countDownTimer3!=null){
                    countDownTimer3.cancel();
                }
            }
        });
        ///Cancel buttons for switch timer
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.fn_insert(getContext(),"switch 1 cancel");

                    countDownTimer1.cancel();
                    textView1.setText("");

            }
        });
        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.fn_insert(getContext(),"switch 2 cancel");
                    countDownTimer2.cancel();
                    textView2.setText("");

            }
        });
        btnCancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.fn_insert(getContext(),"switch 3 cancel");
                    countDownTimer3.cancel();
                    textView3.setText("");
            }
        });

        btnShutDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(getActivity(),"shutdown").show();

            }
        });

        btnReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(getActivity(),"reboot").show();
            }
        });
        return view;

    }


    public AlertDialog.Builder buildDialog(Context c, final String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(message);
        builder.setMessage("Do you want to "+message+ " server");

        builder
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mainActivity.fn_insert(getContext(),message);
                    }
                });

        return builder;
    }

    private void updateTime() {

//        startTime.setText(
//                new StringBuilder()
//                        // Month is 0 based so add 1
//                        .append(mHour).append(" : ")
//                        .append(mMinute).append(" , ")
//                        .append(mDay).append(".")
//                        .append(mMonth + 1).append(".")
//                        .append(mYear).append(""));
    }

//    private DatePickerDialog.OnDateSetListener mDateSetListener =
//            new DatePickerDialog.OnDateSetListener() {
//
//                public void onDateSet(DatePicker view, int year,
//                                      int monthOfYear, int dayOfMonth) {
//                    mYear = year;
//                    mMonth = monthOfYear;
//                    mDay = dayOfMonth;
//                    createFancyDateTimePicker(TIME_DIALOG_ID).show();
//                }
//            };

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
                    mainActivity.fn_insert(getContext(),"switch "+switchNo+" "+ totalTime);
                   Toast.makeText(getContext(),"switch "+switchNo+" "+ totalTime, Toast.LENGTH_SHORT).show();
                   if (switchNo==1){
                     countDownTimer1=   new CountDownTimer((60000*totalTime), 1000){
                           public void onTick(long millisUntilFinished){
                               //textView1.setText(String.valueOf(counter));
                               textView1.setText(""+String.format(FORMAT,
                                       TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                       TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                               TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                       TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                               TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                           }
                           public  void onFinish(){
                               textView1.setText("FINISH!!");
                           }
                       }.start();
                   }
                    if (switchNo==2){
                        countDownTimer2=   new CountDownTimer((60000*totalTime), 1000){
                            public void onTick(long millisUntilFinished){
                                //textView1.setText(String.valueOf(counter));
                                textView2.setText(""+String.format(FORMAT,
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }
                            public  void onFinish(){
                                textView2.setText("FINISH!!");
                            }
                        }.start();
                    }
                    if (switchNo==3){
                        countDownTimer3=   new CountDownTimer((60000*totalTime), 1000){
                            public void onTick(long millisUntilFinished){
                                //textView1.setText(String.valueOf(counter));
                                textView3.setText(""+String.format(FORMAT,
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }
                            public  void onFinish(){
                                textView3.setText("FINISH!!");
                            }
                        }.start();
                    }
                }
            };



    //cancel timer


    private void SpinnerFunction(){
        //        final Spinner dropdown =view.findViewById(R.id.spinner1);
//        String[] items = new String[]{"1", "2", "three"};
//
//
//        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,items);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//                String item = parent.getItemAtPosition(position).toString();
//               // Toast.makeText(getActivity(),item, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, item);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
       // MainActivity mainActivity= new MainActivity();
        //btnSendTimer.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {

//                PRESSED_CALENDAR = START_TIME;
           //     createFancyDateTimePicker(DATE_DIALOG_ID).show();

//                String hr= editHour.getText().toString();
//                String min=editMinutes.getText().toString();
//                String switchNo = dropdown.getSelectedItem().toString();
//
//                if(!hr.equals("")) {
//                    int numHr = Integer.parseInt(hr);
//                    if (numHr < 12) {
//                        editHour.setText("" + numHr);
//                        Toast.makeText(getActivity(), hr + " " + min +" Switch No. "+switchNo,Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(),"Hr 1 to 12 only allowed",Toast.LENGTH_SHORT).show();
//                        editHour.setText("");
//                    }
//                }

//            }
//        });
    }
}