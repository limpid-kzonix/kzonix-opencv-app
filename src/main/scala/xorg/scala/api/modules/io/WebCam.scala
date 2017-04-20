package xorg.scala.api.modules.io

import akka.NotUsed
import akka.actor.{ ActorLogging, ActorSystem, DeadLetterSuppression, Props }
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{ Cancel, Request }
import akka.stream.javadsl.Source
import org.bytedeco.javacpp.opencv_core
import org.bytedeco.javacv.FrameGrabber.ImageMode
import org.bytedeco.javacv.{ Frame, FrameGrabber }
import xorg.scala.api.modules.cv.detection.utils.Dimensions

/**
 * Created by LIMPID-GEEK
 * .
 */
object WebCam {

  def source(
    deviceId: Int,
    dimensions: Dimensions,
    bitsPerPixel: Int = opencv_core.CV_8U,
    imageMode: ImageMode = ImageMode.COLOR
  )(implicit system: ActorSystem): Source[Frame, NotUsed] = {
    val props = Props(
      new WebcamFramePublisher(
        deviceId = deviceId,
        imageWidth = dimensions.width,
        imageHeight = dimensions.height,
        bitsPerPixel = bitsPerPixel,
        imageMode = imageMode
      )
    )
    val webcamActorRef = system.actorOf(props)
    val webcamActorPublisher = ActorPublisher[Frame](webcamActorRef)

    Source.fromPublisher(webcamActorPublisher)
  }

  // Building a started grabber seems finicky if not synchronised; there may be some freaky stuff happening somewhere.
  private def buildGrabber(
    deviceId: Int,
    imageWidth: Int,
    imageHeight: Int,
    bitsPerPixel: Int,
    imageMode: ImageMode
  ): FrameGrabber = synchronized {
    val g = FrameGrabber.createDefault(deviceId)
    g.setImageWidth(imageWidth)
    g.setImageHeight(imageHeight)
    g.setBitsPerPixel(bitsPerPixel)
    g.setImageMode(imageMode)
    g.start()
    g.delayedGrab(1)
    g
  }

  /**
   * Actor that backs the Akka Stream source
   */
  private class WebcamFramePublisher(
      deviceId: Int,
      imageWidth: Int,
      imageHeight: Int,
      bitsPerPixel: Int,
      imageMode: ImageMode
  ) extends ActorPublisher[Frame] with ActorLogging {

    private implicit val ec = context.dispatcher

    // Lazy so that nothing happens until the flow begins
    private lazy val grabber = buildGrabber(
      deviceId = deviceId,
      imageWidth = imageWidth,
      imageHeight = imageHeight,
      bitsPerPixel = bitsPerPixel,
      imageMode = imageMode
    )

    def receive: Receive = {
      case _: Request => emitFrames()
      case Continue => emitFrames()
      case Cancel => onCompleteThenStop()
      case unexpectedMsg => log.warning(s"Unexpected message: $unexpectedMsg")
    }

    private def emitFrames(): Unit = {
      if (isActive && totalDemand > 0) {
        /*
		  Grabbing a frame is a blocking I/O operation, so we don't send too many at once.
		 */
        grabFrame().foreach(onNext)
        if (totalDemand > 0) {
          self ! Continue
        }
      }
    }

    private def grabFrame(): Option[Frame] = {
      var frame = grabber.grab()

      Option(frame)
    }
  }

  private case object Continue extends DeadLetterSuppression

}

