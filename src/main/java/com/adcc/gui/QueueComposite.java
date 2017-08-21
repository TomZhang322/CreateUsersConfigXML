package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Queue;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;
import com.adcc.ags.utility.mq.transfer.manager.MQManager;
import com.adcc.ags.utility.mq.transfer.manager.MQManagerImpl;
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

public class QueueComposite extends Composite {
	public static QueueComposite queueComposite;
	public List<Queue> qList = new ArrayList<Queue>();
	private Table queue_table;

	public void updateQueueTable() {
		queue_table.removeAll();
		for(Queue queue : qList) {
			TableItem  item = new TableItem(queue_table, SWT.NONE);
			String persistent = "";
			if(queue.isPersistent()) {
				persistent = "是";
			} else {
				persistent = "否";
			}
			item.setText(new String[] {queue.getName(), String.valueOf(queue.getPriority()), persistent, String.valueOf(queue.getDepth()),
					String.valueOf(queue.getRetainInterval()), String.valueOf(queue.getMsgMaxLength())});
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QueueComposite(Composite parent, int style) {
		super(parent, style);
		setToolTipText("");
		queueComposite = this;

		queue_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		queue_table.setBounds(10, 43, 541, 187);
		queue_table.setHeaderVisible(true);
		queue_table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(queue_table, SWT.NONE);
		tblclmnNewColumn.setWidth(93);
		tblclmnNewColumn.setText("\u961F\u5217\u540D\u79F0");

		TableColumn tableColumn = new TableColumn(queue_table, SWT.NONE);
		tableColumn.setWidth(92);
		tableColumn.setText("\u4F18\u5148\u7EA7");

		TableColumn tableColumn_1 = new TableColumn(queue_table, SWT.NONE);
		tableColumn_1.setWidth(82);
		tableColumn_1.setText("\u6301\u4E45\u5316");

		TableColumn tblclmnNewColumn_1 = new TableColumn(queue_table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(86);
		tblclmnNewColumn_1.setText("\u961F\u5217\u6DF1\u5EA6");

		TableColumn tableColumn_2 = new TableColumn(queue_table, SWT.NONE);
		tableColumn_2.setWidth(97);
		tableColumn_2.setText("\u8D85\u65F6\u65F6\u95F4");

		TableColumn tblclmnNewColumn_2 = new TableColumn(queue_table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(86);
		tblclmnNewColumn_2.setText("\u6D88\u606F\u957F\u5EA6");

		Button addBtn = new Button(this, SWT.NONE);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new QueueAddDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, queueComposite).open();
			}
		});
		addBtn.setBounds(256, 10, 80, 27);
		addBtn.setText("新增");

		Button editBtn = new Button(this, SWT.NONE);
		editBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(queue_table.getSelectionIndex() != -1) {
					TableItem t = queue_table.getItem(queue_table.getSelectionIndex());
					String[] qStr = {t.getText(0), t.getText(1), t.getText(2), t.getText(3), t.getText(4), t.getText(5)};
					new QueueEditDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, queueComposite, qStr).open();
				}
			}
		});
		editBtn.setText("修改");
		editBtn.setBounds(342, 10, 80, 27);

		Button deleteBtn = new Button(this, SWT.NONE);
		deleteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(queue_table.getSelectionIndex() != -1) {
					try {
						TableItem t = queue_table.getItem(queue_table.getSelectionIndex());
						String key = t.getText(0);
						//移除内存中队列的list
						List<Queue> removeList = new ArrayList<Queue>();
						for(Queue q : qList)
						{
							if(q.getName().equals(key))
							{
								removeList.add(q);
							}
						}
						if(removeList.size()>0)
						{
							qList.removeAll(removeList);
						}
						//ibmmq删除队列
						MQSetUp.setUp();
						MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
						manager.deleteQueue(key);

						queue_table.remove(queue_table.getSelectionIndex());
					} catch (Exception e1) {
						MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("删除队列失败，请检查面板信息!");
						messageBox.open();
						e1.printStackTrace();
					}
				}

			}
		});
		deleteBtn.setText("删除");
		deleteBtn.setBounds(428, 10, 80, 27);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
