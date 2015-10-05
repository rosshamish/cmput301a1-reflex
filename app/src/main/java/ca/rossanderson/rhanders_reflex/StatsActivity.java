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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // load and display ReactionTimer stats
        displayReactionTimerStats();

        // load and display GameShow stats
        displayGameShowStats();

        // Hook up Erase button
        Button btnErase = (Button) findViewById(R.id.btnErase);
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO present dialog confirming this destructive action
                StatsModel.getStatsModel().clear(getApplicationContext());
                displayReactionTimerStats();
                displayGameShowStats();
            }
        });

        // Hook up Email button
        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"rhanders@ualberta.ca"});
                String date = new SimpleDateFormat().format(new Date());
                i.putExtra(Intent.EXTRA_SUBJECT, String.format("rhanders-reflex Statistics [%s]", date));
                i.putExtra(Intent.EXTRA_TEXT, StatsModel.getStatsModel().getEmailBody(getApplicationContext()));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StatsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayGameShowStats() {
        StatsModel stats = StatsModel.getStatsModel();
        ArrayList<Integer> resourceIds = new ArrayList<Integer>() {{
            add(R.id.gs2p1);
            add(R.id.gs2p2);
            add(R.id.gs3p1);
            add(R.id.gs3p2);
            add(R.id.gs3p3);
            add(R.id.gs4p1);
            add(R.id.gs4p2);
            add(R.id.gs4p3);
            add(R.id.gs4p4);
        }};
        TextView tv;
        int resCount = 0;
        for (int players=2; players <= 4; players++) {
            for (int playerNum=1; playerNum <= players; playerNum++) {
                tv = (TextView) findViewById(resourceIds.get(resCount));
                Integer stat = stats.getGameShowStat(players, playerNum, getApplicationContext());
                tv.setText(String.valueOf(stat));
                resCount++;
            }
        }
    }

    private void displayReactionTimerStats() {
        StatsModel stats = StatsModel.getStatsModel();
        ArrayList<Integer> Ns = new ArrayList<Integer>() {{
            add(10);
            add(100);
            add(-1);
        }};
        ArrayList<StatType> statTypes = new ArrayList<StatType>() {{
            add(StatType.MINIMUM);
            add(StatType.MAXIMUM);
            add(StatType.AVERAGE);
            add(StatType.MEDIAN);
        }};
        ArrayList<Integer> resourceIds = new ArrayList<Integer>() {{
            add(R.id.statMin10);
            add(R.id.statMin100);
            add(R.id.statMinAllTime);
            add(R.id.statMax10);
            add(R.id.statMax100);
            add(R.id.statMaxAllTime);
            add(R.id.statAvg10);
            add(R.id.statAvg100);
            add(R.id.statAvgAllTime);
            add(R.id.statMed10);
            add(R.id.statMed100);
            add(R.id.statMedAllTime);
        }};
        int resCount = 0;
        TextView tv;
        for (int i=0; i < statTypes.size(); i++) {
            for (int j=0; j < Ns.size(); j++) {
                tv = (TextView) findViewById(resourceIds.get(resCount));
                Long stat = stats.getReactionTimeStat(statTypes.get(i), Ns.get(j), getApplicationContext());
                tv.setText(String.valueOf(stat));
                resCount++;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
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
}
