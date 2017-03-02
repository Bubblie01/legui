package org.liquidengine.legui.image;

import java.nio.ByteBuffer;

/**
 * Created by Aliaksandr_Shcherbin on 3/2/2017.
 */
public class DummyImage extends Image {
    public DummyImage(String path) {
        super(path);
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public ImageChannels getChannels() {
        return null;
    }

    @Override
    public ByteBuffer getImageData() {
        return null;
    }
}