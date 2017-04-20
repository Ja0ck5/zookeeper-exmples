package com.lyj.zoo;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestQueueZooKeeper {
	
	private ZooKeeper zk;
	private static final int size = 3;;
	
	@Before
	public void setUp() throws IOException{
		   // 创建一个与服务器的连接
        zk = new ZooKeeper("192.168.1.201:2181", 60000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
            	 if (event.getPath() != null && event.getPath().equals("/queue/start") && event.getType() == Event.EventType.NodeCreated) {
                     System.out.println("Queue has Completed.Finish testing!!!");
                 }
            }
        });
	}
	@After
	public void tearDown() throws InterruptedException{
		zk.close();
	}
	
	@Test
	public void TestStandalone() throws Exception {
        initQueue(zk);
        joinQueue(zk, 1);
        joinQueue(zk, 2);
        joinQueue(zk, 3);
    }

	
	public static void joinQueue(ZooKeeper zk, int i) throws KeeperException, InterruptedException {
		 System.out.println("create /queue/x" + i + " x" + i);
	        zk.create("/queue/x" + i, ("x" + i).getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	        isCompleted(zk);		
	}
	
	public static void isCompleted(ZooKeeper zk) throws KeeperException, InterruptedException {
	        int length = zk.getChildren("/queue", true).size();
	        System.out.println("Queue Complete:" + length + "/" + size);
	        if (length >= size) {
	            System.out.println("create /queue/start start");
	            zk.create("/queue/start", "start".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	        } 
	}
	/**
	 * init 
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void initQueue(ZooKeeper zk) throws KeeperException, InterruptedException {
		System.out.println("WATCH => /queue/start");
        zk.exists("/queue/start", true);

        if (zk.exists("/queue", false) == null) {
            System.out.println("create /queue task-queue");
            zk.create("/queue", "task-queue".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            System.out.println("/queue is exist!");
        }
		
	}
	public static ZooKeeper connection(String host) throws IOException {
		 // 创建一个与服务器的连接
		return new ZooKeeper(host, 60000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("EVENT:" + event.getType());
            }
        });
	}
}
