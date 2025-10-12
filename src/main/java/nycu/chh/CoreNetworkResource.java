package nycu.chh;

import static org.onlab.util.Tools.log;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nycu.chh.model.IetfInterface;
import nycu.chh.model.IetfInterfaces;
import nycu.chh.model.RouterInfo;
import nycu.chh.model.RouterInfos;

@Path("core-network")
@Produces(MediaType.APPLICATION_JSON)
public class CoreNetworkResource extends AbstractWebResource {
    private final Logger log = LoggerFactory.getLogger(getClass());
    public CoreNetworkResource() {
        log.warn("CoreNetworkResource loaded!");
    }

    @GET
    @Path("")
    public Response getGreeting() {
        ObjectNode node = mapper().createObjectNode().put("hello", "world");
        return ok(node).build();
    }

    @POST
    @Path("interface")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveInterfaceInfo(String jsonRequest) {
        log.info("Received Interface Info: {}", jsonRequest);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonRequest);
            JsonNode interfaceNodes = jsonNode.get("ietf-interfaces:interfaces");
            if(interfaceNodes != null){
                JsonNode interfaceArray = interfaceNodes.get("interface");
                if (interfaceArray != null && interfaceArray.isArray()) {
                    IetfInterfaces ietfInterfaces = IetfInterfaces.getInstance();
                    for(JsonNode node : interfaceArray){
                        String name = node.get("name").asText();
                        String description = node.get("description").asText();
                        String type = node.get("type").asText();
                        String physAddress = node.get("physAddress").asText();
                        String operStatus = node.get("oper-status").asText();
                        String discontinuityTime = node.get("statistics")
                            .get("discontinuity-time").asText();

                        IetfInterface ietfInterface = new IetfInterface(name, description, type, physAddress, operStatus, discontinuityTime);
                        ietfInterfaces.putIetfInterfaces(ietfInterface);
                        log.info("Parsed Interface Info - Name: {}, Description: {}, Type: {}, PhysAddress: {}, OperStatus: {}, DiscontinuityTime: {}",
                            name, description, type, physAddress, operStatus, discontinuityTime);

                    }
                }else{
                    log.warn("No interface array found in the request");
                }
            }
            
            return Response.ok("{\"status\": \"success\"}").build();
        } catch (IOException e) {
            log.error("Failed to parse interface info", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Invalid JSON format\"}").build();
        }
    }

    @POST
    @Path("router-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveRouterInfo(String jsonRequest) {
        log.info("Received Router Info: {}", jsonRequest);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonRequest);

            // TSCTSF report router information
            String upNodeId = jsonNode.get("upNodeId").asText();
            String ueIp = jsonNode.get("ueIp").asText(); 
            int port = jsonNode.get("port").asInt();
            int mtu = jsonNode.get("mtu").asInt();

            RouterInfo routerInfo = new RouterInfo(upNodeId, ueIp, port, mtu);
            log.info("Storing Router Info: {}", routerInfo);

            RouterInfos.addRouterInfo(routerInfo);
            for(int i = 0; i < RouterInfos.getRouterInfos().size(); i++) {
                log.info("Stored Router Info {}: {}", i, RouterInfos.getRouterInfos().get(i));
            }

            return Response.ok("{\"status\": \"success\"}").build();
        } catch (IOException e) {
            log.error("Failed to parse TSCTSF report", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Invalid JSON format\"}").build();
        }
    }

    @POST
    @Path("tsctsf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveTsctsfReport(String jsonRequest) {
        log.info("Received TSCTSF Report: {}", jsonRequest);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonRequest);

            // 解析 TSCTSF 能力資訊
            String nodeId = jsonNode.get("nodeId").asText();
            int minBandwidth = jsonNode.get("minBandwidth").asInt();
            int maxLatency = jsonNode.get("maxLatency").asInt();

            log.info("Parsed TSCTSF Report - Node: {}, Min BW: {}, Max Latency: {}",
                    nodeId, minBandwidth, maxLatency);

            return Response.ok("{\"status\": \"success\"}").build();
        } catch (IOException e) {
            log.error("Failed to parse TSCTSF report", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Invalid JSON format\"}").build();
        }
    }
}
