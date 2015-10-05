package ca.rossanderson.rhanders_reflex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class GameShowActivity extends AppCompatActivity
        implements NumPlayersPickerDialogFragment.DialogListener {

    private GameShow gameShow;
    int[] layoutIds = {-1, -1,
            R.layout.game_show_2player,
            R.layout.game_show_3player,
            R.layout.game_show_4player};
    ArrayList<Integer> buttonIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // default 2 players
        setContentView(getLayoutInflater().inflate(R.layout.game_show_2player, null));

        gameShow = new GameShow();
        gameShow.setNumPlayers(2); // default 2 players
        buttonIds = new ArrayList<Integer>() {{
            add(R.id.imageButton);
            add(R.id.imageButton2);
            add(R.id.imageButton3);
            add(R.id.imageButton4);
        }};

        NumPlayersPickerDialogFragment picker = new NumPlayersPickerDialogFragment();
        picker.show(getFragmentManager(), "picker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_show, menu);
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

    public void onValueChange(int oldVal, int newVal) {
        gameShow.setNumPlayers(newVal);
        View v = getLayoutInflater().inflate(layoutIds[gameShow.getNumPlayers()], null);
        setContentView(v);
    }

    public void onValueAccept() {
        Button b;
        for (int i=0; i < gameShow.getNumPlayers(); i++) {
            b = (Button) findViewById(buttonIds.get(i));
            final int playerNum = i+1;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameShow.saveGameShowBuzz(playerNum, getApplicationContext());
                    GameShowBuzzerPressNotifierDialogFragment f = GameShowBuzzerPressNotifierDialogFragment.newInstance(playerNum);
                    f.show(getFragmentManager(), "playerBuzzed");
                }
            });
        }
    }
}
