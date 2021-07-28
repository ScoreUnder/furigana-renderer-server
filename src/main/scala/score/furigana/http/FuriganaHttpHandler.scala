package score.furigana
package http

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

import scala.util.chaining.*

import NullUtil.*

class FuriganaHttpHandler extends HttpHandler:
  def handle(exchange: HttpExchange | Null): Unit =
    try
      assert(exchange != null)
      if exchange.getRequestURI.nn.getPath.?.forall(_.length > 1) then
        exchange.sendResponseHeaders(404, 0)
      else
        val args = QueryStringParser(exchange.getRequestURI.nn.getRawQuery.nn)
        val text =
          args.getOrElse("text", Nil) match
            case List(text) => text
            case _ => "error: need exactly one 'text' parameter in URL"

        val parsed = FuriganaParser(text)
        val image =
          FuriganaRenderer.renderPNG(parsed) match
            case Some(data) => data
            case None =>
              FuriganaRenderer.renderPNG(List(("error: no text visible in output", ""))).get

        val headers = exchange.getResponseHeaders.nn
        val body = exchange.getResponseBody.nn

        headers.set("Cache-Control", "immutable")
        headers.set("Content-Type", "image/png")

        exchange.sendResponseHeaders(200, image.length)
        body.write(image)
      exchange.close()
    catch
      case ex =>
        ex.printStackTrace()
        throw ex
