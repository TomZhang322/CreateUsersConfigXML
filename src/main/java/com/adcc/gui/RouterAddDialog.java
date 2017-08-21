package com.adcc.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.adcc.bean.Route;
import com.adcc.bean.User;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class RouterAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text routeName_text;
	private Text sendAddress_text;
	private Text SMI_text;
	private Text AN_text;
	private Text recAddress_text;
	private Text FI_text;
	private Text RGS_text;
	private Text routeId_text;
	private Text specLabel_text;
	private Text index_text;
	private Text destQueue_text;
	Map<String, Route> routesMap;
	Map<String, User> usersMap;
	USERSConfigShell mainshell1;


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RouterAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public RouterAddDialog(Shell parent, Map<String, User> map, USERSConfigShell mainshell, int style) {
		super(parent, style);
		usersMap = map;
		mainshell1 = mainshell;
		routesMap = mainshell1.routesMap;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(614, 555);
		shell.setText("\u8DEF\u7531\u65B0\u589E");



		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(332, 30, 61, 17);
		lblNewLabel.setText("\u8DEF\u7531\u540D\u79F0:");

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("3 \u76EE\u7684\u961F\u5217:");
		label_1.setBounds(30, 300, 61, 17);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u7528\u6237:");
		label_2.setBounds(332, 84, 61, 17);

		routeName_text = new Text(shell, SWT.BORDER);
		routeName_text.setBounds(432, 27, 109, 23);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(30, 143, 84, 17);
		lblNewLabel_1.setText("\u5339\u914D\u6761\u4EF6\u7EC4\u5408:");

		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(30, 166, 540, 57);
		lblNewLabel_2.setText("\u5907\u6CE8\uFF1A\u5339\u914D\u6761\u4EF6\u4F7F\u7528\u8DDF\u9009\u5B9A\u5B57\u6BB5\u76F8\u540C\u957F\u5EA6\u7684\u5B57\u7B26\u4E32\uFF0C\u6A21\u7CCA\u67E5\u8BE2\u4F7F\u7528*\u4EE3\u66FF\uFF0C\u5982\u679C\u6761\u4EF6\u5305\u542B\u591A\u4E2A\uFF0C\r\n\u4F7F\u7528\u201C|\u201D\u94FE\u63A5\u3002\u4F8B\u5982\u5728\u53D1\u9001\u5730\u5740\u586B\u5165PEK**CA|SHA**MU\u8868\u793A\u6EE1\u8DB3PEK**CA\u6216SHA**MU\u7684\u90FD\u8FDB\u884C\r\n\u8F6C\u53D1\u3002\u6761\u4EF6\u7A7A\u767D\u8868\u793A\u5168\u90E8\u8F6C\u53D1\uFF0C\u4E0D\u9700\u8981\u8FC7\u6EE4\u3002");

		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(30, 251, 73, 17);
		lblNewLabel_3.setText("1 \u53D1\u9001\u5730\u5740:");

		Label lblSmi = new Label(shell, SWT.NONE);
		lblSmi.setText("4 SMI:");
		lblSmi.setBounds(332, 300, 61, 17);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("6 \u673A\u5C3E\u53F7:");
		label_4.setBounds(332, 352, 61, 17);

		sendAddress_text = new Text(shell, SWT.BORDER);
		sendAddress_text.setBounds(111, 248, 109, 23);

		SMI_text = new Text(shell, SWT.BORDER);
		SMI_text.setBounds(432, 300, 109, 23);

		AN_text = new Text(shell, SWT.BORDER);
		AN_text.setBounds(432, 352, 109, 23);

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("2 \u63A5\u6536\u5730\u5740:");
		label_6.setBounds(332, 251, 74, 17);

		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("5 \u822A\u73ED\u53F7:");
		label_7.setBounds(30, 352, 61, 17);

		Label lblRgs = new Label(shell, SWT.NONE);
		lblRgs.setText("7 RGS\u7AD9:");
		lblRgs.setBounds(30, 401, 61, 17);

		recAddress_text = new Text(shell, SWT.BORDER);
		recAddress_text.setBounds(432, 248, 109, 23);

		FI_text = new Text(shell, SWT.BORDER);
		FI_text.setBounds(113, 349, 109, 23);

		RGS_text = new Text(shell, SWT.BORDER);
		RGS_text.setBounds(113, 401, 109, 23);

		Label lblNewLabel_4 = new Label(shell, SWT.NONE);
		lblNewLabel_4.setBounds(30, 30, 61, 17);
		lblNewLabel_4.setText("\u8DEF\u7531ID:");

		routeId_text = new Text(shell, SWT.BORDER);
		routeId_text.setBounds(109, 27, 109, 23);

		Label lblNewLabel_5 = new Label(shell, SWT.NONE);
		lblNewLabel_5.setBounds(30, 81, 61, 17);
		lblNewLabel_5.setText("\u8DEF\u7531\u7C7B\u578B:");

		int N=usersMap.size();
		String[] unitIDs=new String[N];
		unitIDs=usersMap.keySet().toArray(unitIDs);
		final Combo user_combo = new Combo(shell, SWT.NONE);
		user_combo.setBounds(432, 81, 109, 25);
		//用户下拉框赋值
		user_combo.setItems(unitIDs);

		final Combo routeType_combo = new Combo(shell, SWT.NONE);
		routeType_combo.setItems(new String[] {"Uplink", "Downlink", "Ground"});
		routeType_combo.setBounds(109, 81, 111, 25);
		routeType_combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String value = routeType_combo.getText();
				if(value.equals("Uplink")) {
					sendAddress_text.setEnabled(true);
					recAddress_text.setEnabled(true);
					destQueue_text.setEnabled(true);
					SMI_text.setEnabled(true);
					FI_text.setEnabled(false);
					AN_text.setEnabled(true);
					RGS_text.setEnabled(false);
				} else if (value.equals("Downlink")) {
					sendAddress_text.setEnabled(true);
					recAddress_text.setEnabled(true);
					destQueue_text.setEnabled(false);
					SMI_text.setEnabled(true);
					FI_text.setEnabled(true);
					AN_text.setEnabled(true);
					RGS_text.setEnabled(true);
				} else if (value.equals("Ground")) {
					sendAddress_text.setEnabled(false);
					recAddress_text.setEnabled(false);
					destQueue_text.setEnabled(true);
					SMI_text.setEnabled(false);
					FI_text.setEnabled(false);
					AN_text.setEnabled(false);
					RGS_text.setEnabled(false);
				} else {
					//路由类型为空
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button confirmBtn = new Button(shell, SWT.NONE);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String routeId = routeId_text.getText();
				String routeName = routeName_text.getText();
				String routeType = routeType_combo.getText();
				String user = user_combo.getText();

				String sendAddress = sendAddress_text.getText();
				String recAddress = recAddress_text.getText();
				String destQueue = destQueue_text.getText();
				String SMI = SMI_text.getText();
				String FI = FI_text.getText();
				String AN = AN_text.getText();
				String RGS = RGS_text.getText();
				String specLabel = specLabel_text.getText();
				String index = index_text.getText();

				Map<String, Route> routesMap2 = new ConcurrentHashMap<String, Route>();
				if(routesMap.containsKey(routeId)) {
					// 新增路由ID重复，需要弹框提示
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING|SWT.OK);
					messageBox.setMessage("新增路由ID重复!");
					messageBox.open();
				} else {
					Route route = new Route();
					route.setId(routeId);
					route.setName(routeName);
					route.setType(routeType);
					route.setUserId(user);
					route.setSendAddress(sendAddress);
					route.setRecvAddress(recAddress);
					route.setDestination(destQueue);
					route.setSmi(SMI);
					route.setFi(FI);
					route.setAn(AN);
					route.setRgs(RGS);
					route.setSpecLabel(specLabel);
					route.setIndex(index);

					routesMap.put(routeId, route);
					routesMap2.put(routeId, route);
//					mainshell1.updateRouteTable();
					mainshell1.updateRouteTable3(user);
				}
				shell.setVisible(false);
			}
		});
		confirmBtn.setBounds(375, 490, 80, 27);
		confirmBtn.setText("\u786E\u5B9A");

		Button cancelBtn = new Button(shell, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setBounds(490, 490, 80, 27);

		Label lblNewLabel_6 = new Label(shell, SWT.NONE);
		lblNewLabel_6.setBounds(332, 401, 84, 17);
		lblNewLabel_6.setText("8 \u5168\u6587\u5173\u952E\u5B57:");

		specLabel_text = new Text(shell, SWT.BORDER);
		specLabel_text.setBounds(435, 401, 106, 23);

		Label label = new Label(shell, SWT.NONE);
		label.setText("9 \u5168\u6587\u5173\u952E\u5B57\u8D77\u59CB\u4F4D:");
		label.setBounds(30, 452, 125, 17);

		index_text = new Text(shell, SWT.BORDER);
		index_text.setText("0");
		index_text.setBounds(161, 452, 61, 23);

		destQueue_text = new Text(shell, SWT.BORDER);
		destQueue_text.setBounds(114, 300, 106, 23);



	}
}
