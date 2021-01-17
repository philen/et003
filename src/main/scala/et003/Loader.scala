package et003

import com.badlogic.gdx.{Application, Gdx}
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode
import monix.catnap.MVar
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global


object Loader {

  private val appChannel: AppChannel = MVar[Task].empty[Application]().runSyncUnsafe()

  def register(app: Application): Unit = {
    appChannel.put(Gdx.app).runAsync(defaultAsyncHandler)
  }

  def load(ui: UIListener): Lwjgl3ApplicationConfiguration = {
    appChannel.take.runAsync { result =>
      result match {
        case Left(err) => throw err
        case Right(app) => app.postRunnable(() => ui.replace(new IcosphereScreen()))
    }}

    // TODO load from file in a Task
    new Lwjgl3ApplicationConfiguration() {
      setTitle("Engine Test 003")
      setWindowPosition(-1, -1)
      setWindowedMode(900, 900)
      setBackBufferConfig(8, 8, 8, 8, 16, 0, 4)
      setHdpiMode(HdpiMode.Pixels)
      setWindowSizeLimits(800, 600, 9999, 9999)
      useOpenGL3(false, 3, 2)
    }
  }

}
