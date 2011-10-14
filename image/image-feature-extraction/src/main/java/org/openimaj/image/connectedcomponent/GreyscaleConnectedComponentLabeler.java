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
package org.openimaj.image.connectedcomponent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.pixel.Pixel;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.math.geometry.shape.Circle;



/**
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>
 *
 */
public class GreyscaleConnectedComponentLabeler {
	public enum Algorithm {
		FLOOD_FILL
	}
	
	public static Algorithm DEFAULT_ALGORITHM = Algorithm.FLOOD_FILL;
	
	
	
	public GreyscaleConnectedComponentLabeler() {
	}
	
	public List<ConnectedComponent> findComponents(FImage image) {
		switch (DEFAULT_ALGORITHM) {
		case FLOOD_FILL:
			return findComponentsFlood(image);
		}
		throw new UnsupportedOperationException("Bad algorithm " + DEFAULT_ALGORITHM);
	}
	
	

	public List<ConnectedComponent> findComponentsFlood(FImage image) {
		List<ConnectedComponent> components = new ArrayList<ConnectedComponent>();
		int [][] labels = new int[image.height][image.width];
		int nextColor=1;
		
		for (int y=0; y<image.height; y++) {
			for (int x=0; x<image.width; x++) {
				if (labels[y][x] == 0) {
					components.add(floodFill(image, new Pixel(x, y), labels, nextColor));
					nextColor++;
				}
			}
		}
		
		return components;
	}
	
	protected ConnectedComponent floodFill(FImage image, Pixel start, int[][] output, int color) {
		ConnectedComponent cc = new ConnectedComponent();
//		Flood-fill (node, target-color, replacement-color):
//			 1. Set Q to the empty queue.
		//Queue<Pixel> queue = new LinkedList<Pixel>();
		LinkedHashSet<Pixel> queue = new LinkedHashSet<Pixel>();
		
//			 2. If the color of node is not equal to target-color, return.
		float targetColour = image.pixels[start.y][start.x];
		
//			 3. Add node to Q.
		queue.add(start);
//			 4. For each element n of Q:
		while (queue.size() > 0) {
			//Pixel n = queue.poll();
			Pixel n = queue.iterator().next();
			queue.remove(n);
			
//			 5.  If the color of n is equal to target-color:
			if (image.pixels[n.y][n.x] == targetColour) {
//			 6.   Set w and e equal to n.
				int e = n.x, w=n.x;
//			 7.   Move w to the west until the color of the node to the west of w no longer matches target-color.
				while (w>0 && image.pixels[n.y][w-1] == targetColour) w--;
				
//			 8.   Move e to the east until the color of the node to the east of e no longer matches target-color.
				while (e<image.width-1 && image.pixels[n.y][e+1] == targetColour) e++;
				
//			 9.   Set the color of nodes between w and e to replacement-color.
				for (int i=w; i<=e; i++) {
					output[n.y][i] = color;
					cc.addPixel(i, n.y);
					
//			10.   For each node n between w and e:
					int north = n.y - 1;
					int south = n.y + 1;
//			11.    If the color of the node to the north of n is target-color, add that node to Q.
					if (north >= 0 && image.pixels[north][i] == targetColour && output[north][i] != color) queue.add(new Pixel(i, north));
//			       If the color of the node to the south of n is target-color, add that node to Q.
					if (south < image.height && image.pixels[south][i] == targetColour && output[south][i] != color) queue.add(new Pixel(i, south));
				}
//			12. Continue looping until Q is exhausted.
			}
		}
//			13. Return.
		return cc;
	}
	
	public static void main(String args[]){
		FImage greyCircles = new FImage(400,400);
		greyCircles.drawShapeFilled(new Circle(100,100,50), 0.5f);
		greyCircles.drawShapeFilled(new Circle(200,200,150), 0.2f);
		greyCircles.drawShapeFilled(new Circle(300,200,150), 0.9f);
		DisplayUtilities.display(greyCircles);
		GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
		List<ConnectedComponent> components = labeler.findComponents(greyCircles);
		MBFImage seghole = new MBFImage(400,400,ColourSpace.RGB);
		SegmentationUtilities.renderSegments(seghole, components);
		DisplayUtilities.display(seghole);
	}
}