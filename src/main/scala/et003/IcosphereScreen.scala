package et003

import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.{Gdx, InputMultiplexer}
import com.badlogic.gdx.graphics.{Camera, Color, GL20, PerspectiveCamera}
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.{Environment, Material, ModelBatch, ModelInstance}
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.{Matrix4, Vector3}
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Table}
import com.badlogic.gdx.utils.viewport.ScreenViewport


class IcosphereScreen extends UIScreen {
  private val backgroundColor = Color.BLACK
  private val textColor = new Color(0xDAA520FF)
  private val shadowColor = Color.DARK_GRAY

  private val modelBatch = new ModelBatch()

  private var fbPixels: FrameBuffer = null
  private val batch = new SpriteBatch()


  // Generate FreeType fonts
  val generator: FreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/Crimson-Roman.ttf"))
  val parameter: FreeTypeFontGenerator.FreeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter
  parameter.size = 150
  parameter.characters = "Engi Test03"
  parameter.borderWidth = 0
  parameter.color = textColor
  parameter.shadowOffsetX = 5
  parameter.shadowOffsetY = 5
  parameter.shadowColor = shadowColor
  private val smallFont = generator.generateFont(parameter)
  parameter.size = 400
  private val bigFont = generator.generateFont(parameter)
  generator.dispose()

  val stage = new Stage(new ScreenViewport())
  resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
  Gdx.graphics.setTitle("Engine Test 003")

  val icosphere = Triangulation.triangulateIcoSphere(1)
  print(icosphere.length)

  private val modelBuilder = new ModelBuilder()
  private val material = new Material()
  modelBuilder.begin()
  val builder = modelBuilder.part(
    s"icosphere",
    GL20.GL_TRIANGLES,
    //GL20.GL_LINES,   // wireframe
    Usage.Position | Usage.ColorPacked | Usage.Normal,
    material
  )
  val c0 = Color.PURPLE
  for (triangles <- icosphere) {
    for (triangle <- triangles.triangles) {
      val normal = new Vector3(triangle.centroid).nor()
      val i1 = builder.vertex(triangle.bounds.head, normal, c0, null)
      val i2 = builder.vertex(triangle.bounds(1), normal, c0, null)
      val i3 = builder.vertex(triangle.bounds(2), normal, c0, null)
      builder.triangle(i1, i2, i3)
    }
  }
  val instance = new ModelInstance(modelBuilder.end())

  // setup environment
  val environment = new Environment()
  environment.set(
    new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f)
  )
  private val directionalLight =
    new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f)
  environment.add(directionalLight)

  private val cam = createCamera()

  private val sphericalController =
    new SphericalCameraController(cam, new Vector3(0, 0, 0))

  private val multiplexer = new InputMultiplexer()
  multiplexer.addProcessor(sphericalController)

  val font = new BitmapFont()

  /** Called when this screen becomes the current screen. */
  def show(): Unit = {
    Gdx.input.setInputProcessor(multiplexer)
  }

  /** Called when the screen should render itself.
   *
   * @param delta The time in seconds since the last render. */
  def render(delta: Float): Unit = {
    if (fbPixels == null) {
      fbPixels = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth / 8, Gdx.graphics.getHeight / 8, true)
      val matrix = new Matrix4
      matrix.setToOrtho2D(0, 0, fbPixels.getWidth, fbPixels.getHeight)
      batch.setProjectionMatrix(matrix)
    }
    fbPixels.begin()
    clearWithColor(backgroundColor)
    sphericalController.update(delta)
    instance.transform.rotate(Vector3.Z, 2f * delta)
    modelBatch.begin(cam)
    modelBatch.render(instance, environment)
    modelBatch.end()
    fbPixels.end()
    batch.begin()
    batch.setColor(1f, 1f, 1f, 1f)
    val t = fbPixels.getColorBufferTexture
    t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
    batch.draw(t, 0, 0)
    font.draw(batch, Gdx.graphics.getFramesPerSecond + " fps", 10, 10)
    batch.end()
  }

  def resize(width: Int, height: Int): Unit = {
    stage.clear()
    val rootTable = new Table()
    rootTable.setFillParent(true)
    val smallLabel = new Label("Engine Test", new Label.LabelStyle() { font = smallFont })
    smallLabel.setPosition(width / 2 - (smallLabel.getPrefWidth / 2), height - (smallLabel.getPrefHeight / 2))
    rootTable.add(smallLabel)
    rootTable.row()
    val bigLabel = new Label("003", new Label.LabelStyle() { font = bigFont })
    bigLabel.setPosition(width / 2 - (bigLabel.getPrefWidth / 2), height - (bigLabel.getPrefHeight / 2))
    rootTable.add(bigLabel)
    stage.addActor(rootTable)
    stage.getViewport.update(width, height, true)
  }

  def pause(): Unit = {

  }

  def resume(): Unit = {

  }

  /** Called when this screen is no longer the current screen. */
  def hide(): Unit = {

  }

  /** Called when this screen should release all resources. */
  def dispose(): Unit = {

  }

  def createCamera(): Camera = {
    new PerspectiveCamera(
      30,
      Gdx.graphics.getWidth.toFloat,
      Gdx.graphics.getHeight.toFloat
    ) {
      position.set(0f, 5.0f, 0f)
      lookAt(0f, 0f, 0f)
      rotate(Vector3.Y, 180f) // results in up being positive Vector3.Z
      near = 0.01f
      far = 300f
      update(true)
    }
  }


}
