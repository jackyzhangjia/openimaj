package org.openimaj.image.processing.face.features;

import org.openimaj.image.processing.face.parts.DetectedFace;

/**
 * LTP based feature using a truncated Euclidean distance transform
 * to estimate the distances within each slice.
 * 
 * Based on: 
 * "Enhanced Local Texture Feature Sets for Face Recognition 
 * Under Difficult Lighting Conditions" by Xiaoyang Tan and 
 * Bill Triggs.
 *
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>
 *
 */
public class TruncatedDistanceLTPFeature extends AbstractLTPFeature<TruncatedDistanceLTPFeature> {
	public static class Factory implements FacialFeatureFactory<TruncatedDistanceLTPFeature> {
		float threshold = 6;
		boolean affineMode = false;
		
		public Factory() {}
		public Factory(float threshold) {
			this.threshold = threshold;
		}
		public Factory(float threshold, boolean affineMode) {
			this.threshold = threshold;
			this.affineMode = affineMode;
		}
		public Factory(boolean affineMode) {
			this.affineMode = affineMode;
		}
		
		@Override
		public TruncatedDistanceLTPFeature createFeature(DetectedFace face, boolean isquery) {
			TruncatedDistanceLTPFeature f = new TruncatedDistanceLTPFeature(threshold, affineMode);
			f.initialise(face, isquery);
			return f;
		}
	}
	
	float threshold = 6;
	
	/**
	 * Construct the TruncatedDistanceLTPFeature with the default
	 * threshold of 6 pixels. Defaults to 
	 * non-affine normalised faces (i.e. just the eye rotation and
	 * position is optimised).
	 */
	public TruncatedDistanceLTPFeature() {
		this(false);
	}
	
	/**
	 * Construct the TruncatedDistanceLTPFeature with the provided
	 * threshold. Defaults to non-affine normalised
	 * faces (i.e. just the eye rotation and position is optimised).
	 * @param threshold the threshold
	 */
	public TruncatedDistanceLTPFeature(float threshold) {
		this(false);
		this.threshold = threshold;
	}
	
	/**
	 * Construct the TruncatedDistanceLTPFeature with the default
	 * threshold of 6 pixels. The affineMode
	 * parameter can be used to enable the feature on affine normalised
	 * faces.
	 * @param affineMode set to true to enable usage on affine normalised faces
	 */
	public TruncatedDistanceLTPFeature(boolean affineMode) {
		super(affineMode);
	}

	/**
	 * Construct the TruncatedDistanceLTPFeature with the provided
	 * threshold. The affineMode parameter can be 
	 * used to enable the feature on affine normalised faces.
	 * @param threshold the threshold
	 * @param affineMode set to true to enable usage on affine normalised faces
	 */
	public TruncatedDistanceLTPFeature(float threshold, boolean affineMode) {
		this(affineMode);
		this.threshold = threshold;
	}

	@Override
	protected float weightDistance(float distance) {
		return Math.min(distance, threshold);
	}
}
