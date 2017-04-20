package xorg.scala.api.modules.io.stream.source

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStageLogic, GraphStageWithMaterializedValue, InHandler, OutHandler}

import scala.concurrent.{Future, Promise}

/**
	* Created by LIMPID-GEEK
	* .
	*/
class FlowStream[A] extends GraphStageWithMaterializedValue[FlowShape[A, A], Future[A]] {

	val in: Inlet[A] = Inlet[A]("FirstValue.in")
	val out: Outlet[A] = Outlet[A]("FirstValue.out")

	val shape: FlowShape[A, A] = FlowShape.of(in, out)

	override def createLogicAndMaterializedValue(inheritedAttributes: Attributes): (GraphStageLogic, Future[A]) = {
		val promise = Promise[A]()
		val logic = new GraphStageLogic(shape) {

			setHandler(in, new InHandler {
				override def onPush(): Unit = {
					val elem = grab(in)
					promise.success(elem)
					push(out, elem)

					// replace handler with one just forwarding
					setHandler(in, new InHandler {
						override def onPush(): Unit = {
							push(out, grab(in))
						}
					})
				}
			})

			setHandler(out, new OutHandler {
				override def onPull(): Unit = {
					pull(in)
				}
			})

		}
		(logic, promise.future)
	}
}
