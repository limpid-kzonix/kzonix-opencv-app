package xorg.scala.api.modules.io.stream.source

import akka.stream._
import akka.stream.stage._

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Created by LIMPID-GEEK
 * .
 */

class SourceStream[Frame](switch: Future[Unit])(implicit ec: ExecutionContext) extends GraphStage[SourceShape[Frame]] {

  val out: Outlet[Frame] = Outlet("SourceStream")

  override def shape: SourceShape[Frame] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) with OutHandler {

      override def materializer: Materializer = super.materializer

      override def beforePreStart(): Unit = {
        val callback = getAsyncCallback[Unit] { (_) =>
          completeStage()
        }
        switch.foreach(callback.invoke)
      }

      override def preStart(): Unit = {

      }

      override def onPull(): Unit = {

      }

      override def postStop(): Unit = {

      }

      override def afterPostStop(): Unit = {

      }

      override def onDownstreamFinish(): Unit = {

        completeStage()
      }
    }
  }

}

