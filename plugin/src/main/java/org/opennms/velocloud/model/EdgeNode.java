/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.velocloud.model;

import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.NodeAssetRecord;
import org.opennms.integration.api.v1.model.SnmpInterface;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EdgeNode implements Node {


    private final List<MetaData> metaData = new ArrayList<>();
    private final List<SnmpInterface> snmpInterfaces = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();
    private final List<IpInterface> ipInterfaces = new ArrayList<>();

    private NodeAssetRecord nodeAssetRecord;
    private Integer nodeId;
    private String foreignSource;
    private String foreignId;
    private String label;
    private String location;
    private String linkStatus;


    public EdgeNode(){

    }

    @Override
    public Integer getId() {
        return nodeId;
    }

    @Override
    public String getForeignSource() {
        return foreignSource;
    }

    @Override
    public String getForeignId() {
        return foreignId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public NodeAssetRecord getAssetRecord() {
        return nodeAssetRecord;
    }

    @Override
    public List<IpInterface> getIpInterfaces() {
        return ipInterfaces;
    }

    @Override
    public Optional<IpInterface> getInterfaceByIp(InetAddress ipAddr) {
        return ipInterfaces.stream().filter(i->i.getIpAddress().equals(ipAddr)).findFirst();
    }

    @Override
    public List<SnmpInterface> getSnmpInterfaces() {
        return snmpInterfaces;
    }

    @Override
    public List<MetaData> getMetaData() {
        return metaData;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    public void setNodeAssetRecord(org.opennms.integration.api.v1.model.NodeAssetRecord nodeAssetRecord) {
        this.nodeAssetRecord = nodeAssetRecord;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public void setForeignSource(String foreignSource) {
        this.foreignSource = foreignSource;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLinkStatus(String linkStatus){
        this.linkStatus = linkStatus;
    }


}


