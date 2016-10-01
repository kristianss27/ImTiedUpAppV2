package com.kristian.android.simpletodo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by kristianss27 on 9/19/16.
 */
    public class MyDialogFragment extends DialogFragment {

    public interface MyDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    MyDialogListener myDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myDialogListener =  (MyDialogListener) activity;
        }catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement MyDialogListener");
        }
    }

    public MyDialogFragment() {
        }

        public static MyDialogFragment newInstance(String title,String message) {
            MyDialogFragment frag = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message",message);
            frag.setArguments(args);
            return frag;
        }



        @Override

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder.setTitle(title);

            alertDialogBuilder.setMessage(message);

            alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {
                    myDialogListener.onDialogPositiveClick(MyDialogFragment.this);
                }

            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {
                    myDialogListener.onDialogNegativeClick(MyDialogFragment.this);
                    //dialog.dismiss();
                }

            });



            return alertDialogBuilder.create();

        }

    }

