package nycu.chh.model;

public class RouterInfo {
    private String upNodeId;
    private String ueIp;
    private int port;
    private int mtu;

    public RouterInfo(String upNodeId, String ueIp, int port, int mtu) {
        this.upNodeId = upNodeId;
        this.ueIp = ueIp;
        this.port = port;
        this.mtu = mtu;
    }

    public String getUpNodeId() {
        return upNodeId;
    }

    public String getUeIp() {
        return ueIp;
    }

    public int getPort() {
        return port;
    }

    public int getMtu() {
        return mtu;
    }
    
    @Override
    public String toString() {
        return "RouterInfo{" +
                "upNodeId='" + upNodeId + '\'' +
                ", ueIp='" + ueIp + '\'' +
                ", port=" + port +
                ", mtu=" + mtu +
                '}';
    }

}
