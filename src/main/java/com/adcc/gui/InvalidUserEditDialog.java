package com.adcc.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.adcc.bean.InvalidUser;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class InvalidUserEditDialog extends Dialog {

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
	String[] inUserStr;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public InvalidUserEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public InvalidUserEditDialog(Shell parent, int style, USERSConfigShell mainshell, String[] ss) {
		super(parent, style);
		mainshell1 = mainshell;
		inUsersMap1 = mainshell1.inUsersMap;
		inUserStr = ss;
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
		shell.setSize(566, 305);
		shell.setText("\u9ED1\u540D\u5355\u7528\u6237\u4FEE\u6539");

		Label label = new Label(shell, SWT.NONE);
		label.setText("ID:");
		label.setBounds(37, 20, 61, 17);

		inUserId_text = new Text(shell, SWT.BORDER);
		inUserId_text.setEditable(false);
		inUserId_text.setBounds(117, 20, 100, 23);
		inUserId_text.setText(inUserStr[0]==null?"":inUserStr[0]);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u7528\u6237\u540D:");
		label_1.setBounds(303, 20, 61, 17);

		inUserName_text = new Text(shell, SWT.BORDER);
		inUserName_text.setBounds(403, 20, 90, 23);
		inUserName_text.setText(inUserStr[1]==null?"":inUserStr[1]);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u53D1\u9001\u5730\u5740:");
		label_2.setBounds(37, 64, 61, 17);

		sendAddress_text = new Text(shell, SWT.BORDER);
		sendAddress_text.setBounds(117, 64, 100, 23);
		sendAddress_text.setText(inUserStr[2]==null?"":inUserStr[2]);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u63A5\u6536\u5730\u5740:");
		label_3.setBounds(303, 64, 61, 17);

		recvAddress_text = new Text(shell, SWT.BORDER);
		recvAddress_text.setBounds(403, 64, 90, 23);
		recvAddress_text.setText(inUserStr[3]==null?"":inUserStr[3]);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("SMI:");
		label_4.setBounds(37, 112, 61, 17);

		smi_text = new Text(shell, SWT.BORDER);
		smi_text.setBounds(117, 112, 100, 23);
		smi_text.setText(inUserStr[4]==null?"":inUserStr[4]);

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setText("AN:");
		label_5.setBounds(303, 112, 61, 17);

		an_text = new Text(shell, SWT.BORDER);
		an_text.setBounds(403, 112, 90, 23);
		an_text.setText(inUserStr[5]==null?"":inUserStr[5]);

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("\u5168\u6587\u5173\u952E\u5B57:");
		label_6.setBounds(37, 164, 74, 17);

		specLabel_text = new Text(shell, SWT.BORDER);
		specLabel_text.setBounds(117, 164, 100, 23);
		specLabel_text.setText(inUserStr[6]==null?"":inUserStr[6]);

		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("\u5173\u952E\u5B57\u8D77\u59CB\u4F4D:");
		label_7.setBounds(303, 164, 75, 17);

		specLabelIndex_text = new Text(shell, SWT.BORDER);
		specLabelIndex_text.setBounds(403, 164, 90, 23);
		specLabelIndex_text.setText(inUserStr[7]==null?"":inUserStr[7]);

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
		});
		confirm_btn.setText("\u786E\u8BA4");
		confirm_btn.setBounds(316, 228, 80, 27);

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.setText("\u53D6\u6D88");
		cancel_btn.setBounds(438, 228, 80, 27);
		cancel_btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.setVisible(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

	}

}
