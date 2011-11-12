package org.openimaj.demos.sandbox.slideshow;

import java.awt.Component;
import java.io.IOException;

public interface Slide {
	public abstract Component getComponent(int width, int height) throws IOException;
	
	public abstract void close();
}