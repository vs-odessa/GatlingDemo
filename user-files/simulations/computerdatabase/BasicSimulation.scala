/**
 * Copyright 2011-2016 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

/* Passing JAVA_OPTS parameters to script  */
  val nbUsersString = System.getProperty("users")
  val nbUsers = Integer.parseInt(nbUsersString)
  val repeatsString = System.getProperty("repeats")
  val repeatsNumber = Integer.parseInt(repeatsString)
  val durationString = System.getProperty("duration")
  val duration = Integer.parseInt(durationString)

  val httpConf = http
    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  val scn = scenario("Scenario Name")   // A scenario is a chain of requests and pauses
//    .repeat(repeatsNumber)
//    .during(duration minutes)
	.during(duration seconds)
	{  
    exec(http("request_1")
      .get("/"))
    .pause(7) // Note that Gatling has recorded real time pauses
    .exec(http("request_2")
      .get("/computers?f=macbook"))
    .pause(2)
    .exec(http("request_3")
      .get("/computers/6"))
    .pause(3)
    .exec(http("request_4")
      .get("/"))
    .pause(2)
    .exec(http("request_5")
      .get("/computers?p=1"))
    .pause(670 milliseconds)
    .exec(http("request_6")
      .get("/computers?p=2"))
    .pause(629 milliseconds)
    .exec(http("request_7")
      .get("/computers?p=3"))
    .pause(734 milliseconds)
    .exec(http("request_8")
      .get("/computers?p=4"))
    .pause(5)
    .exec(http("request_9")
      .get("/computers/new"))
    .pause(1)
    .exec(http("request_10") // Here's an example of a POST request
      .post("/computers")
      .headers(headers_10)
      .formParam("name", "Beautiful Computer") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
      .formParam("introduced", "2012-05-30")
      .formParam("discontinued", "")
      .formParam("company", "37"))
    }	
//  setUp(scn.inject(atOnceUsers(nbUsers)).protocols(httpConf).throttle(holdFor(duration minutes)))
    setUp(scn.inject(atOnceUsers(nbUsers)).protocols(httpConf))
}
