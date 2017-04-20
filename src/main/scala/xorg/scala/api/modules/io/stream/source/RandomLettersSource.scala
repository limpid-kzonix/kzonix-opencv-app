package xorg.scala.api.modules.io.stream.source

import akka.dispatch.forkjoin.ThreadLocalRandom
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler, StageLogging}

/**
	* Created by LIMPID-GEEK
	* .
	*/
final class RandomLettersSource extends GraphStage[SourceShape[String]] {
	val out: Outlet[String] = Outlet[String]("RandomLettersSource.out")
	override val shape: SourceShape[String] = SourceShape(out)

	override def createLogic(inheritedAttributes: Attributes) =
		new GraphStageLogic(shape) with StageLogging {
			setHandler(out, new OutHandler {
				override def onPull(): Unit = {
					val c = nextChar() // ASCII lower case letters

					// `log` is obtained from materializer automatically (via StageLogging)
					log.debug("Randomly generated: [{}]", c)

					push(out, c.toString)
				}
			})
		}

	def nextChar(): Char =
		ThreadLocalRandom.current().nextInt('a', 'z'.toInt + 1).toChar
}
