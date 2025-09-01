package nycu.chh.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onosproject.net.device.DeviceListener;
import org.onosproject.net.device.DeviceEvent;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.Device;

public class DeviceListenerImpl implements DeviceListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final DeviceService deviceService;

    public DeviceListenerImpl(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void event(DeviceEvent event) {
        log.info("【Device事件】type={} subject={}", event.type(), event.subject());
        log.info("=== 所有 Device (Switch/Router) ===");
        for (Device device : deviceService.getDevices()) {
            log.info("Device: {}", device.id());
        }
        // Iterable<Device> devices = deviceService != null ? deviceService.getDevices() : null;
        // if (devices != null && devices.iterator().hasNext()) {
        //     for (Device device : devices) {
        //         log.info("Device: {} type: {}", device.id(), device.type());
        //     }
        // } else {
        //     log.info("No devices.");
        // }
    }
}