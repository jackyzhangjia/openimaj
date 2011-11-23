package org.openimaj.image.feature.local.engine;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.FImage;
import org.openimaj.image.feature.local.descriptor.gradient.IrregularBinningSIFTFeatureProvider;
import org.openimaj.image.feature.local.detector.dog.collector.Collector;
import org.openimaj.image.feature.local.detector.dog.collector.OctaveKeypointCollector;
import org.openimaj.image.feature.local.detector.dog.extractor.GradientFeatureExtractor;
import org.openimaj.image.feature.local.detector.dog.extractor.NullOrientationExtractor;
import org.openimaj.image.feature.local.detector.dog.pyramid.BasicOctaveExtremaFinder;
import org.openimaj.image.feature.local.detector.dog.pyramid.DoGOctaveExtremaFinder;
import org.openimaj.image.feature.local.detector.dog.pyramid.OctaveInterestPointFinder;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.processing.pyramid.gaussian.GaussianOctave;
import org.openimaj.image.processing.pyramid.gaussian.GaussianPyramid;

public class ALTDoGSIFTEngine extends DoGSIFTEngine {
	@Override
	public LocalFeatureList<Keypoint> findFeatures(FImage image) {
		OctaveInterestPointFinder<GaussianOctave<FImage>, FImage> finder = 
			new DoGOctaveExtremaFinder(new BasicOctaveExtremaFinder(options.magnitudeThreshold, options.eigenvalueRatio));
		
		Collector<GaussianOctave<FImage>, Keypoint, FImage> collector = new OctaveKeypointCollector(
				new GradientFeatureExtractor(
						new NullOrientationExtractor(),
						new IrregularBinningSIFTFeatureProvider(options.numOriBins, options.valueThreshold), 
						options.magnificationFactor * options.numSpatialBins
				));
		
		finder.setOctaveInterestPointListener(collector);
		
		options.setOctaveProcessor(finder);
		
		GaussianPyramid<FImage> pyr = new GaussianPyramid<FImage>(options);
		pyr.process(image);
		
		return collector.getFeatures();
	}
}