package org.ozsoft.java3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class SpinningCube extends Applet {

    private static final long serialVersionUID = 1L;

    public SpinningCube() {
        setLayout(new BorderLayout());
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add(BorderLayout.CENTER, canvas3D);

        BranchGroup scene = new BranchGroup();
        
        Transform3D rotation = new Transform3D();
        rotation.rotX(0.25 * Math.PI);
        Transform3D rotation2 = new Transform3D();
        rotation2.rotY(0.2 * Math.PI);
        rotation.mul(rotation2);
        
        TransformGroup transformGroup = new TransformGroup(rotation);
        
        TransformGroup spinningGroup = new TransformGroup();
        spinningGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        spinningGroup.addChild(new ColorCube(0.4));
        transformGroup.addChild(spinningGroup);
        scene.addChild(transformGroup);
        
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, 5000);
        RotationInterpolator rotator =
                new RotationInterpolator(rotationAlpha, spinningGroup, yAxis, 0.0f, (float) (Math.PI * 2.0));
        rotator.setSchedulingBounds(new BoundingSphere());
        spinningGroup.addChild(rotator);

        scene.compile();
        
        SimpleUniverse universe = new SimpleUniverse(canvas3D);
        universe.addBranchGraph(scene);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    public static void main(String[] args) {
        new MainFrame(new SpinningCube(), 400, 400);
    }

}
