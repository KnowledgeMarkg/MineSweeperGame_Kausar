package com.kausar.myapplication;
import java.util.Random;
public class Minefield {
    private int[][] field; // represents the grid
    private int size; // size of the grid
    private int mines; // number of mines

    public Minefield(int size, int mines) {
        this.size = size;
        this.mines = mines;
        field = new int[size][size];
        initializeField();
        placeMines();
        calculateHints();
    }

    private void initializeField() {
        // Initialize the grid with zeros
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = 0;
            }
        }
    }

    private void placeMines() {
        // Randomly place mines
        Random random = new Random();
        int count = 0;
        while (count < mines) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if (field[x][y] != -1) {
                field[x][y] = -1; // -1 indicates a mine
                count++;
            }
        }
    }

    private void calculateHints() {
        // Calculate hints for each cell
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] != -1) {
                    int count = 0;
                    // Check adjacent cells
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nx = i + dx;
                            int ny = j + dy;
                            if (nx >= 0 && nx < size && ny >= 0 && ny < size && field[nx][ny] == -1) {
                                count++;
                            }
                        }
                    }
                    field[i][j] = count;
                }
            }
        }
    }

    public int getCell(int x, int y) {
        return field[x][y];
    }
}

