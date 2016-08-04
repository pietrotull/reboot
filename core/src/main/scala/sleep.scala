package dispatch

import org.jboss.netty.util.{TimerTask, Timeout, Timer}
import scala.concurrent.{ExecutionContext}
import scala.concurrent.duration.Duration
import java.util.{concurrent => juc}

object SleepFuture {
  def apply[T](d: Duration)(todo: => T)
              (implicit timer: Timer,
               executor: ExecutionContext) = {
    val promise = scala.concurrent.Promise[T]()

    val sleepTimeout = timer.newTimeout(new TimerTask {
      def run(timeout: Timeout) = {
        promise.complete(util.Try(todo))
        ()
      }
    }, d.length, d.unit)

    promise.future
  }
}
