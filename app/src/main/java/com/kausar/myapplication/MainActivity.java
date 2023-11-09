package com.kausar.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;

public class MainActivity extends AppCompatActivity {
    private int size = 8; // Change the size of the grid as needed
    private int[][] minefield; // Matrix to hold the mine locations
    private Button[][] buttons; // Buttons representing the cells
    private boolean[][] revealed; // To track which cells are revealed
    private boolean gameOver = false;

    private Button restartButton;
    private Button solutionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restartButton = findViewById(R.id.restartButton);
        solutionButton = findViewById(R.id.solutionButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreateGame(); // Method to recreate the game
            }
        });

        solutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution(); // Method to show the solution
            }
        });

        createGame();
    }

    private void createGame() {
        minefield = new int[size][size]; // 0: No mine, 1: Mine
        buttons = new Button[size][size];
        revealed = new boolean[size][size];

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Initialize the grid of buttons and set onClickListeners
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;
                params.height = 100;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setText("");
                buttons[i][j].setOnClickListener(cellClickListener(i, j));
                gridLayout.addView(buttons[i][j]);
            }
        }

        // Generate mines
        generateMines();
    }

    private void generateMines() {
        // For simplicity, randomly place mines in the minefield
        // You may want to update this logic to place mines as per the game's rules
        // This is a basic example and may need improvement for the game's rules
        for (int m = 0; m < size * 2; m++) {
            int i = (int) (Math.random() * size);
            int j = (int) (Math.random() * size);
            minefield[i][j] = 1;
        }
    }

    private View.OnClickListener cellClickListener(final int x, final int y) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameOver && !revealed[x][y]) {
                    if (minefield[x][y] == 1) {
                        buttons[x][y].setText("M");
                        // Game over - handle logic
                        gameOver = true;
                    } else {
                        int adjacentMines = countAdjacentMines(x, y);
                        if (adjacentMines > 0) {
                            buttons[x][y].setText(String.valueOf(adjacentMines));
                            revealed[x][y] = true;
                        } else {
                            revealEmptyCells(x, y);
                        }
                        checkGameWin();
                    }
                }
            }
        };
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (minefield[i][j] == 1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void revealEmptyCells(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size || revealed[x][y]) {
            return;
        }

        int adjacentMines = countAdjacentMines(x, y);
        if (adjacentMines > 0) {
            buttons[x][y].setText(String.valueOf(adjacentMines));
            revealed[x][y] = true;
            return;
        }

        buttons[x][y].setText(""); // Empty cell
        revealed[x][y] = true;

        // Recursively reveal neighboring cells
        revealEmptyCells(x - 1, y);
        revealEmptyCells(x + 1, y);
        revealEmptyCells(x, y - 1);
        revealEmptyCells(x, y + 1);
        revealEmptyCells(x - 1, y - 1);
        revealEmptyCells(x + 1, y + 1);
        revealEmptyCells(x - 1, y + 1);
        revealEmptyCells(x + 1, y - 1);
    }

    private void checkGameWin() {
        // Check if all non-mine cells are revealed (game win condition)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (minefield[i][j] == 0 && !revealed[i][j]) {
                    return;
                }
            }
        }
        // All non-mine cells are revealed, the player wins - handle game win logic
        gameOver = true;
    }

    private void recreateGame() {
        // Logic to reset the game, reset the minefield, buttons, and other necessary variables
        // This method should reset the game board to the initial state
        // Clear text, reset revealed states, regenerate mines, etc.
        gameOver = false;
        createGame(); // Re-initialize the game
    }

    private void showSolution() {
        // Logic to reveal the solution
        // This method should reveal all cells, showing the positions of mines and numbers
        // Update button texts, mark mine cells, and display all numbers
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (minefield[i][j] == 1) {
                    buttons[i][j].setText("M");
                } else {
                    int adjacentMines = countAdjacentMines(i, j);
                    buttons[i][j].setText(String.valueOf(adjacentMines));
                }
                revealed[i][j] = true;
            }
        }
        gameOver = true; // Game ends after showing the solution
    }
}
