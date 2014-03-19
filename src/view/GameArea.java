package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import javax.swing.JPanel;

import utils.Utils;

import model.Constants;
import model.MovieClip;
import model.Model;

/** This is the main game area in the GUI.
 * It is a fixed sized region that contains all of the
 * important graphical elements of the game. This also contains
 * the paint method which actually updates the GUI.
 * @author Nick Cheng
 */
public class GameArea extends JPanel {
    private Model model;
    private int WIDTH;
    private int HEIGHT;
    
    public GameArea(Model model, int width, int height) {
        this.model = model;
        setOpaque(true);
        setPreferredSize(new Dimension(width, height));
        WIDTH = width;
        HEIGHT = height;
    }
    
    /**
     * The game paint method. This first draws the background
     * according to the background color specified in the model.
     * Then it iterates through the displaylist layers and adds all
     * of the movie clips in order. The ones toward the end of the
     * layer's list will appear in front of the the previous ones.
     */
    @Override
    public void paintComponent (Graphics g) { 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setColor(model.bgColor);
        g2d.fillRect(0,0,WIDTH,HEIGHT);

        for (int i=0;i<Constants.NUM_DISPLAY_LAYERS;i++){
            LinkedList<MovieClip> layer = model.displayList.get(i);
            for(MovieClip mc : layer){
                drawMC(g2d,mc);
            }
        }

        //currently only used for those that delete at end of animation
        //all others use removeChild immediately
        for (MovieClip deadMC : model.removeQueue){
            model.removeChild(deadMC);
        }
        model.removeQueue.clear();

        g2d.dispose();
    }

    /**
     * This will draw a movie clip to the g2d object specified. It
     * takes into account object position, origin setting, scale, and
     * rotation. It makes use of AffineTransform to do the non-exact
     * pixel calculations. It only creates an AffineTransform object
     * if the movie clip needs a transform (i.e. it is scaled or rotated)
     * @param g2d The graphic to draw onto
     * @param mc The movie clip to draw
     */
    private void drawMC (Graphics2D g2d, MovieClip mc){
        //only draw image if on screen
        if (Utils.rect_collision(new Point(((int)mc.x+mc.shiftX)+model.levelShiftX,((int)mc.y+mc.shiftY)+model.levelShiftY),
                mc.getDimensions().x, mc.getDimensions().y, new Point(0,0),
                Constants.GAME_WIDTH, Constants.GAME_HEIGHT)){

            if (mc.isTransformed()){
                double posX = mc.x+model.levelShiftX;
                double posY = mc.y+model.levelShiftY;
                double shiftX = mc.shiftX*mc.scaleX;
                double shiftY = mc.shiftY*mc.scaleY;

                //Use this code for scale then rotate
                AffineTransform at = new AffineTransform();
                at.translate(posX+shiftX,posY+shiftY);
                if (mc.rotation != 0){
                    at.rotate(mc.rotation,-shiftX,-shiftY);
                }
                at.scale(mc.scaleX, mc.scaleY);

                //Use this code for rotate then scale
                /*AffineTransform at = new AffineTransform();
                at.translate(posX+shiftX,posY+shiftY);
                at.scale(mc.scaleX, mc.scaleY);
                if (mc.rotation != 0){
                    at.rotate(mc.rotation,-shiftX,-shiftY);
                }*/

                g2d.drawImage(mc.getNextFrame(), at, null);
            }else{
                g2d.drawImage(mc.getNextFrame(),mc.x+mc.shiftX+model.levelShiftX,
                        mc.y+mc.shiftY+model.levelShiftY,null);
            }

        }
    }
}