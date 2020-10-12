/*
 * Copyright (C) 2020 Alexander Stojanovich <coas91@rocketmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rs.alexanderstojanovich.fo2h.frm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class FRM {

    private int version; //  2-byte unsigned (0x0000)
    private int fps; // 2-byte unsigned (0x0004)
    private int actionFrame; // 2-byte unsigned (0x0006)
    private int framesPerDirection; // 2-byte unsigned (0x0008)

    private final int[] shiftX = new int[6]; // signed 
    private final int[] shiftY = new int[6]; // signed   

    private final int[] offset = new int[6]; // unsigned

    // image composed of frames (but frame 0 is primarily used)
    private final List<ImageData> frames = new ArrayList<>();

    private int frameSize;

    // 16 MB Buffer
    private final byte buffer[] = new byte[0x1000000];
    private int pos = 0x0000;

    /**
     * Create blank FRM
     */
    public FRM() {

    }

    /**
     * Create FRM with given attributes and series of images
     *
     * @param version version number of the FRM file format
     * @param fps frames per second rate of the animation
     * @param actionFrame frame of the animation on which actions occur (shot,
     * open doors, etc.)
     * @param framesPerDirection number of frames for a particular orientation
     * @param frameSize used to allocating memory for frames
     * @param images array of images.
     */
    public FRM(int version, int fps, int actionFrame, int framesPerDirection, int frameSize, BufferedImage[] images) {
        this.version = version;
        this.fps = fps;
        this.actionFrame = actionFrame;
        this.framesPerDirection = framesPerDirection;
        this.frameSize = frameSize;
        for (BufferedImage image : images) {
            ImageData imgData = new ImageData(image);
            frames.add(imgData);
        }
    }

    private void loadFromFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(buffer);
        } catch (FileNotFoundException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } catch (IOException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    FO2HLogger.reportError(ex.getMessage(), ex);
                }
            }
        }
    }

    private void storeToFile(File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(buffer, 0, pos);
        } catch (FileNotFoundException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } catch (IOException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    FO2HLogger.reportError(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Read binary data from the specified FRM file
     *
     * @param file is specified FRM file to read from
     */
    public void read(File file) {
        Arrays.fill(buffer, (byte) 0x00);
        frames.clear();
        if (file.exists()) {
            loadFromFile(file);
        } else {
            return;
        }
        //----------------------------------------------------------------------
        pos = 0x0000;
        // big endian motorola
        version = ((buffer[pos] & 0xFF) << 24) | ((buffer[pos + 1] << 16) & 0xFF) | ((buffer[pos + 2] << 8) & 0xFF) | ((buffer[pos + 3]) & 0xFF);
        pos += 4;
        fps = ((buffer[pos] & 0xFF) << 8) | (buffer[pos + 1] & 0xFF);
        pos += 2;
        actionFrame = ((buffer[pos] & 0xFF) << 8) | (buffer[pos + 1] & 0xFF);
        pos += 2;
        framesPerDirection = ((buffer[pos] & 0xFF) << 8) | (buffer[pos + 1] & 0xFF);
        pos += 2;
        //----------------------------------------------------------------------                
        for (int i = 0; i < 6; i++) {
            shiftX[i] = (buffer[pos] << 8) | buffer[pos + 1];
            pos += 2;
        }

        for (int i = 0; i < 6; i++) {
            shiftY[i] = (buffer[pos] << 8) | buffer[pos + 1];
            pos += 2;
        }
        //----------------------------------------------------------------------        
        for (int i = 0; i < 6; i++) {
            offset[i] = ((buffer[pos] & 0xFF) << 24) | ((buffer[pos + 1] & 0xFF) << 16) | ((buffer[pos + 2] & 0xFF) << 8) | (buffer[pos + 3] & 0xFF);
            pos += 4;
        }
        //----------------------------------------------------------------------
        frameSize = ((buffer[pos] & 0xFF) << 24) | ((buffer[pos + 1] & 0xFF) << 16) | ((buffer[pos + 2] & 0xFF) << 8) | (buffer[pos + 3] & 0xFF);
        pos += 4;
        //----------------------------------------------------------------------        
        for (int i = 0; i < frameSize; i++) {
            for (int j = 0; j < framesPerDirection; j++) {
                int width = ((buffer[pos] & 0xFF) << 8) | (buffer[pos + 1] & 0xFF);
                pos += 2;
                int height = ((buffer[pos] & 0xFF) << 8) | (buffer[pos + 1] & 0xFF);
                pos += 2;
                //--------------------------------------------------------------
                pos += 4;
                pos += 2;
                pos += 2;
                //--------------------------------------------------------------
                ImageData imgData = new ImageData(width, height);
                for (int py = 0; py < imgData.getHeight(); py++) {
                    for (int px = 0; px < imgData.getWidth(); px++) {
                        byte index = buffer[pos++];
                        imgData.setPixel(px, py, index);
                    }
                }
                frames.add(imgData);
            }
        }
    }

    /**
     * Write to binary file
     *
     * @param file to write binary FRM content to
     */
    public void write(File file) {
        pos = 0x0000;
        // big endian motorola
        buffer[pos] = (byte) (version >> 24);
        buffer[pos + 2] = (byte) (version >> 16);
        buffer[pos + 3] = (byte) (version >> 8);
        buffer[pos + 4] = (byte) (version);
        pos += 4;
        buffer[pos] = (byte) (fps >> 8);
        buffer[pos + 1] = (byte) (fps);
        pos += 2;
        buffer[pos] = (byte) (actionFrame >> 8);
        buffer[pos + 1] = (byte) (actionFrame);
        pos += 2;
        buffer[pos] = (byte) (framesPerDirection >> 8);
        buffer[pos + 1] = (byte) (framesPerDirection);
        pos += 2;
        //----------------------------------------------------------------------                
        for (int i = 0; i < 6; i++) {
            buffer[pos] = (byte) (shiftX[i] >> 8);
            buffer[pos + 1] = (byte) (shiftX[i]);
            pos += 2;
        }

        for (int i = 0; i < 6; i++) {
            buffer[pos] = (byte) (shiftY[i] >> 8);
            buffer[pos + 1] = (byte) (shiftY[i]);
            pos += 2;
        }
        //----------------------------------------------------------------------        
        for (int i = 0; i < 6; i++) {
            buffer[pos] = (byte) (offset[i] >> 24);
            buffer[pos + 2] = (byte) (offset[i] >> 16);
            buffer[pos + 3] = (byte) (offset[i] >> 8);
            buffer[pos + 4] = (byte) (offset[i]);
            pos += 4;
        }
        //----------------------------------------------------------------------
        buffer[pos] = (byte) (frameSize >> 24);
        buffer[pos + 2] = (byte) (frameSize >> 16);
        buffer[pos + 3] = (byte) (frameSize >> 8);
        buffer[pos + 4] = (byte) (frameSize);
        pos += 4;
        //----------------------------------------------------------------------
        for (ImageData frame : frames) {
            int width = frame.getWidth();
            buffer[pos] = (byte) (width >> 8);
            buffer[pos + 1] = (byte) (width);
            pos += 2;
            int height = frame.getHeight();
            buffer[pos] = (byte) (height >> 8);
            buffer[pos + 1] = (byte) (height);
            pos += 2;
            //--------------------------------------------------------------
            buffer[pos] = 0x0000;
            buffer[pos + 1] = 0x0000;
            buffer[pos + 2] = 0x0000;
            buffer[pos + 3] = 0x0000;
            pos += 4;
            buffer[pos] = 0x0000;
            buffer[pos + 1] = 0x0000;
            pos += 2;
            buffer[pos] = 0x0000;
            buffer[pos + 1] = 0x0000;
            pos += 2;
            //--------------------------------------------------------------                
            for (int py = 0; py < frame.getHeight(); py++) {
                for (int px = 0; px < frame.getWidth(); px++) {
                    buffer[pos++] = frame.getPixel(px, py);
                }
            }
        }

        storeToFile(file);
    }

    public int getVersion() {
        return version;
    }

    public int getFps() {
        return fps;
    }

    public int getActionFrame() {
        return actionFrame;
    }

    public int getFramesPerDirection() {
        return framesPerDirection;
    }

    public int[] getShiftX() {
        return shiftX;
    }

    public int[] getShiftY() {
        return shiftY;
    }

    public int[] getOffset() {
        return offset;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public List<ImageData> getFrames() {
        return frames;
    }

}
