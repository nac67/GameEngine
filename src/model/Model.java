package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.animation.Animation;
import model.animation.IteratingAnimation;
import model.animation.IteratingAnimation.Direction;
import controller.Controller;
import controller.advancedtools.MiniTimer;

/**
 * This is the model in the model-view-controller pattern.
 * 
 * It handles the internal representation of the data.
 * For the user: declare any movie clips or other data structures
 * you want to here, then you can write startGame which can 
 * actually instantiate the movie clips and add them to the
 * display list.
 * @author Nick Cheng
 */
public class Model {
    
    ///////////////////////////////////
    // XXX: BEGIN USER EDITED SECTION
    ///////////////////////////////////
    
    public Color bgColor = new Color (240,240,240);
    
    public MovieClip player;
    public MovieClip spinner;
    public Point spinV;
    public MovieClip crossHair;
    public MovieClip marker;
    public LinkedList<MovieClip> hits;
    
    public MiniTimer switcher;
    public IteratingAnimation.Direction dir;
    
    public MovieClip dot;
    
    public MovieClip turret;
    
    public void startGame(){
        
        
        player = new MovieClip("images/guy",true);
        player.x = 200;
        player.y = 200;
        //player.scaleX = 3;
        //player.scaleY = 3;
        player.setOrigin(24,24);
        addChild(player,1);
        
        spinner = new MovieClip("images/spinner");
        dir = Direction.FORWARD;
        spinner.x = 300;
        spinner.y = 300;
        addChild(spinner,1);
        spinV = new Point(10,5);
        switcher = new MiniTimer(30);
        
        crossHair = new MovieClip("images/red/");
        crossHair.scaleX = .9;
        crossHair.scaleY = 1.2;
        crossHair.setOrigin(24, 24);
        addChild(crossHair,2);
        
        hits = new LinkedList<MovieClip>();
        
        dot = new MovieClip("images/dot.png");
        addChild(dot,2);
        
        turret = new MovieClip("images/turret",true);
        turret.x = 320;
        turret.y = 480;
        turret.setOrigin(32,32);
        addChild(turret);
        turret.stopAtEnd();
        
        
        
        
        controller.gameMouse.hideMouse();
    }
    
    ///////////////////////////////////
    // END USER EDITED SECTION
    ///////////////////////////////////
    
    /** 
     * Each display layer contains movie clips that are drawn 
     * to the frame sequentially during every time step.
     * If a movie clip is earlier in the list, it will be 
     * drawn earlier and therefore be "behind" other movie clips.
     * 
     * This list contains DISPLAY_LAYERS amount of display lists
     * numbered: 0 to DISPLAY_LAYERS (inclusive)
     * Each layer will be drawn sequentially so that lower
     * number layers will be "behind" other layers.
     * 
     * In summary, each layer has its own ordering, but,
     * for example, everything on layer 1 will be above 
     * everything on layer 0.
     * 
     * If the user does not care about layers, they could simply
     * call addChild without a layer number and everything will
     * be defaulted to layer 0.
     */
    public List<List<MovieClip>> displayList;
    
    /**
     * These movie clips will be removed on the next time step instead
     * of immediately. Currently the only use for this is for movie clips
     * that delete themselves at the end of playback, since otherwise
     * they would be trying to delete themselves while the iterator
     * is going through the list
     */
    public List<MovieClip>removeQueue;
    
    public Controller controller;
    
    public int levelShiftX = 0;
    public int levelShiftY = 0;
    
    /** Constructor */
    public Model (Controller controller){
        if(Constants.DEBUG) System.out.println("model init, ready.");
        Animation.loadedAnimations = new HashMap<String, List<BufferedImage>>();
        displayList = new ArrayList<List<MovieClip>>();
        for (int i=0;i<Constants.NUM_DISPLAY_LAYERS;i++){
            displayList.add(new ArrayList<MovieClip>());
        }
        removeQueue = new ArrayList<MovieClip>();
        this.controller = controller;
    }
    
   
    
