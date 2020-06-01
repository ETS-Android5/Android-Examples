package com.singularitycoder.androidalertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class CustomDialogFragment extends DialogFragment {

    // Use getContext() instance of the interface to deliver action events
    CustomDialogFragment.NoticeDialogListener listener;

    public CustomDialogFragment() {
    }

    public CustomDialogFragment(Map<String, String> dialogDetails) {
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (CustomDialogFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
        }
    }

    // The system calls getContext() only when creating the layout in a dialog.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }

        // The only reason you might override getContext() method when using onCreateView() is to modify any dialog characteristics. For example, the dialog includes a title by default, but your custom layout might not need it. So here you can remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Get the ideal size of a dialog
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Message");
        builder.setMessage("Fire Missiles")
                .setPositiveButton("OK", (dialog1, id) -> {
                    // Send the positive button event back to the host activity
                    listener.onDialogPositiveClick(CustomDialogFragment.this);
                })
                .setNegativeButton("Cancel", (dialog12, id) -> {
                    // Send the negative button event back to the host activity
                    listener.onDialogNegativeClick(CustomDialogFragment.this);
                })
                .setNeutralButton("Later", (dialogInterface, id) -> {
                    // Send the neutral button event back to the host activity
                    listener.onDialogNeutralClick(CustomDialogFragment.this);
                });
        return builder.create();
    }

    // The system calls getContext() to get the DialogFragment's layout, regardless of whether it's being displayed as a dialog or an embedded fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_custom_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = view.findViewById(R.id.et_email);

        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("email")))
            editText.setText(getArguments().getString("email"));

        Button btnDone = view.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(view1 -> {
            CustomDialogFragment.DialogListener dialogListener = (CustomDialogFragment.DialogListener) getActivity();
            dialogListener.onFinishEditDialog(editText.getText().toString());
            dismiss();
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setFullScreen = false;
        if (getArguments() != null)
            setFullScreen = getArguments().getBoolean("fullScreen");

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    @UiThread
    public void simpleAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());   // Use the Builder class for constructing dialog
        alertBuilder.setTitle("Delete Message");    // Optional Title
        alertBuilder.setMessage("Are you sure you want to delete getContext() message?");   // Optional Message
        alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);   // Optional Icon for dialog
        alertBuilder.setCancelable(true);   // Optional - If you want to dismiss dialog on touch of dialog bounds
        alertBuilder.setPositiveButton( // Set positive message
                "Yes",
                (dialog, id) -> {
                    makeText(getContext(), "Yes clicked", LENGTH_SHORT).show();
                    dialog.cancel();
                });
        alertBuilder.setNegativeButton( // Optional - Negative message
                "No",
                (dialog, id) -> {
                    makeText(getContext(), "No clicked", LENGTH_SHORT).show();
                    dialog.cancel();
                });
        alertBuilder.setNeutralButton("Remind Later", (dialog, id) -> { // Optional - neutral if user unable to decide
            makeText(getContext(), "No clicked", LENGTH_SHORT).show();
            dialog.cancel();
        });

        alertBuilder.show();
    }

    @UiThread
    public void listDialog() {
        // Use the Builder class for constructing dialog
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("What do you want to do?");

        // If you don't want to dismiss dialog on touch of dialog bounds
        alertBuilder.setCancelable(false);

        // Create a list
        String[] selectArray = {"Option 1", "Option 2", "Option 3", "Close Dialog"};

        // Add the list to builder
        alertBuilder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position of the selected item
                switch (which) {
                    case 0:
                        // Do something
                        makeText(getContext(), "Option 1 clicked", LENGTH_SHORT).show();
                        break;
                    case 1:
                        // Do something
                        makeText(getContext(), "Option 2 clicked", LENGTH_SHORT).show();
                        break;
                    case 2:
                        // Do something
                        makeText(getContext(), "Option 3 clicked", LENGTH_SHORT).show();
                        break;
                    case 3:
                        // Do something
                        dialog.dismiss();
                        makeText(getContext(), "Dialog Closed", LENGTH_SHORT).show();
                        break;
                }
            }
        });

        alertBuilder.show();
    }

    @UiThread
    public AlertDialog multipleChoiceListDialog() {

        // Track the selected items
        final ArrayList<Integer> selectedItems = new ArrayList();
        String[] dialogList = {"Option 1", "Option 2", "Option 3", "Option 4"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Set dialog title
        builder.setTitle("Select Options")
                // Specify list array, items to be selected by default (null for none), and listener through which to receive callbacks when items selected
                .setMultiChoiceItems(dialogList, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If user checked the item, add it to selected items
                                    selectedItems.add(which);

                                    for (int i = 0; i < selectedItems.size(); i++) {
                                        System.out.println("Print Selected Items: " + selectedItems.get(i));
                                    }
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(which);

                                    for (int i = 0; i < selectedItems.size(); i++) {
                                        System.out.println("Print Deselected Items: " + selectedItems.get(i));
                                    }
                                }
                            }
                        })
                // Set action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Save selectedItems results somewhere or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.show();
    }

    @UiThread
    public AlertDialog singleChoiceListDialog() {
        String[] dialogList = {"Option 1", "Option 2", "Option 3", "Option 4"};

        // Must have an item checked by default in singlezchoiceListDialog
        final int checkedItem = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Set the dialog title
        builder.setTitle("Select an Option")
                // Specify the list array, the items to be selected by default (null for none), and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(dialogList, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Checked Item: " + i);
                    }
                })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Save selectedItems results somewhere or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.show();
    }

    @UiThread
    public void customDialog() {
        // Instantiate Dialog class
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        // Set custom Layout
        dialog.setContentView(R.layout.fragment_custom_dialog);

        // Get the recommended size of a dialog
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        // Instantiate custom dialog views
//        final EditText etDateDialog = dialog.findViewById(R.id.et_date_setter);
//        final String etString = etDateDialog.getText().toString();
//
//        Button btnApply = dialog.findViewById(R.id.btn_ok);
//        btnApply.setOnClickListener(view -> {
//            Toast.makeText(getContext(), etString, LENGTH_LONG).show();
//            dialog.dismiss();
//        });

        dialog.show();
    }

    /* The activity that creates an instance of getContext() dialog fragment must implement getContext() interface in order to receive event callbacks. Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog);
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }
}