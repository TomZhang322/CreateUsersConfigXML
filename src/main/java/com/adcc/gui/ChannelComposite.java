package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Channel;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;
import com.adcc.ags.utility.mq.transfer.manager.MQManager;
import com.adcc.ags.utility.mq.transfer.manager.MQManagerImpl;
import com.adcc.util.Log;
import com.adcc.util.MQSetUp;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ChannelComposite extends Composite {
	private Table channel_table;
	public ChannelComposite channelComposite;
	public List<Channel> listC = new ArrayList<Channel>();

	public void updateChannelTable() {
		channel_table.removeAll();
		for(Channel chl : listC) {
			TableItem item = new TableItem(channel_table, SWT.NONE);
			item.setText(new String[] {chl.getName(), chl.getUser()});
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ChannelComposite(Composite parent, int style) {
		super(parent, style);
		channelComposite = this;

		Button addBtn = new Button(this, SWT.NONE);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ChannelAddDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, channelComposite).open();
			}
		});
		addBtn.setBounds(165, 10, 80, 27);
		addBtn.setText("新增");

		Button editBtn = new Button(this, SWT.NONE);
		editBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(channel_table.getSelectionIndex() != -1) {
					TableItem item = channel_table.getItem(channel_table.getSelectionIndex());
					String[] stsC = {item.getText(0), item.getText(1)};
					new ChannelEditDialog(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM, channelComposite, stsC).open();
				}

			}
		});
		editBtn.setText("修改");
		editBtn.setBounds(251, 10, 80, 27);

		Button deleteBtn = new Button(this, SWT.NONE);
		deleteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(channel_table.getSelectionIndex() != -1) {
						TableItem t = channel_table.getItem(channel_table.getSelectionIndex());
						String key = t.getText(0);

						List<Channel> removeList = new ArrayList<Channel>();
						for(Channel c : listC)
						{
							if(c.getName().equals(key))
							{
								removeList.add(c);
							}
						}
						if(removeList.size()>0)
						{
							listC.removeAll(removeList);
						}
						//ibmmq删除通道
						MQSetUp.setUp();
						MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
						manager.deleteChannel(key);
						channel_table.remove(channel_table.getSelectionIndex());
					}
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("删除通道失败，请检查面板信息!");
					messageBox.open();
					Log.error("删除通道失败", e1);
				}
			}
		});
		deleteBtn.setText("删除");
		deleteBtn.setBounds(337, 10, 80, 27);

		channel_table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		channel_table.setBounds(22, 43, 444, 134);
		channel_table.setHeaderVisible(true);
		channel_table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(channel_table, SWT.NONE);
		tblclmnNewColumn.setWidth(224);
		tblclmnNewColumn.setText("\u901A\u9053\u540D\u79F0");

		TableColumn tableColumn = new TableColumn(channel_table, SWT.NONE);
		tableColumn.setWidth(215);
		tableColumn.setText("\u6240\u5C5E\u7528\u6237");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
