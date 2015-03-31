/**
 * Copyright 2011-2015 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.http.action.polling

import scala.concurrent.duration.FiniteDuration

import akka.actor.{ ActorRef, Props }
import io.gatling.core.result.writer.DataWriters
import io.gatling.core.session._
import io.gatling.http.action.RequestAction
import io.gatling.http.request.{ HttpRequest, HttpRequestDef }

object PollingStartAction {
  def props(pollingName: String,
            duration: Expression[FiniteDuration],
            requestDef: HttpRequestDef,
            dataWriters: DataWriters,
            next: ActorRef): Props =
    Props(new PollingStartAction(pollingName, duration, requestDef, dataWriters, next))
}

// TODO : other stuff missing ?
class PollingStartAction(
    pollingName: String,
    duration: Expression[FiniteDuration],
    requestDef: HttpRequestDef,
    dataWriters: DataWriters,
    val next: ActorRef) extends RequestAction(dataWriters) {

  override def requestName = requestDef.requestName

  override def sendRequest(requestName: String, session: Session) = {
      def startPolling(duration: FiniteDuration, httpRequest: HttpRequest): Unit = {
        logger.info("Starting poller")
        val pollerActor = context.actorOf(PollingActor.props(duration, httpRequest, dataWriters, next), actorName("pollerActor"))
        pollerActor ! StartPolling
      }

    for {
      duration <- duration(session)
      httpRequest <- requestDef.build(requestName, session)
    } yield startPolling(duration, httpRequest)
  }
}
