package score.furigana

import com.sun.net.httpserver.HttpServer
import java.net.{InetAddress, InetSocketAddress}

import http.FuriganaHttpHandler

@main def main() =
  val localhost = InetAddress.getByAddress(Array(127, 0, 0, 1).map(_.toByte))
  val server = HttpServer.create(InetSocketAddress(localhost, 8094), 0).nn
  server.createContext("/", FuriganaHttpHandler())
  server.start()
