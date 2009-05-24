/*
 * XMPPComponent.scala
 *
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
import org.jivesoftware.whack.ExternalComponentManager

/**
 * This class creates an org.xmpp.component.Component implementation
 * as well as instantiating a org.jivesoftware.whack.ExternalComponentManager
 * and then connects them so that the external component metadata is fetched
 * from the provided ComponentConfig and any packets received are sent to
 * the provided actor-based handler as messages.
 */
class XMPPComponent(conf : ComponentConfig, handler : Actor) {

    private var wrapped : ActorBasedXmppComponentImpl = new ActorBasedXmppComponentImpl(conf, handler)

    private var manager : ExternalComponentManager = new ExternalComponentManager(conf.server(), 5275)

    def start() {
        manager.setSecretKey(conf.subdomain(), conf.secret())
        manager.setMultipleAllowed(conf.subdomain(), true)
        manager.addComponent(conf.subdomain(), wrapped)
    }
}
