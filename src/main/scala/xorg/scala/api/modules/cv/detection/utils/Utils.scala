package xorg.scala.api.modules.cv.detection.utils

import org.bytedeco.javacpp.opencv_core.{ Rect, _ }
import org.bytedeco.javacv.FrameGrabber.ImageMode
import org.bytedeco.javacv.OpenCVFrameGrabber

/**
 * Created by LIMPID-GEEK
 * .
 */
object Utils {

  private[detection] def cloneArea(area: Rect): Rect = {

    new Rect(area.x, area.y, area.width, area.height)
  }

  private[detection] def getGrabber(
    deviceId: Int,
    imageWidth: Int,
    imageHeight: Int,
    bitsPerPixel: Int,
    imageMode: ImageMode
  ): OpenCVFrameGrabber = synchronized {
    val grabber = new OpenCVFrameGrabber(deviceId)

    grabber.setImageWidth(imageWidth)
    grabber.setImageHeight(imageHeight)
    grabber.setBitsPerPixel(bitsPerPixel)
    grabber.setImageMode(imageMode)
    grabber.start()

    grabber
  }

  def getGraber(deviceId: Int, dimensions: Dimensions): OpenCVFrameGrabber = synchronized {

    val grabber = getGrabber(deviceId, dimensions.width, dimensions.height, CV_8U, ImageMode.COLOR)

    grabber
  }

}
