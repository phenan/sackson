package com.phenan.sackson.util

import org.scalatest.wordspec.AnyWordSpec

class TupleUtilTest extends AnyWordSpec {
  "tupleMapTransform" should {
    "work" in {
      val tuple: Tuple.Map[(Int, String), Option] = (Some(10), None)
      val listed = TupleUtil.tupleMapTransform[(Int, String), Option, List](tuple) { [t] => (opt: Option[t]) => opt.toList }
      assert(listed == (List(10), Nil))
    }
  }
}
