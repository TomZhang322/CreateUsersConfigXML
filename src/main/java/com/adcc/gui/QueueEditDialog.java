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

public class QueueEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text queueName_text;
	private Text priority_text;
	private Text depth_text;
	private Text retainInterval_text;
	private List<Queue> ql;
	public QueueComposite qComposite;
	private String[] strs;
	private Text msgMaxLength_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public QueueEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public QueueEditDialog(Shell parent, int style, QueueComposite qc, String[] ss) {
		super(parent, style);
		qComposite = qc;
		ql = qComposite.qList;
		strs = ss;
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
		shell.setSize(548, 266);
		shell.setText("\u961F\u5217\u4FEE\u6539");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u961F\u5217\u540D\u79F0:");
		label.setBounds(28, 31, 61, 17);

		queueName_text = new Text(shell, SWT.BORDER);
		queueName_text.setEditable(false);
		queueName_text.setBounds(113, 31, 123, 23);
		queueName_text.setText(strs[0] == null?"":strs[0]);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u961F\u5217\u4F18\u5148\u7EA7:");
		label_1.setBounds(278, 31, 79, 17);

		priority_text = new Text(shell, SWT.BORDER);
		priority_text.setText(strs[1] == null?"":strs[1]);
		priority_text.setBounds(379, 28, 114, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u6301\u4E45\u5316:");
		label_2.setBounds(28, 90, 61, 17);

		final Button persistentYes_btn = new Button(shell, SWT.RADIO);
		persistentYes_btn.setText("\u662F");
		persistentYes_btn.setBounds(113, 90, 45, 17);

		final Button persistentNo_btn = new Button(shell, SWT.RADIO);
		persistentNo_btn.setText("\u5426");
		persistentNo_btn.setBounds(164, 90, 45, 17);
		if(strs[2].equals("是")) {
			persistentYes_btn.setSelection(true);
			persistentNo_btn.setSelection(false);
		} else {
			persistentYes_btn.setSelection(false);
			persistentNo_btn.setSelection(true);
		}

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//移除编辑的旧队列
					List<Queue> removeList = new ArrayList<Queue>();
					for(Queue q : ql)
					{
						if(q.getName().equals(strs[0]))
						{
							removeList.add(q);
						}
					}
					if(removeList.size()>0)
					{
						ql.removeAll(removeList);
					}

					String queueName = queueName_text.getText();
					String priority = priority_text.getText();
					boolean persistentNo = persistentNo_btn.getSelection();
					boolean persistentYes = persistentYes_btn.getSelection();
					String depth = depth_text.getText();
					String retainInterval = retainInterval_text.getText();
					String msgMaxLength = msgMaxLength_text.getText();

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
					manager.updateQueue(Optional.of(q));

					List<Queue> list2 = new ArrayList<Queue>();

					list2.add(q);
					ql.add(q);
					qComposite.updateQueueTable();

					shell.setVisible(false);
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("修改保存失败，请检查面板信息!");
					messageBox.open();
					Log.error("修改保存队列失败", e1);
				}
			}
		});
		confirm_btn.setText("\u786E\u5B9A");
		confirm_btn.setBounds(314, 190, 80, 27);

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancel_btn.setText("\u53D6\u6D88");
		cancel_btn.setBounds(418, 190, 80, 27);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u961F\u5217\u6DF1\u5EA6:");
		label_3.setBounds(278, 92, 61, 17);

		depth_text = new Text(shell, SWT.BORDER);
		depth_text.setBounds(379, 86, 114, 23);
		depth_text.setText(strs[3] == null?"":strs[3]);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("\u8D85\u65F6\u65F6\u95F4\uFF08\u5C0F\u65F6\uFF09:");
		label_4.setBounds(28, 140, 102, 17);

		retainInterval_text = new Text(shell, SWT.BORDER);
		retainInterval_text.setBounds(136, 140, 100, 23);
		retainInterval_text.setText(strs[4] == null?"":strs[4]);

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setText("\u6D88\u606F\u957F\u5EA6:");
		label_5.setBounds(278, 140, 61, 17);

		msgMaxLength_text = new Text(shell, SWT.BORDER);
		msgMaxLength_text.setBounds(379, 140, 114, 23);
		msgMaxLength_text.setText(strs[5] == null?"":strs[5]);

	}

}
