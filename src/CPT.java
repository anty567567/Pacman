/*
Anthony De Luca, Darren Li
Date: 6/19/2017
Last Update: 6/19/2017
Program Name: Pacman
Player 1 vs. Player 2. One player chases the other player in an attempt to stop him/her from collecting all the pellets. Acquire high scores by collecting pellets and eating the ghost after consuming the powerup.
 */
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.JPanel;
import java.awt.Font;

public class CPT extends JPanel implements KeyListener {

    //INITIALIZING ALL VARIABLES INCLUDING 2D ARRAYS, RECTS, AND FONTS
    private int size = 35, playerX = size * 9 + 1, playerY = 9 * size + 1, xVelocity = 1, yVelocity = 1, points = 0, mouthCount = 0, ghostX = 9 * size + 1, ghostY = size * 10 + 1;
    private boolean somethingToLeft = false, somethingToUp = false, somethingToDown = false, left = false, right = false, up = false, down = false, tryUp = false, tryDown = false, tryLeft = false, tryRight = false, somethingToRight = false;
    private boolean somethingToLeftGhost = false, somethingToUpGhost = false, somethingToDownGhost = false, somethingToRightGhost = false, leftGhost = false, rightGhost = false, upGhost = false, downGhost = false, tryLeftGhost = false, tryRightGhost = false, tryUpGhost = false, tryDownGhost = false, ghostKillable = false;
    private Rectangle player = new Rectangle(playerX, playerY, size, size), leftSide = new Rectangle(playerX - 1, playerY, 1, size), rightSide = new Rectangle(playerX + size, playerY, 1, size), topSide = new Rectangle(playerX, playerY - 1, size, 1), bottomSide = new Rectangle(playerX, playerY + size, size, 1), consumer = new Rectangle(playerX + (size / 2), playerY + (size / 2), 1, 1);
    private Rectangle ghost = new Rectangle(ghostX, ghostY, size, size), leftSideGhost = new Rectangle(ghostX - 1, ghostY, 1, size), rightSideGhost = new Rectangle(ghostX + size, ghostY, 1, size), topSideGhost = new Rectangle(ghostX, ghostY - 1, size, 1), bottomSideGhost = new Rectangle(ghostX, ghostY + size, size, 1);
    private Rectangle[][] mapRects = new Rectangle[19][19], pelletRects = new Rectangle[19][19], menuRects = new Rectangle[19][19], instructionsRects = new Rectangle[19][19];
    private int[] mouthRightX = {0, 0, 0}, mouthRightY = {0, 0, 0};
    private int choice = 0, ghostTimer = 0, ghostEscape = 0, highScore = 0;
    private final Font titleFont = new Font("Tekton Pro", Font.BOLD, 125);
    private final Font instructionsPlayer = new Font("Tekton Pro", Font.BOLD, 35);
    private final Font instructionsText = new Font("Tekton Pro", Font.BOLD, 25);
    private final Font pointsFont = new Font("Helvetica", Font.BOLD, 14);

    //INITIALIZING MAP ARRAYS WHICH WOULD LATER BE CONVERTED INTO RECT ARRAYS

