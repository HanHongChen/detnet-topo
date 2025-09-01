package nycu.chh.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onosproject.net.link.LinkListener;
import org.onosproject.net.link.LinkEvent;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.Link;

public class LinkListenerImpl implements LinkListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final LinkService linkService;

    public LinkListenerImpl(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public void event(LinkEvent event) {
        log.info("【Link事件】type={} subject={}", event.type(), event.subject());
        log.info("=== 所有 Link ===");
        Iterable<Link> links = linkService != null ? linkService.getLinks() : null;
        if (links != null && links.iterator().hasNext()) {
            for (Link link : links) {
                log.info("Link: {} -> {} type: {}", link.src(), link.dst(), link.type());
            }
        } else {
            log.info("No links.");
        }
    }
}