package controller;

import java.awt.event.MouseEvent;

import model.Model;
import model.MovieClip;

/**
 * This is the mouse handler to be edited by the user.
 * Make sure to call the super methods.
 * 
 * This also inherits controller and view from AbstractMouseHandler
 * @author Nick Cheng
 *
 */
public class MouseHandler extends AbstractMouseHandler {

    //model variable inherited
    
    public MouseHandler(Controller controller, Model model) {
        super(controller, model);
    }
    
    ///////////////////////////////////
    // XXX: BEGIN USER EDITED SECTION
    ///////////////////////////////////
    
    //override any other mouse functions you want
    //just be sure to call the super mouse function
    
    @Override
    public void mousePressed(MouseEvent m) {
        synchronized(model){
            super.mousePressed(m);
            
            MovieClip dot = new MovieClip("images/bullethole");
            dot.stopAtEnd();
            dot.x = m.getX();
            dot.y = m.getY();
            dot.setOrigin(12, 12);
            
            if(dot.hitTest(controller.model.player,"circle")){
                model.hits.add(dot);
                model.addChild(dot,1);
            }else{
                model.addChild(dot,0);
            }
            
            model.turret.swapAndRestart("shooting");
        }
    }
}
