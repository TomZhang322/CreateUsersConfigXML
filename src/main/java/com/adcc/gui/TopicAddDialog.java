package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Topic;
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

public class TopicAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text topicName_text;
	private Text priority_text;
	private List<Topic> listTopic2;
	private TopicComposite tComposite;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TopicAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public TopicAddDialog(Shell parent, int style, TopicComposite tc) {
		super(parent, style);
		tComposite = tc;
		listTopic2 = tComposite.listTopic;
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
		shell.setSize(562, 255);
		shell.setText("\u65B0\u589E\u4E3B\u9898");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u4E3B\u9898\u540D\u79F0:");
		label.setBounds(37, 41, 61, 17);

		topicName_text = new Text(shell, SWT.BORDER);
		topicName_text.setBounds(122, 41, 123, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u4F18\u5148\u7EA7:");
		label_1.setBounds(287, 41, 79, 17);

		priority_text = new Text(shell, SWT.BORDER);
		priority_text.setText("0");
		priority_text.setBounds(388, 38, 114, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u6301\u4E45\u5316:");
		label_2.setBounds(37, 100, 61, 17);

		final Button persistentYes_btn = new Button(shell, SWT.RADIO);
		persistentYes_btn.setText("\u662F");
		persistentYes_btn.setBounds(122, 100, 45, 17);

		Button persistentNo_btn = new Button(shell, SWT.RADIO);
		persistentNo_btn.setSelection(true);
		persistentNo_btn.setText("\u5426");
		persistentNo_btn.setBounds(173, 100, 45, 17);

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String topicName = topicName_text.getText();
					String priority = priority_text.getText();
					boolean persistentYes = persistentYes_btn.getSelection();
					//主题名称规范
					if(topicName.startsWith("T.")) {
						Topic topic = new Topic();
						topic.setName(topicName);
						topic.setPriority(Integer.parseInt(priority));
						topic.setPersistent(persistentYes);

						//ibmmq新增topic
						MQSetUp.setUp();
						MQManager mqManager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
//						Topic topicCreate = new Topic(topicName, Topic.LOCAL_TOPIC, Integer.parseInt(priority), persistentYes);
						mqManager.createTopic(Optional.of(topic));

						List<Topic> lt = new ArrayList<Topic>();
						lt.add(topic);
						listTopic2.add(topic);
						tComposite.updateTopicTable();
						shell.setVisible(false);
					} else {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("保存失败，主题名称请以  T. 开头!");
						messageBox.open();
					}
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("保存主题失败:请核对主题名称是否与面板信息或MQ Server重复！");
					messageBox.open();
					Log.error("新增主题失败:请核对主题名称是否与面板信息或MQ Server重复！", e1);
				}
			}
		});
		confirm_btn.setText("\u786E\u5B9A");
		confirm_btn.setBounds(321, 159, 80, 27);

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.setText("\u53D6\u6D88");
		cancel_btn.setBounds(435, 159, 80, 27);
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
