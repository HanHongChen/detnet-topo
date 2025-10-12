package nycu.chh.model;

import java.util.HashMap;
import java.util.Map;

public class IetfInterfaces {
    private static IetfInterfaces instance;
    Map<String, IetfInterface> ietfInterfaces;

    public IetfInterfaces() {
        ietfInterfaces = new HashMap<>();
    }

    public static IetfInterfaces getInstance() {
        if (instance == null) {
            instance = new IetfInterfaces();
        }
        return instance;
    }

    public Map<String, IetfInterface> getIetfInterfaces() {
        return ietfInterfaces;
    }
    public void putIetfInterfaces(IetfInterface ietfInterface) {
        ietfInterfaces.put(ietfInterface.getName(), ietfInterface);
    }
}