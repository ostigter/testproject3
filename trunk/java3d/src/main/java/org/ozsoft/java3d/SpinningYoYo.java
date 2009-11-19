package org.ozsoft.java3d;

import java.applet.Applet;

public class SpinningYoYo extends Applet {

    private static final long serialVersionUID = 1L;
    
    private BranchGroup yoyoBG;

    public SpinningYoYo() {
        yoyoBG = new BranchGroup();

        Transform3D rotate = new Transform3D();
        rotate.rotZ(Math.PI / 2.0d);
        TransformGroup yoyoTGR1 = new TransformGroup(rotate);

        Transform3D translate = new Transform3D();
        translate.set(new Vector3f(0.1f, 0.0f, 0.0f));
        TransformGroup yoyoTGT1 = new TransformGroup(translate);

        Cone cone1 = new Cone(0.6f, 0.2f);
        Appearance yoyoAppear = new Appearance();
        cone1.setAppearance(yoyoAppear);
        yoyoBG.addChild(yoyoTGT1);
        yoyoTGT1.addChild(yoyoTGR1);
        yoyoTGR1.addChild(cone1);
        translate.set(new Vector3f(-0.1f, 0.0f, 0.0f));

        TransformGroup yoyoTGT2 = new TransformGroup(translate);
        rotate.rotZ(-Math.PI / 2.0d);

        TransformGroup yoyoTGR2 = new TransformGroup(rotate);
        Cone cone2 = new Cone(0.6f, 0.2f);
        cone2.setAppearance(yoyoAppear);
        yoyoBG.addChild(yoyoTGT2);
        yoyoTGT2.addChild(yoyoTGR2);
        yoyoTGR2.addChild(cone2);

        yoyoBG.compile();
    }

    public BranchGroup getBG() {
        return yoyoBG;
    }

    public static void main(String[] args) {
        new MainFrame(new SpinningYoYo(), 400, 400);
    }

}
