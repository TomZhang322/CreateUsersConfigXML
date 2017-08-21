package com.adcc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Channel;
import com.adcc.ags.utility.mq.entity.Queue;
import com.adcc.ags.utility.mq.entity.Topic;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;
import com.adcc.ags.utility.mq.transfer.manager.MQManager;
import com.adcc.ags.utility.mq.transfer.manager.MQManagerImpl;
import com.adcc.util.Log;
import com.adcc.util.MQSetUp;
import com.adcc.util.PropertyUtil;
import com.adcc.utility.xml.*;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.adcc.bean.InvalidUser;
import com.adcc.bean.Route;
import com.adcc.bean.User;
import com.adcc.gui.USERSConfigShell;

public class GWUsersConfigMain {

	public static void main(String[] args) {
		try {
			//加载日志配置文件
			PropertyConfigurator.configure("./config/log4j.properties");

			Display display = Display.getDefault();
			USERSConfigShell shell = new USERSConfigShell(display);

			Map<String, User> uMap = new ConcurrentHashMap<String, User>();
			Map<String, Route> rMap = new ConcurrentHashMap<String, Route>();
			Map<String, InvalidUser> iuMap = new ConcurrentHashMap<String, InvalidUser>();

			//解析xml配置文件并显示
			String filePath = "./messages/userList.xml";
			ADCCXML ixml= ADCCXMLFactory.getInstance().createADCCXML();
			ixml.parseXMLFile(filePath);
			for(SuperNode superNode : ixml.getSuperNodeList()){
//	            System.out.println(superNode.getName());
				for(Iterator<Map.Entry<String,String>> iterator = superNode.getAttributes().entrySet().iterator();iterator.hasNext();){
					Map.Entry<String,String> entry = iterator.next();
//	                System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());
				}
//	            System.out.println(superNode.getAttributes());
//	            System.out.println("=======================");

				if(superNode.getNodeList().size() > 0){
					if(superNode.getName().equals("userList")) {
						for (Node node : superNode.getNodeList()){
//		                    System.out.println(node.getName());
							User user = new User();
							for(Iterator<Map.Entry<String,String>> iterator = node.getAttributes().entrySet().iterator();iterator.hasNext();){
								Map.Entry<String,String> entry = iterator.next();
//		                        System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());
								if(entry.getKey().equals("id")){user.setId(entry.getValue());}
								if(entry.getKey().equals("name")){user.setName(entry.getValue());}
								if(entry.getKey().equals("downlinkForward")){
									if(entry.getValue().equals("false")) {
										user.setDownlinkForward(false);
									} else {
										user.setDownlinkForward(true);
									}
								}
							}
//		                    System.out.println("----------------------");

							if(node.getUnitList().size() > 0){
								for(Unit unit : node.getUnitList()){
//		                            System.out.println(unit.getName());
									for(Iterator<Map.Entry<String,String>> iterator = unit.getAttributes().entrySet().iterator();iterator.hasNext();){
										Map.Entry<String,String> entry = iterator.next();
//		                                System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());
										if(entry.getValue().equals("SendQueue")){user.setSendQueue(unit.getText());}
										if(entry.getValue().equals("RecvQueue")){user.setRecvQueue(unit.getText());}
										if(entry.getValue().equals("MsgType")){user.setMsgType(unit.getText());}
									}
//		                            System.out.println(unit.getText());
								}
							}
//		                    System.out.println("*************************");
							if(node.getSubNodeList().size() > 0){
								for(Node childNode : node.getSubNodeList()){
//		                            System.out.println(childNode.getName());
									Route route = new Route();
									route.setUserId(user.getId());
									for(Iterator<Map.Entry<String,String>> iterator = childNode.getAttributes().entrySet().iterator();iterator.hasNext();){
										Map.Entry<String,String> entry = iterator.next();
//		                                System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());

										if(entry.getKey().equals("id")) {route.setId(entry.getValue());}
										if(entry.getKey().equals("name")) {route.setName(entry.getValue());}
										if(entry.getKey().equals("type")) {route.setType(entry.getValue());}

									}
									if(childNode.getUnitList().size() > 0){
										for(Unit unit : childNode.getUnitList()){
//		                                    System.out.println(unit.getName());
											for(Iterator<Map.Entry<String,String>> iterator = unit.getAttributes().entrySet().iterator();iterator.hasNext();){
												Map.Entry<String,String> entry = iterator.next();
//		                                        System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());

												if(entry.getValue().equals("SendAddress")) {route.setSendAddress(unit.getText());}
												if(entry.getValue().equals("RecvAddress")) {route.setRecvAddress(unit.getText());}
												if(entry.getValue().equals("Destination")) {route.setDestination(unit.getText());}
												if(entry.getValue().equals("SMI")) {route.setSmi(unit.getText());}
												if(entry.getValue().equals("FI")) {route.setFi(unit.getText());}
												if(entry.getValue().equals("AN")) {route.setAn(unit.getText());}
												if(entry.getValue().equals("RGS")) {route.setRgs(unit.getText());}
												if(entry.getKey().equals("index")){route.setIndex(entry.getValue());}
												if(entry.getValue().equals("SpecLabel")){route.setSpecLabel(unit.getText());}
											}
//		                                    System.out.println(unit.getText());	                                    
										}
									}
									rMap.put(route.getId(), route);
								}
							}
							uMap.put(user.getId(), user);
						}
					}
				}
			}
			shell.usersMap = uMap;
			shell.routesMap = rMap;
			//给用户table赋值
			int signNum = 0;
			for(String userKey : shell.usersMap.keySet()) {
				User user = shell.usersMap.get(userKey);
				String downlink = "";
				if(user.isDownlinkForward() == false) {
					downlink = "否";
				} else {
					downlink = "是";
				}
				TableItem  item = new TableItem(shell.users_table,SWT.NONE);
				item.setText(new String[] {user.getId(), user.getName(), user.getSendQueue(), user.getRecvQueue(),
						user.getMsgType(), downlink});
				//只显示第一个用户的路由				
				signNum ++;
				if(signNum == 1) {
					for(String routeKey : shell.routesMap.keySet()) {
						Route route = shell.routesMap.get(routeKey);
						if(user.getId().equals(route.getUserId())) {
							String routeChooseId = route.getId();
							Route chooseR = shell.routesMap.get(routeChooseId);
							TableItem  item2 = new TableItem(shell.router_table, SWT.NONE);
							item2.setText(new String[] {chooseR.getId(), chooseR.getName(), chooseR.getType(), chooseR.getUserId()});
						}
					}
				}

			}
			//给路由table赋值
//			for(String routeKey : shell.routesMap.keySet()) {
//				Route route = shell.routesMap.get(routeKey);
//				TableItem  item2 = new TableItem(shell.router_table, SWT.NONE);
//				item2.setText(new String[] {route.getId(), route.getName(), route.getType(), route.getUserId()});
//			}

			//启动时，把指定队列管理器的所有队列、主题、通道，加载到列表中
			try {
				MQSetUp.setUp();
				MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
                        MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
				List<Queue> listQueue = manager.findQueue(PropertyUtil.getProperty("config/createxml.properties", "queueSpecil"),Queue.CONSTANT_LOCAL);
				List<Channel> listChannel = manager.findChannel(PropertyUtil.getProperty("config/createxml.properties","channelSpecil"), Channel.CONSTANT_SVRCONN);
				List<Topic> listTopic = manager.findTopic(PropertyUtil.getProperty("config/createxml.properties","topicSpecil"),Topic.LOCAL_TOPIC);
				shell.queueComposite.qList = listQueue;
				shell.channelComposite.listC = listChannel;
				shell.topicComposite.listTopic = listTopic;

				shell.queueComposite.updateQueueTable();
				shell.channelComposite.updateChannelTable();
				shell.topicComposite.updateTopicTable();
			} catch (Exception e) {
				Log.error("MQ Server error",e);
			}

			//解析黑名单xml
			String filePath2 = "./messages/blackList.xml";
			ADCCXML ixml2= ADCCXMLFactory.getInstance().createADCCXML();
			ixml2.parseXMLFile(filePath2);
			for(SuperNode superNode : ixml2.getSuperNodeList()){
				if(superNode.getNodeList().size() > 0){
					if(superNode.getName().equals("blackList")) {
						for (Node node : superNode.getNodeList()){
							InvalidUser inUser = new InvalidUser();
							for(Iterator<Map.Entry<String,String>> iterator = node.getAttributes().entrySet().iterator();iterator.hasNext();){
								Map.Entry<String,String> entry = iterator.next();
//			                     System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());
								if(entry.getKey().equals("id")){inUser.setId(entry.getValue());}
								if(entry.getKey().equals("name")){inUser.setName(entry.getValue());}
							}
							if(node.getUnitList().size() > 0){
								for(Unit unit : node.getUnitList()){
									for(Iterator<Map.Entry<String,String>> iterator = unit.getAttributes().entrySet().iterator();iterator.hasNext();){
										Map.Entry<String,String> entry = iterator.next();
//                                        System.out.println("attribute:" + entry.getKey() + " " + entry.getValue());                                       
										if(entry.getValue().equals("SendAddress")) {inUser.setSendAddress(unit.getText());}
										if(entry.getValue().equals("RecvAddress")) {inUser.setRecvAddress(unit.getText());}
										if(entry.getValue().equals("SMI")) {inUser.setSmi(unit.getText());}
										if(entry.getValue().equals("AN")) {inUser.setAn(unit.getText());}
										if(entry.getKey().equals("index")){inUser.setIndex(entry.getValue());}
										if(entry.getValue().equals("SpecLabel")){inUser.setSpecLabel(unit.getText());}
									}
								}
							}
							iuMap.put(inUser.getId(), inUser);
						}
					}
				}
			}
			shell.inUsersMap = iuMap;
			//给黑名单table赋值
			for(String iuKey : shell.inUsersMap.keySet()) {
				InvalidUser iuser = shell.inUsersMap.get(iuKey);
				TableItem  item = new TableItem(shell.inUser_table, SWT.NONE);
				item.setText(new String[] {iuser.getId(), iuser.getName(), iuser.getRecvAddress(), iuser.getSendAddress(),
						iuser.getSmi(), iuser.getAn(), iuser.getSpecLabel(), iuser.getIndex()});
			}

			shell.open();
			shell.layout();
			Log.info("-------------success open!");
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
