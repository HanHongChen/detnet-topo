package nycu.chh.model;

import java.util.ArrayList;
import java.util.List;

public class RouterInfos {
    private static List<RouterInfo> routerInfos = new ArrayList<RouterInfo>();

    public static synchronized void addRouterInfo(RouterInfo routerInfo) {
        routerInfos.add(routerInfo);
    }

    public static synchronized List<RouterInfo> getRouterInfos() {
        return routerInfos;
    }
}
