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
package io.gatling.http.request.builder.polling

import scala.concurrent.duration.FiniteDuration

import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session._
import io.gatling.http.action.polling.{ PollingStartBuilder, PollingStopBuilder }
import io.gatling.http.ahc.HttpEngine
import io.gatling.http.config.DefaultHttpProtocol
import io.gatling.http.request.builder.HttpRequestBuilder

object Polling {
  val DefaultPollingName = SessionPrivateAttributes.PrivateAttributePrefix + "http.polling"
}
class Polling(pollingName: String = Polling.DefaultPollingName)(implicit configuration: GatlingConfiguration,
                                                                defaultHttpProtocol: DefaultHttpProtocol,
                                                                httpEngine: HttpEngine) {

  def pollingName(pollingName: String) = new Polling(pollingName)

  def every(duration: Expression[FiniteDuration]) = new EveryStep(pollingName, duration)

  def stop = new PollingStopBuilder(pollingName)
}

class EveryStep(pollingName: String,
                duration: Expression[FiniteDuration])(implicit configuration: GatlingConfiguration,
                                                      defaultHttpProtocol: DefaultHttpProtocol,
                                                      httpEngine: HttpEngine) {

  def exec(requestBuilder: HttpRequestBuilder) =
    new PollingStartBuilder(pollingName, duration, requestBuilder)
}