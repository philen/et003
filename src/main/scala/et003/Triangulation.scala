package et003

import com.badlogic.gdx.math.Vector3

/** hierarchy-preserving triangulation functions */
object Triangulation {
  val epsilon = 0.00001f
  private val t = ((1.0 + Math.sqrt(5.0)) / 2.0).toFloat
  private val vertices = Array(
    new Vector3(-1, t, 0).setLength(1),
    new Vector3(1, t, 0).setLength(1),
    new Vector3(-1, -t, 0).setLength(1),
    new Vector3(1, -t, 0).setLength(1),
    new Vector3(0, -1, t).setLength(1),
    new Vector3(0, 1, t).setLength(1),
    new Vector3(0, -1, -t).setLength(1),
    new Vector3(0, 1, -t).setLength(1),
    new Vector3(t, 0, -1).setLength(1),
    new Vector3(t, 0, 1).setLength(1),
    new Vector3(-t, 0, -1).setLength(1),
    new Vector3(-t, 0, 1).setLength(1)
  )
  private val indices: Array[Short] =
    Array(0, 11, 5, 0, 5, 1, 0, 1, 7, 0, 7, 10, 0, 10, 11, 1, 5, 9, 5, 11, 4,
      11, 10, 2, 10, 7, 6, 7, 1, 8, 3, 9, 4, 3, 4, 2, 3, 2, 6, 3, 6, 8, 3, 8, 9,
      4, 9, 5, 2, 4, 11, 6, 2, 10, 8, 6, 7, 9, 8, 1)

  def vectorGreaterThanOrEqual(a: Vector3, b: Vector3): Boolean = {
    if ((a.x - b.x).abs > epsilon) {
      a.x > b.x
    } else if ((a.y - b.y).abs > epsilon) {
      a.y > b.y
    } else if ((a.z - b.z).abs > epsilon) {
      a.z > b.z
    } else {
      true
    }
  }

  def stableMidpoint(p1: Vector3, p2: Vector3): Vector3 = {
    //    val v1 = new Vector3(p1).lerp(p2, 0.5f)
    //    val v2 = new Vector3(p2).lerp(p1, 0.5f)
    //    assert(v1.dst(v2) < 0.0000001f)
    //    assert(v2.dst(v1) < 0.0000001f)
    if (vectorGreaterThanOrEqual(p1, p2)) {
      new Vector3(p1).lerp(p2, 0.5f)
    } else {
      new Vector3(p2).lerp(p1, 0.5f)
    }
  }

  sealed trait Triangulation {
    def bounds: List[Vector3]

    def triangles: List[Triangle]
  }

  case class Triangle(p1: Vector3, p2: Vector3, p3: Vector3)
    extends Triangulation {
    def bounds = List(p1, p2, p3)

    def triangles = List(this)

    def centroid = new Vector3(
      (p1.x + p2.x + p3.x) / 3f,
      (p1.y + p2.y + p3.y) / 3f,
      (p1.z + p2.z + p3.z) / 3f
    )
  }

  case class Triangles(a: Triangulation,
                       b: Triangulation,
                       c: Triangulation,
                       d: Triangulation)
    extends Triangulation {
    def bounds = a.bounds

    def triangles = a.triangles ++ b.triangles ++ c.triangles ++ d.triangles
  }

  def triangulateIcoSphere(depth: Int): Seq[Triangulation] = {
    (0 until 20).map { i =>
      triangulate(
        depth,
        vertices(indices(i * 3)).setLength(1),
        vertices(indices(i * 3 + 1)).setLength(1),
        vertices(indices(i * 3 + 2)).setLength(1)
      )
    }
  }

  def triangulate(depth: Int,
                p1: Vector3,
                p2: Vector3,
                p3: Vector3): Triangulation = {
    if (depth == 0) {
      Triangle(p1, p2, p3)
    } else {
      val m1 = stableMidpoint(p1, p2)
      val m2 = stableMidpoint(p2, p3)
      val m3 = stableMidpoint(p3, p1)
      Triangles(
        triangulate(depth - 1, p1, m1, m3),
        triangulate(depth - 1, p2, m2, m1),
        triangulate(depth - 1, p3, m3, m2),
        triangulate(depth - 1, m1, m2, m3)
      )
    }
  }

}