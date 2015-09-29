package ca.rossanderson.rhanders_reflex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by ross on 15-09-27.
 */
public class GameShowBuzzerPressNotifierDialogFragment extends DialogFragment {

    static GameShowBuzzerPressNotifierDialogFragment newInstance(int playerNum) {
        GameShowBuzzerPressNotifierDialogFragment f = new GameShowBuzzerPressNotifierDialogFragment();

        Bundle args = new Bundle();
        args.putInt("playerNum", playerNum);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(String.format("Player %d was first!", this.getArguments().getInt("playerNum")))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        return builder.create();
    }
}


