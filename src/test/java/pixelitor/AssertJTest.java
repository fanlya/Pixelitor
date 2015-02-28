/*
 * Copyright 2015 Laszlo Balazs-Csiki
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor. If not, see <http://www.gnu.org/licenses/>.
 */

package pixelitor;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.JFileChooserFinder;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.junit.Before;
import org.junit.Test;
import pixelitor.tools.BrushType;
import pixelitor.tools.GradientColorType;
import pixelitor.tools.GradientTool;
import pixelitor.tools.GradientType;
import pixelitor.tools.Symmetry;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_Z;

public class AssertJTest {
    private FrameFixture window;
    private Random random = new Random();
    private Robot robot;

    protected void setUpRobot() {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(500);
    }

    @Before
    public void setUp() {
        setUpRobot();
        onSetUp();
    }

    protected void onSetUp() {
        ApplicationLauncher
                .application("pixelitor.Pixelitor")
                .withArgs("C:\\Users\\Laci\\Desktop\\bagoly.png")
                .start();
        window = WindowFinder.findFrame("frame0")
                .withTimeout(10, TimeUnit.SECONDS)
                .using(robot);
        PixelitorWindow.getInstance().setLocation(0, 0);
    }

    @Test
    public void testApp() {
//        testTools();
        testMenus();
//        testLayers();

        sleep(5, TimeUnit.SECONDS);
    }

    private void testLayers() {
        // TODO add, remove, change visibility, all the Layers menus
    }

    protected void testMenus() {
//        testFileMenu();
//        testEditMenu();
//        testFilters();
//        testZoomCommands();
//        testViewCommands();
        testHelpMenu();
    }

    protected void testHelpMenu() {
        testTipOfTheDay();
//        testCheckForUpdate();
//        testAbout();
    }


    private void testAbout() {
        findMenuItemByText("About").click();
        DialogFixture aboutDialog = findDialogByTitle("About Pixelitor");
        aboutDialog.button("ok").click();
    }

    private void testCheckForUpdate() {
        findMenuItemByText("Check for Update...").click();
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().withTimeout(10, TimeUnit.SECONDS).using(robot);
        optionPane.cancelButton().click();
    }

    private void testTipOfTheDay() {
        findMenuItemByText("Tip of the Day").click();
        DialogFixture dialog = findDialogByTitle("Tip of the Day");
        findButtonInDialogByText(dialog, "Next >").click();
        findButtonInDialogByText(dialog, "Next >").click();
        findButtonInDialogByText(dialog, "< Back").click();
        findButtonInDialogByText(dialog, "Close").click();
    }

    protected void testEditMenu() {
        // TODO testRepeatLast
        // TODO testUndo
        // TODO testRedo
        // TODO testCopyPaste // copy layer, copy composite, paste as layer, paste as new image
        // TODO testResize
        // TODO testRotateFlip
        // TODO testTransformLater
        // TODO testPreferences
    }

    protected void testFileMenu() {
        testNewImage();
        testFileOpen();
        // TODO testFileSave - both new and opened files
        // TODO testFileSaveAs
        // TODO testExportOptimizedJPEG
        // TODO testExportOpenRaster
        // TODO testExportLayerAnimation
        // TODO testExportTweeningAnimation
        // TODO testClose
        // TODO testCloseAll
        // TODO testBatchResize
        // TODO testBatchFilter
        // TODO testExportLayerToPNG
        // TODO testScreenCapture
    }

    protected void testViewCommands() {
        testMenuCommand("Set Default Workspace");
        testMenuCommand("Hide Status Bar");
        testMenuCommand("Show Status Bar");
        testMenuCommand("Show Histograms");
        testMenuCommand("Hide Histograms");
        testMenuCommand("Hide Layers");
        testMenuCommand("Show Layers");

        testMenuCommand("Hide Tools");
        testMenuCommand("Show Tools");

        testMenuCommand("Hide All");
        testMenuCommand("Show Hidden");

        testMenuCommand("Cascade");
        testMenuCommand("Tile");
    }

    protected void testNewImage() {
        findMenuItemByText("New Image...").click();
        DialogFixture newImageDialog = findDialogByTitle("New Image");
        newImageDialog.textBox("widthTF").enterText("600");
        newImageDialog.textBox("heightTF").enterText("400");
        newImageDialog.button("ok").click();
    }

