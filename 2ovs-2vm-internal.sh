sudo ovs-vsctl add-br s1 -- set bridge s1 other-config:datapath-id=0000000000000001 \
    -- set bridge s1 protocols=OpenFlow14 \
    -- set-controller s1 tcp:127.0.0.1:6653
sudo ovs-vsctl add-br s5 -- set bridge s5 other-config:datapath-id=0000000000000005 \
    -- set bridge s5 protocols=OpenFlow14 \
    -- set-controller s5 tcp:127.0.0.1:6653

sudo ip link set enp0s8 up
sudo ip link set enp0s9 up
sudo ovs-vsctl add-port s1 enp0s8
sudo ovs-vsctl add-port s5 enp0s9
sudo ovs-vsctl show

sudo ovs-vsctl add-port s1 s1_s5 -- set interface s1_s5 type=patch options:peer=s5_s1
sudo ovs-vsctl add-port s5 s5_s1 -- set interface s5_s1 type=patch options:peer=s1_s5
sudo ip link set enp0s8 promisc on
sudo ip link set enp0s9 promisc on

sudo ifconfig s1 up
sudo ifconfig s5 up
sudo ip addr add 192.170.10.20/24 dev s1
sudo ip addr add 192.170.20.20/24 dev s5

sudo ufw disable
sudo sysctl -w net.ipv4.ip_forward=1