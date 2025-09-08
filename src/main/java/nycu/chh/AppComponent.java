/*
 * Copyright 2025-present Open Networking Foundation
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
 */
package nycu.chh;

import org.onlab.packet.IpPrefix;
import org.onlab.packet.MacAddress;
import org.onosproject.cfg.ComponentConfigService;
import org.onosproject.core.CoreService;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.*;
import org.onosproject.net.topology.*;
import org.onosproject.net.device.*;
import org.onosproject.net.flow.DefaultFlowRule;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.host.*;
import org.onosproject.net.intent.IntentService;
import org.onosproject.net.link.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nycu.chh.listener.DeviceListenerImpl;
import nycu.chh.listener.HostListenerImpl;
import nycu.chh.listener.LinkListenerImpl;
import nycu.chh.listener.TopoListener;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.onlab.util.Tools.get;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class AppComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected TopologyService topologyService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected HostService hostService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected LinkService linkService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowRuleService flowRuleService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected IntentService intentService;

    
    /** Some configurable property. */
    // private String someProperty;

    private ApplicationId appId;
    private final TopologyListener topoListener = new TopoListener(deviceService, 
        linkService, 
        hostService);

    private HostListenerImpl hostListener;
    private DeviceListenerImpl deviceListener;
    private LinkListenerImpl linkListener;

    @Activate
    protected void activate() {
        // cfgService.registerProperties(getClass());
        log.info("Started");
        appId = coreService.registerApplication("nycu.chh.detnet.topo");
        log.warn("DetNet Controller Activated with App ID: {}", appId.id());

        // free5gc enp0s8
        Host src1 = hostService.getHost(HostId.hostId("08:00:27:90:a4:e5/None"));
        // server enp0s8
        Host dst1 = hostService.getHost(HostId.hostId("08:00:27:33:ec:3a-None"));
        // free5gc enp0s9
        Host src2 = hostService.getHost(HostId.hostId("08:00:27:c5:f1:89-None"));
        // server enp0s9
        Host dst2 = hostService.getHost(HostId.hostId("08:00:27:eb:c2:92-None"));
        log.warn("src1{} dst1{}", src1, dst1);
        log.warn("src2{} dst2{}", src2, dst2);
        List<Path> disPathWithHost = findNodeDisjointPathsWithHost(
            src1,
            dst1,
            src2,
            dst2);

        if(disPathWithHost != null){
            installPathFlowWithHost(disPathWithHost.get(0), src1, dst1, appId, flowRuleService);
            installPathFlowWithHost(disPathWithHost.get(1), src2, dst2, appId, flowRuleService);
        }
        
        // Device src1 = deviceService.getDevice(DeviceId.deviceId("of:0000000000000001"));
        // Device src2 = deviceService.getDevice(DeviceId.deviceId("of:0000000000000002"));
        // Device dst1 = deviceService.getDevice(DeviceId.deviceId("of:0000000000000006"));
        // Device dst2 = deviceService.getDevice(DeviceId.deviceId("of:0000000000000005"));
        // List<Path> disjointPaths = findNodeDisjointPaths(
        //     src1.id(),
        //     dst1.id(),
        //     src2.id(),
        //     dst2.id()
        //     );

        // if(disjointPaths != null){
        //     installPathForwarding(disjointPaths.get(0));
        //     installPathForwarding(disjointPaths.get(1));
        // } 


        // topologyService.addListener(topoListener);
        // deviceListener = new DeviceListenerImpl(deviceService);
        // hostListener = new HostListenerImpl(hostService);
        // linkListener = new LinkListenerImpl(linkService);
        // deviceService.addListener(deviceListener);
        // hostService.addListener(hostListener);
        // linkService.addListener(linkListener);
        //         // 取得全部設備
        // for (Device device : deviceService.getDevices()) {
        //     log.info("Device: {}", device.id());
        // }

        // // 取得全部連線
        // for (Link link : linkService.getLinks()) {
        //     log.info("Link: {} -> {}", link.src(), link.dst());
        // }

        // // 取得全部主機
        // for (Host host : hostService.getHosts()) {
        //     log.info("Host: {} IPs:{} Location:{}", host.mac(), host.ipAddresses(), host.location());
        // }
    }

    @Deactivate
    protected void deactivate() {
        // topologyService.removeListener(topoListener);
        // deviceService.removeListener(deviceListener);
        // hostService.removeListener(hostListener);
        // linkService.removeListener(linkListener);
        // cfgService.unregisterProperties(getClass(), false);
        log.info("Stopped");
    }
    
    public void installPathFlowWithHost(Path path, Host srcHost, Host dstHost, 
        ApplicationId appId, 
        FlowRuleService flowRuleService) {
        List<Link> links = path.links();

        // === 去程：srcHost → dstHost ===
        // 1. 起點switch: host port → 第一條link的src port
        if (!links.isEmpty()) {
            Link firstLink = links.get(0);
            applyPortFlowRule(
                srcHost.location().deviceId(),
                srcHost.location().port(),
                firstLink.src().port(),
                50000, appId, flowRuleService, (short)0x0800);//IPV4
            applyPortFlowRule(
                srcHost.location().deviceId(),
                srcHost.location().port(),
                firstLink.src().port(),
                50000, appId, flowRuleService, (short)0x0806);//ARP

            applyPortFlowRule(
                srcHost.location().deviceId(),
                firstLink.src().port(),
                srcHost.location().port(),
                50000, appId, flowRuleService, (short)0x0800);
            applyPortFlowRule(
                srcHost.location().deviceId(),
                firstLink.src().port(),
                srcHost.location().port(),
                50000, appId, flowRuleService, (short)0x0806);
        }
        // 2. 中間switch: 前一條link的dst port → 下一條link的src port
        for (int i = 0; i < links.size() - 1; i++) {
            Link prev = links.get(i);
            Link next = links.get(i + 1);
            applyPortFlowRule(
                prev.dst().deviceId(),
                prev.dst().port(),
                next.src().port(),
                50000, appId, flowRuleService, (short)0x0800
            );
            applyPortFlowRule(
                prev.dst().deviceId(),
                prev.dst().port(),
                next.src().port(),
                50000, appId, flowRuleService, (short)0x0806
            );
            
            applyPortFlowRule(
                prev.dst().deviceId(),
                next.src().port(),
                prev.dst().port(),
                50000, appId, flowRuleService, (short)0x0800
            );
            applyPortFlowRule(
                prev.dst().deviceId(),
                next.src().port(),
                prev.dst().port(),
                50000, appId, flowRuleService, (short)0x0806
            );
        }
        // 3. 終點switch: 最後一條link的dst port → dstHost port
        if (!links.isEmpty()) {
            Link lastLink = links.get(links.size() - 1);
            applyPortFlowRule(
                dstHost.location().deviceId(),
                lastLink.dst().port(),
                dstHost.location().port(),
                50000, appId, flowRuleService, (short)0x0800
            );
            applyPortFlowRule(
                dstHost.location().deviceId(),
                lastLink.dst().port(),
                dstHost.location().port(),
                50000, appId, flowRuleService, (short)0x0806
            );

            applyPortFlowRule(
                dstHost.location().deviceId(),
                dstHost.location().port(),
                lastLink.dst().port(),
                50000, appId, flowRuleService, (short)0x0800
            );
            applyPortFlowRule(
                dstHost.location().deviceId(),
                dstHost.location().port(),
                lastLink.dst().port(),
                50000, appId, flowRuleService, (short)0x0806
            );
        }

    }

    // Helper function: 安裝只配對port的flow rule
    private void applyPortFlowRule(DeviceId deviceId, PortNumber inPort, PortNumber outPort, int priority,
                                ApplicationId appId, FlowRuleService flowRuleService, short ethType) {
        TrafficSelector selector = DefaultTrafficSelector.builder()
            .matchInPort(inPort)
            .matchEthType(ethType)
            .build();
        TrafficTreatment treatment = DefaultTrafficTreatment.builder()
            .setOutput(outPort)
            .build();
        FlowRule rule = DefaultFlowRule.builder()
            .forDevice(deviceId)
            .withSelector(selector)
            .withTreatment(treatment)
            .withPriority(priority)
            .fromApp(appId)
            .makePermanent()
            .build();
        flowRuleService.applyFlowRules(rule);
    }

    public List<Path> findNodeDisjointPathsWithHost(
        Host srcHost1, Host dstHost1, Host srcHost2, Host dstHost2) {

        DeviceId src1 = srcHost1.location().deviceId();
        DeviceId dst1 = dstHost1.location().deviceId();
        DeviceId src2 = srcHost2.location().deviceId();
        DeviceId dst2 = dstHost2.location().deviceId();

        List<Path> paths1 = topologyService.getPaths(topologyService.currentTopology(), src1, dst1)
            .stream().collect(Collectors.toList());
        List<Path> paths2 = topologyService.getPaths(topologyService.currentTopology(), src2, dst2)
            .stream().collect(Collectors.toList());
        log.warn("paths1{}, paths2{}", paths1, paths2);
        for (Path p1 : paths1) {
            Set<DeviceId> nodes1 = getIntermediateNodes(p1, src1, dst1);
            // 把 src/dst host 的 deviceId/port(ConnectPoint)也加進去
            nodes1.add(srcHost1.location().deviceId());
            nodes1.add(dstHost1.location().deviceId());

            for (Path p2 : paths2) {
                Set<DeviceId> nodes2 = getIntermediateNodes(p2, src2, dst2);
                nodes2.add(srcHost2.location().deviceId());
                nodes2.add(dstHost2.location().deviceId());

                Set<DeviceId> common = new HashSet<>(nodes1);
                common.retainAll(nodes2);

                if (common.isEmpty()) {
                    log.info("Found node-disjoint paths (with host access device)");
                    log.info("Node-disjoint Path 1 (src1->dst1): {}", p1.links());
                    log.info("Node-disjoint Path 2 (src2->dst2): {}", p2.links());

                    return Arrays.asList(p1, p2);
                }
            }
        }
        log.info("找不到 node-disjoint path（含 host access device）!");
        return null;
    }

    public List<Path> findNodeDisjointPaths(DeviceId src1, DeviceId dst1, DeviceId src2, DeviceId dst2) {
        // 取得所有路徑
        List<Path> paths1 = topologyService.getPaths(topologyService.currentTopology(), src1, dst1)
            .stream().collect(Collectors.toList());
        List<Path> paths2 = topologyService.getPaths(topologyService.currentTopology(), src2, dst2)
            .stream().collect(Collectors.toList());
        // 比較 node-disjoint
        for (Path p1 : paths1) {
            Set<DeviceId> nodes1 = getIntermediateNodes(p1, src1, dst1);

            for (Path p2 : paths2) {
                Set<DeviceId> nodes2 = getIntermediateNodes(p2, src2, dst2);

                Set<DeviceId> common = new HashSet<>(nodes1);
                common.retainAll(nodes2);

                if (common.isEmpty()) {
                    log.info("Found node-disjoint paths");
                    log.info("Node-disjoint Path 1 (src1->dst1): {}", p1.links());
                    log.info("Node-disjoint Path 2 (src2->dst2): {}", p2.links());

                    return Arrays.asList(p1, p2);
                }
            }
        }
        log.info("找不到 node-disjoint path!");
        return null;
    }

    // 取得 path 的所有中繼節點（不含起點跟終點）
    private Set<DeviceId> getIntermediateNodes(Path path, DeviceId src, DeviceId dst) {
        Set<DeviceId> nodes = path.links().stream()
            .map(link -> link.src().deviceId())
            .collect(Collectors.toSet());
        nodes.remove(src);
        nodes.remove(dst);
        return nodes;
    }

    // @Modified
    // public void modified(ComponentContext context) {
    //     Dictionary<?, ?> properties = context != null ? context.getProperties() : new Properties();
    //     if (context != null) {
    //         someProperty = get(properties, "someProperty");
    //     }
    //     log.info("Reconfigured");
    // }

    // @Override
    // public void someMethod() {
    //     log.info("Invoked");
    // }

}
