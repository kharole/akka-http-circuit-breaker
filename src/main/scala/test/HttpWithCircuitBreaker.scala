package test

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.Success
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.pattern.CircuitBreaker
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import scala.concurrent.duration._

object HttpWithCircuitBreaker extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val breaker =
    new CircuitBreaker(
      system.scheduler,
      maxFailures = 2,
      callTimeout = 3.seconds,
      resetTimeout = 25.seconds)
      .onOpen(println("circuit breaker opened"))
      .onClose(println("circuit breaker closed"))
      .onHalfOpen(println("circuit breaker half-open"))

  while (true) {
    val futureResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new"))
    breaker.withCircuitBreaker(futureResponse).map(resp => resp.status match {
      case Success(_) =>
        resp.entity.discardBytes()
        println("http success")
      case _ =>
        resp.entity.discardBytes()
        println(s"http error ${resp.status.intValue()}")
    }).recover {
      case e@_ =>
        println(s"exception ${e.getMessage}")
    }
    
    Thread.sleep(1000)
  }
}
