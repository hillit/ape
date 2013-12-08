/*
 * Copyright (C) 2008-2010 Martin Riesz <riesz.martin at gmail.com>
 * Copyright (C) 2013 Hillit Saathoff <mail at hillit.de>
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

package org.ape.petrinet.view;

import java.awt.Graphics;
import java.awt.Point;

import org.ape.petrinet.model.Arc;
import org.ape.petrinet.model.ArcEdge;
import org.ape.petrinet.model.DrawingOptions;

public class GraphicalArc extends GraphicalEdge {

	public static final int NEAR_TOLERANCE = 10;

	public GraphicalArc(ArcEdge arc) {
		super(arc);
	}

	@Override
	public void draw(Graphics g, DrawingOptions drawingOptions) {
		g.setColor(getArc().getColor());
		drawSegmentedLine(g);
		Point arrowTip = computeArrowTipPoint();
		drawArrow(g, arrowTip);
		int multiplicity = ((Arc) getArc()).getMultiplicity();
		if (multiplicity >= 2) {
			drawMultiplicityLabel(g, arrowTip, multiplicity);
		}
	}

}