/*
 * Copyright 2018 Laszlo Balazs-Csiki and Contributors
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

package pixelitor.tools;

import pixelitor.Composition;
import pixelitor.gui.ImageComponent;
import pixelitor.gui.ImageComponents;
import pixelitor.layers.Layer;
import pixelitor.tools.move.ObjectsFinder;
import pixelitor.tools.move.ObjectsSelection;
import pixelitor.tools.util.ArrowKey;
import pixelitor.tools.util.DragDisplayType;
import pixelitor.tools.util.ImDrag;
import pixelitor.tools.util.PMouseEvent;
import pixelitor.utils.Cursors;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * The move tool.
 */
public class MoveTool extends DragTool {
    private static final String AUTO_SELECT_LABEL = "Auto Select Layer";
    private final JCheckBox autoselectCheckBox = new JCheckBox(AUTO_SELECT_LABEL);
    private ObjectsFinder objectFinder = new ObjectsFinder();

    public MoveTool() {
        super("Move", 'v', "move_tool_icon.png",
                "<b>drag</b> to move the active layer, " +
                        "<b>Alt-drag</b> (or <b>right-mouse-drag</b>) to move a duplicate of the active layer. " +
                        "<b>Shift-drag</b> to constrain the movement.",
                Cursors.DEFAULT, false, true, true, ClipStrategy.CANVAS);
    }

    @Override
    public void initSettingsPanel() {
        settingsPanel.add(autoselectCheckBox);
    }

    @Override
    public void mouseMoved(MouseEvent e, ImageComponent ic) {
        super.mouseMoved(e, ic);

        if (autoselectCheckBox.isSelected()) {
            Point2D p = ic.componentToImageSpace(e.getPoint());
            ObjectsSelection objectsSelection = objectFinder.findLayerAtPoint(p, ic.getComp());

            if (objectsSelection.isEmpty()) {
                ic.setCursor(Cursors.DEFAULT);
                return;
            }
        }

        ic.setCursor(Cursors.MOVE);
    }

    @Override
    public void dragStarted(PMouseEvent e) {
        if (autoselectCheckBox.isSelected()) {
            Point2D p = e.getComp().getIC().componentToImageSpace(e.getPoint());
            ObjectsSelection objectsSelection = objectFinder.findLayerAtPoint(p, e.getComp());

            if (objectsSelection.isEmpty()) {
                userDrag.cancel();
                return;
            }
            e.getComp().setActiveLayer((Layer) objectsSelection.getObject());
        }

        e.getComp().startMovement(e.isAltDown() || e.isRight());
    }

    @Override
    public void ongoingDrag(PMouseEvent e) {
        ImDrag imDrag = userDrag.toImDrag();
        double relX = imDrag.getDX();
        double relY = imDrag.getDY();

        e.getComp().moveActiveContentRelative(relX, relY);
    }

    @Override
    public DragDisplayType getDragDisplayType() {
        return DragDisplayType.REL_MOUSE_POS;
    }

    @Override
    public void dragFinished(PMouseEvent e) {
        e.getComp().endMovement();
    }

    /**
     * Moves the active layer programmatically.
     */
    public static void move(Composition comp, int relX, int relY) {
        comp.startMovement(false);
        comp.moveActiveContentRelative(relX, relY);
        comp.endMovement();
    }

    @Override
    public boolean arrowKeyPressed(ArrowKey key) {
        Composition comp = ImageComponents.getActiveCompOrNull();
        if (comp != null) {
            move(comp, key.getMoveX(), key.getMoveY());
            return true;
        }
        return false;
    }
}