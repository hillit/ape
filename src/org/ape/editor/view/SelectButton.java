/*
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

package org.ape.editor.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JToggleButton;

import org.ape.editor.controller.Root;
import org.ape.editor.model.ActiveFeature;
import org.ape.util.FeatureType;

@SuppressWarnings("serial")
public class SelectButton extends JToggleButton implements Observer {

	private final FeatureType featureType = FeatureType.SELECT;
	private ActiveFeature activeFeature;

	public SelectButton(Root root, Action selectTool_SelectionAction) {
		super(selectTool_SelectionAction);
		this.activeFeature = root.getActiveFeatureType();
		activeFeature.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		setSelected(activeFeature.getActiveFeatureType() == featureType);
	}

}
