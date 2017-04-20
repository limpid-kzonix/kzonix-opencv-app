package xorg.scala.api.modules.cv.detection.cascade

import org.bytedeco.javacpp.opencv_core.Rect

/**
 * Created by LIMPID-GEEK
 * .
 */
case class Body(id: Int, description: String, bodyArea: Rect, faceArea: Rect) {

  def this(id: Int, bodyArea: Rect) = {

    this(id, "Typical body", bodyArea, null)
  }

  def this(id: Int, description: String, bodyArea: Rect) = {

    this(id, description, bodyArea, null)
  }
}