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

import com.adcc.bean.InvalidUser;
import com.adcc.bean.User;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InvalidUserAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text inUserId_text;
	private Text inUserName_text;
	private Text sendAddress_text;
	private Text recvAddress_text;
	private Text smi_text;
	private Text an_text;
	private Text specLabel_text;
	private Text specLabelIndex_text;

	public Map<String, InvalidUser> inUsersMap1;
	public USERSConfigShell mainshell1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public InvalidUserAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public InvalidUserAddDialog(Shell parent, int style, USERSConfigShell mainshell) {
		super(parent, style);
		mainshell1 = mainshell;
		inUsersMap1 = mainshell1.inUsersMap;
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
		shell.setSize(566, 304);
		shell.setText("\u9ED1\u540D\u5355\u7528\u6237\u65B0\u589E");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(20, 21, 61, 17);
		lblNewLabel.setText("ID:");

		inUserId_text = new Text(shell, SWT.BORDER);
		inUserId_text.setBounds(100, 21, 100, 23);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(286, 21, 61, 17);
		lblNewLabel_1.setText("\u7528\u6237\u540D:");

		inUserName_text = new Text(shell, SWT.BORDER);
		inUserName_text.setBounds(386, 21, 90, 23);

		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(20, 65, 61, 17);
		lblNewLabel_2.setText("\u53D1\u9001\u5730\u5740:");

		sendAddress_text = new Text(shell, SWT.BORDER);
		sendAddress_text.setBounds(100, 65, 100, 23);

		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(286, 65, 61, 17);
		lblNewLabel_3.setText("\u63A5\u6536\u5730\u5740:");

		recvAddress_text = new Text(shell, SWT.BORDER);
		recvAddress_text.setBounds(386, 65, 90, 23);

		Label lblNewLabel_4 = new Label(shell, SWT.NONE);
		lblNewLabel_4.setBounds(20, 113, 61, 17);
		lblNewLabel_4.setText("SMI:");

		smi_text = new Text(shell, SWT.BORDER);
		smi_text.setBounds(100, 113, 100, 23);

		Label lblNewLabel_5 = new Label(shell, SWT.NONE);
		lblNewLabel_5.setBounds(286, 113, 61, 17);
		lblNewLabel_5.setText("AN:");

		an_text = new Text(shell, SWT.BORDER);
		an_text.setBounds(386, 113, 90, 23);

		Label lblNewLabel_6 = new Label(shell, SWT.NONE);
		lblNewLabel_6.setBounds(20, 165, 74, 17);
		lblNewLabel_6.setText("\u5168\u6587\u5173\u952E\u5B57:");

		specLabel_text = new Text(shell, SWT.BORDER);
		specLabel_text.setBounds(100, 165, 100, 23);

		Label lblNewLabel_7 = new Label(shell, SWT.NONE);
		lblNewLabel_7.setBounds(286, 165, 75, 17);
		lblNewLabel_7.setText("\u5173\u952E\u5B57\u8D77\u59CB\u4F4D:");

		specLabelIndex_text = new Text(shell, SWT.BORDER);
		specLabelIndex_text.setBounds(386, 165, 90, 23);

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String inUserId = inUserId_text.getText();
				String inUserName = inUserName_text.getText();
				String sendAddress = sendAddress_text.getText();
				String recvAddress = recvAddress_text.getText();
				String smi = smi_text.getText();
				String an = an_text.getText();
				String specLabel = specLabel_text.getText();
				String specLabelIndex = specLabelIndex_text.getText();

				Map<String, InvalidUser> inUsersMap2 = new ConcurrentHashMap<String, InvalidUser>();
				if(inUsersMap1.containsKey(inUserId)) {
					// 新增用户重复，需要弹框提示
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING|SWT.OK);
					messageBox.setMessage("新增黑名单用户ID重复!");
					messageBox.open();
				} else {
					InvalidUser inUser = new InvalidUser();
					inUser.setId(inUserId);
					inUser.setName(inUserName);
					inUser.setSendAddress(sendAddress);
					inUser.setRecvAddress(recvAddress);
					inUser.setSmi(smi);
					inUser.setAn(an);
					inUser.setSpecLabel(specLabel);
					inUser.setIndex(specLabelIndex);

					inUsersMap1.put(inUserId, inUser);
					inUsersMap2.put(inUserId, inUser);
					mainshell1.updateInUserTable();
					shell.setVisible(false);
				}
			}
		});
		confirm_btn.setBounds(299, 229, 80, 27);
		confirm_btn.setText("\u786E\u8BA4");

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancel_btn.setBounds(421, 229, 80, 27);
		cancel_btn.setText("\u53D6\u6D88");

	}
}
