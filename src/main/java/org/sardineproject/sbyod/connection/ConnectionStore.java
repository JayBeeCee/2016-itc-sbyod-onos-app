/*
 * Copyright 2015 Lorenz Reinhart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package org.sardineproject.sbyod.connection;

import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.TpPort;
import org.onosproject.net.Host;
import org.sardineproject.sbyod.service.Service;

import java.util.Set;

/**
 * Created by lorry on 06.03.16.
 */
public interface ConnectionStore {

    /**
     * Add a new connection to the service
     *
     * @param connection connection to add
     */
    void addConnection(Connection connection);

    /**
     * Removes the connection between user and service
     *
     * @param connection connection to remove
     */
    void removeConnection(Connection connection);

    /**
     * Get the connection between the user and the service
     *
     * @param user user
     * @param service service
     * @return connection between user and service
     */
    Connection getConnection(Host user, Service service);

    /**
     * Get all connections of a service
     *
     * @param service service
     * @return Set of connections
     */
    Set<Connection> getConnections(Service service);

    /**
     * Get all connections of a host defined by the id
     *
     * @param host Host user of the connection
     * @return Set of connections
     */
    Set<Connection> getConnections(Host host);

    /**
     * Get all registered connections
     *
     * @return set of connections
     */
    Set<Connection> getConnections();

    /**
     * Ask if connection is already installed
     *
     * @param connection the connection to check
     * @return true if connection already installed
     */
    Boolean contains(Connection connection);

}