    //INSTRUCTION SCREEN MAP
    private int[][] instructions = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 7, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},

            //GAME BOARD MAP
    }, map = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 9, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 9, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 2, 1, 2, 1, 1, 4, 1, 1, 2, 1, 2, 3, 3, 3, 3},
            {0, 0, 0, 0, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 0, 0, 0, 0},
            {3, 3, 3, 3, 2, 1, 2, 1, 0, 0, 0, 1, 2, 1, 2, 3, 3, 3, 3},
            {3, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 2, 2, 0, 2, 2, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 9, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 9, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    },
    //DEFAULT RESET MAP
    backupMap = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 9, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 9, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 2, 1, 2, 1, 1, 4, 1, 1, 2, 1, 2, 3, 3, 3, 3},
            {0, 0, 0, 0, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 0, 0, 0, 0},
            {3, 3, 3, 3, 2, 1, 2, 1, 0, 0, 0, 1, 2, 1, 2, 3, 3, 3, 3},
            {3, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 2, 2, 0, 2, 2, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 9, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 9, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    },
    //MENU SCREEN MAP
    menu = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {3, 3, 3, 3, 3, 3, 3, 3, 1, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 0, 0, 0, 0, 5, 5, 5, 1, 0, 1, 6, 6, 6, 0, 0, 0, 0, 3},
            {3, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 3},
            {3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3},
            {3, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 3},
            {3, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 3},
            {3, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 3},
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    };

    //CAUSES A DELAY OF THE GAME FOR TRANSITIONS
    private void delay(int x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException ex) {

        }
    }

    //ACTS AS THE MAIN METHOD
    private CPT() {

        //SETTING FRAME AND DIMENSIONS
        JFrame pacMan = new JFrame("WAKAWAKA");
        pacMan.setSize(800, 1000);
        pacMan.setVisible(true);
        pacMan.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pacMan.add(this);
        pacMan.addKeyListener(this);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        pacMan.setPreferredSize(new Dimension(688, 720));
        pacMan.pack();

        //CONVERTS 2D INT ARRAYS TO
        initializeMap(menuRects, menu);
        initializeMap(instructionsRects, instructions);

        //MAIN LOOP OF GAME
        while (true) {

            //SECONDARY LOOP FOR GAME SEGMENTS
            while (choice == 0 || choice == 2) {
                if (choice == 0) {

                    //CHECKS FOR COLLISIONS WITHIN MENU SCREEN
                    checkCollisions(menuRects, menu);
                } else {

                    //CHECKS FOR COLLISIONS WITHIN INSTRUCTIONS SCREEN
                    checkCollisions(instructionsRects, instructions);
                }

                //EXECUTES MOVEMENT OF PACMAN
                tryMovement();
                playerMovement();
                delay(8);
            }

            //INITIALIZES THE GAME BOARD
            initializeMap(mapRects, map);

            //SECONDARY LOOP FOR GAME SEGMENT
            while (choice == 1) {

                //ALL METHODS PERTAINING TO MOVEMENT AND COLLISIONS
                checkCollisions(mapRects, map);
                tryMovement();
                checkCollisionsGhost();
                checkTunnel();
                tryMovementGhost();
                playerMovement();
                ghostMovement();
                delay(8);
                checkGhostOnPlayer();

                //CHECKS WHETHER THE GHOST CAN LEAVE THE HUB OR BE KILLED
                checkGhostEscape();
                checkKillable();

                //CHECKS IF THE ROUND IS OVER AND THEN RESETS EVERYTHING
                if (!checkPelletsLeft()) {
                    delay(2000);
                    player.x = 9 * size + 1;
                    leftSide.x = 9 * size;
                    bottomSide.x = 9 * size + 1;
                    topSide.x = 9 * size + 1;
                    rightSide.x = 10 * size + 1;
                    consumer.x = 9 * size + size / 2;
                    player.y = 12 * size + 1;
                    leftSide.y = 12 * size + 1;
                    bottomSide.y = 13 * size + 1;
                    topSide.y = 12 * size;
                    rightSide.y = 12 * size + 1;
                    consumer.y = 12 * size + size / 2;
                    ghostEscape = 0;
                    ghost.x = ghostX;
                    leftSideGhost.x = ghostX - 1;
                    rightSideGhost.x = ghostX + size;
                    topSideGhost.x = ghostX;
                    bottomSideGhost.x = ghostX;
                    ghost.y = ghostY;
                    leftSideGhost.y = ghostY;
                    rightSideGhost.y = ghostY;
                    topSideGhost.y = ghostY - 1;
                    bottomSideGhost.y = ghostY + size;
                    ghostKillable = false;
                    resetMap(map, backupMap);
                    initializeMap(mapRects, map);
                    delay(5000);
                }

            }
        }
    }

    //THESE METHODS CONTROL THE MOVEMENT OF PACMAN BY CHANGING HIS COORDINATE VALUES
    private void moveUp() {
        down = false;
        right = false;
        left = false;
        player.y -= yVelocity;
        leftSide.y -= yVelocity;
        rightSide.y -= yVelocity;
        topSide.y -= yVelocity;
        bottomSide.y -= yVelocity;
        consumer.y -= yVelocity;
    }

    private void moveDown() {
        up = false;
        left = false;
        right = false;
        player.y += yVelocity;
        leftSide.y += yVelocity;
        bottomSide.y += yVelocity;
        topSide.y += yVelocity;
        rightSide.y += yVelocity;
        consumer.y += yVelocity;
    }

    private void moveLeft() {
        right = false;
        up = false;
        down = false;
        player.x -= xVelocity;
        leftSide.x -= xVelocity;
        rightSide.x -= xVelocity;
        topSide.x -= xVelocity;
        bottomSide.x -= xVelocity;
        consumer.x -= xVelocity;
    }

    private void moveRight() {
        left = false;
        up = false;
        down = false;
        player.x += xVelocity;
        leftSide.x += xVelocity;
        rightSide.x += xVelocity;
        topSide.x += xVelocity;
        bottomSide.x += xVelocity;
        consumer.x += xVelocity;
    }

    //THESE METHODS STOP PACMANS MOVEMENT WHEN HE IS COLLIDING WITH A RECT ON THAT SIDE OF HIS BODY
    private void stopWhenCollidingLeft() {
        left = false;
        somethingToLeft = true;
    }

    private void stopWhenCollidingRight() {
        right = false;
        somethingToRight = true;
    }

    private void stopWhenCollidingTop() {
        up = false;
        somethingToUp = true;
    }

    private void stopWhenCollidingDown() {
        down = false;
        somethingToDown = true;
    }

    //THESE METHODS CONTROL THE MOVEMENT OF THE GHOST PLAYER
    private void moveUpGhost() {
        downGhost = false;
        rightGhost = false;
        leftGhost = false;
        ghost.y -= yVelocity;
        leftSideGhost.y -= yVelocity;
        rightSideGhost.y -= yVelocity;
        topSideGhost.y -= yVelocity;
        bottomSideGhost.y -= yVelocity;
    }

    private void moveDownGhost() {
        upGhost = false;
        leftGhost = false;
        rightGhost = false;
        ghost.y += yVelocity;
        leftSideGhost.y += yVelocity;
        bottomSideGhost.y += yVelocity;
        topSideGhost.y += yVelocity;
        rightSideGhost.y += yVelocity;
    }

    private void moveLeftGhost() {
        rightGhost = false;
        upGhost = false;
        downGhost = false;
        ghost.x -= xVelocity;
        leftSideGhost.x -= xVelocity;
        rightSideGhost.x -= xVelocity;
        topSideGhost.x -= xVelocity;
        bottomSideGhost.x -= xVelocity;
    }

    private void moveRightGhost() {
        leftGhost = false;
        upGhost = false;
        downGhost = false;
        ghost.x += xVelocity;
        leftSideGhost.x += xVelocity;
        rightSideGhost.x += xVelocity;
        topSideGhost.x += xVelocity;
        bottomSideGhost.x += xVelocity;
    }

    //THESE METHODS STOP THE GHOST'S MOVEMENT IF HE IS COLLIDING WITH A RECT ON THAT SIDE OF HIS BODY
    private void stopWhenCollidingLeftGhost() {
        leftGhost = false;
        somethingToLeftGhost = true;
    }

    private void stopWhenCollidingRightGhost() {
        rightGhost = false;
        somethingToRightGhost = true;
    }

    private void stopWhenCollidingTopGhost() {
        upGhost = false;
        somethingToUpGhost = true;
    }

    private void stopWhenCollidingDownGhost() {
        downGhost = false;
        somethingToDownGhost = true;
    }

    //INITIALIZES THE MAP BY CONVERTING A 2D INT ARRAY INTO A 2D RECT ARRAY
    private void initializeMap(Rectangle[][] rectangles, int[][] arrayMap) {
        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                if (arrayMap[i][j] == 1 || arrayMap[i][j] == 3 || arrayMap[i][j] == 4 || arrayMap[i][j] == 5 || arrayMap[i][j] == 6 || arrayMap[i][j] == 7) {
                    //CREATES WALLS
                    rectangles[i][j] = new Rectangle(1 + (j * size), 1 + (i * size), size, size);
                } else if (arrayMap[i][j] == 2 || arrayMap[i][j] == 9) {
                    //CREATES PELLETS
                    pelletRects[i][j] = new Rectangle(1 + (j * size), 1 + (i * size), size, size);
                }

            }
        }

    }

    //DECIDES WHICH DIRECTION THE PLAYER IS ABLE TO AND WANTS TO MOVE AND CALLS THE METHOD TO DO SO
    private void playerMovement() {
        if (up) {
            moveUp();
        } else if (down) {
            moveDown();
        } else if (left) {
            moveLeft();
        } else if (right) {
            moveRight();
        }
    }

    //THE SAME AS PLAYER MOVEMENT BUT FOR THE GHOST
    private void ghostMovement() {
        if (upGhost) {
            moveUpGhost();
        } else if (downGhost) {
            moveDownGhost();
        } else if (leftGhost) {
            moveLeftGhost();
        } else if (rightGhost) {
            moveRightGhost();
        }
    }

    //CHECKS COLLISIONS WITH WALLS AND PELLETS FOR PACMAN
    private void checkCollisions(Rectangle[][] rectangles, int[][] arrayMap) {
        somethingToLeft = false;
        somethingToRight = false;
        somethingToUp = false;
        somethingToDown = false;

        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                //CHECKS WALLS
                if (arrayMap[i][j] == 1 || arrayMap[i][j] == 3 || arrayMap[i][j] == 4) {
                    if (leftSide.intersects(rectangles[i][j])) {
                        stopWhenCollidingLeft();
                    }
                    if (rightSide.intersects(rectangles[i][j])) {
                        stopWhenCollidingRight();
                    }
                    if (topSide.intersects(rectangles[i][j])) {
                        stopWhenCollidingTop();
                    }
                    if (bottomSide.intersects(rectangles[i][j])) {
                        stopWhenCollidingDown();
                    }
                }
                //CHECKS PELLETS
                if ((arrayMap[i][j] == 2 || arrayMap[i][j] == 9) && consumer.intersects(pelletRects[i][j])) {
                    eatPellet(i, j);
                }
                //CHECKS PORTALS BETWEEN GAME SEGMENTS
                if (arrayMap[i][j] == 5 && rightSide.intersects(rectangles[i][j])) {
                    startGame();
                }
                if (arrayMap[i][j] == 6 && leftSide.intersects(rectangles[i][j])) {
                    startInstructions();
                }
                if (arrayMap[i][j] == 7 && leftSide.intersects(rectangles[i][j])) {
                    startMenu();
                }
            }
        }
    }

    //CHECKS IF THE GHOST AND PLAYER ARE COLLIDING
    private void checkGhostOnPlayer() {
        //CHECKS IF THE GHOST CAN BE EATEN BY THE PLAYER
        if (player.intersects(ghost) && ghostKillable) {
            points += 200;
            ghost.x = ghostX;
            leftSideGhost.x = ghostX - 1;
            rightSideGhost.x = ghostX + size;
            topSideGhost.x = ghostX;
            bottomSideGhost.x = ghostX;
            ghost.y = ghostY;
            leftSideGhost.y = ghostY;
            rightSideGhost.y = ghostY;
            topSideGhost.y = ghostY - 1;
            bottomSideGhost.y = ghostY + size;

            //CHECKS IF THE GHOST KILLS THE PLAYER AND THEN RESETS THE GAME
        } else if (player.intersects(ghost)) {
            if (points > highScore) {
                highScore = points;
            }
            resetMap(map, backupMap);
            initializeMap(mapRects, map);
            ghostEscape = 0;
            startMenu();
        }
    }

    //CHECKS IF THERE ARE PELLETS LEFT TO CONSUME, IF FALSE THEN THE GAME RESETS
    private boolean checkPelletsLeft() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    //CHECKS IF THE REQUESTED DIRECTION IS POSSIBLE
    private void tryMovement() {
        if (tryLeft && !somethingToLeft) {
            left = true;
            right = false;
            up = false;
            down = false;
        }
        if (tryRight && !somethingToRight) {
            right = true;
            left = false;
            up = false;
            down = false;
        }
        if (tryDown && !somethingToDown) {
            down = true;
            left = false;
            right = false;
            up = false;
        }
        if (tryUp && !somethingToUp) {
            down = false;
            left = false;
            right = false;
            up = true;
        }
    }

    //SAME AS CHECK COLLISIONS FOR PLAYER, BUT FOR GHOST
    private void checkCollisionsGhost() {
        somethingToLeftGhost = false;
        somethingToRightGhost = false;
        somethingToUpGhost = false;
        somethingToDownGhost = false;

        for (int i = 0; i < mapRects.length; i++) {
            for (int j = 0; j < mapRects[i].length; j++) {
                if (map[i][j] == 1 || map[i][j] == 3 || map[i][j] == 4) {
                    if (leftSideGhost.intersects(mapRects[i][j])) {
                        stopWhenCollidingLeftGhost();
                    }
                    if (rightSideGhost.intersects(mapRects[i][j])) {
                        stopWhenCollidingRightGhost();
                    }
                    if (topSideGhost.intersects(mapRects[i][j])) {
                        stopWhenCollidingTopGhost();
                    }
                    if (bottomSideGhost.intersects(mapRects[i][j])) {
                        stopWhenCollidingDownGhost();
                    }
                }
            }
        }

    }

    //SAME AS TRY MOVEMENT FOR PLAYER, BUT FOR GHOST
    private void tryMovementGhost() {
        if (tryLeftGhost && !somethingToLeftGhost) {
            leftGhost = true;
            rightGhost = false;
            upGhost = false;
            downGhost = false;
        }
        if (tryRightGhost && !somethingToRightGhost) {
            leftGhost = false;
            rightGhost = true;
            upGhost = false;
            downGhost = false;
        }
        if (tryDownGhost && !somethingToDownGhost) {
            leftGhost = false;
            rightGhost = false;
            upGhost = false;
            downGhost = true;
        }
        if (tryUpGhost && !somethingToUpGhost) {
            leftGhost = false;
            rightGhost = false;
            upGhost = true;
            downGhost = false;
        }
    }

    //CHECKS THE TUNNELS ON THE SIDE OF THE GAME BOARD AND IF PACMAN OR GHOST CAN GO THROUGH THEM
    private void checkTunnel() {

        //CHECKS FOR PACMAN IN THE TUNNELS AND MOVES TO OPPOSITE SIDE
        if (rightSide.x <= 2 && left) {
            player.x = (size * 19) + 1;
            leftSide.x = (size * 19);
            bottomSide.x = (size * 19) + 1;
            topSide.x = (size * 19) + 1;
            rightSide.x = (size * 20) + 1;
            consumer.x = (size * 19) + size / 2;
        }

        if (leftSide.x >= (size * 19 - 1) && right) {
            player.x = -(size - 1);
            leftSide.x = -(size);
            bottomSide.x = -(size - 1);
            topSide.x = -(size - 1);
            rightSide.x = 1;
            consumer.x = -size / 2;
        }

        //CHECKS FOR GHOST IN THE TUNNELS AND MOVES TO OPPOSITE SIDE
        if (leftSideGhost.x >= (size * 19 - 1) && rightGhost) {
            ghost.x = -(size - 1);
            leftSideGhost.x = -(size);
            bottomSideGhost.x = -(size - 1);
            topSideGhost.x = -(size - 1);
            rightSideGhost.x = 1;
        }

        if (rightSideGhost.x <= 2 && leftGhost) {
            ghost.x = (size * 19) + 1;
            leftSideGhost.x = (size * 19);
            bottomSideGhost.x = (size * 19) + 1;
            topSideGhost.x = (size * 19) + 1;
            rightSideGhost.x = (size * 20) + 1;
        }

    }

    //CHECKS IF THE GHOST CAN LEAVE THE HUB
    private void checkGhostEscape() {
        //COUNTS UP TO ACT AS A TIMER AND THEN RELEASES THE GHOST
        if (ghostEscape >= 800) {
            map[8][9] = 0;
            mapRects[8][9] = null;
        } else {
            ghostEscape++;
        }
    }

    //CHECKS IF PACMAN CAN EAT THE GHOST
    private void checkKillable() {
        //ACTS AS A TIMER FOR THE DURATION OF PACMAN POWERUP
        if (ghostKillable) {
            ghostTimer++;
        }
        if (ghostTimer >= 700) {
            ghostKillable = false;
            ghostTimer = 0;
        }
    }

    //CONSUMES THE PELLET THAT PACMAN IS COLLIDING WITH, ADDS POINTS, AND REMOVES IT FROM ARRAY
    private void eatPellet(int i, int j) {
        if (map[i][j] == 9) {
            ghostKillable = true;
        }
        pelletRects[i][j] = null;
        map[i][j] = 0;
        points += 10;
    }

    //RESETS THE MAP TO A DEFAULT SO THE GAME CAN BE RUN AGAIN
    private void resetMap(int[][] map, int[][] backupMap) {
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(backupMap[i], 0, map[i], 0, map[i].length);
        }
    }

    //INITIALIZES THE GAME SECTION
    private void startGame() {
        //SETS POSITIONS AND VARIABLES
        choice = 1;
        ghostEscape = 0;
        points = 0;
        player.x = 9 * size + 1;
        leftSide.x = 9 * size;
        bottomSide.x = 9 * size + 1;
        topSide.x = 9 * size + 1;
        rightSide.x = 10 * size + 1;
        consumer.x = 9 * size + (size / 2);
        player.y = 12 * size + 1;
        leftSide.y = 12 * size + 1;
        bottomSide.y = 13 * size + 1;
        topSide.y = 12 * size;
        rightSide.y = 12 * size + 1;
        consumer.y = 12 * size + (size / 2);
        ghost.x = ghostX;
        leftSideGhost.x = ghostX - 1;
        rightSideGhost.x = ghostX + size;
        topSideGhost.x = ghostX;
        bottomSideGhost.x = ghostX;
        ghost.y = ghostY;
        leftSideGhost.y = ghostY;
        rightSideGhost.y = ghostY;
        topSideGhost.y = ghostY - 1;
        bottomSideGhost.y = ghostY + size;
        delay(4000);
        left = false;
        right = false;
        up = false;
        down = false;
    }

    //INITIALIZES THE INSTRUCTIONS SECTION
    private void startInstructions() {
        //SETS POSITIONS AND VARIABLES
        choice = 2;
        player.x = 9 * size + 1;
        leftSide.x = 9 * size;
        bottomSide.x = 9 * size + 1;
        topSide.x = 9 * size + 1;
        rightSide.x = 10 * size + 1;

        player.y = 14 * size + 1;
        leftSide.y = 14 * size + 1;
        bottomSide.y = 15 * size + 1;
        topSide.y = 14 * size;
        rightSide.y = 14 * size + 1;
        left = false;
        right = false;
        up = false;
        down = false;
    }

    //INITIALIZES THE MENU SECTION
    private void startMenu() {
        //SETS POSITIONS AND VARIABLES
        choice = 0;
        left = false;
        right = false;
        up = false;
        down = false;
        player.x = playerX;
        leftSide.x = playerX - 1;
        bottomSide.x = playerX;
        topSide.x = playerX;
        rightSide.x = playerX + size;
        player.y = playerY;
        leftSide.y = playerY;
        bottomSide.y = playerY + size;
        topSide.y = playerY - 1;
        rightSide.y = playerY;
        tryRight = true;
    }

    //DRAWS ALL GRAPHICS
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        //CHECKS IF THE GAME IS RUNNING AND DRAWS ACCORDINGLY
        if (choice == 1 && checkPelletsLeft()) {
            g.clearRect(0, 0, 1000, 1000);
            g.setColor(Color.black);
            g.fillRect(0, 0, 700, 700);
            //DRAWS WALLS AND PELLETS
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    g.setColor(Color.black);
                    if (map[i][j] == 1) {
                        g.setColor(Color.blue);

                    } else if (map[i][j] == 2) {
                        g.setColor(Color.white);

                    } else if (map[i][j] == 3) {
                        g.setColor(Color.gray);

                    } else if (map[i][j] == 4) {
                        g.setColor(Color.red);

                    } else if (map[i][j] == 5) {
                        g.setColor(Color.green);

                    } else if (map[i][j] == 6) {
                        g.setColor(Color.magenta);

                    }

                    //REGULAR PELLET
                    if (map[i][j] == 2) {
                        g.setColor(Color.white);
                        g.fillOval(12 + (j * size), 12 + (i * size), 10, 10);
                    }
                    //POWER PELLET
                    else if (map[i][j] == 9) {
                        g.setColor(Color.white);
                        g.fillOval(7 + (j * size), 7 + (i * size), 20, 20);
                    } else {
                        g.fillRect(1 + (j * size), 1 + (i * size), size, size);
                    }

                    //DRAWS POINTS
                    g.setColor(Color.black);
                    g.setFont(pointsFont);
                    g.drawString("Points: ", 300, 20);
                    g.drawString(String.valueOf(points), 350, 20);
                    //CHANGES THE GHOST'S COLOUR WHEN IT IS KILLABLE
                    if (choice == 1) {
                        if (ghostKillable) {
                            g.setColor(Color.blue);
                        } else {
                            g.setColor(Color.magenta);
                        }
                        g.fillOval(ghost.x, ghost.y, size, size);
                    }
                }
            }
            //CHECKS IF IT IS THE MENU AND DRAWS ACCORDINGLY
        } else if (choice == 0) {
            g.clearRect(0, 0, 1000, 1000);
            g.setColor(Color.black);
            g.fillRect(0, 0, 700, 700);
            //DRAWS THE WALLS
            for (int i = 0; i < menu.length; i++) {
                for (int j = 0; j < menu[i].length; j++) {
                    g.setColor(Color.black);
                    if (menu[i][j] == 1) {
                        g.setColor(Color.blue);

                    } else if (menu[i][j] == 2) {
                        g.setColor(Color.white);

                    } else if (menu[i][j] == 3) {
                        g.setColor(Color.gray);

                    } else if (menu[i][j] == 4) {
                        g.setColor(Color.red);

                    } else if (menu[i][j] == 5) {
                        g.setColor(Color.green);

                    } else if (menu[i][j] == 6) {
                        g.setColor(Color.magenta);
                    }

                    g.fillRect(1 + (j * size), 1 + (i * size), size, size);

                }
            }
            //DRAWS ALL TEXT ON SCREEN
            g.setFont(titleFont);
            g.setColor(Color.yellow);
            g.drawString("PACMAN", 65, 200);
            g.setColor(Color.pink);
            g.setFont(pointsFont);
            g.drawString("HIGH SCORE: ", 280, 20);
            g.drawString(String.valueOf(highScore), 380, 20);
            g.setColor(Color.red);
            g.drawString("Pac - arrows, ghost - wasd", 250, 275);
            g.setColor(Color.white);
            g.drawString("PLAY TILL YOUR OPPONENT CATCHES", 10, 340);
            g.drawString("YOU THEN SWAP TO GET HIGH SCORE", 390, 340);
            g.setColor(Color.blue);
            g.drawString("PLAY", 180 + 30, 11 * size + 23);
            g.setColor(Color.white);
            g.drawString("INSTRUCTIONS", 387, 11 * size + 23);


            //CHECKS IF IT IS INSTRUCTIONS AND DRAWS ACCORDINGLY
        } else if (choice == 2) {
            g.clearRect(0, 0, 1000, 1000);
            g.setColor(Color.black);
            g.fillRect(0, 0, 700, 700);
            //DRAWS THE WALLS OF THE INSTRUCTIONS SCREEN
            for (int i = 0; i < instructions.length; i++) {
                for (int j = 0; j < instructions[i].length; j++) {
                    g.setColor(Color.black);
                    if (instructions[i][j] == 1) {
                        g.setColor(Color.blue);

                    } else if (instructions[i][j] == 2) {
                        g.setColor(Color.white);

                    } else if (instructions[i][j] == 3) {
                        g.setColor(Color.gray);

                    } else if (instructions[i][j] == 4) {
                        g.setColor(Color.red);

                    } else if (instructions[i][j] == 5) {
                        g.setColor(Color.green);

                    } else if (instructions[i][j] == 6) {
                        g.setColor(Color.magenta);
                    } else if (instructions[i][j] == 7) {
                        g.setColor(Color.white);
                    }

                    g.fillRect(1 + (j * size), 1 + (i * size), size, size);

                }
            }

            //DRAWS THE TEXT ON THE INSTRUCTION SCREEN
            g.setFont(instructionsPlayer);
            g.setColor(Color.CYAN);
            g.drawString("Instructions", 250, 100);
            g.setColor(Color.green);
            g.drawString("Arrow Keys", 70, 430);
            g.drawString("WASD", 460, 430);
            g.setColor(Color.yellow);
            g.drawString("Player", 100, 150 + 25);
            g.setColor(Color.magenta);
            g.drawString("Ghost", 460, 150 + 25);
            g.setFont(instructionsText);
            g.setColor(Color.yellow);
            g.drawString("1) Collect Pellets", 50, 225 + 25);
            g.drawString("2) Run From Ghost", 50, 300 + 25);
            g.setColor(Color.magenta);
            g.drawString("Stop. Pacman.", 445, 225 + 25);
            g.setFont(pointsFont);
            g.setColor(Color.black);
            g.drawString("MENU", size + 15, size * 15 + 23);
            g.setColor(Color.blue);
            g.drawString("PLAY", size * 16 + 17, 15 * size + 23);

        }
        //DRAWS PACMAN
        g.setColor(Color.yellow);
        g.fillOval(player.x, player.y, size, size);

        g.setColor(Color.black);

        //DRAWS HIS MOUTH OPENING AND CLOSING BASED ON THE DIRECTION HE IS MOVING
        if (mouthCount % 2 == 0 && (left || right || up || down))

        {
            if (up) {
                mouthRightX[0] = player.x + size / 2;
                mouthRightX[1] = player.x + size / 6;
                mouthRightX[2] = player.x + (size * 5 / 6);
                mouthRightY[0] = player.y + size / 2;
                mouthRightY[1] = player.y;
                mouthRightY[2] = player.y;
            }

            if (right) {
                mouthRightX[0] = player.x + size / 2;
                mouthRightX[1] = player.x + size;
                mouthRightX[2] = player.x + size;
                mouthRightY[0] = player.y + size / 2;
                mouthRightY[1] = player.y + size / 6;
                mouthRightY[2] = player.y + (size * 5 / 6);
            }

            if (down) {
                mouthRightX[0] = player.x + size / 2;
                mouthRightX[1] = player.x + size / 6;
                mouthRightX[2] = player.x + (size * 5 / 6);
                mouthRightY[0] = player.y + size / 2;
                mouthRightY[1] = player.y + size;
                mouthRightY[2] = player.y + size;
            }

            if (left) {
                mouthRightX[0] = player.x + size / 2;
                mouthRightX[1] = player.x;
                mouthRightX[2] = player.x;
                mouthRightY[0] = player.y + size / 2;
                mouthRightY[1] = player.y + size / 6;
                mouthRightY[2] = player.y + (size * 5 / 6);
            }

            g.fillPolygon(mouthRightX, mouthRightY, 3);
        }

        mouthCount++;

        repaint();

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CPT g = new CPT();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //KEY LISTENER TO TAKE INPUT
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            tryLeft = true;
            tryRight = false;
            tryUp = false;
            tryDown = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            tryLeft = false;
            tryRight = true;
            tryUp = false;
            tryDown = false;
        }

        if (key == KeyEvent.VK_UP) {
            tryLeft = false;
            tryRight = false;
            tryUp = true;
            tryDown = false;
        }

        if (key == KeyEvent.VK_DOWN) {
            tryLeft = false;
            tryRight = false;
            tryUp = false;
            tryDown = true;
        }

        if (key == KeyEvent.VK_S) {
            tryLeftGhost = false;
            tryRightGhost = false;
            tryUpGhost = false;
            tryDownGhost = true;
        }

        if (key == KeyEvent.VK_W) {
            tryLeftGhost = false;
            tryRightGhost = false;
            tryUpGhost = true;
            tryDownGhost = false;
        }

        if (key == KeyEvent.VK_A) {
            tryLeftGhost = true;
            tryRightGhost = false;
            tryUpGhost = false;
            tryDownGhost = false;
        }

        if (key == KeyEvent.VK_D) {
            tryLeftGhost = false;
            tryRightGhost = true;
            tryUpGhost = false;
            tryDownGhost = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}