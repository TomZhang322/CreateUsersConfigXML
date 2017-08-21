package com.adcc.gui;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adcc.util.Log;
import com.adcc.utility.xml.ADCCXML;
import com.adcc.utility.xml.ADCCXMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import org.dom4j.Document;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.adcc.util.MD5Util;
import com.adcc.util.PropertyUtil;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
//import org.json.JSONObject;

import com.adcc.bean.InvalidUser;
import com.adcc.bean.Route;
import com.adcc.bean.User;
import com.adcc.service.XMLHandlerService;

import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Composite;

public class USERSConfigShell extends Shell {
	public static USERSConfigShell mainShell;
	public Table users_table;
	public Table router_table;
	public Map<String, User> usersMap = new ConcurrentHashMap<String, User>();
	public Map<String, Route> routesMap = new ConcurrentHashMap<String, Route>();
	public Map<String, InvalidUser> inUsersMap = new ConcurrentHashMap<String, InvalidUser>();
	XMLHandlerService xmlHandlerService = new XMLHandlerService();
	private String activeUserConfigXML = "";
	//	MQManagerImpl mqmService = new MQManagerImpl();
	public QueueComposite queueComposite;
	public ChannelComposite channelComposite;
	public TopicComposite topicComposite;
	private Tree MQ_tree;
	public Table inUser_table;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			USERSConfigShell shell = new USERSConfigShell(display);
			mainShell = shell;
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			Log.error("-----------fail open!", e);
		}
	}

	/**
	 * 新增、修改黑名单用户时更新黑名单用户列表
	 */
	public void updateInUserTable() {
		inUser_table.removeAll();
		for(String inUserKey : inUsersMap.keySet()) {
			InvalidUser inUser = inUsersMap.get(inUserKey);
			TableItem  item = new TableItem(inUser_table,SWT.NONE);
			item.setText(new String[] {inUser.getId(), inUser.getName(), inUser.getSendAddress(), inUser.getRecvAddress(),
					inUser.getSmi(), inUser.getAn(), inUser.getSpecLabel(), inUser.getIndex()});
		}
	}

	/**
	 * 新增、修改用户时更新用户列表
	 */
	public void updateUserTable() {
		users_table.removeAll();
		for(String userKey : usersMap.keySet()) {
			User user = usersMap.get(userKey);
			String downlink = "";
			if(user.isDownlinkForward() == false) {
				downlink = "否";
			} else {
				downlink = "是";
			}
			TableItem  item = new TableItem(users_table,SWT.NONE);
			item.setText(new String[] {user.getId(), user.getName(), user.getSendQueue(), user.getRecvQueue(),
					user.getMsgType(), downlink});
		}
	}

	/**
	 * 新增、修改路由时更新路由列表
	 */
	public void updateRouteTable() {
		router_table.removeAll();
		for(String routeKey : routesMap.keySet()) {
			Route route = routesMap.get(routeKey);
			TableItem  item = new TableItem(router_table, SWT.NONE);
			item.setText(new String[] {route.getId(), route.getName(), route.getType(), route.getUserId()});
		}
	}

	/**
	 * 选中某行用户时，更新路由表数据
	 * 新增、修改路由时，显示当前对应用户的所有路由
	 * @param uid
	 */
	public void updateRouteTable3(String uid) {
		router_table.removeAll();
		for(String routeKey : routesMap.keySet()) {
			Route route = routesMap.get(routeKey);
			if(uid.equals(route.getUserId())) {
				String routeChooseId = route.getId();
				Route chooseR = routesMap.get(routeChooseId);
				TableItem  item = new TableItem(router_table, SWT.NONE);
				item.setText(new String[] {chooseR.getId(), chooseR.getName(), chooseR.getType(), chooseR.getUserId()});
			}
		}
	}

	/**
	 * 删除用户校验
	 * @param uid
	 * @return
	 */
