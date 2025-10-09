IFACE0=enp0s9
IFACE1=enp0s8
IFACE2=enp0s16
IFACE3=enp0s10
sudo tc qdisc add dev $IFACE0 root handle 1: netem delay 0ms 2>/dev/null || true
sudo tc qdisc add dev $IFACE1 root handle 1: netem delay 0ms 2>/dev/null || true
sudo tc qdisc add dev $IFACE2 root handle 1: netem delay 0ms 2>/dev/null || true
sudo tc qdisc add dev $IFACE3 root handle 1: netem delay 0ms 2>/dev/null || true

sudo tc qdisc change dev $IFACE0 root netem loss 100%   # 先開 B
sudo tc qdisc change dev $IFACE1 root netem loss 100% # 再關 A
sudo tc qdisc change dev $IFACE2 root netem loss 100%   # 先開 B
sudo tc qdisc change dev $IFACE3 root netem loss 100% # 再關 A
sleep 5
# sudo tc qdisc change dev $IFACE1 root netem loss 0%   # 先開 B
# sudo tc qdisc change dev $IFACE0 root netem loss 100% # 再關 A
# sleep 10
# sudo tc qdisc change dev $IFACE0 root netem loss 0%   # 先開 A
# sudo tc qdisc change dev $IFACE1 root netem loss 100% # 再關 B
# sleep 10
# sudo tc qdisc change dev $IFACE1 root netem loss 0%   # 先開 B
# sudo tc qdisc change dev $IFACE0 root netem loss 100% # 再關 A
# sleep 10
# sudo tc qdisc change dev $IFACE0 root netem loss 0%   # 先開 A
# sudo tc qdisc change dev $IFACE1 root netem loss 100% # 再關 B
# sleep 10
sudo tc qdisc change dev $IFACE1 root netem loss 0%   # 先開 B
sudo tc qdisc change dev $IFACE0 root netem loss 0% # 再關 A
sudo tc qdisc change dev $IFACE2 root netem loss 0%   # 先開 B
sudo tc qdisc change dev $IFACE3 root netem loss 0% # 再關 A
