package com.kristian.android.simpletodo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by kristianss27 on 9/19/16.
 */
    public class MyDialogFragment extends DialogFragment {

        public MyDialogFragment() {

            // Empty constructor required for DialogFragment

        }



        public static MyDialogFragment newInstance(String title) {

            MyDialogFragment frag = new MyDialogFragment();

            Bundle args = new Bundle();

            args.putString("title", title);

            frag.setArguments(args);

            return frag;

        }



        @Override

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getArguments().getString("title");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder.setTitle(title);

            alertDialogBuilder.setMessage("Do you want to storage your data in a DataBase?");

            alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    // on success

                }

            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }

            });



            return alertDialogBuilder.create();

        }

    }

