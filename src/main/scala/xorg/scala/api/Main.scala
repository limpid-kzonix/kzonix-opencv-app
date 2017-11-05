package xorg.scala.api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.bytedeco.javacv.CanvasFrame
import xorg.scala.api.modules.cv.detection.utils.{ Dimensions, Flip, MediaConversion }
import xorg.scala.api.modules.io.WebCam

/**
 * Created by LIMPID-GEEK
 * .
 */
object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val canvas = new CanvasFrame("WebCam")
  canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)

  val imageDimensions = Dimensions(width = 640, height = 480)
  val webCamSource = WebCam.source(deviceId = 0, dimensions = imageDimensions)

  val graph = webCamSource
    .map(MediaConversion.toMat) // most OpenCV manipulations require a Matrix
    .map(Flip.horizontal)

    .map(MediaConversion.toFrame)
    .map(frame => {
      canvas.showImage(frame)
    })
    .to(Sink.ignore)

  graph.run(materializer)
}
