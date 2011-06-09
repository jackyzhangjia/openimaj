/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.math.geometry.shape;

import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.point.Point2dImpl;

import Jama.Matrix;

/**
 * A circle shape
 * 
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>
 *
 */
public class Circle implements Shape {
	private static final long serialVersionUID = 1L;
	
	protected Point2d centre;
	protected float radius;
	
	/**
	 * Construct a circle with the given position and radius
	 * @param x the x-coordinate of the centre
	 * @param y the y-coordinate of the centre
	 * @param radius the radius
	 */
	public Circle(float x, float y, float radius) {
		this(new Point2dImpl(x, y), radius);
	}
	
	/**
	 * Construct a circle with the given position and radius
	 * @param centre the coordinate of the centre
	 * @param radius the radius
	 */
	public Circle(Point2d centre, float radius) {
		this.centre = centre;
		this.radius = radius;
	}

	@Override
	public boolean isInside(Point2d point) {
		double dx = (centre.getX() - point.getX());
		double dy = (centre.getY() - point.getY());
		
		double dist = Math.sqrt(dx*dx + dy*dy);
	    return dist <= radius;
	}

	@Override
	public Rectangle calculateRegularBoundingBox() {
		int x = Math.round(centre.getX() - radius);
		int y = Math.round(centre.getY() - radius);
		int r = Math.round(radius * 2); 
		
		return new Rectangle(x, y, r, r);
	}

	@Override
	public void translate(float x, float y) {
		centre.setX(centre.getX() + x);
		centre.setY(centre.getY() + y);
	}

	@Override
	public void scale(float sc) {
		radius *= sc;
		centre.setX(centre.getX() * sc);
		centre.setY(centre.getY() * sc);
	}

	@Override
	public void scale(Point2d centre, float sc) {
		this.translate( -centre.getX(), -centre.getY() );
		scale(sc);
		this.translate( centre.getX(), centre.getY() );
	}

	@Override
	public void scaleCOG(float sc) {
		radius *= sc;
	}

	@Override
	public Point2d getCOG() {
		return centre;
	}

	@Override
	public double calculateArea() {
		return Math.PI * radius * radius;
	}

	@Override
	public double minX() {
		return Math.round(centre.getX() - radius);
	}

	@Override
	public double minY() {
		return Math.round(centre.getY() - radius);
	}

	@Override
	public double maxX() {
		return Math.round(centre.getX() + radius);
	}

	@Override
	public double maxY() {
		return Math.round(centre.getY() + radius);
	}

	@Override
	public double getWidth() {
		return Math.round(2 * radius);
	}

	@Override
	public double getHeight() {
		return Math.round(2 * radius);
	}

	@Override
	public Shape transform(Matrix transform) {
		//TODO: could handle different cases and hand
		//back correct shape here (i.e. circle or ellipse)
		//depending on transform
		return asPolygon().transform(transform);
	}

	@Override
	public Polygon asPolygon() {
		Polygon poly = new Polygon();
		
		for (float theta = 0; theta<2 * Math.PI; theta += Math.PI/180.0) {
			float xx = (float) (radius * Math.cos(theta));
			float yy = (float) (radius * Math.sin(theta));
			poly.vertices.add(new Point2dImpl(xx, yy));
		}
		
		poly.translate(centre.getX(), centre.getY());
		
		return poly;
	}

	public void setX(float x) {
		this.centre.setX(x);
	}
	
	public void setY(float y) {
		this.centre.setY(y);
	}
	
	public void setRadius(float r) {
		this.radius = r;
	}
	
	public float getX() {
		return centre.getX();
	}
	
	public float getY() {
		return centre.getY();
	}
	
	public float getRadius() {
		return radius;
	}
}
