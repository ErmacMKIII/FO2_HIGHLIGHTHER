/* 
 * Copyright (C) 2021 Alexander Stojanovich <coas91@rocketmail.com>
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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
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

    private static final float LUMA_RED_COEFF = 0.2126f;
    private static final float LUMA_GREEN_COEFF = 0.7152f;
    private static final float LUMA_BLUE_COEFF = 0.0722f;

    private static final String TEXTFILE = "Dictionary.txt";

    private float progress = 0.0f;
    private final Configuration config;

    private final Font myFont;

    public static final Map<String, Obj> DICTIONARY = new HashMap<>();
    public static final Map<String, String> MAPPED_BY = new HashMap<>();

    private final JPanel colorPanel;
    // via several labels coloured differently
    private final JLabel[] colorVector = new JLabel[256];

    private boolean stopped = false;

    public Highligther(Configuration config, JPanel colorPanel) {
        this.config = config;
        this.colorPanel = colorPanel;
        this.myFont = new Font(config.getFontName(), config.getFontStyle(), config.getFontSize());
        initColorPanel();
    }

    public static void initDictionary() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(TEXTFILE));
            String line;
            Obj obj = null;
            boolean labeled = false;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("@LABELED")) {
                    labeled = true;
                } else if (line.startsWith("@") && !line.startsWith("@LABELED")) {
                    obj = Obj.valueOf(line.trim().substring(1));
                    obj.setLabeled(labeled);
                    labeled = false;
                } else if (obj != null) {
                    String[] things = line.trim().replaceAll("\"", "").split("=>", -1);
                    DICTIONARY.put(things[0].trim(), obj);
                    if (obj.isLabeled() && things.length == 2) {
                        MAPPED_BY.put(things[0].trim(), things[1].trim());
                    }
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

        FO2HLogger.reportInfo("Dictionary initialized!", null);
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

    /**
     * Gets item color from the dictionary.
     *
     * @param extLessFilename filename without extension as it is written in the
     * dictionary
     * @return item color
     */
    private Color getItemColor(String extLessFilename) {
        Color result;
        Obj obj = DICTIONARY.getOrDefault(extLessFilename, Obj.UNUSED);
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
     * Remove blue color from the image. Reason - blue is transparent for
     * Fallout.
     *
     * @param img parsed image
     */
    public static final void removeBlue(BufferedImage img) {
        for (int px = 0; px < img.getWidth(); px++) {
            for (int py = 0; py < img.getHeight(); py++) {
                Color pixCol = new Color(img.getRGB(px, py), true);
                if (pixCol.equals(Color.BLUE)) {
                    img.setRGB(px, py, 0);
                }
            }
        }
    }

    /**
     * Create outline around the image.
     *
     * @param img parsed image
     * @param outlineColor color of the outline (around)
     * @param fillInterior fill interior of the outline
     * @param drawOutline draws outline around the image
     */
    public static final void createOutline(BufferedImage img, Color outlineColor, boolean fillInterior, boolean drawOutline) {
        // outline & fill interior effect
        WritableRaster wr = img.copyData(null);
        for (int px = 0; px < img.getWidth(); px++) {
            for (int py = 0; py < img.getHeight(); py++) {
                Color pixCol = new Color(img.getRGB(px, py), true);
                // writtable raster must be associated with ARGB image!!
                ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                if (pixCol.getAlpha() > 0 && fillInterior) {
                    final Color itemCol = outlineColor;
                    float luma = (pixCol.getRed() * LUMA_RED_COEFF + pixCol.getGreen() * LUMA_GREEN_COEFF + pixCol.getBlue() * LUMA_BLUE_COEFF) / 255.0f;
                    int outRed = Math.min(Math.max(Math.round(luma * itemCol.getRed()), 0), 255);
                    int outGreen = Math.min(Math.max(Math.round(luma * itemCol.getGreen()), 0), 255);
                    int outBlue = Math.min(Math.max(Math.round(luma * itemCol.getBlue()), 0), 255);
                    final Color outCol = new Color(outRed, outGreen, outBlue);
                    img.setRGB(px, py, outCol.getRGB());
                } else if (pixCol.getAlpha() == 0 && cs.getAlpha() > 0 && drawOutline) {
                    img.setRGB(px, py, outlineColor.getRGB());
                }
            }
        }
    }

    /**
     *
     * @param img parsed image to put a label on
     * @param font parsed font
     * @param label draw string
     * @param color string color
     * @param fillInterior fill interior option
     * @return
     */
    public static final BufferedImage putLabel(BufferedImage img, Font font, String label, Color color, boolean fillInterior) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D strBounds = font.getStringBounds(label, frc);
        int w = (int) Math.round(strBounds.getWidth());
        int h = (int) Math.round(strBounds.getHeight());

        BufferedImage result = new BufferedImage(Math.max(w, img.getWidth()) + 2, 2 * h + img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D resG2D = result.createGraphics();

        resG2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        resG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        resG2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        resG2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        resG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        resG2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        resG2D.setColor(color);
        resG2D.drawRoundRect(1, 1, w, h, 1, 1);
        if (fillInterior) {
            float luma = (color.getRed() * LUMA_RED_COEFF + color.getGreen() * LUMA_GREEN_COEFF + color.getBlue() * LUMA_BLUE_COEFF) / 255.0f;
            Color grey = new Color(0.5f * luma, 0.5f * luma, 0.5f * luma, 1.0f);
            resG2D.setColor(grey);
            resG2D.fillRoundRect(1, 1, w, h, 1, 1);
        }
        resG2D.setColor(color);
        resG2D.setFont(font);
        resG2D.translate(0, -Math.round(strBounds.getY()));
        resG2D.drawString(label, 2, 2);
        resG2D.drawLine(w / 2 - 1, h / 2 - 1, w / 2 - 1, 2 * h - 1);
        resG2D.translate(0, h / 2);
        resG2D.drawImage(img, (result.getWidth() - img.getWidth()) / 2, (result.getHeight() - img.getHeight()) / 2 - h / 4, null);

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

        stopped = false;

        if (config.getInDir().isDirectory()) {
            File[] fileArray = config.getInDir().listFiles();
            FO2HLogger.reportInfo("Detected " + fileArray.length + " files..", null);
            for (File srcFile : fileArray) {
                if (stopped) {
                    FO2HLogger.reportInfo("Highlighter stopped!", null);
                    break;
                }

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
                            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc[i], 1, 1, null);

                            // blue color removal
                            removeBlue(imgDst[i]);
                            // outline & fill interior effect
                            createOutline(imgDst[i], getItemColor(extLessFilename), config.isFillInterior(), config.isDrawOutline());

                            // label
                            Obj obj = DICTIONARY.get(extLessFilename);
                            boolean labeled = config.isPutLabels() && obj != null && obj.isLabeled();
                            if (labeled) {
                                imgDst[i] = putLabel(imgDst[i], myFont, MAPPED_BY.getOrDefault(extLessFilename, extLessFilename), getItemColor(extLessFilename), true);
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
                            BufferedImage imgDst = new BufferedImage(imgSrc.getWidth() + 2, imgSrc.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D graphics2D = imgDst.createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc, 1, 1, null);
                            // blue color removal
                            removeBlue(imgDst);
                            // outline & fill interior effect
                            createOutline(imgDst, getItemColor(extLessFilename), config.isFillInterior(), config.isDrawOutline());

                            // label
                            Obj obj = DICTIONARY.get(extLessFilename);
                            if (config.isPutLabels() && obj != null && obj.isLabeled()) {
                                imgDst = putLabel(imgDst, myFont, MAPPED_BY.getOrDefault(extLessFilename, extLessFilename), getItemColor(extLessFilename), true);
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

        FO2HLogger.reportInfo("Highlighter work finished!", null);
        progress = 100.0f;
    }

    public void resetProgress() {
        progress = 0.0f;
    }

    public float getProgress() {
        return progress;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

}
