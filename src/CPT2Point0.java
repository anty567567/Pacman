import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.JPanel;

public class CPT2Point0 extends JPanel implements KeyListener {

    public int size = 35, playerX = size * 9 + 1, playerY = 9 * size + 1, xVelocity = 1, yVelocity = 1, points = 0, mouthCount = 0, ghostX = size + 1, ghostY = size + 1;
    public boolean somethingToLeft = false, somethingToUp = false, somethingToDown = false, left = false, right = false, up = false, down = false, tryUp = false, tryDown = false, tryLeft = false, tryRight = false, somethingToRight = false;
    public boolean somethingToLeftGhost = false, somethingToUpGhost = false, somethingToDownGhost = false, somethingToRightGhost = false, leftGhost = false, rightGhost = false, upGhost = false, downGhost = false, tryLeftGhost = false, tryRightGhost = false, tryUpGhost = false, tryDownGhost = false;
    public Rectangle player = new Rectangle(playerX, playerY, size, size), leftSide = new Rectangle(playerX - 1, playerY, 1, size), rightSide = new Rectangle(playerX + size, playerY, 1, size), topSide = new Rectangle(playerX, playerY - 1, size, 1), bottomSide = new Rectangle(playerX, playerY + size, size, 1), consumer = new Rectangle(playerX + (size / 2), playerY + (size / 2), 1, 1);
    public Rectangle ghost = new Rectangle(ghostX, ghostY, size, size), leftSideGhost = new Rectangle(ghost.x - 1, ghostY, 1, size), rightSideGhost = new Rectangle(ghost.x + size, ghostY, 1, size), topSideGhost = new Rectangle(ghost.x, ghostY - 1, size, 1), bottomSideGhost = new Rectangle(ghost.x, ghostY + size, size, 1);
    public Rectangle[][] mapRects = new Rectangle[19][19], pelletRects = new Rectangle[19][19], menuRects = new Rectangle[19][19], ghostDirectionRects = new Rectangle[19][19];
    public int[] mouthRightX = {0, 0, 0}, mouthRightY = {0, 0, 0};
    public boolean play = false;
    
