from mininet.topo import Topo

class DisjointTopo(Topo):
    def build(self):
        # 建立 6 個 switch
        s1 = self.addSwitch('s1')
        s2 = self.addSwitch('s2')
        s3 = self.addSwitch('s3')
        s4 = self.addSwitch('s4')
        s5 = self.addSwitch('s5')
        s6 = self.addSwitch('s6')

        # 第一層連接（1-2, 1-4, 2-3, 2-4）
        self.addLink(s1, s4)
        self.addLink(s1, s3)
        self.addLink(s2, s3)
        self.addLink(s2, s4)

        # 第二層連接（3-4, 3-6, 4-5, 4-6）
        self.addLink(s3, s6)
        self.addLink(s3, s5)
        self.addLink(s4, s5)
        self.addLink(s4, s6)

    # 可自行加 host 連到每個 switch
        h1 = self.addHost('h1', mac='00:00:00:00:00:01', ip='10.0.0.1/24')
        h2 = self.addHost('h2', mac='00:00:00:00:00:02', ip='10.0.0.2/24')
        h3 = self.addHost('h3', mac='00:00:00:00:00:03', ip='10.0.0.3/24')
        h4 = self.addHost('h4', mac='00:00:00:00:00:04', ip='10.0.0.4/24')
        h5 = self.addHost('h5', mac='00:00:00:00:00:05', ip='10.0.0.5/24')
        h6 = self.addHost('h6', mac='00:00:00:00:00:06', ip='10.0.0.6/24')
        self.addLink(h1, s1)
        self.addLink(h2, s2)
        self.addLink(h3, s3)
        self.addLink(h4, s4)
        self.addLink(h5, s5)
        self.addLink(h6, s6)

topos = {'dualpath': (lambda: DisjointTopo())}