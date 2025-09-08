ONOS_API="http://localhost:8181/onos/v1/flows"
AUTH="onos:rocks"

sudo ovs-vsctl add-br s1 -- set bridge s1 other-config:datapath-id=0000000000000001 \
    -- set bridge s1 protocols=OpenFlow14 \
    -- set-controller s1 tcp:127.0.0.1:6653
sudo ovs-vsctl add-br s3 -- set bridge s3 other-config:datapath-id=0000000000000003 \
    -- set bridge s3 protocols=OpenFlow14 \
    -- set-controller s3 tcp:127.0.0.1:6653
sudo ovs-vsctl add-br s5 -- set bridge s5 other-config:datapath-id=0000000000000005 \
    -- set bridge s5 protocols=OpenFlow14 \
    -- set-controller s5 tcp:127.0.0.1:6653

sudo ip link set enp0s8 up
sudo ip link set enp0s10 up

sudo ovs-vsctl add-port s1 enp0s8
sudo ovs-vsctl add-port s5 enp0s10
sudo ovs-vsctl show

sudo ovs-vsctl add-port s1 s1_s3 -- set interface s1_s3 type=patch options:peer=s3_s1
sudo ovs-vsctl add-port s3 s3_s1 -- set interface s3_s1 type=patch options:peer=s1_s3
sudo ovs-vsctl add-port s3 s3_s5 -- set interface s3_s5 type=patch options:peer=s5_s3
sudo ovs-vsctl add-port s5 s5_s3 -- set interface s5_s3 type=patch options:peer=s3_s5

sudo ip link set enp0s8 promisc on
sudo ip link set enp0s10 promisc on

sudo ifconfig s1 up
sudo ifconfig s3 up
sudo ifconfig s5 up
sudo ip addr add 192.170.10.20/24 dev s1
sudo ip addr add 192.170.20.20/24 dev s5

function add_flow() {
  local DEVICE=$1
  local IN=$2
  local OUT=$3
  echo "Adding flow on device $DEVICE: in_port=$IN -> out_port=$OUT"
  # IPv4 流量
  curl -u $AUTH -X POST -H "Content-Type: application/json" \
    -d "{
      \"priority\": 50000,
      \"isPermanent\": true,
      \"deviceId\": \"of:${DEVICE}\",
      \"selector\": {
        \"criteria\": [
          { \"type\": \"ETH_TYPE\", \"ethType\": \"0x0800\" },
          { \"type\": \"IN_PORT\", \"port\": \"${IN}\" }
        ]
      },
      \"treatment\": {
        \"instructions\": [ { \"type\": \"OUTPUT\", \"port\": \"${OUT}\" } ]
      }
    }" \
    ${ONOS_API}/of:${DEVICE}

  # ARP 流量
  curl -u $AUTH -X POST -H "Content-Type: application/json" \
    -d "{
      \"priority\": 50000,
      \"isPermanent\": true,
      \"deviceId\": \"of:${DEVICE}\",
      \"selector\": {
        \"criteria\": [
          { \"type\": \"ETH_TYPE\", \"ethType\": \"0x0806\" },
          { \"type\": \"IN_PORT\", \"port\": \"${IN}\" }
        ]
      },
      \"treatment\": {
        \"instructions\": [ { \"type\": \"OUTPUT\", \"port\": \"${OUT}\" } ]
      }
    }" \
    ${ONOS_API}/of:${DEVICE}
}

S1_FREE5GC=$(sudo ovs-vsctl get Interface enp0s8 ofport)
S1_S3=$(sudo ovs-vsctl get Interface s1_s3 ofport)

S3_S1=$(sudo ovs-vsctl get Interface s3_s1 ofport)
S3_S5=$(sudo ovs-vsctl get Interface s3_s5 ofport)

S5_S3=$(sudo ovs-vsctl get Interface s5_s3 ofport)
S5_SERVER=$(sudo ovs-vsctl get Interface enp0s10 ofport)

# s1: h1 <-> s3
add_flow "0000000000000001" $S1_FREE5GC $S1_S3
add_flow "0000000000000001" $S1_S3 $S1_FREE5GC

# s3: s1 <-> h3, s3 <-> s5
add_flow "0000000000000003" $S3_S1 $S3_S5
add_flow "0000000000000003" $S3_S5 $S3_S1

# # s5: s3 <-> h5
add_flow "0000000000000005" $S5_S3 $S5_SERVER
add_flow "0000000000000005" $S5_SERVER $S5_S3


sudo ufw disable
sudo sysctl -w net.ipv4.ip_forward=1