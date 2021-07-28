package score.furigana
package http

import java.net.URLDecoder

import NullUtil.*

object QueryStringParser:
  def apply(query: String): Map[String, List[String]] =
    query
      .splitnn("&")
      .view
      .map { kv =>
        kv.splitnn("=", 2).map(URLDecoder.decode(_, "UTF-8").nn)
      }
      .collect {
        case Array(key, value) => key -> value
        case Array(key) => key -> ""
      }
      .groupMap {
        case (key, value) => key
      } {
        case (key, value) => value
      }
      .view
      .mapValues(_.toList)
      .toMap
