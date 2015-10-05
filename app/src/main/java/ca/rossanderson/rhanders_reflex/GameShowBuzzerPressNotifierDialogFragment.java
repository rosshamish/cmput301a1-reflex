/*
 * Reflex - Reaction Timer and Game Show Buzzer
 * Copyright (C) 2015 Ross Anderson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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


