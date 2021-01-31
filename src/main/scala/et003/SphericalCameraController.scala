package et003

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

case class CameraConfig(
                         center: Vector3 = new Vector3(0, 0, 0),
                         height: Float = 5.0f,
                         minHeight: Float = 1.05f,
                         maxHeight: Float = 6.0f,
                         rotateSpeed: Int = 40,
                         rotateUp: Int = Keys.W,
                         rotateDown: Int = Keys.S,
                         rotateLeft: Int = Keys.A,
                         rotateRight: Int = Keys.D,
                         moveIn: Int = Keys.E,
                         moveOut: Int = Keys.Q,
                       )

/** Camera controller that faces and orbits around a focus
 *
 * @param camera the camera to control
 * @param focus the focus of the camera
 */
class SphericalCameraController(camera: Camera,
                                focus: Vector3)
  extends InputProcessor {

  private val config: CameraConfig = CameraConfig()

  private var height = config.height
  private var movingIn = false
  private var movingOut = false

  private var horizontalSpeed = 0f
  private var horizontalAcceleration = 0f
  private var verticalSpeed = 0f
  private var verticalAcceleration = 0f

  private val heightRange = config.maxHeight - config.minHeight
  val rotationAcceleration = 5f
  val rotationFriction = 0.8f
  val maxSpeed = 320f

  override def keyDown(keycode: Int): Boolean = {
    println(keycode)
    keycode match {
      case config.rotateUp =>
        verticalAcceleration += rotationAcceleration
        true
      case config.rotateDown =>
        verticalAcceleration -= rotationAcceleration
        true
      case config.rotateLeft =>
        horizontalAcceleration -= rotationAcceleration
        true
      case config.rotateRight =>
        horizontalAcceleration += rotationAcceleration
        true
      case config.moveIn =>
        movingIn = true
        true
      case config.moveOut =>
        movingOut = true
        true
      case _ => false
    }
  }

  override def keyUp(keycode: Int): Boolean = {
    keycode match {
      case config.rotateUp =>
        verticalAcceleration -= rotationAcceleration
        true
      case config.rotateDown =>
        verticalAcceleration += rotationAcceleration
        true
      case config.rotateLeft =>
        horizontalAcceleration += rotationAcceleration
        true
      case config.rotateRight =>
        horizontalAcceleration -= rotationAcceleration
        true
      case config.moveIn =>
        movingIn = false
        true
      case config.moveOut =>
        movingOut = false
        true
      case _ => false
    }
  }

  override def keyTyped(character: Char): Boolean = false

  def update(delta: Float): Unit = {
    val ratio = Math.max((height - config.minHeight) / heightRange, 0.01f)
    val maxSpeedRatio = maxSpeed * ratio

    if (horizontalAcceleration != 0f) {
      horizontalSpeed += horizontalAcceleration
    } else {
      horizontalSpeed = horizontalSpeed * rotationFriction
    }
    horizontalSpeed = (-maxSpeedRatio) max horizontalSpeed min maxSpeedRatio
    camera.position.rotate(camera.up, delta * horizontalSpeed)

    if (verticalAcceleration != 0f) {
      verticalSpeed += verticalAcceleration
    } else {
      verticalSpeed = verticalSpeed * rotationFriction
    }
    val axis = new Vector3(camera.position.sub(focus)).crs(camera.up)
    verticalSpeed = (-maxSpeedRatio) max verticalSpeed min maxSpeedRatio
    camera.position.rotate(axis, delta * verticalSpeed)

    if (movingIn) {
      val half = Math.max((height - config.minHeight) / 30, 0.01f)
      height = Math.max(height - half, config.minHeight)
      camera.position.set(camera.position.sub(focus).setLength(height))
    }
    if (movingOut) {
      val half = Math.max((height - config.minHeight) / 30, 0.01f)
      height = Math.min(height + half, config.maxHeight)
      camera.position.set(camera.position.sub(focus).setLength(height))
    }

    camera.lookAt(focus)
    camera.update()
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = false

  override def scrolled(amountX: Float, amountY: Float): Boolean = false
}