    /**
     * This will add a movie clip to layer 0 of the
     * display list
     * @param mc The movie clip to add
     */
    public void addChild(MovieClip mc){
        addChild(mc,0);
    }
    
    /**
     * This will add a movie clip to a given layer of
     * the display list. Layers are numbered  0 to DISPLAY_LAYERS (inclusive)
     * @param mc The movie clip to add
     * @param layer The layer to add it to
     */
    public void addChild(MovieClip mc, int layer){
        if(layer>=Constants.NUM_DISPLAY_LAYERS){
            System.err.println("Layer is out of range");
        }
        displayList.get(layer).add(mc);
        mc.parent = this;
    }
    
    /**
     * This returns the number of the layer containing
     * a given movie clip
     * @param mc The movie clip to search for
     * @return The layer that contains the movie clip
     */
    public int layerContaining(MovieClip mc){
        int layer = -1;
        for (int i=0;i<Constants.NUM_DISPLAY_LAYERS;i++){
            if(displayList.get(i).contains(mc)){
                layer = i;
            }
        }
        if (layer == -1){
            System.err.println("could not find movie clip");
        }
        return layer;
    }
    
    /**
     * This will move a movie clip to the top of a different layer
     * @param mc The movie clip to move
     * @param layer The layer to move it to
     */
    public void moveLayer(MovieClip mc, int layer){
        int oldLayer = layerContaining(mc);
        displayList.get(oldLayer).remove(mc);
        displayList.get(layer).add(mc);
    }
    
    /**
     * This will remove a movie clip from the display list
     * so that it no longer appears visible. It can be re-added later
     * @param mc The movie clip to remove
     */
    public void removeChild(MovieClip mc){
        int layer = layerContaining(mc);
        displayList.get(layer).remove(mc);
    }
    
    /**
     * This will remove a movie clip on the next time step
     * @param mc The movie clip to remove
     */
    public void removeNextTime(MovieClip mc){
        removeQueue.add(mc);
    }
    
    /**
     * This will remove all movie clips from all layers of
     * the display list
     */
    public void removeAllChildren(){
        for (int i=0;i<Constants.NUM_DISPLAY_LAYERS;i++){
            displayList.get(i).clear();
        }
    }
    
    /**
     * This will remove all movie clips from all layers of
     * the display list on a given layer
     * @param layer The layer to remove all children from
     */
    public void removeAllChildrenOnLayer(int layer){
        displayList.get(layer).clear();
    }
    
    /**
     * This will swap the depths of two movie clips. These
     * movie clips can be on the same layer, and they will
     * switch depths, or they can be on different layers in
     * which case the depth within each layer will be preserved.
     * CAUTION: Linear time
     * @param mc1 The first movie clip
     * @param mc2 The movie clip to switch it with
     */
    public void swapChildren(MovieClip mc1, MovieClip mc2){
        int l1 = layerContaining(mc1);
        int l2 = layerContaining(mc2);
        int p1 = displayList.get(l1).indexOf(mc1);
        int p2 = displayList.get(l2).indexOf(mc2);
        
        if(l1 == l2){
            //If on same layer
            Collections.swap(displayList.get(l1), p1, p2);
        }else{
            //If on different layers
            displayList.get(l1).remove(mc1);
            displayList.get(l2).remove(mc2);
            displayList.get(l2).add(p2, mc1);
            displayList.get(l1).add(p1, mc2);
        }
        
    }
    
    /**
     * This will bring a movie clip to the front of its layer
     * @param mc1 The movie clip to move
     */
    public void bringToFront(MovieClip mc1){
        int layer = layerContaining(mc1);
        int p1 = displayList.get(layer).indexOf(mc1);
        Collections.swap(displayList.get(layer), p1, displayList.get(layer).size()-1);
    }
    
    /**
     * This will send a movie clip to the back of its layer
     * @param mc1
     */
    public void sendToBack(MovieClip mc1){
        int layer = layerContaining(mc1);
        int p1 = displayList.get(layer).indexOf(mc1);
        Collections.swap(displayList.get(layer), p1, 0);
    }
}
