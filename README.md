# sackson

Sackson generates statically typed JSON parser and printer from JSON scheme defined in Scala without macros.

## Usage

```scala
import com.phenan.sackson.scheme._

val myScheme = struct {
  required("b1", boolean) *: 
  optional("b2", boolean) *:
  nil
}

val parsed = myScheme.parser.parseString("""{"b1":true,"b2":false}""")  // Right((true, Some(false)))
val printed = myScheme.printer.printString((true, Some(false)))         // {"b1":true,"b2":false}
```

## Author

[@phenan](https://twitter.com/phenan)