/*	private boolean isUsedByRoute(String uid) {
		boolean flag = false;
		for(String routeKey : routesMap.keySet()) {
			Route route = routesMap.get(routeKey);
			if(uid.equals(route.getUserId())) {
				flag = true;
			}
		}		
		return flag;		
	}*/

	/**
	 * 删除用户和用户下的所有路由
	 * @param uid
	 */
	private void delChooseUserAndRoutes(String uid) {
		router_table.removeAll();
		for(String routeKey : routesMap.keySet()) {
			Route route = routesMap.get(routeKey);
			if(uid.equals(route.getUserId())) {
				routesMap.remove(route.getId());
			}
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public USERSConfigShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setText("\u7528\u6237\u914D\u7F6E\u63A8\u9001\u4E3B\u9762\u677F");
		setSize(1347, 823);
		setToolTipText("");
		mainShell = this;
		Button addUserBtn = new Button(this, SWT.NONE);
		addUserBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new UserAddDialog(Display.getCurrent().getActiveShell(), mainShell, SWT.DIALOG_TRIM).open();//SWT.RESIZE SWT.NONE
			}
		});
		addUserBtn.setBounds(340, 25, 80, 27);
		addUserBtn.setText("\u65B0\u589E");

		users_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		users_table.setBounds(21, 65, 601, 251);
		users_table.setHeaderVisible(true);
		users_table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(users_table, SWT.NONE);
		tblclmnNewColumn.setWidth(88);
		tblclmnNewColumn.setText("    \u7528\u6237ID");

		TableColumn tblclmnNewColumn_1 = new TableColumn(users_table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(84);
		tblclmnNewColumn_1.setText("\u7528\u6237\u540D");

		TableColumn tableColumn = new TableColumn(users_table, SWT.NONE);
		tableColumn.setWidth(107);
		tableColumn.setText("\u4E0A\u884C\u961F\u5217");

		TableColumn tableColumn_1 = new TableColumn(users_table, SWT.NONE);
		tableColumn_1.setWidth(102);
		tableColumn_1.setText("\u4E0B\u884C\u961F\u5217");

		TableColumn tblclmnNewColumn_7 = new TableColumn(users_table, SWT.NONE);
		tblclmnNewColumn_7.setWidth(106);
		tblclmnNewColumn_7.setText("\u6D88\u606F\u7C7B\u578B");

		TableColumn tblclmnNewColumn_2 = new TableColumn(users_table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(109);
		tblclmnNewColumn_2.setText("\u4E0B\u884C\u662F\u5426\u5168\u90E8\u8F6C\u53D1");

		//选中用户表某一行时，更新对应用户的路由表信息
		TableCursor cursor = new TableCursor(users_table, SWT.NONE);
		cursor.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				TableItem t = users_table.getItem(users_table.getSelectionIndex());
				String key = t.getText(0);
				updateRouteTable3(key);
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button delUserBtn = new Button(this, SWT.NONE);
		delUserBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(users_table.getSelectionIndex() != -1) {
					//删除用户校验，用户是否已被路由配置					
/*					TableItem t = users_table.getItem(users_table.getSelectionIndex());
					String key = t.getText(0);
					boolean usedFlag = isUsedByRoute(key);
					if(usedFlag) {
						// 新增路由ID重复，需要弹框提示
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("用户ID为"+key+"的用户已被路由占用，请先删除对应的路由!");
						messageBox.open();
					} else {
						usersMap.remove(key);
						users_table.remove(users_table.getSelectionIndex());
					}*/

					//删除用户时，用户对应的路由也同时删除
					TableItem t = users_table.getItem(users_table.getSelectionIndex());
					String key = t.getText(0);
					delChooseUserAndRoutes(key);
					usersMap.remove(key);
					users_table.remove(users_table.getSelectionIndex());
				}
			}
		});
		delUserBtn.setBounds(531, 25, 80, 27);
		delUserBtn.setText("\u5220\u9664");

		Button creUserConfig = new Button(this, SWT.NONE);
		creUserConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					boolean flag = false;
					try {
						Document document = xmlHandlerService.pushGWXml(usersMap, routesMap);
						if(document != null) {
							String filePath = "./messages/userList.xml";
							XMLWriter writer = null;
							writer = new XMLWriter(new FileWriter(filePath), OutputFormat.createPrettyPrint());
							writer.write(document);
							writer.close();
							flag = true;
						}
					} catch (Exception ex) {
						Log.error("-----------write userList.xml to local fail!", ex);
					}

					if(flag) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("保存配置成功!");
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("保存配置失败!");
						messageBox.open();
					}
				} catch (Exception e1) {
					Log.error("-----------create userList.xml fail!", e1);
				}

			}
		});
		creUserConfig.setBounds(405, 634, 80, 27);
		creUserConfig.setText("\u4FDD\u5B58\u7528\u6237\u914D\u7F6E");

		Button readUserConfig = new Button(this, SWT.NONE);
		readUserConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				Document document = xmlHandlerService.pushGWXml(usersMap, routesMap);
				//推送至CGW
				try {
					String response = null;
					try {
						File file = new File("./messages/userList.xml");

//					FileInputStream fis = new FileInputStream(file);
//					FileDataBodyPart fileData = new FileDataBodyPart("file",file);
//					IXML ixml = new ADCCXML();
//					ixml.parse(fis);
//					String strMD5 = MD5Util.getMD5(ixml.createXMLString());

						System.out.println("#########################");
//						System.out.println(document.asXML());
						ADCCXML adccxml = ADCCXMLFactory.getInstance().createADCCXML();
						adccxml.parseXMLFile("./messages/userList.xml");
						String strMD5 = MD5Util.getMD5(adccxml.createXMLString());
						System.out.println(adccxml.createXMLString());
						System.out.println("strMD5 :" + strMD5);
						System.out.println("#########################");

//						String strMD5 = MD5Util.getMD5(document.asXML());
						FileDataBodyPart fileData = new FileDataBodyPart("file",file);
						FormDataMultiPart multiPart = new FormDataMultiPart();
						multiPart.field("MD5",strMD5).bodyPart(fileData);
						Client client = ClientBuilder.newClient().register(MultiPartFeature.class);
						String targetURL = PropertyUtil.getProperty("config/createxml.properties", "url");
						response = client.target(targetURL)
                                .request()
                                .post(Entity.entity(multiPart, multiPart.getMediaType())).readEntity(String.class);
						System.out.println(response);
					} catch (Exception e1) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("推送用户配置失败，服务器连接有问题!");
						messageBox.open();
						Log.error("-----------推送用户配置失败，服务器连接有问题!", e1);
					}
					//给出提示
					if(response.equals("")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("推送配置失败!");
						messageBox.open();
					}

					Map<String, String> map = new ObjectMapper().readValue(response, Map.class);
					if (map.get("result").equals("SUCCESS")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("推送配置成功!");
						messageBox.open();
//						router_table.removeAll();
//						users_table.removeAll();
//						routesMap.clear();
//						usersMap.clear();
					} else {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("推送配置失败!");
						messageBox.open();
					}

