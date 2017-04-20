package xorg.scala.api.modules

import xorg.scala.api.modules.ResolverType.ResolverType

/**
 * Created by LIMPID-GEEK
 * .
 */
object ResolverType extends Enumeration {

  type ResolverType = Value
  val HAAR, CUDA_HAAR, FLAND_MARK, HOG, LBP = Value
}

case class CascadeType(resolverType: ResolverType) {

  lazy val directory: String = {
    resolverType match {
      case ResolverType.LBP => "detection/lbpcascades/"
      case ResolverType.CUDA_HAAR => "detection/haarcascadescuda/"
      case ResolverType.HAAR => "detection/haarcascades/"
      case ResolverType.FLAND_MARK => "detection/flandmark/"
      case ResolverType.HOG => "detection/hogcascades/"
    }
  }
}

