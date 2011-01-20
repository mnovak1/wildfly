/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.messaging.jms;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

import org.jboss.as.controller.NewExtension;
import org.jboss.as.controller.NewExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ModelNodeRegistration;

/**
 * @author Emanuel Muckenhuber
 */
public class NewJMSExtension implements NewExtension {

    private static final PathElement CFS_PATH = PathElement.pathElement(CommonAttributes.CONNECTION_FACTORY);
    private static final PathElement QUEUE_PATH = PathElement.pathElement(CommonAttributes.QUEUE);
    private static final PathElement TOPIC_PATH = PathElement.pathElement(CommonAttributes.TOPIC);

    private static final NewJMSSubsystemParser parsers = NewJMSSubsystemParser.getInstance();

    /** {@inheritDoc} */
    public void initialize(NewExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem("jms");
        final ModelNodeRegistration registration = subsystem.registerSubsystemModel(NewJMSSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ADD, NewJMSSubsystemAdd.INSTANCE, NewJMSSubsystemProviders.SUBSYSTEM_ADD, false);
        // Connection factories
        final ModelNodeRegistration cfs = registration.registerSubModel(CFS_PATH, NewJMSSubsystemProviders.CF);
        cfs.registerOperationHandler(ADD, NewConnectionFactoryAdd.INSTANCE, NewJMSSubsystemProviders.CF_ADD, false);
        cfs.registerOperationHandler(REMOVE, NewConnectionFactoryRemove.INSTANCE, NewJMSSubsystemProviders.CF_REMOVE, false);
        // Queues
        final ModelNodeRegistration queues = registration.registerSubModel(QUEUE_PATH, NewJMSSubsystemProviders.JMS_QUEUE);
        queues.registerOperationHandler(ADD, NewJMSQueueAdd.INSTANCE, NewJMSSubsystemProviders.JMS_QUEUE_ADD, false);
        queues.registerOperationHandler(REMOVE, NewJMSQueueRemove.INSTANCE, NewJMSSubsystemProviders.JMS_QUEUE_REMOVE, false);
        // Topics
        final ModelNodeRegistration topics = registration.registerSubModel(TOPIC_PATH, NewJMSSubsystemProviders.JMS_TOPIC);
        topics.registerOperationHandler(ADD, NewJMSTopicAdd.INSTANCE, NewJMSSubsystemProviders.JMS_TOPIC_ADD, false);
        topics.registerOperationHandler(REMOVE, NewJMSTopicRemove.INSTANCE, NewJMSSubsystemProviders.JMS_TOPIC_REMOVE, false);
    }

    /** {@inheritDoc} */
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(Namespace.CURRENT.getUriString(), parsers, parsers);
    }

}
