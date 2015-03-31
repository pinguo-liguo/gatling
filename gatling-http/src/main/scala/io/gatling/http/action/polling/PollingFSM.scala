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

import akka.actor.FSM
import io.gatling.core.akka.BaseActor
import io.gatling.core.session.Session

private[polling] abstract class PollingFSM extends BaseActor with FSM[PollingState, PollingData]

private[polling] sealed trait PollingState
private[polling] case object Uninitialized extends PollingState
private[polling] case object PollingRequests extends PollingState

private[polling] sealed trait PollingData
private[polling] case object NoData extends PollingData
private[polling] case class PollingRequestsData(update: Session => Session) extends PollingData

private[polling] case object Poll
