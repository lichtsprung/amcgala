package org.amcgala.framework.event;

import com.google.common.eventbus.Subscribe;
import org.amcgala.framework.camera.Camera;
import org.amcgala.framework.math.Quaternion;
import org.amcgala.framework.math.Vector3d;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Ein WASD-Controller, der es dem Nutzer ermöglicht, die Kamera mit der Maus und des Tasten WASD zu steuern.
 *
 * @author Robert Giacinto
 */
public class WASDController implements InputHandler {
    private Camera camera;
    private Vector3d axis;
    private Quaternion quaternion;

    public WASDController(Camera camera) {
        this.camera = camera;
    }

    @Subscribe
    public void handleKeyboardEvents(KeyEvent e) {
        Vector3d cameraPosition = camera.getPosition();
        Vector3d cameraDirection = camera.getDirection();
        if (e.getKeyCode() == KeyEvent.VK_W) {
            cameraPosition.z += 0.5;
            cameraDirection.z += 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            cameraPosition.x -= 0.5;
            cameraDirection.x -= 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            cameraPosition.z -= 0.5;
            cameraDirection.z -= 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            cameraPosition.x += 0.5;
            cameraDirection.x += 0.5;
        }
        camera.setPosition(cameraPosition);
        camera.setDirection(cameraDirection);
    }

    @Subscribe
    public void handleMouseEvents(MouseEvent e) {

    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
