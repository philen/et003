package et003

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class IcoSphereRegionSpec extends AnyFlatSpec with Matchers {

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
}