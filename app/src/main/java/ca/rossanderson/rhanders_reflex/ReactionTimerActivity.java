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

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReactionTimerActivity extends AppCompatActivity
        implements ReactionTimerInfoDialogFragment.DialogListener {

    private ReactionTimer reactionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_timer);

        // present info dialog
        DialogFragment infoDialog = new ReactionTimerInfoDialogFragment();
        infoDialog.show(getFragmentManager(), "info");

        this.reactionTimer = new ReactionTimer() {
            @Override
            void onStart() {
                findViewById(R.id.txtReactionGo).setVisibility(View.INVISIBLE);
            }

            @Override
            void onComplete() {
                findViewById(R.id.txtReactionGo).setVisibility(View.VISIBLE);
            }

            @Override
            void onKill() {
                // TODO something?
            }
        };
        final ImageButton btnReaction = (ImageButton) findViewById(R.id.btnReaction);
        final ReactionTimerActivity activity = this;
        btnReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long elapsed = activity.reactionTimer.getElapsed();
                TextView tv = (TextView) findViewById(R.id.txtReactionTime);
                if (elapsed == null) {
                    // display error message
                    tv.setText("Too early!");
                } else {
                    // display reactionTime
                    tv.setText(String.valueOf(elapsed).concat("ms"));

                    // persist reactionDelay
                    reactionTimer.saveReactionTime(elapsed, getApplicationContext());
                }
                activity.reactionTimer.restart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reaction_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogDismiss() {
        this.reactionTimer.start();
    }
}
