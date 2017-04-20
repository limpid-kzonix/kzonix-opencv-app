package xorg.scala.api.modules

import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier

/**
 * Created by LIMPID-GEEK
 * .
 */
trait HaarCascadeLoader {
  def load(haarCascadeName: String): CascadeClassifier
}
