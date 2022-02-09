package com.example.mypocketnavv;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class mapActivityDialog1 extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
       // builder.setTitle("Guide")
               //// .setMessage("")
                //.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   // @Override
                    //public void onClick(DialogInterface dialog, int which) {

                    //}
                //});
        return builder.create();
    }
}
