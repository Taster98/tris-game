package it.luiggi.projects.trisgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
public class SinglePlayerActivity extends AppCompatActivity implements View.OnClickListener {
    //Mi definisco una variabile che mi serve per capire se il gioco è attivo o no
    public boolean gameActive = true;
    //Mi definisco una variabile per stabilire di chi è il turno (false -> x, true -> cerchio)
    public boolean activePlayer = true;
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
        initViews();
    }
    //Metodo che inizializza le viste
    private void initViews(){
        Button button = (Button)findViewById(R.id.replay);
        button.setOnClickListener(this);
    }
    //Definisco un metodo per la gestione di un "tocco" sulla cella
    public void toccoPlayer(View view){
        //Deve funzionare solo se è il turno del giocatore!
        if(activePlayer) {
            //Costruisco un puntatore alla cella toccata
            ImageView myImageView = (ImageView) view;
            //Raccolgo l'id dell'immagine toccata
            int toccata = Integer.parseInt(myImageView.getTag().toString());

            //Controllo se non sono arrivato alla fine del gioco chiamando la funzione resetGame()
            if (!gameActive)
                resetGame();

            //Controllo lo stato dell'immagine toccata: va riempita solo se è vuota
            if (gameState[toccata] == -1) {
                //A questo punto, come prima cosa incremento cont
                cont++;
                //Se il cont è a 9, il gioco è finito e resetto
                if (cont >= 9)
                    gameActive = false;

                //Marchio la posizione toccata (se falso allora metto 0 altrimenti 1)
                gameState[toccata] = 1;

                //Animiamo un po' l'oggetto: simulo la pressione
                myImageView.setTranslationY(-777f);
                //Vero, quindi giocatore cerchio
                myImageView.setImageResource(R.drawable.cerchio);
                activePlayer = false;
                TextView stato = (TextView) findViewById(R.id.status);
                stato.setText(R.string.icsTurno);

                //Animiamo l'oggetto: simulo il rilascio
                myImageView.animate().translationYBy(777f).setDuration(300);
                if(checkWin()){
                    gameActive = false;
                }
                //L'algoritmo lo chiamo solo se la partita non è già finita ovviamente.
                if(cont < 9 && gameActive)
                    superRobot();
            }
        }
    }

    //Metodo scarso
    private void robot() {
        int choice = ThreadLocalRandom.current().nextInt(0, 9);
        if(gameState[choice] != -1){
            robot();
        }else{
            cont++;
            //Controllo se non sono arrivato alla fine del gioco chiamando la funzione resetGame()
            if (!gameActive)
                resetGame();
            //Se il cont è a 9, il gioco è finito e resetto
            if (cont >= 9)
                gameActive = false;
            //Marchio la posizione toccata (se falso allora metto 0 altrimenti 1)
            gameState[choice] = 0;
            //Prendo il puntatore dell'immagine scelta

            ImageView myImageView = parseImageView(choice);
            //Animiamo un po' l'oggetto: simulo la pressione
            myImageView.setTranslationY(-777f);
            myImageView.setImageResource(R.drawable.croce);
            activePlayer = true;
            TextView stato = (TextView) findViewById(R.id.status);
            stato.setText(R.string.cerchioTurno);
            //Animiamo l'oggetto: simulo il rilascio
            myImageView.animate().translationYBy(777f).setDuration(600);
            if(checkWin()){
                gameActive = false;
            }
        }
    }

    //Metodo che parsa la scelta con l'immagine da piazzare nella griglia
    private ImageView parseImageView(int choice) {
        switch (choice){
            case 0:
                return (ImageView) findViewById(R.id.imageView0);
            case 1:
                return (ImageView) findViewById(R.id.imageView1);
            case 2:
                return (ImageView) findViewById(R.id.imageView2);
            case 3:
                return (ImageView) findViewById(R.id.imageView3);
            case 4:
                return (ImageView) findViewById(R.id.imageView4);
            case 5:
                return (ImageView) findViewById(R.id.imageView5);
            case 6:
                return (ImageView) findViewById(R.id.imageView6);
            case 7:
                return (ImageView) findViewById(R.id.imageView7);
            case 8:
                return (ImageView) findViewById(R.id.imageView8);
            default:
                return null;
        }
    }

    //Metodo che contiene l'algoritmo intelligente
    private void superRobot(){
        int bestMove = -1;
        int bestVal = Integer.MIN_VALUE;
        int toMove;
        for (int j : gameState) {
            if (j != -1) {
                toMove = minimax(2, false)[1];
                if (toMove > bestVal) {
                    bestMove = toMove;
                    bestVal = toMove;
                }
            }
        }
        int moveToDo = bestMove;
        cont++;
        //Controllo se non sono arrivato alla fine del gioco chiamando la funzione resetGame()
        if (!gameActive)
            resetGame();
        //Se il cont è a 9, il gioco è finito e resetto
        if (cont >= 9)
            gameActive = false;
        //Marchio la posizione toccata (se falso allora metto 0 altrimenti 1)
        gameState[moveToDo] = 0;
        //Prendo il puntatore dell'immagine scelta

        ImageView myImageView = parseImageView(moveToDo);
        //Animiamo un po' l'oggetto: simulo la pressione
        myImageView.setTranslationY(-777f);
        myImageView.setImageResource(R.drawable.croce);
        activePlayer = true;
        TextView stato = (TextView) findViewById(R.id.status);
        stato.setText(R.string.cerchioTurno);
        //Animiamo l'oggetto: simulo il rilascio
        myImageView.animate().translationYBy(777f).setDuration(600);
        if(checkWin()){
            gameActive = false;
        }
    }

    private int[] minimax(int depth, boolean player) {
        // Genero le mosse possibili in liste di array ({riga, colonna}).
        List<Integer> nextMoves = generateMoves();

        // falso è il computer, che quindi vuole massimizzare mentre vero è il giocatore
        // che vuole minimizzare
        int bestScore = !player ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // O la partita è finita o sono arrivato in fondo alla previsione
            bestScore = evaluate();
        } else {
            for (int move : nextMoves) {
                // Simuliamo la mossa per vedere se è massimizzata (o minimizzata)
                gameState[move] = 0;
                if (!player) {  // Computer -> massimizzare
                    currentScore = minimax(depth - 1, true)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move;
                    }
                }else {  // Giocatore -> minimizzare
                    currentScore = minimax(depth - 1, false)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move;
                    }
                }
                // Faccio "ctrl-z" della mossa
                gameState[move] = -1;
            }
        }
        return new int[] {bestScore, bestRow};
    }
    private List<Integer> generateMoves() {
        List<Integer> nextMoves = new ArrayList<>();
        // Se la partita è finita non serve fare previsioni, ritorno null
        if (hasWon(false) || hasWon(true)) {
            return nextMoves;
        }
        //Aggiungo tutte le mosse possibili che l'AI può fare e lo ritorno
        for(int i=0;i<gameState.length;i++){
            if(gameState[i] == -1)
                nextMoves.add(i);
        }
        return nextMoves;
    }
    //Metodo che ritorna true se e solo se b ha vinto
    private boolean hasWon(boolean b) {
        int tocheck = b ? 1 : 0;
        return (gameState[0] == tocheck && gameState[1] == tocheck && gameState[2] == tocheck)
                || (gameState[3] == tocheck && gameState[4] == tocheck && gameState[5] == tocheck)
                || (gameState[6] == tocheck && gameState[7] == tocheck && gameState[8] == tocheck)
                || (gameState[0] == tocheck && gameState[3] == tocheck && gameState[6] == tocheck)
                || (gameState[1] == tocheck && gameState[4] == tocheck && gameState[7] == tocheck)
                || (gameState[2] == tocheck && gameState[5] == tocheck && gameState[8] == tocheck)
                || (gameState[0] == tocheck && gameState[4] == tocheck && gameState[8] == tocheck)
                || (gameState[2] == tocheck && gameState[4] == tocheck && gameState[6] == tocheck);
    }

    //Funzione euristica che valuta il punteggio per massimizzare (o minimizzare)
    private int evaluate() {
        int score = 0;
        // Valuto ognuna delle 8 combinazioni vincenti (3 righe, 3 colonne, 2 diagonali)
        score += evaluateLine(0, 1, 2);  // riga 0
        score += evaluateLine(3, 4, 5);  // riga 1
        score += evaluateLine(6, 7, 8);  // riga 2
        score += evaluateLine(0, 3, 6);  // colonna 0
        score += evaluateLine(1, 4, 7);  // colonna 1
        score += evaluateLine(2, 5, 8);  // colonna 2
        score += evaluateLine(0, 4, 8);  // diagonale
        score += evaluateLine(2, 4, 6);  // diagonale inversa
        return score;
    }

    /**
     * Questo è il cuore dell'euristica: devo massimizzare o minimizzare il punteggio.
     * Do a questo algoritmo le 3 posizioni e controllo chi è vincente: il pc deve MASSIMIZZARE
     * mentre il giocatore deve MINIMIZZARE.
     * Se la prima posizione è occupata dal pc ritorno +1, altrimenti se è occupata dal giocatore
     * ritorno -1, infine se è vuota ritorno ovviamente 0.
     * Per la seconda posizione controllo una cosa simile: se è occupata dal pc controllo anche la prima:
     * se anch'essa è occupata dal pc ritorno 10, altrimenti se è occupata dal giocatore torno 0. Altrimenti
     * metto score a 1.
     * Per la terza "tripla" faccio lo stesso ragionamento: se è occupata dal pc, controllo anche la 2 e la 1
     * e il ragionamento è iterativo in questo modo. Uso i punteggi +-1, +-10, +-100.
     */
    //Convertire una matrice in array monodimensionale: 3*row + col
    private int evaluateLine(int cella1, int cella2,int cella3) {
        int score = 0;

        // Prima cella
        if (gameState[cella1] == 0)
            score = 1;
        else if (gameState[cella1] == 1)
            score = -1;

        // Seconda cella
        if (gameState[cella2] == 0) {
            if (score == 1)
                score = 10;
            else if (score == -1)
                return 0;
            else
                score = 1;
        }else if (gameState[cella2] == 1) {
            if (score == -1)
                score = -10;
            else if (score == 1)
                return 0;
            else
                score = -1;
        }

        // Terza cella
        if (gameState[cella3] == 0) {
            if (score > 0)
                score *= 10;
            else if (score < 0)
                return 0;
            else
                score = 1;
        }else if (gameState[cella3] == 1) {
            if (score < 0)
                score *= 10;
            else if (score > 0)
                return 0;
            else
                score = -1;
        }
        return score;
    }

    private boolean checkWin() {
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
                TextView stato = (TextView) findViewById(R.id.status);
                if (gameState[winPosition[0]] == 0) {
                    stato.setTextColor(Color.parseColor("yellow"));
                    winnerStr = "X " + getText(R.string.vinto).toString();
                } else {
                    stato.setTextColor(getColor(R.color.green));
                    winnerStr = "O " + getText(R.string.vinto).toString();
                }
                // A questo punto aggiorniamo il testo per annunciare la vittoria del giocatore fortunato

                stato.setText(winnerStr);
                //Mostro il bottone
                Button button = (Button) findViewById(R.id.replay);
                button.setBackgroundColor(Color.parseColor("darkgray"));
                button.setTextColor(Color.parseColor("white"));
                button.setVisibility(View.VISIBLE);
            }
        }
        //In questo caso ci si arriva solo se si è in parità
        if (cont >= 9 && !flag) {
            TextView stato = (TextView) findViewById(R.id.status);
            stato.setText(getText(R.string.parita).toString());
            stato.setTextColor(Color.parseColor("darkgray"));
            //Mostro il bottone
            Button button = (Button) findViewById(R.id.replay);
            button.setVisibility(View.VISIBLE);
            return true;
        }else{
            return false;
        }
    }

    // Metodo per resettare il gioco
    public void resetGame() {
        gameActive = true;
        activePlayer = true;
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
        stato.setTextColor(Color.parseColor("darkgray"));
        //Nascondo il bottone
        Button button = (Button) findViewById(R.id.replay);
        button.setVisibility(View.INVISIBLE);
        cont=0;
    }

    @Override
    public void onClick(View view) {
        resetGame();
    }
}