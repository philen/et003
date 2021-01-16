package et003

import com.badlogic.gdx.math.Vector3
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TriangulationSpec extends AnyFlatSpec with Matchers {

  def countTriangulation(t: Triangulation.Triangulation): Int = {
    t match {
      case Triangulation.Triangle(_, _, _) => 1
      case Triangulation.Triangles(a, b, c, d) =>
        countTriangulation(a) + countTriangulation(b) + countTriangulation(c) + countTriangulation(d)
    }
  }

  def countIcoSphere(i: Seq[Triangulation.Triangulation]): Int = {
    i.map(countTriangulation).sum
  }

  "Number of triangles" should "equal 20*4^depth" in {
    for (depth <- 0 to 8) {
      assert(countIcoSphere(Triangulation.triangulateIcoSphere(depth)) == (20 * Math.pow(4, depth)))
    }
  }

  "stableMidpoint" should "return same result regardless of argument order" in {
    val a = new Vector3(1f, 2f, 3f)
    val b = new Vector3(4f, 5f, 6f)
    assert(Triangulation.stableMidpoint(a, b).x == Triangulation.stableMidpoint(b, a).x)
    assert(Triangulation.stableMidpoint(a, b).y == Triangulation.stableMidpoint(b, a).y)
    assert(Triangulation.stableMidpoint(a, b).z == Triangulation.stableMidpoint(b, a).z)
  }
}