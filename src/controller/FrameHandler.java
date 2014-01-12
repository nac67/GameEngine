package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Constants;
import model.animation.IteratingAnimation.Direction;
import model.Model;
import model.MovieClip;

/**
 * This is the frame handler. It contains the main game loop which
 * runs at a fixed rate determined in Constants.
 * @author Nick Cheng
 */
public class FrameHandler implements ActionListener{

    private Controller controller;
    private Model model;
    
    private AbstractMouseHandler mouse;
    private KeyHandler keys;
    
    //private long lastTime;
    
    public FrameHandler(Controller controller, Model model) {
        if(Constants.DEBUG) System.out.println("frame handler init, ready.");
        this.controller = controller;
        this.model = model;
        mouse = controller.gameMouse;
        keys = controller.gameKeys;
    }
    
    public void actionPerformed(ActionEvent e) {
       synchronized(model){
           keys.updateKeyPresses();
           enterFrame();
           controller.notifyView();
           //if (lastTime != 0) System.out.println(System.currentTimeMillis()-lastTime);
           //lastTime = System.currentTimeMillis();
       }
    }
    
    ///////////////////////////////////
    // XXX: BEGIN USER EDITED SECTION
    ///////////////////////////////////
    
    /**
     * The main game loop which runs every TIME_STEP milliseconds
     */
    private void enterFrame(){
        
        if (model.player.hitTest(model.spinner,"circle")){
            model.player.swapAndResume("hurt");
        }else{
            model.player.swapAndResume("walk");
        }
        
        //move cross hair
        model.crossHair.x = mouse.mouseX();
        model.crossHair.y = mouse.mouseY();
        
        
        //move and switch direction of spinner
        model.spinner.x += model.spinV.x;
        model.spinner.y += model.spinV.y;
        if(model.switcher.isReady()){
            if(model.dir == Direction.FORWARD){
                model.dir = Direction.BACKWARD;
            }else{
                model.dir = Direction.FORWARD;
            }
            model.spinner.setDirection(model.dir);
        }
        
        //bounce spinner
        if (model.spinner.x <0 ||  model.spinner.x > Constants.GAME_WIDTH-40){
            model.spinV.x *=-1;
        }
        if (model.spinner.y <0 || model.spinner.y > Constants.GAME_HEIGHT-40){
            model.spinV.y *=-1;
        }
        
        KeyHandler k = keys;
        
        //move player with arrows or aswd
        int xmove = (int) (k.arrowVector().getX()*10.0) + (int) (k.aswdVector().getX()*10.0);
        int ymove = (int) (k.arrowVector().getY()*10.0) + (int) (k.aswdVector().getY()*10.0);
        
        model.player.x += xmove;
        model.player.y += ymove;

        //choose animation for player
        if (k.aswdVector().getX()!=0.0 || k.aswdVector().getY()!=0.0||
                k.arrowVector().getX()!=0.0 || k.arrowVector().getY()!=0.0){
            model.player.swapAndResume("walk");
        }else{
            model.player.swapAndResume("stand");
        }
        
        //collision detect
        if(model.player.hitTest(model.spinner,"circle")){
            model.player.swapAndResume("hurt");
        }
        
        //move bullets attached to player
        for(MovieClip mc : model.hits){
            mc.x += xmove;
            mc.y += ymove;
        }
        
        model.crossHair.rotation += .1;
        
        //aim turret
        if(mouse.isOnScreen()) model.turret.aimAt(new Point(mouse.mouseX(),mouse.mouseY()));
        if(model.turret.getCurrentAnim() == "shooting"){
            if (model.turret.isAtEnd()){
                model.turret.swapAndRestart("neutral.png");
            }
        }
        
    }

}
