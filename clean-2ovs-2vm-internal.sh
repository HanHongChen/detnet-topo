sudo ovs-vsctl del-br s1
sudo ovs-vsctl del-br s5
sudo ovs-vsctl show

sudo ip link set enp0s8 down
sudo ip link set enp0s9 down
ip a