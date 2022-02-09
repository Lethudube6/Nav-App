package com.example.mypocketnavv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    Spinner ChooseT,SaveT;

    ArrayAdapter<CharSequence>adapter,adapter2,adapter3;
    Button save,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ChooseT=findViewById(R.id.etTypeS);
        SaveT=findViewById(R.id.etTypeS2);

        save=findViewById(R.id.btnSaveS);
        cancel=findViewById(R.id.btnCancelS);


        adapter=ArrayAdapter.createFromResource(this,R.array.measurement_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2=ArrayAdapter.createFromResource(this,R.array.transport_type,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3=ArrayAdapter.createFromResource(this,R.array.save_history,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ChooseT.setAdapter(adapter);
        SaveT.setAdapter(adapter2);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Thank you for entering", Toast.LENGTH_LONG).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MapActivity.class));
            }
        });

        opendialog();
    }

    public void opendialog(){
        settingsActivityDialog1 dialog1= new settingsActivityDialog1();
        dialog1.show(getSupportFragmentManager(),"");
    }
}
