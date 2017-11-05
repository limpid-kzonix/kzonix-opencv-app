package xorg.scala.api.modules.cv.detection.utils

import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgproc

/**
 * Created by LIMPID-GEEK
 * .
 */
object Flip {

  def horizontal(mat: Mat): Mat = {
    flip(mat, mat, 1)
    mat
  }

  def gray(mat: Mat): Mat = {
    val greyMat = new Mat(640, 480, CV_8U)
    opencv_imgproc.cvtColor(mat, greyMat, opencv_imgproc.CV_BGR2GRAY, 2)
    opencv_imgproc.equalizeHist(greyMat, greyMat)
    greyMat
  }

  def color(mat: Mat): Mat = {
    val greyMat = new Mat(640, 480, CV_AUTO_STEP)
    opencv_imgproc.cvtColor(mat, greyMat, opencv_imgproc.CV_BGR2HSV, 1)
    opencv_imgproc.equalizeHist(greyMat, greyMat)
    greyMat
  }
  def grayBayerScale(mat: Mat): Mat = {
    val greyMat = new Mat(640, 480, CV_8U)
    opencv_imgproc.cvtColor(mat, greyMat, opencv_imgproc.CV_BGR2GRAY, 2)
    opencv_imgproc.equalizeHist(greyMat, greyMat)
    greyMat
  }
}
