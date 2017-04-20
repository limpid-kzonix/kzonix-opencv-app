package xorg.scala.api.modules.cv.detection.cascade

import org.bytedeco.javacpp.opencv_core.Rect

/**
 * Created by LIMPID-GEEK
 * .
 */
case class Face(id: Int, faceArea: Rect, leftEyeArea: Rect, rightEyeArea: Rect)

case class FrontalFace(id: Int, description: String, faceArea: Rect)

