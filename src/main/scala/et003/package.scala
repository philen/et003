import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.{Application, Gdx, Screen}
import monix.catnap.MVar
import monix.eval.Task
import monix.execution.Callback

import scala.concurrent.duration.FiniteDuration


package object et003 {

  type AppChannel = MVar[Task, Application]

  trait UIScreen extends Screen

  /** requires an execution context where imported */
  val defaultAsyncHandler: Callback[Throwable, Unit] = new Callback[Throwable, Unit] {
    def onSuccess(value: Unit): Unit = ()
    def onError(e: Throwable): Unit = {
      println("defaultAsyncHandler caught an exception:")
      e.printStackTrace()
    }
  }

  implicit class RetryableTask[A](val task: Task[A]) extends AnyVal {
    /** Add retries with exponential back-off to a Task
     * @param retries number of retries remaining
     * @param delay delay for the next retry
     * @return
     */
    def addRetries(retries: Int,
                   delay: FiniteDuration): Task[A] = {
      task.onErrorHandleWith {
        case ex: Exception =>
          if (retries > 0)
          // Recursive call, it's OK as Monix is stack-safe
          task.addRetries(retries - 1, delay * 2)
            .delayExecution(delay)
          else
          Task.raiseError(ex)
      }
    }
  }

  def clearWithColor(c: Color): Unit = {
    //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth / 8, Gdx.graphics.getHeight / 8)
    Gdx.gl.glClearColor(c.r, c.g, c.b, c.a)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)
  }

}
