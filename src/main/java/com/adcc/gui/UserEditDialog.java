package com.adcc.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.adcc.bean.User;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UserEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text userId_text;
	private Text userName_text;
	USERSConfigShell mainShell2;
	String[] usersStr;
	Map<String, User> usersMap2;
	private Text upQueue_text;
	private Text downQueue_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UserEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public UserEditDialog(Shell parent, USERSConfigShell mainShell, String[] strs, int style) {
		super(parent, style);
		mainShell2 = mainShell;
		usersStr = strs;
		usersMap2 = mainShell2.usersMap;
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
		shell.setSize(677, 279);
		shell.setText("\u4FEE\u6539\u7528\u6237");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u7528\u6237ID:");
		label.setBounds(20, 22, 61, 17);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u7528\u6237\u540D:");
		label_1.setBounds(20, 70, 61, 17);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u4E0B\u884C\u961F\u5217:");
		label_3.setBounds(350, 73, 61, 17);

		userId_text = new Text(shell, SWT.BORDER);
		userId_text.setEditable(false);
		userId_text.setBounds(132, 22, 129, 23);
		userId_text.setText(usersStr[0]==null?"":usersStr[0]);

		userName_text = new Text(shell, SWT.BORDER);
		userName_text.setBounds(132, 70, 129, 23);
		userName_text.setText(usersStr[1]==null?"":usersStr[1]);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("\u4E0B\u884C\u62A5\u6587\u5168\u90E8\u8F6C\u53D1:");
		label_4.setBounds(350, 22, 113, 17);

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setText("\u4E0A\u884C\u961F\u5217:");
		label_5.setBounds(20, 123, 61, 17);

		final Button yesBtn = new Button(shell, SWT.RADIO);
		yesBtn.setText("\u662F");
		yesBtn.setBounds(469, 22, 54, 17);

		Button noBtn = new Button(shell, SWT.RADIO);
		noBtn.setText("\u5426");
		noBtn.setSelection(true);
		noBtn.setBounds(541, 22, 57, 17);
		if(usersStr[5].equals("是")) {
			yesBtn.setSelection(true);
			noBtn.setSelection(false);
		} else {
			yesBtn.setSelection(false);
			noBtn.setSelection(true);
		}

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("\u6D88\u606F\u7C7B\u578B:");
		label_6.setBounds(350, 123, 61, 17);

		final Combo msgType_combo = new Combo(shell, SWT.NONE);
		msgType_combo.setItems(new String[]{"RawMsg", "SplitResult"});
		msgType_combo.setBounds(469, 120, 129, 25);
		msgType_combo.setText(usersStr[4]==null?"":usersStr[4]);



		Button confirmBtn = new Button(shell, SWT.NONE);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String userId = userId_text.getText();
				String userName = userName_text.getText();
				String upQueue = upQueue_text.getText();
				String downQueue = downQueue_text.getText();
				String msgType = msgType_combo.getText();
				Boolean isOriginMsg = yesBtn.getSelection();
				Map<String, User> usersMap3 = new ConcurrentHashMap<String, User>();

				User u = new User();
				u.setId(userId);
				u.setName(userName);
				u.setSendQueue(upQueue);
				u.setRecvQueue(downQueue);
				u.setMsgType(msgType);
				if (isOriginMsg == true) {
					u.setDownlinkForward(true);
				} else {
					u.setDownlinkForward(false);
				}
				usersMap3.remove(userId);
				usersMap2.put(userId, u);
				usersMap3.put(userId, u);
				mainShell2.updateUserTable();
				shell.setVisible(false);
			}
		});
		confirmBtn.setText("\u786E\u5B9A");
		confirmBtn.setBounds(388, 187, 80, 27);

		Button cancelBtn = new Button(shell, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setBounds(510, 187, 80, 27);

		upQueue_text = new Text(shell, SWT.BORDER);
		upQueue_text.setBounds(132, 123, 129, 23);
		upQueue_text.setText(usersStr[2]==null?"":usersStr[2]);

		downQueue_text = new Text(shell, SWT.BORDER);
		downQueue_text.setBounds(469, 70, 129, 23);
		downQueue_text.setText(usersStr[3] == null ? "" : usersStr[3]);

	}
}
