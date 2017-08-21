package com.adcc.gui;

import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.entity.Channel;
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

public class ChannelEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text channelName_text;
	private ChannelComposite chlComposite;
	private List<Channel> listChl;
	private String[] strs;
	private Text usersOfChannel_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ChannelEditDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public ChannelEditDialog(Shell parent, int style, ChannelComposite cc,String[] ss) {
		super(parent, style);
		chlComposite = cc;
		listChl = chlComposite.listC;
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
		shell.setSize(495, 203);
		shell.setText("\u901A\u9053\u4FEE\u6539");

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u901A\u9053\u540D\u79F0:");
		label.setBounds(10, 37, 61, 17);

		channelName_text = new Text(shell, SWT.BORDER);
		channelName_text.setEditable(false);
		channelName_text.setBounds(102, 37, 117, 23);
		channelName_text.setText(strs[0]==null?"":strs[0]);

		usersOfChannel_text = new Text(shell, SWT.BORDER);
		usersOfChannel_text.setBounds(349, 37, 99, 23);
		usersOfChannel_text.setText(strs[1]==null?"":strs[1]);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u901A\u9053\u7528\u6237:");
		label_1.setBounds(273, 37, 61, 17);

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//去除旧的通道
					List<Channel> removeList = new ArrayList<Channel>();
					for(Channel c : listChl)
					{
						if(c.getName().equals(strs[0]))
						{
							removeList.add(c);
						}
					}
					if(removeList.size()>0)
					{
						listChl.removeAll(removeList);
					}

					String channelName = channelName_text.getText();
					String usersOfChannel = usersOfChannel_text.getText();
					List<Channel> listChl2 = new ArrayList<Channel>();
					Channel channel = new Channel();
					channel.setName(channelName);
					channel.setUser(usersOfChannel);
					//ibmmq修改通道
					MQSetUp.setUp();
					MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
							MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
					manager.updateChannel(Optional.of(channel));

					listChl2.add(channel);
					listChl.add(channel);
					chlComposite.updateChannelTable();
					shell.setVisible(false);
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("修改保存失败，请检查面板信息!");
					messageBox.open();
					Log.error("修改保存通道失败", e1);
				}

			}
		});
		confirm_btn.setText("确定");
		confirm_btn.setBounds(256, 110, 80, 27);

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
		cancel_btn.setText("取消");
		cancel_btn.setBounds(379, 110, 80, 27);

	}

}