/*					JSONObject jsonObj = new JSONObject(response);
					Iterator<String> nameItr = jsonObj.keys();
					String resultValue = "";
					while (nameItr.hasNext()) {
						String name = nameItr.next();
						if(name.equals("result")) {
							resultValue = jsonObj.getString(name);
							if (resultValue.equals("SUCCESS")) {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
								messageBox.setMessage("推送配置成功!");
								messageBox.open();
								router_table.removeAll();
								users_table.removeAll();
								routesMap.clear();
								usersMap.clear();
							} else {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
								messageBox.setMessage("推送配置失败!");
								messageBox.open();
							}
						}
					}*/
				} catch (Exception e1) {
					Log.error("-----------推送用户配置失败!", e1);
				}
			}
		});
		readUserConfig.setBounds(517, 634, 80, 27);
		readUserConfig.setText("\u63A8\u9001\u7528\u6237\u914D\u7F6E");

		Label label = new Label(this, SWT.NONE);
		label.setBounds(21, 25, 95, 17);
		label.setText("\u7528\u6237\u914D\u7F6E\u5217\u8868:");

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(25, 335, 111, 17);
		lblNewLabel.setText("\u7528\u6237\u7684\u8DEF\u7531\u4FE1\u606F:");

		router_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		router_table.setBounds(21, 382, 534, 228);
		router_table.setHeaderVisible(true);
		router_table.setLinesVisible(true);

		TableColumn tblclmnid = new TableColumn(router_table, SWT.NONE);
		tblclmnid.setWidth(126);
		tblclmnid.setText("      \u8DEF\u7531ID");

		TableColumn tblclmnNewColumn_3 = new TableColumn(router_table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(127);
		tblclmnNewColumn_3.setText("\u8DEF\u7531\u540D\u79F0");

		TableColumn tblclmnNewColumn_9 = new TableColumn(router_table, SWT.NONE);
		tblclmnNewColumn_9.setText("\u8DEF\u7531\u7C7B\u578B\r\n");
		tblclmnNewColumn_9.setWidth(131);

		TableColumn tblclmnNewColumn_11 = new TableColumn(router_table, SWT.NONE);
		tblclmnNewColumn_11.setWidth(146);
		tblclmnNewColumn_11.setText("            \u6240\u5C5E\u7528\u6237ID");



		Button routerAddBtn = new Button(this, SWT.NONE);
		routerAddBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new RouterAddDialog(Display.getCurrent().getActiveShell(), usersMap, mainShell, SWT.DIALOG_TRIM).open();

			}
		});
		routerAddBtn.setBounds(275, 349, 80, 27);
		routerAddBtn.setText("\u65B0\u589E");

		Button routerDelBtn = new Button(this, SWT.NONE);
		routerDelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(router_table.getSelectionIndex() != -1) {
					TableItem t = router_table.getItem(router_table.getSelectionIndex());
					String key = t.getText(0);
					routesMap.remove(key);
					router_table.remove(router_table.getSelectionIndex());
				}
			}
		});
		routerDelBtn.setBounds(475, 349, 80, 27);
		routerDelBtn.setText("\u5220\u9664");

		Button editUserBtn = new Button(this, SWT.NONE);
		editUserBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(users_table.getSelectionIndex() != -1) {
					TableItem t = users_table.getItem(users_table.getSelectionIndex());
					String[] usersStr = {t.getText(0), t.getText(1), t.getText(2), t.getText(3), t.getText(4), t.getText(5)};
					new UserEditDialog(Display.getCurrent().getActiveShell(), mainShell, usersStr, SWT.DIALOG_TRIM).open();
				}
			}
		});
		editUserBtn.setText("\u4FEE\u6539");
		editUserBtn.setBounds(445, 25, 80, 27);

		Button editRouteBtn = new Button(this, SWT.NONE);
		editRouteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(router_table.getSelectionIndex() != -1) {
					TableItem t = router_table.getItem(router_table.getSelectionIndex());
					Route r = routesMap.get(t.getText(0));
					String[] routesStr = {t.getText(0), t.getText(1), t.getText(2), t.getText(3), r.getSendAddress(),
							r.getRecvAddress(), r.getDestination(), r.getSmi(), r.getFi(), r.getAn(), r.getRgs(), r.getSpecLabel(), r.getIndex()};
					new RouterEditDialog(Display.getCurrent().getActiveShell(), mainShell, routesStr, usersMap, SWT.DIALOG_TRIM).open();
				}
			}
		});

		editRouteBtn.setText("\u4FEE\u6539");
		editRouteBtn.setBounds(375, 349, 80, 27);

		Composite upMQ_composite = new Composite(this, SWT.NONE);
		upMQ_composite.setBounds(640, 65, 392, 118);

		MQ_tree = new Tree(upMQ_composite, SWT.BORDER);
		MQ_tree.setBounds(21, 10, 349, 104);
		MQ_tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				treeElemenSelected(e);

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		TreeItem qmElemen = new TreeItem(MQ_tree,SWT.NONE);
		qmElemen.setText("MQ");

		TreeItem queueElemen = new TreeItem(qmElemen,SWT.NONE);
		queueElemen.setText("队列");

		TreeItem topicElemen = new TreeItem(qmElemen,SWT.NONE);
		topicElemen.setText("主题");

		TreeItem channelElemen = new TreeItem(qmElemen,SWT.NONE);
		channelElemen.setText("通道");

		Composite downMQ_composite = new Composite(this, SWT.NONE);
		downMQ_composite.setBounds(640, 189, 670, 294);

		queueComposite = new QueueComposite(downMQ_composite, SWT.NONE);
		queueComposite.setSize(660, 284);
		queueComposite.setLocation(0, 0);

		topicComposite = new TopicComposite(downMQ_composite, SWT.NONE);
		topicComposite.setSize(593, 231);
		topicComposite.setLocation(10, 0);

		channelComposite = new ChannelComposite(downMQ_composite, SWT.NONE);
		channelComposite.setSize(593, 231);
		channelComposite.setLocation(10, 0);

		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setBounds(652, 25, 111, 17);
		lblNewLabel_1.setText("IBMMQ\u914D\u7F6E\u4FE1\u606F:");

		inUser_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		inUser_table.setBounds(652, 560, 648, 181);
		inUser_table.setHeaderVisible(true);
		inUser_table.setLinesVisible(true);

		TableColumn tblclmnNewColumn_4 = new TableColumn(inUser_table, SWT.NONE);
		tblclmnNewColumn_4.setWidth(85);
		tblclmnNewColumn_4.setText("\u9ED1\u540D\u5355\u7528\u6237ID");

		TableColumn tblclmnNewColumn_5 = new TableColumn(inUser_table, SWT.NONE);
		tblclmnNewColumn_5.setWidth(87);
		tblclmnNewColumn_5.setText("\u9ED1\u540D\u5355\u7528\u6237\u540D");

		TableColumn tableColumn_2 = new TableColumn(inUser_table, SWT.NONE);
		tableColumn_2.setWidth(73);
		tableColumn_2.setText("\u53D1\u9001\u5730\u5740");

		TableColumn tableColumn_3 = new TableColumn(inUser_table, SWT.NONE);
		tableColumn_3.setWidth(75);
		tableColumn_3.setText("\u63A5\u6536\u5730\u5740");

		TableColumn tblclmnSmi = new TableColumn(inUser_table, SWT.NONE);
		tblclmnSmi.setWidth(73);
		tblclmnSmi.setText("SMI");

		TableColumn tblclmnAn = new TableColumn(inUser_table, SWT.NONE);
		tblclmnAn.setWidth(83);
		tblclmnAn.setText("\u673A\u5C3E\u53F7(AN)");

		TableColumn tblclmnNewColumn_8 = new TableColumn(inUser_table, SWT.NONE);
		tblclmnNewColumn_8.setWidth(78);
		tblclmnNewColumn_8.setText("\u5168\u6587\u5173\u952E\u5B57");

		TableColumn tblclmnNewColumn_10 = new TableColumn(inUser_table, SWT.NONE);
		tblclmnNewColumn_10.setWidth(89);
		tblclmnNewColumn_10.setText("\u5173\u952E\u5B57\u8D77\u59CB\u4F4D");

		Button inUserAdd_btn = new Button(this, SWT.NONE);
		inUserAdd_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//黑名单用户新增页面
				new InvalidUserAddDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, mainShell).open();
			}
		});
		inUserAdd_btn.setBounds(996, 527, 80, 27);
		inUserAdd_btn.setText("\u65B0\u589E");

		Button inUserEdit_btn = new Button(this, SWT.NONE);
		inUserEdit_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//黑名单用户编辑页面				
				if(inUser_table.getSelectionIndex() != -1) {
					TableItem t = inUser_table.getItem(inUser_table.getSelectionIndex());
					String[] inUserArry = {t.getText(0), t.getText(1), t.getText(2), t.getText(3), t.getText(4),
							t.getText(5), t.getText(6), t.getText(7)};
					new InvalidUserEditDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, mainShell.mainShell, inUserArry).open();
				}
			}
		});
		inUserEdit_btn.setBounds(1082, 527, 80, 27);
		inUserEdit_btn.setText("\u4FEE\u6539");

		Button inUserDel_btn = new Button(this, SWT.NONE);
		inUserDel_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//黑名单用户删除			
				if(inUser_table.getSelectionIndex() != -1) {
					TableItem t = inUser_table.getItem(inUser_table.getSelectionIndex());
					String key = t.getText(0);
					inUsersMap.remove(key);
					inUser_table.remove(inUser_table.getSelectionIndex());
				}

			}
		});
		inUserDel_btn.setBounds(1168, 527, 80, 27);
		inUserDel_btn.setText("\u5220\u9664");

		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setBounds(652, 498, 111, 17);
		lblNewLabel_2.setText("\u9ED1\u540D\u5355\u7528\u6237\u7EF4\u62A4:");

		Button saveInUser_btn = new Button(this, SWT.NONE);
		saveInUser_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//生成并保存黑名单配置
				boolean flag = xmlHandlerService.saveInUserXML(inUsersMap);
				if(flag) {
					MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
					messageBox.setMessage("保存黑名单用户配置成功!");
					messageBox.open();
				} else {
					MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("保存黑名单用户配置失败!");
					messageBox.open();
				}
			}
		});
		saveInUser_btn.setBounds(1042, 747, 95, 27);
		saveInUser_btn.setText("\u4FDD\u5B58\u9ED1\u540D\u5355\u914D\u7F6E");

		Button pushInUser_btn = new Button(this, SWT.NONE);
		pushInUser_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//推送黑名单配置
					String response = null;
					try {
						System.out.println("********************");
//						String inUserXML = xmlHandlerService.createInuserXML(inUsersMap);
						File file = new File("./messages/blackList.xml");
						ADCCXML adccxml = ADCCXMLFactory.getInstance().createADCCXML();
						adccxml.parseXMLFile("./messages/blackList.xml");
						System.out.println(adccxml.createXMLString());
						String strMD5 = MD5Util.getMD5(adccxml.createXMLString());
						System.out.println("strMD5 :" + strMD5);
						System.out.println("********************");

//						String strMD5 = MD5Util.getMD5(inUserXML);
						FileDataBodyPart fileData = new FileDataBodyPart("file",file);
						FormDataMultiPart multiPart = new FormDataMultiPart();
						multiPart.field("MD5",strMD5).bodyPart(fileData);
						Client client = ClientBuilder.newClient().register(MultiPartFeature.class);
						String targetURL = PropertyUtil.getProperty("config/createxml.properties", "url2");
						response = client.target(targetURL)
                                .request()
                                .post(Entity.entity(multiPart, multiPart.getMediaType())).readEntity(String.class);
						System.out.println(response);
					} catch (Exception e1) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("推送黑名单配置失败，服务器连接有问题!");
						messageBox.open();
						Log.error("-----------推送黑名单配置失败，服务器连接有问题!", e1);
					}

					//给出提示
					if(response.equals("")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("推送黑名单配置失败!");
						messageBox.open();
					}
					Map<String, String> map = new ObjectMapper().readValue(response, Map.class);
					if (map.get("result").equals("SUCCESS")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("推送黑名单配置成功!");
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("推送黑名单配置失败!");
						messageBox.open();
					}

