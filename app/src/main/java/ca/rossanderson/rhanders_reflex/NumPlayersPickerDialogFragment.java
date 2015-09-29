package ca.rossanderson.rhanders_reflex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

/**
 * Created by ross on 15-09-27.
 */
public class NumPlayersPickerDialogFragment extends DialogFragment {
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NumberPicker np = (NumberPicker) new NumberPicker(getContext());
        np.setMinValue(2);
        np.setMaxValue(4);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dialogListener.onValueChange(oldVal, newVal);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.numPlayersMsg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onValueAccept();
                    }
                })
                .setView(np);
        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getClass() + " must implement DialogListener");
        }
    }

    public interface DialogListener {
        public void onValueChange(int oldVal, int newVal);
        public void onValueAccept();
    }
}


