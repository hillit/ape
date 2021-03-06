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

package org.ape.petrinet.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ape.petrinet.view.DoubleQuad;
import org.ape.util.CachedGraphics2D;

/**
 * Represents a subnet or net in Petri net
 * 
 * @author Martin Riesz <riesz.martin at gmail.com>
 */
public class Subnet extends TransitionNode {

	private List<Element> elements = new LinkedList<Element>();

	public Subnet() {
		setGraphicalElement(new DoubleQuad(this));
	}

	public Point getViewTranslation() {
		return getBounds().getLocation();
	}

	public List<Element> getElements() {
		return Collections.unmodifiableList(elements);
	}

	public List<Element> getElementsCopy() {
		return new LinkedList<Element>(elements);
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
		for (Element element : elements) {
			element.setParentSubnet(this);
		}
	}

	public Element getElementByXY(int x, int y) {
		for (int i = elements.size() - 1; i >= 0; i--) { // Check elements from
															// front to back.
			Element element = (Element) elements.get(i);
			if (element.containsPoint(x, y)) {
				return element;
			}
		}
		return null;
	}

	public Set<Subnet> getSubnets() {
		Set<Subnet> subnets = new HashSet<Subnet>();
		for (Element element : getElements()) {
			if (element instanceof Subnet) {
				subnets.add((Subnet) element);
			}
		}
		return subnets;
	}

	public void addElement(Element element) {
		element.setParentSubnet(this);
		if (element instanceof ReferenceArc || element instanceof ArcEdge) {
			elements.add(0, element); // background
		} else {
			elements.add(element);
		}
	}

	public void removeElement(Element element) {
		elements.remove(element);
	}

	public void removeElements() {
		elements.clear();
	}

	public void addAll(Set<Element> elements) {
		for (Element element : elements) {
			addElement(element);
		}
	}

	public void removeAll(Set<Element> elements) {
		for (Element element : elements) {
			removeElement(element);
		}
	}

	public Set<ArcEdge> getArcEdges() {
		Set<ArcEdge> arcs = new HashSet<ArcEdge>();
		for (Element element : elements) {
			if (element instanceof ArcEdge) {
				ArcEdge arc = (ArcEdge) element;
				arcs.add(arc);
			}
		}
		return arcs;
	}

	public Set<Arc> getArcs() {
		Set<Arc> arcs = new HashSet<Arc>();
		for (Element element : elements) {
			if (element instanceof Arc) {
				Arc arc = (Arc) element;
				arcs.add(arc);
			}
		}
		return arcs;
	}

	public Set<ReferenceArc> getReferenceArcs() {
		Set<ReferenceArc> referenceArcs = new HashSet<ReferenceArc>();
		for (Element element : elements) {
			if (element instanceof ReferenceArc) {
				ReferenceArc referenceArc = (ReferenceArc) element;
				referenceArcs.add(referenceArc);
			}
		}
		return referenceArcs;
	}

	public ArcEdge getArcEdge(PlaceNode placeNode,
			TransitionNode transitionNode, boolean placeToTransition) {
		for (Element element : getElements()) {
			if (element instanceof ArcEdge) {
				ArcEdge arcEdge = (ArcEdge) element;
				if (arcEdge.getPlaceNode() == placeNode
						&& arcEdge.getTransitionNode() == transitionNode
						&& arcEdge.isPlaceToTransition() == placeToTransition) {
					return arcEdge;
				}
			}
		}
		return null;
	}

	public Set<Transition> getTransitions() {
		Set<Transition> transitions = new HashSet<Transition>();
		for (Element element : getElements()) {
			if (element instanceof Transition) {
				transitions.add((Transition) element);
			}
		}
		return transitions;
	}

	public Set<Place> getPlaces() {
		Set<Place> places = new HashSet<Place>();
		for (Element element : getElements()) {
			if (element instanceof Place) {
				places.add((Place) element);
			}
		}
		return places;
	}

	public Set<Node> getNodes() {
		Set<Node> nodes = new HashSet<Node>();
		for (Element element : getElements()) {
			if (element instanceof Node) {
				nodes.add((Node) element);
			}
		}
		return nodes;
	}

	public Set<Node> getNodesRecursively() {
		Set<Node> nodes = new HashSet<Node>();
		for (Element element : elements) {
			if (element instanceof Subnet) {
				Subnet subnet = (Subnet) element;
				nodes.addAll(subnet.getNodesRecursively());
			}
			if (element instanceof Node) { // including subnets
				Node node = (Node) element;
				nodes.add(node);
			}
		}
		return nodes;
	}

	public Set<Place> getPlacesRecursively() {
		Set<Place> places = new HashSet<Place>();
		for (Element element : elements) {
			if (element instanceof Subnet) {
				Subnet subnet = (Subnet) element;
				places.addAll(subnet.getPlacesRecursively());
			} else if (element instanceof Place) {
				Place place = (Place) element;
				places.add(place);
			}
		}
		return places;
	}

