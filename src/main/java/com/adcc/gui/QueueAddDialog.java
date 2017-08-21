package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Queue;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;
import com.adcc.ags.utility.mq.transfer.manager.MQManager;
import com.adcc.ags.utility.mq.transfer.manager.MQManagerImpl;
import com.adcc.util.Log;
import com.adcc.util.MQSetUp;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Optional;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class QueueAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text queueName_text;
	private Text priority_text;
	private List<Queue> ql;
	public QueueComposite qComposite;
	private Text depth_text;
	private Text retainInterval_text;
	private Text msgMaxLength_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public QueueAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public QueueAddDialog(Shell parent, int style, QueueComposite qc) {
		super(parent, style);
		qComposite = qc;
		ql = qComposite.qList;
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
		shell.setSize(558, 279);
		shell.setText("\u961F\u5217\u65B0\u589E");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(25, 37, 61, 17);
		lblNewLabel.setText("\u961F\u5217\u540D\u79F0:");

		queueName_text = new Text(shell, SWT.BORDER);
		queueName_text.setBounds(110, 37, 123, 23);

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u961F\u5217\u4F18\u5148\u7EA7:");
		label.setBounds(275, 37, 79, 17);

		priority_text = new Text(shell, SWT.BORDER);
		priority_text.setText("0");
		priority_text.setBounds(376, 34, 114, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u6301\u4E45\u5316:");
		label_1.setBounds(25, 96, 61, 17);

		final Button persistentYes_btn = new Button(shell, SWT.RADIO);
		persistentYes_btn.setBounds(110, 96, 45, 17);
		persistentYes_btn.setText("\u662F");

		final Button persistentNo_btn = new Button(shell, SWT.RADIO);
		persistentNo_btn.setSelection(true);
		persistentNo_btn.setText("\u5426");
		persistentNo_btn.setBounds(161, 96, 45, 17);

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				try {
					String queueName = queueName_text.getText();
					String priority = priority_text.getText();
					boolean persistentNo = persistentNo_btn.getSelection();
					boolean persistentYes = persistentYes_btn.getSelection();
					String depth = depth_text.getText();
					String retainInterval = retainInterval_text.getText();
					String msgMaxLength = msgMaxLength_text.getText();
					//队列名称规范
					if(queueName.startsWith("Q.")) {
						Queue q = new Queue();
						q.setName(queueName);
						q.setPriority(Integer.parseInt(priority));
						q.setPersistent(persistentYes);
						q.setDepth(Integer.parseInt(depth));
						q.setRetainInterval(Integer.parseInt(retainInterval));
						q.setMsgMaxLength(Integer.parseInt(msgMaxLength));

						MQSetUp.setUp();
						MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
						manager.createQueue(Optional.of(q));

						List<Queue> list2 = new ArrayList<Queue>();
						ql.add(q);
						list2.add(q);
						qComposite.updateQueueTable();

						shell.setVisible(false);
					} else {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("保存失败，队列名称请以  Q. 开头!");
						messageBox.open();
					}

				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("保存队列失败:请核对队列名称是否与面板信息或MQ Server重复！");
					messageBox.open();
					Log.error("保存队列失败:请核对队列名称是否与面板信息或MQ Server重复！", e);
				}
			}
		});
		confirm_btn.setBounds(308, 193, 80, 27);
		confirm_btn.setText("\u786E\u5B9A");

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.setText("\u53D6\u6D88");
		cancel_btn.setBounds(419, 193, 80, 27);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(275, 96, 61, 17);
		lblNewLabel_1.setText("\u961F\u5217\u6DF1\u5EA6:");

		depth_text = new Text(shell, SWT.BORDER);
		depth_text.setText("5000");
		depth_text.setBounds(376, 90, 114, 23);

		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(25, 144, 102, 17);
		lblNewLabel_2.setText("\u8D85\u65F6\u65F6\u95F4\uFF08\u5C0F\u65F6\uFF09:");

		retainInterval_text = new Text(shell, SWT.BORDER);
		retainInterval_text.setText("999999999");
		retainInterval_text.setBounds(133, 144, 100, 23);

		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(275, 144, 61, 17);
		lblNewLabel_3.setText("\u6D88\u606F\u957F\u5EA6:");

		msgMaxLength_text = new Text(shell, SWT.BORDER);
		msgMaxLength_text.setText("4194304");
		msgMaxLength_text.setBounds(376, 144, 114, 23);
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
