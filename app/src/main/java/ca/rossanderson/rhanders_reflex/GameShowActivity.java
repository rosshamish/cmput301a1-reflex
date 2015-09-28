package ca.rossanderson.rhanders_reflex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GameShowActivity extends AppCompatActivity implements NumPlayersPickerDialogFragment.DialogListener {

    private GameShow gameShow;
    int[] layoutIds = {-1, -1,
            R.layout.game_show_2player,
            R.layout.game_show_3player,
            R.layout.game_show_4player};
    int[] buttonIds = {R.id.imageButton, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_show);

        gameShow = new GameShow();

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
        this.setContentView(layoutIds[gameShow.getNumPlayers()]);
    }

    public void onValueAccept() {
        Button b;
        for (int btnId : buttonIds) {
            b = (Button) findViewById(btnId);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
