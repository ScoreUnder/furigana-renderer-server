package score.furigana

import NullUtil.*

object FuriganaParser:
  private val FURI_PATTERN = raw"[｛{](?<left>[^：:]*)[：:](?<right>[^｝}]*)[｝}]|(?<other>[^{｛]+)".r

  def apply(args: String): Seq[(String, String)] =
    FURI_PATTERN
      .findAllMatchIn(args)
      .flatMap { m =>
        val other: String | Null = m.group("other")
        if other == null then Seq((m.group("left"), m.group("right")))
        else other.splitnn("\n", -1).flatMap(line => Seq(("\n", ""), (line, ""))).tail
      }
      .filter(t => !t._1.isEmpty || !t._2.isEmpty)
      .toSeq
