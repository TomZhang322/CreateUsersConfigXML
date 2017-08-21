package com.adcc.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

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

public class RouterEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text routeName_text;
	private Text sendAddress_text;
	private Text smi_text;
	private Text an_text;
	private Text reciveAddress_text;
	private Text fi_text;
	private Text rgs_text;
	private Text routeId_text;
	private Text specLabel_text;
	private Text index_text;
	private Text destQueue_text;
	Map<String, Route> routesMap;
	Map<String, User> usersMap;
	USERSConfigShell mainshell1;
	String[] routesStrs;



	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RouterEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public RouterEditDialog(Shell parent, USERSConfigShell mainshell, String[] strs, Map<String, User> map, int style) {
		super(parent, style);
		mainshell1 = mainshell;
		routesStrs = strs;
		usersMap = map;
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
		shell.setSize(599, 552);
		shell.setText("\u8DEF\u7531\u4FEE\u6539");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u8DEF\u7531\u540D\u79F0:");
		label.setBounds(333, 13, 61, 17);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("3 \u76EE\u7684\u961F\u5217:");
		label_1.setBounds(31, 283, 61, 17);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u7528\u6237:");
		label_2.setBounds(333, 67, 61, 17);

		routeName_text = new Text(shell, SWT.BORDER);
		routeName_text.setBounds(433, 10, 109, 23);
		routeName_text.setText(routesStrs[1]==null?"":routesStrs[1]);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u5339\u914D\u6761\u4EF6\u7EC4\u5408:");
		label_3.setBounds(31, 126, 84, 17);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("\u5907\u6CE8\uFF1A\u5339\u914D\u6761\u4EF6\u4F7F\u7528\u8DDF\u9009\u5B9A\u5B57\u6BB5\u76F8\u540C\u957F\u5EA6\u7684\u5B57\u7B26\u4E32\uFF0C\u6A21\u7CCA\u67E5\u8BE2\u4F7F\u7528*\u4EE3\u66FF\uFF0C\u5982\u679C\u6761\u4EF6\u5305\u542B\u591A\u4E2A\uFF0C\r\n\u4F7F\u7528\u201C|\u201D\u94FE\u63A5\u3002\u4F8B\u5982\u5728\u53D1\u9001\u5730\u5740\u586B\u5165PEK**CA|SHA**MU\u8868\u793A\u6EE1\u8DB3PEK**CA\u6216SHA**MU\u7684\u90FD\u8FDB\u884C\r\n\u8F6C\u53D1\u3002\u6761\u4EF6\u7A7A\u767D\u8868\u793A\u5168\u90E8\u8F6C\u53D1\uFF0C\u4E0D\u9700\u8981\u8FC7\u6EE4\u3002");
		label_4.setBounds(31, 149, 540, 57);

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setText("1 \u53D1\u9001\u5730\u5740:");
		label_5.setBounds(31, 234, 73, 17);

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("4 SMI:");
		label_6.setBounds(333, 283, 61, 17);

		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("6 \u673A\u5C3E\u53F7:");
		label_7.setBounds(333, 335, 61, 17);

		sendAddress_text = new Text(shell, SWT.BORDER);
		sendAddress_text.setBounds(112, 231, 109, 23);
		sendAddress_text.setText(routesStrs[4]==null?"":routesStrs[4]);

		smi_text = new Text(shell, SWT.BORDER);
		smi_text.setBounds(433, 283, 109, 23);
		smi_text.setText(routesStrs[7]==null?"":routesStrs[7]);


		an_text = new Text(shell, SWT.BORDER);
		an_text.setBounds(433, 335, 109, 23);
		an_text.setText(routesStrs[9]==null?"":routesStrs[9]);

		Label label_8 = new Label(shell, SWT.NONE);
		label_8.setText("2 \u63A5\u6536\u5730\u5740:");
		label_8.setBounds(333, 234, 74, 17);

		Label label_9 = new Label(shell, SWT.NONE);
		label_9.setText("5 \u822A\u73ED\u53F7:");
		label_9.setBounds(31, 335, 61, 17);

		Label label_10 = new Label(shell, SWT.NONE);
		label_10.setText("7 RGS\u7AD9:");
		label_10.setBounds(31, 384, 61, 17);

		reciveAddress_text = new Text(shell, SWT.BORDER);
		reciveAddress_text.setBounds(433, 231, 109, 23);
		reciveAddress_text.setText(routesStrs[5]==null?"":routesStrs[5]);

		fi_text = new Text(shell, SWT.BORDER);
		fi_text.setBounds(114, 332, 109, 23);
		fi_text.setText(routesStrs[8]==null?"":routesStrs[8]);

		rgs_text = new Text(shell, SWT.BORDER);
		rgs_text.setBounds(114, 384, 109, 23);
		rgs_text.setText(routesStrs[10]==null?"":routesStrs[10]);

		Label label_11 = new Label(shell, SWT.NONE);
		label_11.setText("\u8DEF\u7531ID:");
		label_11.setBounds(31, 13, 61, 17);

		routeId_text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		routeId_text.setBounds(110, 10, 109, 23);
		routeId_text.setText(routesStrs[0]==null?"":routesStrs[0]);

		Label label_12 = new Label(shell, SWT.NONE);
		label_12.setText("\u8DEF\u7531\u7C7B\u578B:");
		label_12.setBounds(31, 64, 61, 17);

		int N=usersMap.size();
		String[] unitIDs=new String[N];
		unitIDs=usersMap.keySet().toArray(unitIDs);
		final Combo userId_combo = new Combo(shell, SWT.NONE);
		userId_combo.setItems(unitIDs);
		userId_combo.setBounds(433, 64, 109, 25);
		userId_combo.setText(routesStrs[3]==null?"":routesStrs[3]);

		final Combo routeType_combo = new Combo(shell, SWT.NONE);
		routeType_combo.setItems(new String[] {"Uplink", "Downlink", "Ground"});
		routeType_combo.setBounds(110, 64, 111, 25);
		routeType_combo.setText(routesStrs[2]==null?"":routesStrs[2]);
		routeType_combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String value = routeType_combo.getText();
				if(value.equals("Uplink")) {
					sendAddress_text.setEnabled(true);
					reciveAddress_text.setEnabled(true);
					destQueue_text.setEnabled(true);
					smi_text.setEnabled(true);
					fi_text.setEnabled(false);
					an_text.setEnabled(true);
					rgs_text.setEnabled(false);
				} else if (value.equals("Downlink")) {
					sendAddress_text.setEnabled(true);
					reciveAddress_text.setEnabled(true);
					destQueue_text.setEnabled(false);
					smi_text.setEnabled(true);
					fi_text.setEnabled(true);
					an_text.setEnabled(true);
					rgs_text.setEnabled(true);
				} else if (value.equals("Ground")) {
					sendAddress_text.setEnabled(false);
					reciveAddress_text.setEnabled(false);
					destQueue_text.setEnabled(true);
					smi_text.setEnabled(false);
					fi_text.setEnabled(false);
					an_text.setEnabled(false);
					rgs_text.setEnabled(false);
				} else {
					//路由类型为空
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button confirm_button = new Button(shell, SWT.NONE);
		confirm_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String routeId = routeId_text.getText();
				String routeName = routeName_text.getText();
				String routeType = routeType_combo.getText();
				String user = userId_combo.getText();

				String sendAddress = sendAddress_text.getText();
				String recAddress = reciveAddress_text.getText();
				String destQueue = destQueue_text.getText();
				String SMI = smi_text.getText();
				String FI = fi_text.getText();
				String AN = an_text.getText();
				String RGS = rgs_text.getText();
				String specLabel = specLabel_text.getText();
				String index = index_text.getText();

				Map<String, Route> routesMap2 = new ConcurrentHashMap<String, Route>();

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

				routesMap2.remove(routeId);
				routesMap.put(routeId, route);
				routesMap2.put(routeId, route);
//				mainshell1.updateRouteTable();
				mainshell1.updateRouteTable3(user);
				shell.setVisible(false);
			}
		});
		confirm_button.setText("\u786E\u5B9A");
		confirm_button.setBounds(367, 476, 80, 27);

		Button cancel_button = new Button(shell, SWT.NONE);
		cancel_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancel_button.setText("\u53D6\u6D88");
		cancel_button.setBounds(468, 476, 80, 27);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(333, 384, 94, 17);
		lblNewLabel.setText("8 \u5168\u6587\u5173\u952E\u5B57:");

		specLabel_text = new Text(shell, SWT.BORDER);
		specLabel_text.setBounds(433, 384, 109, 23);
		specLabel_text.setText(routesStrs[11]==null?"":routesStrs[11]);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(31, 443, 125, 17);
		lblNewLabel_1.setText("9 \u5168\u6587\u5173\u952E\u5B57\u8D77\u59CB\u4F4D:");

		index_text = new Text(shell, SWT.BORDER);
		index_text.setBounds(162, 443, 61, 23);
		index_text.setText(routesStrs[12]==null?"":routesStrs[12]);

		destQueue_text = new Text(shell, SWT.BORDER);
		destQueue_text.setBounds(112, 283, 107, 23);
		destQueue_text.setText(routesStrs[6]==null?"":routesStrs[6]);
	}

}
