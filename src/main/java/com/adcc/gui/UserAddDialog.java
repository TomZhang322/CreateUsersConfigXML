package com.adcc.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.adcc.bean.User;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UserAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text userId_text;
	private Text userName_text;
	public Map<String, User> usersMap2;
	public USERSConfigShell mainshell1;
	private Text upQueue_text;
	private Text downQueue_text;


	/**
	 * Create the dialog.
	 *
	 * @param parent
	 * @param style
	 */
	public UserAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}


	public UserAddDialog(Shell parent, USERSConfigShell mainshell, int style) {
		super(parent, style);
		mainshell1 = mainshell;
		usersMap2 = mainshell1.usersMap;
	}


	/**
	 * Open the dialog.
	 *
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
		shell.setSize(667, 302);
		shell.setText("\u65B0\u589E\u7528\u6237");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(35, 25, 61, 17);
		lblNewLabel.setText("\u7528\u6237ID:");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u7528\u6237\u540D:");
		label.setBounds(35, 73, 61, 17);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u4E0B\u884C\u961F\u5217:");
		label_3.setBounds(365, 87, 61, 17);

		userId_text = new Text(shell, SWT.BORDER);
		userId_text.setBounds(147, 25, 115, 23);

		userName_text = new Text(shell, SWT.BORDER);
		userName_text.setBounds(147, 73, 115, 23);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("\u4E0B\u884C\u62A5\u6587\u5168\u90E8\u8F6C\u53D1:");
		label_4.setBounds(365, 25, 113, 17);

		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("\u4E0A\u884C\u961F\u5217:");
		label_7.setBounds(35, 126, 61, 17);

		final Button yseBtnRadioButton = new Button(shell, SWT.RADIO);
		yseBtnRadioButton.setBounds(484, 25, 54, 17);
		yseBtnRadioButton.setText("\u662F");

		Button noBtnRadioButton = new Button(shell, SWT.RADIO);
		noBtnRadioButton.setSelection(true);
		noBtnRadioButton.setBounds(556, 25, 57, 17);
		noBtnRadioButton.setText("\u5426");

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(365, 126, 61, 17);
		lblNewLabel_1.setText("\u6D88\u606F\u7C7B\u578B:");

		final Combo msgType_combo = new Combo(shell, SWT.NONE);
		msgType_combo.setItems(new String[]{"RawMsg", "SplitResult"});
		msgType_combo.setBounds(484, 126, 115, 25);
		msgType_combo.setText("RawMsg\r\n");

		Button confirmBtn = new Button(shell, SWT.NONE);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String userId = userId_text.getText();
				String userName = userName_text.getText();
				String upQueue = upQueue_text.getText();
				String downQueue = downQueue_text.getText();
				String msgType = msgType_combo.getText();
				Boolean isOriginMsg = yseBtnRadioButton.getSelection();
				// Boolean noOriginMsg = noBtnRadioButton.getSelection();
				Map<String, User> usersMap3 = new ConcurrentHashMap<String, User>();
				if (usersMap2.containsKey(userId)) {
					// 新增用户重复，需要弹框提示
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING|SWT.OK);
					messageBox.setMessage("新增用户ID重复!");
					messageBox.open();
				} else {// 第一次新增
					User user = new User();
					user.setId(userId);
					user.setName(userName);
					user.setSendQueue(upQueue);
					user.setRecvQueue(downQueue);
					user.setMsgType(msgType);
					if (isOriginMsg == true) {
						user.setDownlinkForward(true);
					} else {
						user.setDownlinkForward(false);
					}
					usersMap2.put(userId, user);
					usersMap3.put(userId, user);
					mainshell1.updateUserTable();
					mainshell1.updateRouteTable3(userId);
				}
				shell.setVisible(false);
			}
		});
		confirmBtn.setBounds(407, 208, 80, 27);
		confirmBtn.setText("\u786E\u5B9A");

		Button cancelBtn = new Button(shell, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelBtn.setBounds(529, 208, 80, 27);
		cancelBtn.setText("\u53D6\u6D88");

		upQueue_text = new Text(shell, SWT.BORDER);
		upQueue_text.setBounds(147, 126, 115, 23);

		downQueue_text = new Text(shell, SWT.BORDER);
		downQueue_text.setBounds(484, 84, 115, 23);

	}
}
