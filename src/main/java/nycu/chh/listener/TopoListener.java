package nycu.chh.listener;

import org.onosproject.net.topology.TopologyListener;
import org.onosproject.net.Device;
import org.onosproject.net.Host;
import org.onosproject.net.Link;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostService;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.topology.TopologyEvent;
import org.onosproject.net.topology.TopologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopoListener implements TopologyListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final DeviceService deviceService;
    private final LinkService linkService;
    private final HostService hostService;

    public TopoListener(DeviceService deviceService, LinkService linkService, HostService hostService) {
        this.deviceService = deviceService;
        this.linkService = linkService;
        this.hostService = hostService;
    }

    @Override
    public void event(TopologyEvent event) {
        log.info("拓樸事件: {}", event);
        try { 
            Thread.sleep(5000); 
            Iterable<Device> devices = deviceService != null ? deviceService.getDevices() : null;
            if (devices != null) {
                for (Device device : devices) {
                    log.info("Device: {} type: {}", device.id(), device.type());
                }
            } else {
                log.info("No devices.");
            }

            log.info("=== 所有 Link ===");
            Iterable<Link> links = linkService != null ? linkService.getLinks() : null;
            if (links != null) {
                for (Link link : links) {
                    log.info("Link: {} -> {} type: {}", link.src(), link.dst(), link.type());
                }
            } else {
                log.info("No links.");
            }

            log.info("=== 所有 Host ===");
            Iterable<Host> hosts = hostService != null ? hostService.getHosts() : null;
            if (hosts != null) {
                for (Host host : hosts) {
                    log.info("Host: {} MAC:{} IP:{} Location:{}",
                            host.id(), host.mac(), host.ipAddresses(), host.location());
                }
            } else {
                log.info("No hosts.");
            }
        } catch (InterruptedException e) {

        }

        
    }
}
