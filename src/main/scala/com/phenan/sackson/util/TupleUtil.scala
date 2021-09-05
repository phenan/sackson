package com.phenan.sackson.util

object TupleUtil {
  type Unwrap[F[_]] = [T] =>> T match {
    case F[t] => t
  }

  def tupleMapTransform[T <: Tuple, F[_], G[_]](tuple: Tuple.Map[T, F])(f: [t] => F[t] => G[t]): Tuple.Map[T, G] = {
    tuple.map [[t] =>> G[Unwrap[F][t]]] { [t] => (value: t) =>
      f[Unwrap[F][t]](value.asInstanceOf[F[Unwrap[F][t]]])
    }.asInstanceOf[Tuple.Map[T, G]]
  }

  def zipSameOrigin[T <: Tuple, F[_]](tuple: T, mapping: Tuple.Map[T, F]): Tuple.Map[T, [t] =>> (t, F[t])] = {
    tuple.zip(mapping).asInstanceOf[Tuple.Map[T, [t] =>> (t, F[t])]]
  }
}
