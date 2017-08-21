package com.adcc.bean;

public class InvalidUser {

	// ID
	private String id;

	// 名称
	private String name;

	// 发送地址
	private String sendAddress;

	// 接收地址
	private String recvAddress;

	// SMI
	private String smi;

	// 机尾号
	private String an;

	// 全文关键字
	private String specLabel;

	// 关键字起始位
	private String index;

	public InvalidUser() {
		super();
	}

	public InvalidUser(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getRecvAddress() {
		return recvAddress;
	}

	public void setRecvAddress(String recvAddress) {
		this.recvAddress = recvAddress;
	}

	public String getSmi() {
		return smi;
	}

	public void setSmi(String smi) {
		this.smi = smi;
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getSpecLabel() {
		return specLabel;
	}

	public void setSpecLabel(String specLabel) {
		this.specLabel = specLabel;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
