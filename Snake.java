/*
 * Author: Timothy Shane, tshane2022@my.fit.edu
 * Course: CSE 1002, Section 01, Spring 2023
 * Project: Snake Game
 */

import java.util.Random;

public final class Snake {
   static final int LEFT_BOUND = 1;
   static final int UP_BOUND = 19;
   static final int RIGHT_BOUND = 19;
   static final int DOWN_BOUND = 1;
   static final double TEXT_X_POSITION = 0.5;
   static final double SNAKE_TEXT_Y_POSITION = 0.7;
   static final double GAME_OVER_TEXT_Y_POSITION = 0.8;
   static final double SCORE_TEXT_Y_POSITION = 0.7;
   static final double HIGHSCORE_TEXT_Y_POSITION = 0.6;
   static final double SPACEBAR_TEXT_Y_POSITION = 0.5;
   static final int SPACEBAR_KEYCODE = 32;
   static final int LEFT_KEYCODE = 37;
   static final int UP_KEYCODE = 38;
   static final int RIGHT_KEYCODE = 39;
   static final int DOWN_KEYCODE = 40;
   static final double RIGHT_BORDER_X_POSITION = 0.99;
   static final double BORDER_CENTER = 0.5;
   static final double LEFT_BORDER_X_POSITION = 0.01;
   static final double HALF_WIDTH = 0.02;
   static final int HALF_HEIGHT = 1;
   static final int FOOD_NUMBER = 1000;
   static final int RNG_RANGE = 18;
   static final int STDDRAW_PAUSE_TIME = 20;
   static final int BOARD_SIZE = 22;
   static final double SCORE_Y_POSITION = 0.98;
   static final double COORDINATE_CONVERSION_CONSTANT = 0.05;
   static final double SNAKE_SIZE = 0.02;

   public static void main (final String[] args) {
      // Welcome screen for first round
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.filledSquare(1, 1, 1);
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.text(TEXT_X_POSITION, SNAKE_TEXT_Y_POSITION, "Snake");
      StdDraw.text(TEXT_X_POSITION, SPACEBAR_TEXT_Y_POSITION, "Press spacebar to start game");
      while (!StdDraw.isKeyPressed(SPACEBAR_KEYCODE)) {
         // Wait for spacebar to be pressed
      }
      int score = runGame();
      int highScore = score;

      // Game over screen for all other rounds
      while (true) {
         StdDraw.disableDoubleBuffering();
         StdDraw.setPenColor(StdDraw.WHITE);
         StdDraw.filledSquare(1, 1, 1);
         StdDraw.setPenColor(StdDraw.BLACK);
         StdDraw.text(TEXT_X_POSITION, GAME_OVER_TEXT_Y_POSITION, "Game Over");
         StdDraw.text(TEXT_X_POSITION, SCORE_TEXT_Y_POSITION, "Score: " + score);
         StdDraw.text(TEXT_X_POSITION, HIGHSCORE_TEXT_Y_POSITION, "High Score: " + highScore);
         StdDraw.text(TEXT_X_POSITION, SPACEBAR_TEXT_Y_POSITION,
               "Press spacebar to start a new game");
         while (!StdDraw.isKeyPressed(SPACEBAR_KEYCODE)) {
            // Wait for spacebar to be pressed
         }
         score = runGame();
         if (score > highScore) {
            highScore = score;
         }
      }      
   }

   // Draw the boundaries of the board
   public static void drawBorder () {
      StdDraw.setPenColor (StdDraw.GRAY);
      StdDraw.filledRectangle(RIGHT_BORDER_X_POSITION, BORDER_CENTER, HALF_WIDTH, HALF_HEIGHT);
      StdDraw.filledRectangle(LEFT_BORDER_X_POSITION, BORDER_CENTER, HALF_WIDTH, HALF_HEIGHT);
      StdDraw.filledRectangle(BORDER_CENTER, RIGHT_BORDER_X_POSITION, HALF_HEIGHT, HALF_WIDTH);
      StdDraw.filledRectangle(BORDER_CENTER, LEFT_BORDER_X_POSITION, HALF_HEIGHT, HALF_WIDTH);
   }

