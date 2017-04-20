package xorg.scala.api.modules.io

import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier
import xorg.scala.api.modules.{ CascadeType, HaarCascadeLoader }

/**
 * Created by LIMPID-GEEK
 * .
 */
class SourceResolver(cascadeType: CascadeType) extends HaarCascadeLoader {

  override def load(cascadeName: String): CascadeClassifier = {
    val url = SourceResolver.getClass.getResource(cascadeType.directory + cascadeName).getPath
    val haarCascade = new CascadeClassifier(url)

    haarCascade
  }
}

object SourceResolver {

  def apply(cascadeType: CascadeType): SourceResolver = new SourceResolver(cascadeType)

}
