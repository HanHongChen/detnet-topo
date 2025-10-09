#!/bin/bash

# 參數
DURATION=60
INTERVAL=10

GROUP_B=(enp0s8 enp0s10)
GROUP_A=(enp0s9 enp0s16)

for IFACE in "${GROUP_A[@]}" "${GROUP_B[@]}"; do
    sudo tc qdisc add dev $IFACE root handle 1: netem delay 0ms 2>/dev/null || true
done

LOOPS=$((DURATION / INTERVAL))

for ((i=0; i<$LOOPS; i++)); do
    if ((i % 2 == 0)); then
        for IFACE in "${GROUP_B[@]}"; do
            sudo tc qdisc change dev $IFACE root netem loss 0%
        done

        for IFACE in "${GROUP_A[@]}"; do
            sudo tc qdisc change dev $IFACE root netem loss 100%
        done
        
    else
        for IFACE in "${GROUP_A[@]}"; do
            sudo tc qdisc change dev $IFACE root netem loss 0%
        done
        for IFACE in "${GROUP_B[@]}"; do
            sudo tc qdisc change dev $IFACE root netem loss 100%
        done
        
    fi
    sleep $INTERVAL
done

# 全部恢復（loss 0%）
for IFACE in "${GROUP_A[@]}" "${GROUP_B[@]}"; do
    sudo tc qdisc change dev $IFACE root netem loss 0%
done
echo "全部恢復"