/*					JSONObject jsonObj = new JSONObject(response);
					Iterator<String> nameItr = jsonObj.keys();
					String resultValue = "";
					while (nameItr.hasNext()) {
						String name = nameItr.next();
						if(name.equals("result")) {
							resultValue = jsonObj.getString(name);
							if (resultValue.equals("SUCCESS")) {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
								messageBox.setMessage("推送黑名单配置成功!");
								messageBox.open();
							} else {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
								messageBox.setMessage("推送黑名单配置失败!");
								messageBox.open();
							}
						}
					}*/
//					inUser_table.removeAll();
//					inUsersMap.clear();
				} catch (Exception e1) {
					Log.error("-----------推送黑名单配置失败!", e1);
				}
			}
		});
		pushInUser_btn.setBounds(1168, 747, 95, 27);
		pushInUser_btn.setText("\u63A8\u9001\u9ED1\u540D\u5355\u914D\u7F6E");

		Button openEmergency_btn = new Button(this, SWT.NONE);
		openEmergency_btn.setBounds(405, 694, 80, 27);
		openEmergency_btn.setText("开启应急模式");
		openEmergency_btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Client client = ClientBuilder.newClient();
					String urlOpen = PropertyUtil.getProperty("config/createxml.properties", "urlOpen");
					Response response = client.target(urlOpen).request().get();
					String strResult = response.readEntity(String.class);

					Map<String, String> map = new ObjectMapper().readValue(strResult, Map.class);
					if (map.get("result").equals("SUCCESS")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("开启应急模式成功!");
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("开启应急模式失败!");
						messageBox.open();
					}


