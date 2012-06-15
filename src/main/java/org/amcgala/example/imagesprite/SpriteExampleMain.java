package org.amcgala.example.imagesprite;

import org.amcgala.Framework;
import org.amcgala.framework.animation.Animation;
import org.amcgala.framework.shape.shape2d.Sprite;

import java.io.IOException;
import java.io.InputStream;

public class SpriteExampleMain extends Framework {

    public SpriteExampleMain(int width, int height) {
        super(width, height);

    }

    @Override
    public void initGraph() {
        try {
            InputStream inputStream = ClassLoader
                    .getSystemResourceAsStream("org/amcgala/example/spriteshape/image.jpg");
            final Sprite a = new Sprite(inputStream, -200, 50);
            a.setAnimation(new Animation<Sprite>() {
                @Override
                public void update() {
                    if ((a.getX() + 2) < -100) {
                        a.setX((a.getX() + 2));
                    } else {
                        a.setX(-200);
                    }
                }
            });
            add(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpriteExampleMain fp = new SpriteExampleMain(320, 200);
        fp.start();
    }
}