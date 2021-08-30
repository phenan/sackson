package com.phenan.sackson.util

object TupleUtil {
  type Unwrap[F[_]] = [T] =>> T match {
    case F[t] => t
  }

  def tupleMapTransform[F[_], G[_], T <: Tuple](tuple: Tuple.Map[T, F])(f: [t] => F[t] => G[t]): Tuple.Map[T, G] = {
    tuple.map [[t] =>> G[Unwrap[F][t]]] { [t] => (value: t) =>
      f[Unwrap[F][t]](value.asInstanceOf[F[Unwrap[F][t]]])
    }.asInstanceOf[Tuple.Map[T, G]]
  }

  def zipSameOrigin[F[_], T <: Tuple](tuple: T, mapping: Tuple.Map[T, F]): Tuple.Map[T, [t] =>> (t, F[t])] = {
    tuple.zip(mapping).asInstanceOf[Tuple.Map[T, [t] =>> (t, F[t])]]
  }
}