   // Draw the snake
   public static void drawSnake (final double x, final double y, final int[][] board) {
      StdDraw.setPenColor (StdDraw.GREEN);      
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] != 0) {
               StdDraw.filledSquare (convertCoordinates(i), convertCoordinates(j), SNAKE_SIZE);
            }
         }
      }
   }
   
   // Draw the food
   public static void drawFood (final int[][] board) {
      StdDraw.setPenColor(StdDraw.RED);
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] == FOOD_NUMBER) {
               StdDraw.filledSquare (convertCoordinates(i), convertCoordinates(j), SNAKE_SIZE);
            }
         }
      }
   }

   // Convert canvas coordinates to an array position
   public static double convertCoordinates (final int position) {
      return (position * COORDINATE_CONVERSION_CONSTANT);
   }

   // Ensure that the snake is drawn the right way and is the correct length
   public static void refresh (final int[][] board, final int direction, final int length) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] != FOOD_NUMBER && board[i][j] != 0) {
               board[i][j]--;
            }
         }
      }
   }

   // Draw the score
   public static void drawScore (final int score) {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.text(TEXT_X_POSITION, SCORE_Y_POSITION, "Score: " + score);
   }

   // Accepts user input and detects if the snake eats the food, or collides with something
   public static int runGame () {
      final int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
      StdDraw.enableDoubleBuffering();
      board[10][10] = 3;
      board[9][10] = 2;
      board[8][10] = 1;
      int x = 10;
      int y = 10;
      int direction = -1;
      int length = 3;
      final Random RNG = new Random();
      int foodX = RNG.nextInt(RNG_RANGE) + 1;
      int foodY = RNG.nextInt(RNG_RANGE) + 1;

      // Put food in a random spot that is not on the snake
      do {
         foodX = RNG.nextInt(RNG_RANGE) + 1;
         foodY = RNG.nextInt(RNG_RANGE) + 1;
      } while (board[foodX][foodY] != 0);

      boolean gameOver = false;

      while (!gameOver) {
         StdDraw.clear();
         drawBorder();         
         drawSnake(x, y, board);
         board[foodX][foodY] = FOOD_NUMBER;
         drawFood(board);
         drawScore(length - 3);
         StdDraw.show();

         // Detect user input
         for (int i = 0; i < 5; i++) {
            // left
            if (StdDraw.isKeyPressed(LEFT_KEYCODE)) {
               if (direction != 2) {
                  direction = 0;
               }
            }
            // up
            if (StdDraw.isKeyPressed(UP_KEYCODE)) {
               if (direction != 3) {
                  direction = 1;
               }
            }
            // right
            if (StdDraw.isKeyPressed(RIGHT_KEYCODE)) {
               if (direction != 0) {
                  direction = 2;
               }
            }
            // down
            if (StdDraw.isKeyPressed(DOWN_KEYCODE)) {
               if (direction != 1) {
                  direction = 3;
               }
            }
            StdDraw.pause(STDDRAW_PAUSE_TIME);
         }

         // Automatically move the snake and detect wall collisions
         // Left
         if (direction == 0) {
            if (x == LEFT_BOUND || ((board[x][y + 1] != 0 && board[x][y - 1] != 0)
                  && (board[x][y + 1] != FOOD_NUMBER && board[x][y - 1] != FOOD_NUMBER))) {
               gameOver = true;
            } else {
               x--;
               refresh(board, direction, length);
            }
         }
         // Up
         if (direction == 1) {
            if (y == UP_BOUND || ((board[x + 1][y] != 0 && board[x - 1][y] != 0)
                  && (board[x + 1][y] != FOOD_NUMBER && board[x - 1][y] != FOOD_NUMBER))) {
               gameOver = true;
            } else {
               y++;
               refresh(board, direction, length);
            }
         }
         // Right
         if (direction == 2) {
            if (x == RIGHT_BOUND || ((board[x][y + 1] != 0 && board[x][y - 1] != 0)
                  && (board[x][y + 1] != FOOD_NUMBER && board[x][y - 1] != FOOD_NUMBER))) {
               gameOver = true;
            } else {
               x++;
               refresh(board, direction, length);
            }
         }
         // Down
         if (direction == 3) {
            if (y == DOWN_BOUND || ((board[x + 1][y] != 0 && board[x - 1][y] != 0)
                  && (board[x + 1][y] != FOOD_NUMBER && board[x - 1][y] != FOOD_NUMBER))) {
               gameOver = true;
            } else {
               y--;
               refresh(board, direction, length);
            }
         }

         // Put food in a random spot that is not on the snake
         if (x == foodX && y == foodY) {
            length++;
            do {
               foodX = RNG.nextInt(RNG_RANGE) + 1;
               foodY = RNG.nextInt(RNG_RANGE) + 1;
            } while (board[foodX][foodY] != 0);
         }
         board[x][y] = length;
      }
      // Return the score after the game ends
      return length - 3;
   }
}
