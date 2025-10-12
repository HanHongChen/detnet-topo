package nycu.chh.model;

enum OperStatus{
    up,
    down,
    testing,
    unknown,
    dormant,
    notPresent,
    lowerLayerDown
}

public class IetfInterface {
    String name;
    String description;
    String type;
    
    String physAddress;
    IetfStatistic statistics;
    OperStatus operStatus;
    
    public IetfInterface() {
        statistics = new IetfStatistic();
    }
    public IetfInterface(String name, String description, String type, String physAddress, String operStatus, String discontinuityTime) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.physAddress = physAddress;
        this.operStatus = OperStatus.valueOf(operStatus);
        this.statistics = new IetfStatistic(discontinuityTime);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPhysAddress() {
        return physAddress;
    }
    public void setPhysAddress(String physAddress) {
        this.physAddress = physAddress;
    }
    public IetfStatistic getStatistics() {
        return statistics;
    }
    public void setStatistics(IetfStatistic statistics) {
        this.statistics = statistics;
    }
    public OperStatus getOperStatus() {
        return operStatus;
    }
    public void setOperStatus(String operStatus) {
        this.operStatus = OperStatus.valueOf(operStatus);
    }
}
