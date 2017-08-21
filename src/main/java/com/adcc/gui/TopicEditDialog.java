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

public class TopicEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text topicName_text;
	private Text priority_text;
	private TopicComposite topicComposite;
	private List<Topic> listTopic2;
	private String[] strs;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TopicEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public TopicEditDialog(Shell parent, int style, TopicComposite tc, String[] ss) {
		super(parent, style);
		topicComposite = tc;
		listTopic2 = topicComposite.listTopic;
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
		shell.setSize(583, 250);
		shell.setText("\u4E3B\u9898\u4FEE\u6539");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u4E3B\u9898\u540D\u79F0:");
		label.setBounds(37, 27, 61, 17);

		topicName_text = new Text(shell, SWT.BORDER);
		topicName_text.setEditable(false);
		topicName_text.setBounds(122, 27, 123, 23);
		topicName_text.setText(strs[0]==null?"":strs[0]);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u4F18\u5148\u7EA7:");
		label_1.setBounds(287, 27, 79, 17);

		priority_text = new Text(shell, SWT.BORDER);
		priority_text.setText(strs[1]==null?"":strs[1]);
		priority_text.setBounds(388, 24, 114, 23);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u6301\u4E45\u5316:");
		label_2.setBounds(37, 86, 61, 17);

		final Button persistentYes_btn = new Button(shell, SWT.RADIO);
		persistentYes_btn.setText("\u662F");
		persistentYes_btn.setBounds(122, 86, 45, 17);

		Button persistentNo_btn = new Button(shell, SWT.RADIO);
		persistentNo_btn.setText("\u5426");
		persistentNo_btn.setBounds(173, 86, 45, 17);
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
					//移除旧的topic
					List<Topic> removeList = new ArrayList<Topic>();
					for(Topic t : listTopic2)
					{
						if(t.getName().equals(strs[0]))
						{
							removeList.add(t);
						}
					}
					if(removeList.size()>0)
					{
						listTopic2.removeAll(removeList);
					}


					String topicName = topicName_text.getText();
					String priority = priority_text.getText();
					boolean persistentYes = persistentYes_btn.getSelection();

					List<Topic> lt = new ArrayList<Topic>();
					Topic topic = new Topic();
					topic.setName(topicName);
					topic.setPriority(Integer.parseInt(priority));
					topic.setPersistent(persistentYes);
					lt.add(topic);
					listTopic2.add(topic);
					//ibmmq修改topic
					MQSetUp.setUp();
					MQManager mqManager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
							MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
//					Topic topicEdit = new Topic(topicName,Topic.CLUSTER_TOPIC,Integer.parseInt(priority),persistentYes);
					mqManager.updateTopic(Optional.of(topic));

					topicComposite.updateTopicTable();
					shell.setVisible(false);
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("修改保存失败，请检查面板信息!");
					messageBox.open();
					Log.error("修改保存主题失败", e1);
				}
			}
		});
		confirm_btn.setText("\u786E\u5B9A");
		confirm_btn.setBounds(321, 145, 80, 27);

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancel_btn.setText("\u53D6\u6D88");
		cancel_btn.setBounds(435, 145, 80, 27);

	}


}
