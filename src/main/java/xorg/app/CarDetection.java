package xorg.app;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.BackgroundSubtractorMOG2;
import static org.bytedeco.javacpp.opencv_video.createBackgroundSubtractorMOG2;
import static org.bytedeco.javacpp.opencv_videoio.CV_CAP_ANY;

/**
 * Created by LIMPID-GEEK
 * .
 */
public class CarDetection {


	private static final int DELAY = 100;    // ms

	private static final int MIN_PIXELS = 100;
	// minimum number of non-black pixels needed for COG calculation


	private static CvMemStorage contourStorage;


	public static void main( String[] args ) throws Exception {
		// Preload the opencv_objdetect module to work around a known
		Loader.load( opencv_objdetect.class );

		contourStorage = CvMemStorage.create( );

		System.out.println( "Initializing frame grabber..." );
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber( CV_CAP_ANY );
		OpenCVFrameConverter toIplImage = new OpenCVFrameConverter.ToIplImage();
		OpenCVFrameConverter toMat = new OpenCVFrameConverter.ToMat();

		grabber.start( );

		IplImage grab = toIplImage.convertToIplImage( grabber.grab( ) );
		int width = grab.width( );
		int height = grab.height( );

		IplImage fgMask = IplImage.create( width, height, IPL_DEPTH_8U, 1 );
	          /* b&w version of the grabbed image, with movement shown in white,
                 the rest in black, based on the BackgroundSubtractorMOG2 algorithm */

		IplImage background = IplImage.create( width, height, IPL_DEPTH_8U, 3 );
              /* the background of the grabbed image as determined by the
                 BackgroundSubtractorMOG2 algorithm */

		CanvasFrame grabCanvas = new CanvasFrame( "Camera" );
		grabCanvas.setLocation( 0, 0 );
		CanvasFrame mogCanvas = new CanvasFrame( "MOG Info" );
		mogCanvas.setLocation( width + 5, 0 );

		BackgroundSubtractorMOG2 mog = createBackgroundSubtractorMOG2( 300, 16, false );
		// a larger motion history tends to create larger blobs
		// motion history, var Threshold, Shadow Detection
		mog.setNMixtures( 3 );    // was 5
		System.out.println( "MOG num. mixtures: " + mog.getNMixtures() );
		System.out.println( "MOG shadow detection: " + mog.getDetectShadows( ) );
		// in OpenCV version 2.4.2 only detectShadows, history and nmixtures can be set/get;
		// fixed in v.2.4.6
         /* other params: backgroundRatio, varThresholdGen,
                          fVarInit, fVarMin, fVarMax, etc
            explained (a little) in opencv\build\include\opencv2\video\background_segm.hpp
         */
		try {
			System.out.println( "MOG background ratio: " + mog.getBackgroundRatio( ) );
			System.out.println( "MOG var threshold gen: " + mog.getVarThresholdGen(  ) );
			System.out.println( "MOG fVar init: " + mog.getVarInit(  ) + ", min: " +
					mog.getVarMin(  ) + ", max: " + mog.getVarMax(  ) );
		} catch ( RuntimeException e ) {
			System.out.println( e );
		}

		// process the grabbed camera image
		while ( grabCanvas.isVisible( ) && mogCanvas.isVisible( ) ) {
			long startTime = System.currentTimeMillis( );
			grab = toIplImage.convertToIplImage( grabber.grab( ) );
			if ( grab == null ) {
				System.out.println( "Image grab failed" );
				break;
			}

			//  create a binary mask of foreground objects (and update the background)
			mog.apply( toMat.convertToMat(  toMat.convert( grab )) , toMat.convertToMat( toMat.convert( fgMask ) ), 0.005 );
// -1);
			// learning rate; set close to 0 if initial background varies little
			// so start out with only the background and wait a few seconds
			mog.getBackgroundImage( toMat.convertToMat(  toMat.convert( background ) ) );

			// opening: erosion then dilation to reduce noise
			cvErode( fgMask, fgMask, null, 5 );
			cvDilate( fgMask, fgMask, null, 5 );
			cvSmooth( fgMask, fgMask, CV_BLUR, 5, 0, 0, 0 );  // more noise reduction
			cvThreshold( fgMask, fgMask, 128, 255, CV_THRESH_BINARY );   // make b&w

			mogCanvas.showImage( toMat.convert( fgMask ) );
			// mogCanvas.showImage(background);

			Point pt = findCOG( fgMask );
			if ( pt != null )    // only update COG point if there is a new point
				cvCircle( grab, cvPoint( pt.x(), pt.y() ), 16,      // radius of circle
						CvScalar.BLUE, CV_FILLED, CV_AA, 0 );
			grabCanvas.showImage( toMat.convert( grab ) );

			long duration = System.currentTimeMillis( ) - startTime;
			System.out.println( "Processing time: " + duration );
			if ( duration < DELAY ) {
				try {
					Thread.sleep( DELAY - duration );
				} catch ( InterruptedException e ) {}
			}
		}

		grabber.stop( );
		grabCanvas.dispose( );
		mogCanvas.dispose( );
	}  // end of main()


	private static Point findCOG( IplImage maskImg )
  /*  If there are enough non-black pixels in the mask image
      (white means movement), then calculate the moments,
      and use them to calculate the (x,y) center of the white areas.
      These values are returned as a Point object. */ {

		Point pt = null;

		int numPixels = cvCountNonZero( maskImg );   // non-zero (non-black) means motion
		if ( numPixels > MIN_PIXELS ) {
			CvMoments moments = new CvMoments( );
			cvMoments( maskImg, moments, 1 );    // 1 == treat image as binary (0,255) --> (0,1)
			double m00 = cvGetSpatialMoment( moments, 0, 0 );
			double m10 = cvGetSpatialMoment( moments, 1, 0 );
			double m01 = cvGetSpatialMoment( moments, 0, 1 );

			if ( m00 != 0 ) {   // create COG Point
				int xCenter = ( int ) Math.round( m10 / m00 );
				int yCenter = ( int ) Math.round( m01 / m00 );
				pt = new Point( xCenter, yCenter );
			}
		}
		return pt;
	}  // end of findCOG()


}  // end of MogCog class