    //The tile maps for the menu screen and game
    public int[][] map = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3},
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
            {3, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    }, backupMap = {
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 3},
            {3, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 2, 1, 2, 1, 4, 4, 4, 1, 2, 1, 2, 3, 3, 3, 3},
            {0, 0, 0, 0, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 0, 0, 0, 0},
            {3, 3, 3, 3, 2, 1, 2, 1, 0, 0, 0, 1, 2, 1, 2, 3, 3, 3, 3},
            {3, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 2, 2, 0, 2, 2, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 3},
            {3, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 3},
            {3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 3},
            {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
    }, menu = {
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

    private void delay(int x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException ex) {

        }
    }

    public CPT2Point0() {
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
        
        initializeMap(menuRects, menu);
        while (!play) {
            checkCollisions(menuRects, menu);
            tryMovement();
            playerMovement();
            delay(8);
        }

        initializeMap(mapRects, map);

        while (checkPelletsLeft()) {
            checkCollisions(mapRects, map);
            tryMovement();
            checkTunnel();
            checkCollisionsGhost();
            playerMovement();
            ghostMovement();
            delay(8);
        }
    }

    //moves the character up
    public void moveUp() {
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

    //moves the character down
    public void moveDown() {
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

    //moves the character left
    public void moveLeft() {
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

    //moves the character right
    public void moveRight() {
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

    //These methods stop movement when there is a wall the player is going against
    public void stopWhenCollidingLeft() {
        left = false;
        somethingToLeft = true;
    }

    public void stopWhenCollidingRight() {
        right = false;
        somethingToRight = true;
    }

    public void stopWhenCollidingTop() {
        up = false;
        somethingToUp = true;
    }

    public void stopWhenCollidingDown() {
        down = false;
        somethingToDown = true;
    }

    //These methods move the ghost in the pressed direction
    public void moveUpGhost() {
        downGhost = false;
        rightGhost = false;
        leftGhost = false;
        ghost.y -= yVelocity;
        leftSideGhost.y -= yVelocity;
        rightSideGhost.y -= yVelocity;
        topSideGhost.y -= yVelocity;
        bottomSideGhost.y -= yVelocity;
    }

    public void moveDownGhost() {
        upGhost = false;
        leftGhost = false;
        rightGhost = false;
        ghost.y += yVelocity;
        leftSideGhost.y += yVelocity;
        bottomSideGhost.y += yVelocity;
        topSideGhost.y += yVelocity;
        rightSideGhost.y += yVelocity;
    }

    public void moveLeftGhost() {
        rightGhost = false;
        upGhost = false;
        downGhost = false;
        ghost.x -= xVelocity;
        leftSideGhost.x -= xVelocity;
        rightSideGhost.x -= xVelocity;
        topSideGhost.x -= xVelocity;
        bottomSideGhost.x -= xVelocity;
    }

    public void moveRightGhost() {
        leftGhost = false;
        upGhost = false;
        downGhost = false;
        ghost.x += xVelocity;
        leftSideGhost.x += xVelocity;
        rightSideGhost.x += xVelocity;
        topSideGhost.x += xVelocity;
        bottomSideGhost.x += xVelocity;
    }

    //These methods stop the ghost's movement when going against a wall
    public void stopWhenCollidingLeftGhost() {
        leftGhost = false;
        somethingToLeftGhost = true;
    }

    public void stopWhenCollidingRightGhost() {
        rightGhost = false;
        somethingToRightGhost = true;
    }

    public void stopWhenCollidingTopGhost() {
        upGhost = false;
        somethingToUpGhost = true;
    }

    public void stopWhenCollidingDownGhost() {
        downGhost = false;
        somethingToDownGhost = true;
    }

    //creates a 2D array of rectangles based off of the tile maps to act as the hitboxes for the walls and pellets
    public void initializeMap(Rectangle[][] rectangles, int[][] arrayMap) {
        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                if (arrayMap[i][j] == 1 || arrayMap[i][j] == 3 || arrayMap[i][j] == 4 || arrayMap[i][j] == 5 || arrayMap[i][j] == 6) {
                    rectangles[i][j] = new Rectangle(1 + (j * size), 1 + (i * size), size, size);
                } else if (arrayMap[i][j] == 2) {
                    pelletRects[i][j] = new Rectangle(1 + (j * size), 1 + (i * size), size, size);
                }
            }
        }
    }

    //checks the wanted direction of movement
    public void playerMovement() {
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

    public void ghostMovement() {
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

    //stops movement when against a wall or recognizes the picking up of a pellet
    public void checkCollisions(Rectangle[][] rectangles, int[][] arrayMap) {
        somethingToLeft = false;
        somethingToRight = false;
        somethingToUp = false;
        somethingToDown = false;

        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
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
                if (arrayMap[i][j] == 2 && consumer.intersects(pelletRects[i][j])) {
                    eatPellet(i, j);
                }
                if (arrayMap[i][j] == 5 && rightSide.intersects(rectangles[i][j])) {
                    startGame();
                }
            }
        }
    }

    //initializes all start values
    public void startGame() {
        play = true;
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
        delay(3000);
        left = false;
        right = false;
        up = false;
        down = false;
    }

    //determines if there are pellets remaining in the game
    public boolean checkPelletsLeft() {
        boolean gameOver;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    //These methods attempt movement in a direction if there is no wall there
    public void tryMovement() {
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

    public void checkCollisionsGhost() {
        somethingToLeftGhost = false;
        somethingToRightGhost = false;
        somethingToUpGhost = false;
        somethingToDownGhost = false;

        for (int i = 0; i < ghostDirectionRects.length; i++) {
            for (int j = 0; j < ghostDirectionRects[i].length; j++) {
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

    //transports the player between the tunnels at the sides of the screen
    public void checkTunnel() {

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

    //removes pellets from the screen and adds points
    public void eatPellet(int i, int j) {
        pelletRects[i][j] = null;
        map[i][j] = 0;
        points += 10;
    }

    public void resetMap(int[][] map, int[][] backupMap) {

        for (int i = 0; i < backupMap.length; i++) {
            for (int j = 0; j < backupMap[i].length; j++) {
                map[i][j] = backupMap[i][j];
            }
        }
        map = backupMap;

    }

    //all graphics for the game
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (play && checkPelletsLeft()) {
            g.clearRect(0, 0, 1000, 1000);
            g.setColor(Color.black);
            g.fillRect(0, 0, 700, 700);
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

                    if (map[i][j] == 2) {
                        g.setColor(Color.white);
                        g.fillOval(12 + (j * size), 12 + (i * size), 10, 10);
                    } else {
                        g.fillRect(1 + (j * size), 1 + (i * size), size, size);
                    }

                    g.setColor(Color.black);
                    g.drawString("Points: ", 300, 20);
                    g.drawString(String.valueOf(points), 350, 20);

                }
            }
        } else if (!play) {
            g.clearRect(0, 0, 1000, 1000);
            g.setColor(Color.black);
            g.fillRect(0, 0, 700, 700);
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
        }
        g.setColor(Color.yellow);
        g.fillOval(player.x, player.y, size, size);
        if (play) {
            g.setColor(Color.cyan);
            g.fillOval(ghost.x, ghost.y, size, size);
        }
        g.setColor(Color.black);


        if (mouthCount % 2 == 0) {
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

            if (left || right || down || up) {
                g.fillPolygon(mouthRightX, mouthRightY, 3);
            }
        }
        mouthCount++;

        repaint();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        CPT2Point0 g = new CPT2Point0();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

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
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
