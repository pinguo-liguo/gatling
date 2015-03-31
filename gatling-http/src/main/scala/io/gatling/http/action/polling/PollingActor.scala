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

import io.gatling.core.session.Session

import scala.concurrent.duration.FiniteDuration

import akka.actor.{ ActorRef, Props }

import io.gatling.core.result.writer.DataWriters
import io.gatling.http.request.HttpRequest

object PollingActor {
  def props(duration: FiniteDuration,
            requestDef: HttpRequest,
            dataWriters: DataWriters,
            next: ActorRef): Props =
    Props(new PollingActor(duration, requestDef, dataWriters, next))

  val PollTimerName = "pollTimer"
}

class PollingActor(duration: FiniteDuration,
                   requestDef: HttpRequest,
                   dataWriters: DataWriters,
                   next: ActorRef) extends PollingFSM {

  import PollingActor.PollTimerName

  startWith(Uninitialized, NoData)

  when(Uninitialized) {
    case Event(StartPolling, NoData) =>
      setTimer(PollTimerName, Poll, duration, repeat = true)
      goto(PollingRequests) using PollingRequestsData(Session.Identity)
  }

  when(PollingRequests) {
    case Event(Poll, PollingRequestsData(update)) =>
      stay() // TODO : do some stuff here
    case Event(StopPolling, PollingRequestsData(update)) => // TODO : state
      cancelTimer(PollTimerName)
      stop()
  }

  initialize()
}
