 package com.example.samsunghackathon2021.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.samsunghackathon2021.ItemClickSupport;
import com.example.samsunghackathon2021.MqttHelper;
import com.example.samsunghackathon2021.R;
import com.example.samsunghackathon2021.base.DatabaseHelper;
import com.example.samsunghackathon2021.base.NoteModel;
import com.example.samsunghackathon2021.base.NotesAdapter;
import com.example.samsunghackathon2021.databinding.FragmentProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;

 public class ProfileFragment extends Fragment {
    FloatingActionButton btn_add;
    ArrayList<NoteModel> arrayList;

    DatabaseHelper database_helper;
    MqttHelper mqttHelperPrefs, mqttHelperPomp;
    RecyclerView recyclerView;

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    int hum_ground = 0;
    boolean check = false;
    String name_plant = "";

    public String profile_text;

    int a = 0;
    private byte[] ByteArrayOutputStream;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database_helper = new DatabaseHelper(getContext());
        recyclerView = (RecyclerView) root.findViewById(R.id.listR);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        btn_add = (FloatingActionButton) root.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(v);
            }
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startPompMqtt("test/mode");
            }
        };
        handler.sendEmptyMessage(0);
        displayNotes();

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Toast.makeText(getContext(), " ???????? ???????? " + position, Toast.LENGTH_LONG).show();
                        Log.d("ITEM CLICK", "Item single clicked " + position);
                        database_helper.selectMode(position);

                        profile_text = "??????????????????";
                    }

                    @Override
                    public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                        Log.d("ITEM CLICK", "Item double clicked ");
                        Toast.makeText(getContext(), " ?????? ???????? " + position, Toast.LENGTH_LONG).show();
                        sendMessage(database_helper.selectMode(position));
                        database_helper.selectModeInFrag(position);

                    }
                });

        return root;  
    }




     public void displayNotes() {
        arrayList = new ArrayList<>(database_helper.getNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NotesAdapter adapter = new NotesAdapter(getContext().getApplicationContext(), getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
    }


    public void showAlert(View v){

        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle("??????????");
        alert.setMessage("???????????????? ?????? ????????????");
        alert.setButton(Dialog.BUTTON_POSITIVE,"??????????????????",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_LONG).show();
                check = false;
                showAlertHum(v);
            }
        });

        alert.setButton(Dialog.BUTTON_NEGATIVE,"??????????",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "??????????", Toast.LENGTH_LONG).show();
                check = true;
                showTimeHum(v);

            }
        });

        alert.setButton(Dialog.BUTTON_NEUTRAL,"????????????",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "????????????", Toast.LENGTH_LONG).show();
            }
        });

        alert.show();

        // ???????????????????? Alert

    }



    public void showAlertHum(View v){

        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_hum, null);
        SeekBar humm = cl.findViewById(R.id.seekBar);
        TextView proc = cl.findViewById(R.id.textView4);
        humm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                proc.setText(String.valueOf(progress) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("??????????");

        builder.setPositiveButton("????",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), humm.getProgress() + " ?????????????????? ??????????????", Toast.LENGTH_LONG).show();

                hum_ground = humm.getProgress();
                nameAlert(v);

            }
        });




        builder.setView(cl);
        builder.show();

    }


    public void nameAlert(View v){
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.name_alert, null);
        TextInputEditText namee = cl.findViewById(R.id.inputText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("????????????????");


        builder.setPositiveButton("????",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), namee.getText() + " ?????? ??????????????", Toast.LENGTH_LONG).show();
                name_plant = namee.getText() + "";
                database_helper.addNotes(name_plant, "?????????? ???????? ?????????????????? ???????????? " + hum_ground + "%", "hum." + hum_ground);
                displayNotes();


            }
        });
        builder.setView(cl);
        builder.show();

    }
    //?????????? ?????????????? ?? ????????????????
     public void set_duration(View v){
         ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.time_water_duration, null);
         NumberPicker nb = cl.findViewById(R.id.number_time_picker);
         nb.setMaxValue(60);
         nb.setMinValue(1);
         final int[] sel_value = {1};
         nb.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
             @Override
             public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sel_value[0] = newVal;
             }
         });
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("?????????? ????????????");


         builder.setPositiveButton("????",new DialogInterface.OnClickListener(){
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 String type__ = Integer.toString(sel_value[0]);
                 Toast.makeText(getContext(), "??????????: " + type__, Toast.LENGTH_LONG).show();


             }
         });
         builder.setView(cl);
         builder.show();

     }
// ?????????????? ???? ??????????
     public void showTimeHum(View v) {

         ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.time_layout_mini, null);
         CheckBox checkBox_mo = cl.findViewById(R.id.monday_check);
         CheckBox checkBox_tu = cl.findViewById(R.id.tuesday_check);
         CheckBox checkBox_wd = cl.findViewById(R.id.wednesday_check);
         CheckBox checkBox_th = cl.findViewById(R.id.thursday_check);
         CheckBox checkBox_fr = cl.findViewById(R.id.friday_check);
         CheckBox checkBox_st = cl.findViewById(R.id.saturday_check);
         CheckBox checkBox_sn = cl.findViewById(R.id.sunday_check);
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("??????????");

         builder.setPositiveButton("????", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 ArrayList<String> chose_days = new ArrayList<String>();
                 if (checkBox_mo.isChecked()){chose_days.add("monday");}
                 if (checkBox_tu.isChecked()){chose_days.add("tuesday");}
                 if (checkBox_wd.isChecked()){chose_days.add("wednesday");}
                 if (checkBox_th.isChecked()){chose_days.add("thursday");}
                 if (checkBox_fr.isChecked()){chose_days.add("friday");}
                 if (checkBox_st.isChecked()){chose_days.add("saturday");}
                 if (checkBox_sn.isChecked()){chose_days.add("sunday");}

                 Toast.makeText(getContext(), chose_days + " - ?????????????????? ??????", Toast.LENGTH_LONG).show();
                if (chose_days.isEmpty()){
                    // ?????????? ?????????????? ???????? ???? ???????????? ???? ???????? ????????
                     }
                else {
                 set_duration(v);
                }
             }
         });
         builder.setView(cl);
         builder.show();
     }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startPompMqtt(String topic) {
        mqttHelperPomp = new MqttHelper(getActivity().getApplicationContext(), topic);
        mqttHelperPomp.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                //dataReceived.setText(mqttMessage.toString());
                String[] answer = mqttMessage.toString().split(" ");


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void sendMessage(String opcode) {
        Handler handler1 = new Handler();

        Log.i("water", " on");
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mqttHelperPomp.publishMessage("" + opcode);
            }
        }, 10);
    }
}