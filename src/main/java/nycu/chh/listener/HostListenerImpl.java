package nycu.chh.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostService;
import org.onosproject.net.Host;

public class HostListenerImpl implements HostListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final HostService hostService;

    public HostListenerImpl(HostService hostService) {
        this.hostService = hostService;
    }

    @Override
    public void event(HostEvent event) {
        log.info("【Host事件】type={} subject={}", event.type(), event.subject());
        log.info("=== 所有 Host ===");
        Iterable<Host> hosts = hostService != null ? hostService.getHosts() : null;
        if (hosts != null && hosts.iterator().hasNext()) {
            for (Host host : hosts) {
                log.info("Host: {} MAC:{} IP:{} Location:{}",
                        host.id(), host.mac(), host.ipAddresses(), host.location());
            }
        } else {
            log.info("No hosts.");
        }
    }
}