    protected void testFileOpen() {
        findMenuItemByText("Open...").click();
        JFileChooserFixture openDialog = JFileChooserFinder.findFileChooser("open").using(robot);
        openDialog.cancel();
        window.menuItem("open").click();
        openDialog = JFileChooserFinder.findFileChooser("open").using(robot);
        openDialog.selectFile(new File("C:\\Users\\Laci\\Desktop\\ee.png"));
        openDialog.approve();
    }

    protected void testTools() {
        testZoomTool();
        testMoveTool();
        testCropTool();
        testSelectionTool();
        testCloneTool();
        testEraserTool();
        testBrushTool();
        testGradientTool();
        testPaintBucketTool();
        testColorPickerTool();
        testShapesTool();
        testHandTool();
    }

    protected void testZoomCommands() {
        testMenuCommand("Zoom In");
        testMenuCommand("Zoom Out");
        testMenuCommand("Actual Pixels");
        testMenuCommand("Fit Screen");
        testMenuCommand("12.5 %");
        testMenuCommand("17.7 %");
        testMenuCommand("25 %");
        testMenuCommand("35.3 %");
        testMenuCommand("50 %");
        testMenuCommand("70.7 %");
        testMenuCommand("100 %");
        testMenuCommand("141.4 %");
        testMenuCommand("200 %");
        testMenuCommand("282.8 %");
        testMenuCommand("400 %");
        testMenuCommand("565.7 %");
        testMenuCommand("800 %");
        testMenuCommand("1131.4 %");
        testMenuCommand("1600 %");
        testMenuCommand("2262.7 %");
        testMenuCommand("3200 %");
        testMenuCommand("4525.5 %");
        testMenuCommand("6400 %");
    }

    protected void testFilters() {
        testFilterWithDialog("Color Balance...", true);
        testFilterWithDialog("Hue/Saturation...", true);
        testFilterWithDialog("Colorize...", true);
        testFilterWithDialog("Levels...", false);
        testFilterWithDialog("Brightness/Contrast...", true);
        testFilterWithDialog("Solarize...", true);
        testFilterWithDialog("Sepia...", false);
        testNoDialogFilter("Invert");
        testFilterWithDialog("Channel Invert...", false);
        testFilterWithDialog("Channel Mixer...", false); // TODO
        testFilterWithDialog("Extract Channel...", true);
        testNoDialogFilter("Luminosity");
        testNoDialogFilter("Value = max(R,G,B)");
        testNoDialogFilter("Desaturate");
        testNoDialogFilter("Hue");
        testNoDialogFilter("Hue (with colors)");
        testNoDialogFilter("Saturation");
        testFilterWithDialog("Quantize...", true);
        testFilterWithDialog("Posterize...", false);
        testFilterWithDialog("Threshold...", true);
        testFilterWithDialog("Tritone...", true);
        testFilterWithDialog("Gradient Map...", false);
        testFilterWithDialog("Color Halftone...", true);
        testFilterWithDialog("Dither...", true);
        testNoDialogFilter("Foreground Color");
        testNoDialogFilter("Background Color");
        testNoDialogFilter("Transparent");
        testFilterWithDialog("Color Wheel...", true);
        testFilterWithDialog("Four Color Gradient...", false); // TODO
        testFilterWithDialog("Starburst...", true);
        testFilterWithDialog("Gaussian Blur...", true);
        testFilterWithDialog("Smart Blur...", true);
        testFilterWithDialog("Box Blur...", true);
        testFilterWithDialog("Fast Blur...", true);
        testFilterWithDialog("Lens Blur...", true);
        testFilterWithDialog("Motion Blur...", true);
        testFilterWithDialog("Spin and Zoom Blur...", true);
        testFilterWithDialog("Unsharp Mask...", true);
        testFilterWithDialog("Swirl, Pinch, Bulge...", true);
        testFilterWithDialog("Circle to Square...", true);
        testFilterWithDialog("Perspective...", false); // TODO
        testFilterWithDialog("Lens Over Image...", true);
        testFilterWithDialog("Magnify...", true);
        testFilterWithDialog("Turbulent Distortion...", true);
        testFilterWithDialog("Underwater...", true);
        testFilterWithDialog("Water Ripple...", true);
        testFilterWithDialog("Waves...", true);
        testFilterWithDialog("Angular Waves...", true);
        testFilterWithDialog("Radial Waves...", true);
        testFilterWithDialog("Glass Tiles...", true);
        testFilterWithDialog("Polar Glass Tiles...", true);
        testFilterWithDialog("Frosted Glass...", true);
        testFilterWithDialog("Little Planet...", true);
        testFilterWithDialog("Polar Coordinates...", true);
        testFilterWithDialog("Wrap Around Arc...", true);
        testFilterWithDialog("Kaleidoscope...", true);
        testFilterWithDialog("Video Feedback...", true);
        testFilterWithDialog("Offset...", false); // TODO
        testFilterWithDialog("Slice...", true);
        testFilterWithDialog("Mirror...", true);
        testFilterWithDialog("Glow...", true);
        testFilterWithDialog("Sparkle...", true);
        testFilterWithDialog("Rays...", true);
        testFilterWithDialog("Glint...", true);
        testNoDialogFilter("Reduce Single Pixel Noise");
        testNoDialogFilter("3x3 Median Filter");
        testFilterWithDialog("Add Noise...", true);
        testFilterWithDialog("Pixelate...", true);
        testFilterWithDialog("Clouds...", true);
        testFilterWithDialog("Value Noise...", true);
        testFilterWithDialog("Caustics...", true);
        testFilterWithDialog("Plasma...", true);
        testFilterWithDialog("Wood...", true);
        testFilterWithDialog("Cells...", true);
        testFilterWithDialog("Brushed Metal...", true);
        testFilterWithDialog("Crystallize...", true);
        testFilterWithDialog("Pointillize...", true);
        testFilterWithDialog("Stamp...", true);
        testFilterWithDialog("Dry Brush...", true);
        testFilterWithDialog("Random Spheres...", true);
        testFilterWithDialog("Smear...", true);
        testFilterWithDialog("Emboss...", true);
        testFilterWithDialog("Orton Effect...", true);
        testFilterWithDialog("Photo Collage...", true);
        testFilterWithDialog("Convolution Edge Detection...", true);
        testNoDialogFilter("Laplacian");
        testFilterWithDialog("Difference of Gaussians...", true);
        testFilterWithDialog("Canny Edge Detector...", true);
        testFilterWithDialog("Drop Shadow...", true);
        testFilterWithDialog("2D Transitions...", true);

        // TODO    Custom 3x3 Convolution...
        // TODO    Custom 5x5 Convolution...
        // TODO    Random Filter...
        // TODO    Text...
    }

