package xorg.scala.api.modules.io

import javax.swing.JFrame

import org.bytedeco.javacv.CanvasFrame

/**
 * Created by LIMPID-GEEK
 * .
 */
class CanvasOutput(name: String) {

  val title: String = name
  val canvas = new CanvasFrame(title)

  def this() = {

    this("DEFAULT")
  }

  canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  def get: CanvasFrame = {

    canvas
  }
}


