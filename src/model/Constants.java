package model;

public class Constants {

    public static final int GAME_WIDTH = 640;
    public static final int GAME_HEIGHT = 480;
    
    /**
     * Number of milliseconds between each time-step
     * Try to keep it a multiple of 1/60th of a second
     * to match screen refresh rate.
     * 
     * Use 33 for 30fps
     * Use 16 for 60fps
     */
    public static final int TIME_STEP = 16;
    
    /**
     * Number of layers to contain movie clips.
     * Layers will be numbered 0 to DISPLAY_LAYERS (inclusive)
     * Each layer can hold as many movie clips as it needs to.
     * see displayList in model for more details
     */
    public static final int NUM_DISPLAY_LAYERS = 3;
    
    /**
     * Display debugging terminal messages
     */
    public static final boolean DEBUG = true;

}
