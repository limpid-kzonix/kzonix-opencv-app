package xorg.scala.api.modules.cv.detection.utils

import java.util.function.Supplier

import org.bytedeco.javacpp.opencv_core.Mat
import org.bytedeco.javacv.{ Frame, OpenCVFrameConverter }

/**
 * Created by LIMPID-GEEK
 * .
 */
object MediaConversion {

  private val converter = ThreadLocal.withInitial(new Supplier[OpenCVFrameConverter.ToMat] {

    override def get(): OpenCVFrameConverter.ToMat = new OpenCVFrameConverter.ToMat
  })

  /**
   * Returns an OpenCV Mat for a given JavaCV frame
   */
  def toMat(frame: Frame): Mat = converter.get().convert(frame)

  /**
   * Returns a JavaCV Frame for a given OpenCV Mat
   */
  def toFrame(mat: Mat): Frame = converter.get().convert(mat)
}
