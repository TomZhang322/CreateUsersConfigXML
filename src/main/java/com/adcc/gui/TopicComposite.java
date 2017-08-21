package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Topic;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;
import com.adcc.ags.utility.mq.transfer.manager.MQManager;
import com.adcc.ags.utility.mq.transfer.manager.MQManagerImpl;
import com.adcc.util.Log;
import com.adcc.util.MQSetUp;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TopicComposite extends Composite {
	private Table topic_table;
	public TopicComposite topicComposite;
	public List<Topic> listTopic = new ArrayList<Topic>();

	public void updateTopicTable() {
		topic_table.removeAll();
		for(Topic topic : listTopic) {
			String persistentStr = "";
			boolean persistent = topic.isPersistent();
			if(persistent) {
				persistentStr = "是";
			} else {
				persistentStr = "否";
			}
			TableItem item = new TableItem(topic_table, SWT.NONE);
			item.setText(new String[] {topic.getName(), String.valueOf(topic.getPriority()), persistentStr});
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TopicComposite(Composite parent, int style) {
		super(parent, style);
		topicComposite = this;

		topic_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		topic_table.setBounds(30, 43, 379, 171);
		topic_table.setHeaderVisible(true);
		topic_table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(topic_table, SWT.NONE);
		tblclmnNewColumn.setWidth(130);
		tblclmnNewColumn.setText("\u4E3B\u9898\u540D\u79F0");

		TableColumn tableColumn = new TableColumn(topic_table, SWT.NONE);
		tableColumn.setWidth(130);
		tableColumn.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumn_1 = new TableColumn(topic_table, SWT.NONE);
		tableColumn_1.setWidth(115);
		tableColumn_1.setText("\u6301\u4E45\u5316");

		Button addBtn = new Button(this, SWT.NONE);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TopicAddDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, topicComposite).open();
			}
		});
		addBtn.setBounds(132, 10, 80, 27);
		addBtn.setText("新增");

		Button editBtn = new Button(this, SWT.NONE);
		editBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(topic_table.getSelectionIndex() != -1) {
					TableItem item = topic_table.getItem(topic_table.getSelectionIndex());
					String[] topicStr = {item.getText(0), item.getText(1), item.getText(2)};
					new TopicEditDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, topicComposite, topicStr).open();
				}
			}
		});
		editBtn.setText("修改");
		editBtn.setBounds(218, 10, 80, 27);

		Button deleteBtn = new Button(this, SWT.NONE);
		deleteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(topic_table.getSelectionIndex() != -1) {
						TableItem t = topic_table.getItem(topic_table.getSelectionIndex());
						String key = t.getText(0);
						//移除列表中选中队列的list
						List<Topic> removeList = new ArrayList<Topic>();
						for(Topic top : listTopic)
						{
							if(top.getName().equals(key))
							{
								removeList.add(top);
							}
						}
						if(removeList.size()>0)
						{
							listTopic.removeAll(removeList);
						}
						//ibmmq删除主题
						MQSetUp.setUp();
						MQManager mqManager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
						mqManager.deleteTopic(key);

						topic_table.remove(topic_table.getSelectionIndex());
					}
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("删除主题失败，请检查面板信息!");
					messageBox.open();
					Log.error("删除主题失败", e1);
				}

			}
		});
		deleteBtn.setText("\u5220\u9664");
		deleteBtn.setBounds(302, 10, 80, 27);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