	public Set<ReferencePlace> getReferencePlaces() {
		Set<ReferencePlace> referencePlaces = new HashSet<ReferencePlace>();
		for (Element element : elements) {
			if (element instanceof ReferencePlace) {
				ReferencePlace referencePlace = (ReferencePlace) element;
				referencePlaces.add(referencePlace);
			}
		}
		return referencePlaces;
	}

	public Set<ReferencePlace> getReferencePlacesRecursively() {
		Set<ReferencePlace> referencePlaces = new HashSet<ReferencePlace>();
		for (Element element : elements) {
			if (element instanceof Subnet) {
				Subnet subnet = (Subnet) element;
				referencePlaces.addAll(subnet.getReferencePlacesRecursively());
			} else if (element instanceof ReferencePlace) {
				ReferencePlace referencePlace = (ReferencePlace) element;
				referencePlaces.add(referencePlace);
			}
		}
		return referencePlaces;
	}

	public Set<Transition> getTransitionsRecursively() {
		Set<Transition> transitions = new HashSet<Transition>();
		for (Element element : elements) {
			if (element instanceof Subnet) {
				Subnet subnet = (Subnet) element;
				transitions.addAll(subnet.getTransitionsRecursively());
			} else if (element instanceof Transition) {
				Transition transition = (Transition) element;
				transitions.add(transition);
			}
		}
		return transitions;
	}

	public Set<Subnet> getSubnetsRecursively() {
		Set<Subnet> subnets = new HashSet<Subnet>();
		for (Element element : elements) {
			if (element instanceof Subnet) {
				Subnet subnet = (Subnet) element;
				subnets.add(subnet);
				subnets.addAll(subnet.getSubnetsRecursively());
			}
		}
		return subnets;
	}

	public Rectangle getBounds() {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		;

		for (Element element : elements) {
			// if (bounds == null) {
			// bounds = new Rectangle(element.getStart().x,
			// element.getStart().y, element.getWidth(), getHeight());
			// }
			bounds.add(element.getStart().x, element.getStart().y);
			bounds.add(element.getEnd().x, element.getEnd().y);
			bounds.add(element.getStart().x, element.getEnd().y);
			bounds.add(element.getEnd().x, element.getStart().y);
			if (element instanceof Edge) {
				Edge edge = (Edge) element;
				for (Point breakPoint : edge.getBreakPoints()) {
					bounds.add(breakPoint);
				}
			}
		}
		// if (bounds == null) {
		// bounds = new Rectangle();
		// }
		bounds.width++;
		bounds.height++;
		return bounds;
	}

	public Rectangle getBoundsRecursively() {
		return getBoundsRecursively(this);
	}

	private Rectangle getBoundsRecursively(Subnet subnet) {
		Rectangle bounds = subnet.getBounds();
		for (Element element : subnet.elements) {
			if (element instanceof Subnet) {
				Subnet subsubnet = (Subnet) element;
				Rectangle subsubnetBounds = getBoundsRecursively(subsubnet);
				subsubnetBounds.translate(subsubnet.getCenter().x,
						subsubnet.getCenter().y);
				bounds = bounds.createUnion(subsubnetBounds).getBounds();
			}
		}
		return bounds;
	}

	public Node getNodeByLabel(String label) {
		for (Element element : elements) {
			if (element instanceof Node) {
				Node node = (Node) element;
				if (label.equals(node.getLabel())) {
					return node;
				}
			}
		}
		return null;
	}

	public Node getNodeById(String id) {
		for (Element element : elements) {
			if (element instanceof Node) {
				Node node = (Node) element;
				if (id.equals(node.getId())) {
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * Returns an preview image of the subnet with specified marking. Scale
	 * image: image.getScaledInstance(preferredWidth, preferredHeight,
	 * Image.SCALE_SMOOTH) Save image: ImageIO.write(image, "png", file);
	 */
	public BufferedImage getPreview(Marking marking) {
		CachedGraphics2D cachedGraphics = new CachedGraphics2D();
		cachedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		DrawingOptions drawingOptions = new DrawingOptions();
		drawingOptions.setMarking(marking);
		marking.getLock().readLock().lock();
		try {
			for (Element element : getElements()) {
				element.getGraphicalElement().draw(cachedGraphics,
						drawingOptions);
			}
		} finally {
			marking.getLock().readLock().unlock();
		}
		Rectangle bounds = cachedGraphics.getIntegerBounds();
		int width = bounds.width;
		int height = bounds.height;
		width = Math.max(width, 1);
		height = Math.max(height, 1);
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
		imageGraphics.fillRect(0, 0, width, height); // paint the background
														// white
		imageGraphics.translate(-bounds.x, -bounds.y);
		cachedGraphics.applyToGraphics(imageGraphics);
		return bufferedImage;
	}

	@Override
	public Subnet getClone() {
		Subnet subnet = (Subnet) super.getClone();
		// subnet.viewTranslation = this.viewTranslation.getLocation();

		subnet.elements = new LinkedList<Element>();
		for (Element element : this.getElements()) {
			subnet.addElement(element.getClone());
		}
		return subnet;
	}
}
