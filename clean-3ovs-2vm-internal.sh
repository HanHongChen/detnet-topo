ONOS_API="http://localhost:8181/onos/v1/flows"
AUTH="onos:rocks"


for DEVICE in of:0000000000000001 of:0000000000000003 of:0000000000000005 ; do
  # 過濾掉 appId=org.onosproject.core 的 flow
  for fid in $(curl -s -u $AUTH ${ONOS_API}/${DEVICE} \
      | jq -r '.flows[] | select(.appId!="org.onosproject.core") | .id'); do
    echo "Deleting custom flow $fid from $DEVICE"
    curl -s -u $AUTH -X DELETE ${ONOS_API}/${DEVICE}/$fid
  done
done

sudo ovs-vsctl del-br s1
sudo ovs-vsctl del-br s3
sudo ovs-vsctl del-br s5
sudo ovs-vsctl show

sudo ip link set enp0s8 down
sudo ip link set enp0s10 down
ip a