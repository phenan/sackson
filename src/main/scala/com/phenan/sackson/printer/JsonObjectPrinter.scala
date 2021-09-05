package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath
import com.phenan.sackson.util.TupleUtil

class JsonObjectPrinter [T <: Tuple] (fields: Tuple.Map[T, JsonFieldPrinter]) extends JsonPrinter[T] {
  override def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): T => Unit = { tuple =>
    generator.writeStartObject()

    TupleUtil.tupleMapTransform [T, [t] =>> (t, JsonFieldPrinter[t]), [t] =>> Unit] (TupleUtil.zipSameOrigin(tuple, fields)) { [t] => (pair: (t, JsonFieldPrinter[t])) =>
      pair._2.run(generator, path, options)(pair._1)
    }

    generator.writeEndObject()
  }
}
