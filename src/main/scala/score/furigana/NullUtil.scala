package score.furigana

object NullUtil:
  extension (me: String)
    inline def splitnn(regex: String, limit: Int = 0): Array[String] =
      import scala.language.unsafeNulls
      me.split(regex, limit)

  extension [T](me: T | Null)
    inline def ? : Option[T] =
      if me == null then None else Some(me)
