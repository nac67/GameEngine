package utils.tileboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * A BufferedImage-Based Tileboard
 *
 * This class contains all the tools needed to easily create a tiled base image
 * for use in graphics, or interactive games. The constructor allows the user
 * to either just set the image dimensions. Or optionally specify the tile dimensions
 * and/or amount of tiles to place. Note: tiles can be non-square.
 * 
 * In order to draw tiles, the user should first call loadTile, which allows the user
 * to specify an ID for the tile as well as the file path. The ID is a string that
 * is paired with the image, so that the user can easily recall images to draw.
 * @author Nick Cheng
 * TODO: interface the tileboard for use with the movie clip/displaylist model
 *       I think the best way to do this would be to extend MovieClip
 */
public class ImageTileboard extends BufferedImage implements Tileboard {
    private int WIDTH;
    private int HEIGHT;
    private int numRows = -1;
    private int numCols = -1;
    private int tileWidth = -1;
    private int tileHeight = -1;
    
    private Hashtable<String,BufferedImage> tileset;
    private Graphics2D graphics;
    private Boolean active;

    /**
     * Creates a tileboard. You must call setTileSize before you
     * can draw tiles.
     * @param width How wide in pixels the image should be
     * @param height How tall in pixels the image should be
     */
    public ImageTileboard(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        WIDTH = width;
        HEIGHT = height;
        tileset = new Hashtable<String,BufferedImage>(10);
        graphics = this.createGraphics();
        active = true;
    }
    
    /**
     * Creates a tileboard.
     * @param i_width How wide in pixels the image should be
     * @param i_height How tall in pixels the image should be
     * @param t_width How wide in pixels each tile is
     * @param t_height How tall in pixels each tile is
     */
    public ImageTileboard(int i_width, int i_height, int t_width, int t_height) {
        super(i_width, i_height, TYPE_INT_ARGB);
        WIDTH = i_width;
        HEIGHT = i_height;
        tileset = new Hashtable<String,BufferedImage>(10);
        graphics = this.createGraphics();
        active = true;
        setTileSize(t_width,t_height);
    }
    
    /**
     * Creates a tileboard.
     * Note: the constructor will fail if the user specifies more tiles than the
     * image dimensions can fit.
     * @param i_width How wide in pixels the image should be
     * @param i_height How tall in pixels the image should be
     * @param t_width How wide in pixels each tile is
     * @param t_height How tall in pixels each tile is
     * @param tilesX How many tiles there should be horizontally
     * @param tilesY How many tiles there should be vertically
     */
    public ImageTileboard(int i_width, int i_height, int t_width, int t_height, int tilesX, int tilesY) {
        super(i_width, i_height, TYPE_INT_ARGB);
        
        if (t_width*tilesX > i_width || t_height*tilesY > i_height){
            System.out.println("You cannot fit that many tiles.");
            return;
        }
        
        WIDTH = i_width;
        HEIGHT = i_height;
        tileset = new Hashtable<String,BufferedImage>(10);
        graphics = this.createGraphics();
        active = true;
        tileWidth = t_width;
        tileHeight = t_height;
        numCols = tilesX;
        numRows = tilesY;
    }

    /**
     * This sets the dimensions of a tile in pixels
     * @param width The width of the tile in pixels
     * @param height The height of a tile in pixels
     */
    @Override
    public void setTileSize(int width, int height) {
        tileWidth = width;
        tileHeight = height;
        numCols = WIDTH/width;
        numRows = HEIGHT/height;
    }

    /**
     * This will pair an ID with an image and load that image
     * into memory. This must be done before calling drawTile.
     * @param name The ID the user desires to use for easy recall
     * of the tile during drawTile
     * @param filepath A string representation of the path of an image the user
     * desires to load
     */
    @Override
    public void loadTile(String name, String filepath) {
        if (active){
            try {
                BufferedImage newBufferedImage = ImageIO.read(new File(filepath));
                tileset.put(name, newBufferedImage);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not load "+filepath);
            }
        }else{
            System.out.println("Tileboard has been finalized.");
        }
    }

    /**
     * This will draw a tile onto the image at a given position
     * @param name The ID for the tile (specified in loadTile)
     * @param x The horizontal position to place the tile (in columns, not pixels)
     * @param y The vertical position to place the tile (in rows, not pixels)
     */
    @Override
    public void drawTile(String name, int x, int y) {
        if (active){
            if (x<0 || x>= numCols || y<0 || y>= numRows){
                System.out.println("("+Integer.toString(x)+","+Integer.toString(y)+") is out of bounds");
                return;
            }
            if (numRows == -1 || numCols == -1 || tileWidth == -1 || tileHeight == -1){
                System.out.println("Board not initiliazed correctly. Check for setting of tilesize.");
                return;
            }
            if(tileset.containsKey(name)){
                BufferedImage img = tileset.get(name);
                graphics.drawImage(img, x*tileWidth, y*tileHeight, null);
            }else{
                System.out.println(name+" is not a loaded tile.");
            }
        }else{
            System.out.println("Tileboard has been finalized.");
        }

    }

    /**
     * This will draw a tile onto the image at a given position
     * @param name The ID for the tile (specified in loadTile)
     * @param p The position to place the tile (column,row)
     */
    @Override
    public void drawTile(String name, Point p) {
        drawTile(name,p.x,p.y);
    }

    /**
     * This will fill the entire board with a given tile
     * @param name The ID for the tile (specified in loadTile)
     */
    @Override
    public void fillBoard(String name) {
        for(int i=0;i<numRows;i++){
            for(int j=0;j<numCols;j++){
                drawTile(name,j,i);
            }
        }
    }
    
    /**
     * If the board has been finalized it is no longer active and can no longer
     * be changed. This function lets the user know if the board is active.
     * @return Whether the board is active
     */
    public boolean isAcive () {
        return active;
    }
    
    public void clearEverything() {
        graphics.setBackground(new Color(255,255,255,0));
        graphics.clearRect(0, 0, WIDTH, HEIGHT);
    }
    
    /**
     * Free memory associated with updating graphics, while retaining the image
     * for future use. The image will no longer be active and can no longer be changed.
     */
    public void finalizeImage(){
        active = false;
        graphics.dispose();
    }

    /**
     * Yields the width and height of the the board in columns and rows respectively
     * @return A point (number of columns, number of rows) 
     */
    @Override
    public Point getDimensions() {
        return new Point(numCols,numRows);
    }

}
