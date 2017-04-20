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
		   // ����һ���������������
        zk = new ZooKeeper("192.168.72.134:2181", 60000, new Watcher() {
            // ������б��������¼�
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
		 // �鿴���ڵ�
        System.out.println("ls / =======> " + zk.getChildren("/", true));

        // ����һ��Ŀ¼�ڵ�
        if (zk.exists("/node", true) == null) {
            zk.create("/node", "ja0ck5".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node ja0ck5");
            // �鿴/node�ڵ�����
            System.out.println("get /node =======> " + new String(zk.getData("/node", true, new Stat())));
            // �鿴���ڵ�
            System.out.println("ls / =======> " + zk.getChildren("/", true));
        }

        // ����һ����Ŀ¼�ڵ�
        if (zk.exists("/node/sub1", true) == null) {
            zk.create("/node/sub1", "sub1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node/sub1 sub1");
            // �鿴node�ڵ�
            System.out.println("ls /node =======> " + zk.getChildren("/node", true));
        }

        // �޸Ľڵ�����
        if (zk.exists("/node", true) != null) {
            zk.setData("/node", "changed".getBytes(), -1);
            // �鿴/node�ڵ�����
            System.out.println("get /node =======> " + new String(zk.getData("/node", false, null)));
        }

        // ɾ���ڵ�
        if (zk.exists("/node/sub1", true) != null) {
            zk.delete("/node/sub1", -1);
            zk.delete("/node", -1);
            // �鿴���ڵ�
            System.out.println("ls / =======> " + zk.getChildren("/", true));
        }

	}

}
