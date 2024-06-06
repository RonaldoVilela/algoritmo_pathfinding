package application;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MouseInput implements MouseMotionListener, MouseListener{
	public boolean clicked = false;
	public boolean rClicked = false;
	@Override
	public void mouseDragged(MouseEvent e) {
		Program.mouseX = e.getX();
		Program.mouseY = e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Program.mouseX = e.getX();
		Program.mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			clicked = true;
		}
		if(e.getButton() == MouseEvent.BUTTON3) {
			rClicked = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			clicked = false;
		}
		if(e.getButton() == MouseEvent.BUTTON3) {
			rClicked = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
