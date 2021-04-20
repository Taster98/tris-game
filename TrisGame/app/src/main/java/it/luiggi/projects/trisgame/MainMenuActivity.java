package it.luiggi.projects.trisgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    private Button singlePlayerButton, multiPlayerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

    }

    @Override
    public void onClick(View view) {
        Intent myIntent = new Intent(MainMenuActivity.this, MultiPlayerActivity.class);
        this.startActivity(myIntent);
    }
}
