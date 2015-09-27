package ca.rossanderson.rhanders_reflex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // TODO load and display ReactionTimer stats
        StatsModel model = StatsModel.getStatsModel();
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
                Long stat = model.getReactionTimeStat(statTypes.get(i), Ns.get(j), getApplicationContext());
                tv.setText(String.valueOf(stat));
                resCount++;
            }
        }

        // TODO load and display GameShow stats
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