    private void testMenuCommand(String name) {
        window.menuItem(name).click();
    }

    private void testNoDialogFilter(String name) {
        window.menuItem(name).click();
    }

    private void testFilterWithDialog(String name, boolean randomize) {
        window.menuItem(name).click();
        DialogFixture filterDialog = WindowFinder.findDialog("filterDialog").using(robot);
        if(randomize) {
            filterDialog.button("Randomize Settings").click();
            filterDialog.button("Reset All").click();
            filterDialog.button("Randomize Settings").click();
        }
        filterDialog.button("ok").click();
    }

    protected void testHandTool() {
        window.toggleButton("Hand Tool Button").click();
    }

    protected void testShapesTool() {
        window.toggleButton("Shapes Tool Button").click();
    }

    protected void testColorPickerTool() {
        window.toggleButton("Color Picker Tool Button").click();
        move(300, 300);
        window.click();
        drag(400, 400);
    }

    protected void testPaintBucketTool() {
        window.toggleButton("Paint Bucket Tool Button").click();
        move(300, 300);
        window.click();
    }

    protected void testGradientTool() {
        window.toggleButton("Gradient Tool Button").click();
        for(GradientType gradientType : GradientType.values()) {
            window.comboBox("gradientTypeSelector").selectItem(gradientType.toString());
            for(String cycleMethod : GradientTool.CYCLE_METHODS) {
                window.comboBox("gradientCycleMethodSelector").selectItem(cycleMethod);
                GradientColorType[] gradientColorTypes = GradientColorType.values();
                for(GradientColorType colorType : gradientColorTypes) {
                    window.comboBox("gradientColorTypeSelector").selectItem(colorType.toString());
                    window.checkBox("gradientInvert").uncheck();
                    move(200, 200);
                    drag(400, 400);
                    window.checkBox("gradientInvert").check();
                    move(200, 200);
                    drag(400, 400);
                }
            }
        }
    }

