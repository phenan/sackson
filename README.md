# sackson

High level Jackson wrapper in Scala3.

## Usage

```scala
import com.phenan.sackson.scheme._

val myScheme = struct {
  required("b1", boolean) *: 
  optional("b2", boolean) *:
  nil
}

val parser = myScheme.parser
val printer = myScheme.printer
```

## Author

[@phenan](https://twitter.com/phenan)