/*					JSONObject jsonObj = new JSONObject(strResult);
					Iterator<String> nameItr = jsonObj.keys();
					String resultValue = "";
					while (nameItr.hasNext()) {
						String name = nameItr.next();
						if(name.equals("result")) {
							resultValue = jsonObj.getString(name);
							if (resultValue.equals("SUCCESS")) {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
								messageBox.setMessage("开启应急模式成功!");
								messageBox.open();
							} else {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
								messageBox.setMessage("开启应急模式失败!");
								messageBox.open();
							}
						}
					}*/

				} catch (Exception e1) {
					Log.error("-----------开启应急模式失败!",e1);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button closeEmergency_btn = new Button(this, SWT.NONE);
		closeEmergency_btn.setText("关闭应急模式");
		closeEmergency_btn.setBounds(517, 694, 80, 27);
		closeEmergency_btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Client client = ClientBuilder.newClient();
					String urlClose = PropertyUtil.getProperty("config/createxml.properties", "urlClose");
					Response response = client.target(urlClose).request().get();
					String strResult = response.readEntity(String.class);

					Map<String, String> map = new ObjectMapper().readValue(strResult, Map.class);
					if (map.get("result").equals("SUCCESS")) {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
						messageBox.setMessage("关闭应急模式成功!");
						messageBox.open();
					} else {
						MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("关闭应急模式失败!");
						messageBox.open();
					}

/*					JSONObject jsonObj = new JSONObject(strResult);
					Iterator<String> nameItr = jsonObj.keys();
					String resultValue = "";
					while (nameItr.hasNext()) {
						String name = nameItr.next();
						if(name.equals("result")) {
							resultValue = jsonObj.getString(name);
							if (resultValue.equals("SUCCESS")) {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WORKING |SWT.OK);
								messageBox.setMessage("关闭应急模式成功!");
								messageBox.open();
							} else {
								MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_WARNING |SWT.OK);
								messageBox.setMessage("关闭应急模式失败!");
								messageBox.open();
							}
						}
					}*/

				} catch (Exception e) {
					Log.error("-----------关闭应急模式失败!", e);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		initPopup();
		Log.info("-------------加载完成!");

	}

	public void treeElemenSelected(SelectionEvent e) {
		try {
			TreeItem[] items = this.MQ_tree.getSelection();
			if(items[0].getText().equals("队列")) {
				queueComposite.setVisible(true);
				topicComposite.setVisible(false);
				channelComposite.setVisible(false);
				//队列table赋值

			} else if (items[0].getText().equals("主题")) {
				queueComposite.setVisible(false);
				topicComposite.setVisible(true);
				channelComposite.setVisible(false);
				//主题table赋值

			} else if (items[0].getText().equals("通道")) {
				queueComposite.setVisible(false);
				topicComposite.setVisible(false);
				channelComposite.setVisible(true);
				//通道table赋值

			}
		} catch (Exception e1) {
			Log.error("-----------MQ Server 加载失败!", e1);
		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("GW Users Config");
		setSize(801, 735);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void initPopup(){
	}
}