    protected void testEraserTool() {
        window.toggleButton("Erase Tool Button").click();
        testBrushStrokes();
    }

    protected void testBrushTool() {
        window.toggleButton("Brush Tool Button").click();
        testBrushStrokes();
    }

    protected void testCloneTool() {
        window.toggleButton("Clone Tool Button").click();
        move(300, 300);
        window.pressKey(VK_ALT).click().releaseKey(VK_ALT);
        move(400, 300);
        for(int i = 1; i <= 20; i++) {
            drag(400 + i * 5, 300);
            drag(400 + i * 5, 400);
        }
    }

    protected void testBrushStrokes() {
        for(BrushType brushType : BrushType.values()) {
            window.comboBox("brushTypeSelector").selectItem(brushType.toString());
            for(Symmetry symmetry : Symmetry.values()) {
                window.comboBox("symmetrySelector").selectItem(symmetry.toString());
                window.pressAndReleaseKeys(KeyEvent.VK_R);
                moveRandom();
                dragRandom();
            }
        }
    }

    protected void testSelectionTool() {
        window.toggleButton("Selection Tool Button").click();
        move(200, 200);
        drag(400, 400);
        window.button("brushTraceButton").click();
        deselect();
        undo(); // undo deselection
        undo(); // undo tracing
        window.comboBox("selectionTypeCombo").selectItem("Ellipse");
        move(200, 200);
        drag(400, 400);
        window.comboBox("selectionInteractionCombo").selectItem("Add");
        move(400, 200);
        drag(500, 300);
        window.button("eraserTraceButton").click();
        deselect();
        // TODO test crop from tool and also from menu
        // TODO test all items from selection menu
    }

    protected void testCropTool() {
        window.toggleButton("Crop Tool Button").click();
        move(200, 200);
        drag(400, 400);
        drag(450, 450);
        move(200, 200);
        drag(150, 150);
        sleep(1, TimeUnit.SECONDS);
        window.button("cropButton").click();
        undo();
    }

    protected void testMoveTool() {
        window.toggleButton("Move Tool Button").click();
        move(300, 300);
        drag(400, 400);
        undo();
    }

    protected void testZoomTool() {
        window.toggleButton("Zoom Tool Button").click();
        move(300, 300);
        window.click();
        window.click();
        // TODO Alt-click to zoom out and all the zoom methods, including mouse wheel
    }

    private void undo() {
        window.pressKey(VK_CONTROL).pressKey(VK_Z).releaseKey(VK_Z).releaseKey(VK_CONTROL);
    }

    private void deselect() {
        window.pressKey(VK_CONTROL).pressKey(VK_D).releaseKey(VK_D).releaseKey(VK_CONTROL);
    }

    private void move(int x, int y) {
        robot.moveMouse(x, y);
    }

    private void moveRandom() {
        int x = 200 + random.nextInt(400);
        int y = 200 + random.nextInt(400);
        move(x, y);
    }

    private void dragRandom() {
        int x = 200 + random.nextInt(400);
        int y = 200 + random.nextInt(400);
        drag(x, y);
    }

    private void drag(int x, int y) {
        robot.pressMouse(MouseButton.LEFT_BUTTON);
        robot.moveMouse(x, y);
        robot.releaseMouse(MouseButton.LEFT_BUTTON);
    }

    private void sleep(int duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch(InterruptedException e) {
            throw new IllegalStateException("interrupted!");
        }
    }

    private JMenuItemFixture findMenuItemByText(String guiName) {
        return new JMenuItemFixture(robot, robot.finder().find(new GenericTypeMatcher<JMenuItem>(JMenuItem.class) {
            @Override
            protected boolean isMatching(JMenuItem menuItem) {
                return guiName.equals(menuItem.getText());
            }
        }));
    }

    private DialogFixture findDialogByTitle(String title) {
        return new DialogFixture(robot, robot.finder().find(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog dialog) {
                return dialog.getTitle().equals(title);
            }
        }));
    }

    private JButtonFixture findButtonInDialogByText(DialogFixture dialog, String text) {
        JButtonFixture button = dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText().equals(text);
            }
        });

        return button;
    }

}