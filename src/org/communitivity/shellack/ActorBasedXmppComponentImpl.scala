/*
 * ActorBasedXmppComponentImpl.scala
 *
 * Licensed to the Communitivity, Inc under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at*
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.communitivity.shellack

import scala.actors.Actor
import scala.actors.Actor._
import org.xmpp.component.Component
import org.xmpp.component.ComponentManager
import org.xmpp.packet.Packet
import org.xmpp.packet.JID



// Needed to use fully qualified class name for Actor or NetBeans scala plugin gives hidden by sparqlweather.Actor error
protected class ActorBasedXmppComponentImpl(conf : ComponentConfig, wrapped : Actor) extends Component {

    var manager : ComponentManager = null

    var out = actor {
        loop {
            react {
                case pkt : Packet =>
                    manager.sendPacket(this, pkt)
            }
        }
    }

    def getName() : String =
    {
        return conf.name()
    }

    def getDescription() : String =
    {
        return conf.description()
    }

    def initialize(jid : JID, componentManager : ComponentManager) {
        manager = componentManager
    }

    def start()  { wrapped.start() }

    def shutdown() {}

    def processPacket(pkt : Packet) {
        Console.println("Packet heard, "+pkt.toXML)
        wrapped ! (pkt, out)
    }
}
