package com.lyj.zoo;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestZookeeper {
	
	private ZooKeeper zk;
	
	@Before
	public void setUp() throws IOException{
		   // 创建一个与服务器的连接
        zk = new ZooKeeper("192.168.72.134:2181", 60000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("EVENT:" + event.getType());
            }
        });
	}
	@After
	public void tearDown() throws InterruptedException{
		zk.close();
	}
	@Test
	public void testName() throws Exception {
		 // 查看根节点
        System.out.println("ls / =======> " + zk.getChildren("/", true));

        // 创建一个目录节点
        if (zk.exists("/node", true) == null) {
            zk.create("/node", "ja0ck5".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node ja0ck5");
            // 查看/node节点数据
            System.out.println("get /node =======> " + new String(zk.getData("/node", true, new Stat())));
            // 查看根节点
            System.out.println("ls / =======> " + zk.getChildren("/", true));
        }

        // 创建一个子目录节点
        if (zk.exists("/node/sub1", true) == null) {
            zk.create("/node/sub1", "sub1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node/sub1 sub1");
            // 查看node节点
            System.out.println("ls /node =======> " + zk.getChildren("/node", true));
        }

        // 修改节点数据
        if (zk.exists("/node", true) != null) {
            zk.setData("/node", "changed".getBytes(), -1);
            // 查看/node节点数据
            System.out.println("get /node =======> " + new String(zk.getData("/node", false, null)));
        }

        // 删除节点
        if (zk.exists("/node/sub1", true) != null) {
            zk.delete("/node/sub1", -1);
            zk.delete("/node", -1);
            // 查看根节点
            System.out.println("ls / =======> " + zk.getChildren("/", true));
        }

	}

}
