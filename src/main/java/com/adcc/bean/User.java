package com.adcc.bean;

import java.util.List;

public class User {

	// ID
	private String id;
	// 名称
	private String name;
	// 下行是否全部转发
	private boolean downlinkForward = false;
	// IP
	private String ip;
	// 发送队列
	private String sendQueue;
	// 接收队列
	private String recvQueue;
	// 消息类型
	private String msgType;
	// 用户路由
	private List<Route> routeList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDownlinkForward() {
		return downlinkForward;
	}
	public void setDownlinkForward(boolean downlinkForward) {
		this.downlinkForward = downlinkForward;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSendQueue() {
		return sendQueue;
	}
	public void setSendQueue(String sendQueue) {
		this.sendQueue = sendQueue;
	}
	public String getRecvQueue() {
		return recvQueue;
	}
	public void setRecvQueue(String recvQueue) {
		this.recvQueue = recvQueue;
	}
	public List<Route> getRouteList() {
		return routeList;
	}
	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}



}
