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
package rs.alexanderstojanovich.fo2h.highlighter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import rs.alexanderstojanovich.fo2h.frm.FRM;
import rs.alexanderstojanovich.fo2h.frm.ImageData;
import rs.alexanderstojanovich.fo2h.frm.Palette;
import rs.alexanderstojanovich.fo2h.util.ColorSample;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class Highligther {

    private static final String TEXTFILE = "Dictionary.txt";

    private float progress = 0.0f;
    private final Configuration config;

    public static enum Obj {
        UNUSED,
        IMPLANTS, T4_WEAPONS, T4_ARMORS, T4_AMMO, T4_ITEMS,
        T3_WEAPONS, T3_ARMORS, T3_AMMO, T3_ITEMS,
        T2_WEAPONS, T2_ARMORS, T2_AMMO, T2_ITEMS,
        T1_WEAPONS, T1_ARMORS, T1_AMMO, T1_ITEMS,
        T0_WEAPONS, T0_ARMORS, T0_AMMO, T0_ITEMS,
        RESOURCES, BOOKS, ORES, CONTAINERS
    };

    public static final Map<String, Obj> DICTIONARY = new HashMap<>();

    private final JPanel colorPanel;
    // via several labels coloured differently
    private final JLabel[] colorVector = new JLabel[256];

    public Highligther(Configuration config, JPanel colorPanel) {
        this.config = config;
        this.colorPanel = colorPanel;
        initDictionary();
        initColorPanel();
    }

    private void initDictionary() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(TEXTFILE));
            String line;
            Obj obj = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@")) {
                    obj = Obj.valueOf(line.trim().substring(1));
                } else {
                    DICTIONARY.put(line.trim().toLowerCase(), obj);
                }
            }
        } catch (FileNotFoundException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } catch (IOException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    FO2HLogger.reportError(ex.getMessage(), ex);
                }
            }
        }
    }

    // init Palette display (it's called Color Vector)
    private void initColorPanel() {
        int[] colors = Palette.getColors();
        if (colors != null) {
            for (int i = 0; i < colorVector.length; i++) {
                colorVector[i] = new JLabel();
                colorVector[i].setBackground(Color.BLACK);
                colorVector[i].setOpaque(true);
                colorVector[i].setSize(9, 9);
                colorVector[i].setBorder(new BevelBorder(BevelBorder.RAISED));

                Color col = new Color(Palette.getColors()[i]);

                colorVector[i].setBackground(col);
                colorVector[i].setToolTipText("Red = " + col.getRed()
                        + ", Green = " + col.getGreen() + ", Blue = " + col.getBlue());

                colorPanel.add(colorVector[i], new Integer(i));
            }
        }
    }

    private Color getOutlineColor(String extLessFilename) {
        Color result;
        Obj obj = DICTIONARY.getOrDefault(extLessFilename.toLowerCase(), Obj.UNUSED);
        switch (obj) {
            case IMPLANTS:
                result = config.getImplantColor();
                break;
            case T4_WEAPONS:
            case T4_ARMORS:
            case T4_AMMO:
            case T4_ITEMS:
                result = config.getT4Color();
                break;
            case T3_WEAPONS:
            case T3_ARMORS:
            case T3_AMMO:
            case T3_ITEMS:
                result = config.getT3Color();
                break;
            case T2_WEAPONS:
            case T2_ARMORS:
            case T2_AMMO:
            case T2_ITEMS:
                result = config.getT2Color();
                break;
            case T1_WEAPONS:
            case T1_ARMORS:
            case T1_AMMO:
            case T1_ITEMS:
                result = config.getT1Color();
                break;
            case T0_WEAPONS:
            case T0_ARMORS:
            case T0_AMMO:
            case T0_ITEMS:
                result = config.getT0Color();
                break;
            case BOOKS:
                result = config.getBookColor();
                break;
            case ORES:
                result = config.getOreColor();
                break;
            case RESOURCES:
                result = config.getResourcesColor();
                break;
            case CONTAINERS:
                result = config.getContainerColor();
                break;
            case UNUSED:
            default:
                result = config.getUnusedColor();
                break;
        }
        return result;
    }

    /**
     * Start highlighter working.
     *
     */
    public void work() {
        progress = 0.0f;
        if (!config.getInDir().exists()) {
            progress = 100.0f;
            return;
        }

        if (!config.getOutDir().exists()) {
            config.getOutDir().mkdirs();
        }

        if (config.getInDir().isDirectory()) {
            File[] fileArray = config.getInDir().listFiles();
            for (File srcFile : fileArray) {
                // if file is fofrm copy it to the output                                      
                if (srcFile.getName().toLowerCase().endsWith(".fofrm")) {
                    File dstFile = new File(config.getOutDir() + File.separator + srcFile.getName());
                    try {
                        Files.copy(srcFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        FO2HLogger.reportError(ex.getMessage(), ex);
                    }
                } else { // if it's png or FRM file
                    // get extensionless filename
                    String extLessFilename = srcFile.getName().replaceFirst("[.][^.]+$", "");
                    //----------------------------------------------------------
                    if (srcFile.getName().toLowerCase().endsWith(".frm")) {
                        FRM srcFRM = new FRM(srcFile);
                        //-----------------------------------------------------
                        List<ImageData> frames = srcFRM.getFrames();
                        final BufferedImage[] imgSrc = new BufferedImage[frames.size()];
                        final BufferedImage[] imgDst = new BufferedImage[frames.size()];
                        final int[] frameOffsetsX = new int[frames.size()];
                        final int[] frameOffsetsY = new int[frames.size()];
                        for (int i = 0; i < frames.size(); i++) {
                            imgSrc[i] = frames.get(i).toBufferedImage();
                            imgDst[i] = new BufferedImage(imgSrc[i].getWidth() + 2, imgSrc[i].getHeight() + 2, BufferedImage.TYPE_INT_ARGB);

                            Graphics2D graphics2D = imgDst[i].createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc[i], 1, 1, null);
                            // blue color removal
                            for (int px = 0; px < imgDst[i].getWidth(); px++) {
                                for (int py = 0; py < imgDst[i].getHeight(); py++) {
                                    Color pixCol = new Color(imgDst[i].getRGB(px, py), true);
                                    if (pixCol.equals(Color.BLUE)) {
                                        imgDst[i].setRGB(px, py, 0);
                                    }
                                }
                            }
                            // outline effect
                            WritableRaster wr = imgDst[i].copyData(null);
                            for (int px = 0; px < imgDst[i].getWidth(); px++) {
                                for (int py = 0; py < imgDst[i].getHeight(); py++) {
                                    Color pixCol = new Color(imgDst[i].getRGB(px, py), true);
                                    // writtable raster must be associated with ARGB image!!
                                    ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                                    if (pixCol.getAlpha() == 0 && cs.getAlpha() > 0) {
                                        imgDst[i].setRGB(px, py, getOutlineColor(extLessFilename).getRGB());
                                    }
                                }
                            }

                            frameOffsetsX[i] = frames.get(i).getOffsetX();
                            frameOffsetsY[i] = frames.get(i).getOffsetY();
                        }
                        //-------------------------------------------------------
                        FRM dstFRM = new FRM(
                                srcFRM.getVersion(),
                                srcFRM.getFps(),
                                srcFRM.getActionFrame(),
                                srcFRM.getFramesPerDirection(),
                                srcFRM.getShiftX(),
                                srcFRM.getShiftY(),
                                srcFRM.getOffset(),
                                imgDst,
                                frameOffsetsX,
                                frameOffsetsY
                        );
                        File outFile = new File(config.getOutDir() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                        dstFRM.write(outFile);
                    } else if (srcFile.getName().toLowerCase().endsWith(".png")) {
                        BufferedImage imgSrc = null;
                        //------------------------------------------------------
                        try {
                            imgSrc = ImageIO.read(srcFile);
                        } catch (IOException ex) {
                            FO2HLogger.reportError(ex.getMessage(), ex);
                        }
                        //------------------------------------------------------
                        if (imgSrc != null) {
                            final BufferedImage imgDst = new BufferedImage(imgSrc.getWidth() + 2, imgSrc.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D graphics2D = imgDst.createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc, 1, 1, null);
                            // blue color removal
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    if (pixCol.equals(Color.BLUE)) {
                                        imgDst.setRGB(px, py, 0);
                                    }
                                }
                            }
                            // outline effect
                            WritableRaster wr = imgDst.copyData(null);
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    // writtable raster must be associated with ARGB image!!
                                    ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                                    if (pixCol.getAlpha() == 0 && cs.getAlpha() > 0) {
                                        imgDst.setRGB(px, py, getOutlineColor(extLessFilename).getRGB());
                                    }
                                }
                            }
                            //--------------------------------------------------
                            File outFile = new File(config.getOutDir() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".png"));

                            try {
                                ImageIO.write(imgDst, "png", outFile);
                            } catch (IOException ex) {
                                FO2HLogger.reportError(ex.getMessage(), ex);
                            }
                        }
                    }
                }
                progress += 100.0f / fileArray.length;
            }
        }
        progress = 100.0f;
    }

    public void reset() {
        config.reset();
        DICTIONARY.clear();
        initDictionary();
        initColorPanel();
        progress = 0.0f;
    }

    public float getProgress() {
        return progress;
    }

}
