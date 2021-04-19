package it.luiggi.projects.trisgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

/*
* GIOCO
* Ci salviamo in una matrice 3x3 tutte le posizioni vincenti
* La matrice è la seguente:
*
* 0 1 2
* 3 4 5
* 6 7 8
*
*
* */
public class MainActivity extends AppCompatActivity {
    //Mi definisco una variabile che mi serve per capire se il gioco è attivo o no
    public boolean gameActive = true;
    //Mi definisco una variabile per stabilire di chi è il turno (false -> x, true -> cerchio)
    public boolean activePlayer = false;
    //Mi definisco un array che indica lo stato del gioco, ossia le posizioni attualmente da chi sono coperte
    /*
    -1 -> Vuoto
    0 -> x
    1 -> cerchio
    */
    public int[] gameState = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
    // Mi definisco un contatore inizializzato a 0; ogni volta che viene toccata una cella, lo incremento.
    // Quando raggiunge 9 devo fermare il gioco.
    public static int cont = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Definisco un metodo per la gestione di un "tocco" sulla cella
    public void toccoPlayer(View view){
        //Costruisco un puntatore alla cella toccata
        ImageView myImageView = (ImageView) view;
        //Raccolgo l'id dell'immagine toccata
        int toccata = Integer.parseInt(myImageView.getTag().toString());

        //Controllo se non sono arrivato alla fine del gioco chiamando la funzione resetGame()
        if(!gameActive)
            resetGame(view);

        //Controllo lo stato dell'immagine toccata: va riempita solo se è vuota
        if(gameState[toccata] == -1){
            //A questo punto, come prima cosa incremento cont
            cont++;

            //Se il cont è a 9, il gioco è finito e resetto
            if(cont == 9)
                gameActive = false;

            //Marchio la posizione toccata (se falso allora metto 0 altrimenti 1)
            gameState[toccata] = !activePlayer ? 0 : 1;

            //Animiamo un po' l'oggetto: simulo la pressione
            myImageView.setTranslationY(-777f);

            if(activePlayer){
                //Vero, quindi giocatore cerchio
                myImageView.setImageResource(R.drawable.cerchio);
                activePlayer = false;
                TextView stato = (TextView)findViewById(R.id.status);
                stato.setText(R.string.icsTurno);
            }else{
                //Falso, quindi giocatore croce
                myImageView.setImageResource(R.drawable.croce);
                activePlayer = true;
                TextView stato = (TextView)findViewById(R.id.status);
                stato.setText(R.string.cerchioTurno);
            }
            //Animiamo l'oggetto: simulo il rilascio
            myImageView.animate().translationYBy(777f).setDuration(300);

            boolean flag = false;
            // Controlliamo se il giocatore ha vinto
            for (int[] winPosition : Constants.winningPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != -1) {
                    flag = true;

                    // Qualcuno ha vinto quindi dobbiamo scriverlo
                    String winnerStr;

                    // Dobbiamo resettare il gioco
                    gameActive = false;
                    if (gameState[winPosition[0]] == 0) {
                        winnerStr = "X " + getText(R.string.vinto).toString();
                    } else {
                        winnerStr = "O " + getText(R.string.vinto).toString();
                    }
                    // A questo punto aggiorniamo il testo per annunciare la vittoria del giocatore fortunato
                    TextView stato = (TextView)findViewById(R.id.status);
                    stato.setText(winnerStr);
                }
            }
            //In questo caso ci si arriva solo se si è in parità
            if (cont == 9 && !flag) {
                TextView stato = (TextView)findViewById(R.id.status);
                stato.setText(getText(R.string.parita).toString());
            }
        }
    }

    // Metodo per resettare il gioco
    public void resetGame(View view) {
        gameActive = true;
        activePlayer = false;
        Arrays.fill(gameState, -1);
        // Se resetto il gioco rimuovo tutte le immagini dalla griglia
        ((ImageView) findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);

        TextView stato = findViewById(R.id.status);
        stato.setText(R.string.icsTurno);
    }
}