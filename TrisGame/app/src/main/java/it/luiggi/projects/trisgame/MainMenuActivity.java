package it.luiggi.projects.trisgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    private Button singlePlayer, multiPlayer;
    private Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        singlePlayer = (Button)findViewById(R.id.singlePlayer);
        multiPlayer = (Button)findViewById(R.id.multiPlayer);
        multiPlayer.setOnClickListener(this);
        singlePlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.multiPlayer)
            myIntent = new Intent(MainMenuActivity.this, MultiPlayerActivity.class);
        else if(view.getId() == R.id.singlePlayer)
            myIntent = new Intent(MainMenuActivity.this, SinglePlayerActivity.class);
        this.startActivity(myIntent);
    }
}
