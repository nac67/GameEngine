package model.tileboard;

import java.awt.Point;

interface Tileboard {
    
    /**
     * Sets the size of the tiles for the tileboard
     * @param width in pixels
     * @param height in pixels
     */
    void setTileSize(int width, int height);

    /**
     * loads an image file into the tileboard that
     * can later be recalled by referring to its name
     * @param name the identifier for the tile
     * @param filepath the path to load the file from
     */
    void loadTile(String name, String filepath);

    /**
     * Draws a tile onto the tileboard
     * @param name the identifier that was specified in loadTile
     * @param x the x position in tiles, or column number
     * @param y the y position in tiles, or row number
     */
    void drawTile(String name, int x, int y);

    /**
     * Draws a tile onto the tileboard
     * @param name the identifier that was specified in loadTile
     * @param p the position to be placed in unit of tiles
     */
    void drawTile(String name, Point p);

    /**
     * Fills the board with a given tile specified by its ID
     * @param name the identifier that was specified in loadTile
     */
    void fillBoard(String name);
    
    /**
     * Yields the width of the board in tiles and the height of the
     * board in tiles
     * @return a Point with x as number of tiles wide and y
     *     as number of tiles tall
     */
    Point getDimensions();
}