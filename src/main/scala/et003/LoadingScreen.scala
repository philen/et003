package et003

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Table}
import com.badlogic.gdx.utils.viewport.ScreenViewport


class LoadingScreen extends UIScreen {
  private val backgroundColor = new Color(0x604087FF)
  private val textColor = new Color(0xDAA520FF)
  private val shadowColor = Color.DARK_GRAY

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

  val icosphere = Triangulation.triangulateIcoSphere(4)
  print(icosphere.length)

  /** Called when this screen becomes the current screen. */
  def show(): Unit = {
  }

  /** Called when the screen should render itself.
   *
   * @param delta The time in seconds since the last render. */
  def render(delta: Float): Unit = {
    clearWithColor(backgroundColor)
    stage.draw()
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

}
