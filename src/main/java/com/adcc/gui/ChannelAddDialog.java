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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Optional;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import java.util.ArrayList;
import java.util.List;

public class ChannelAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text channelName_text;
	private ChannelComposite chlComposite;
	private List<Channel> listChl;
	private Text usersOfChannel_text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ChannelAddDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public ChannelAddDialog(Shell parent, int style, ChannelComposite cc) {
		super(parent, style);
		chlComposite = cc;
		listChl = chlComposite.listC;
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
		shell.setSize(547, 198);
		shell.setText("\u901A\u9053\u65B0\u589E");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(29, 27, 61, 17);
		lblNewLabel.setText("\u901A\u9053\u540D\u79F0:");

		channelName_text = new Text(shell, SWT.BORDER);
		channelName_text.setBounds(121, 27, 117, 23);

		usersOfChannel_text = new Text(shell, SWT.BORDER);
		usersOfChannel_text.setBounds(366, 27, 98, 23);

		Label label = new Label(shell, SWT.NONE);
		label.setText("\u901A\u9053\u7528\u6237:");
		label.setBounds(292, 27, 61, 17);

		Button confirm_btn = new Button(shell, SWT.NONE);
		confirm_btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String channelName = channelName_text.getText();
					String usersOfChannel = usersOfChannel_text.getText();
					List<Channel> listChl2 = new ArrayList<Channel>();
					Channel channel = new Channel();
					channel.setName(channelName);
					channel.setUser(usersOfChannel);
					//通道名称规范
					if(channelName.startsWith("CHL.")) {
						//ibmmq新增通道
						MQSetUp.setUp();
						MQManager manager = new MQManagerImpl(MQConfigurationFactory.getInstance().getConfiguration("AMQS"),
								MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS"));
						manager.createChannel(Optional.of(channel));
						listChl2.add(channel);
						listChl.add(channel);
						chlComposite.updateChannelTable();
						shell.setVisible(false);
					} else {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
						messageBox.setMessage("保存失败，通道名称请以 CHL. 开头!");
						messageBox.open();
					}
				} catch (Exception e1) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING |SWT.OK);
					messageBox.setMessage("保存通道失败:请核对通道名称是否与面板信息或MQ Server重复！");
					messageBox.open();
					Log.error("新增通道失败:请核对通道名称是否与面板信息或MQ Server重复！",e1);
				}
			}
		});
		confirm_btn.setBounds(279, 118, 80, 27);
		confirm_btn.setText("确定");

		Button cancel_btn = new Button(shell, SWT.NONE);
		cancel_btn.setText("取消");
		cancel_btn.setBounds(404, 118, 80, 27);

		cancel_btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				shell.setVisible(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

	}
}
