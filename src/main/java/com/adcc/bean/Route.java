package com.adcc.bean;

/**
 * 路由实体
 */
public  class Route {

    // ID
    private String id;
    // 名称
    private String name;
    // 类型
    private String type;
    //所属用户
    private String userId;
    // 发送地址
    private String sendAddress;
    // 接收地址
    private String recvAddress;
    // 目的地址
    private String destination;
	// SMI
    private String smi;
    // 航班号
    private String fi;
    // 机尾号
    private String an;
    
    private String rgs;
    // 全文关键字
    private String specLabel;
    // 关键字起始位
    private String index;
    
    /**
     * 构造函数
     */
    public Route() {}

    /**
     * 构造函数
     * @param id
     * @param name
     */
    public Route(String id,String name){
        this.id = id;
        this.name = name;
    }
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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

	public String getRgs() {
		return rgs;
	}

	public void setRgs(String rgs) {
		this.rgs = rgs;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFi() {
		return fi;
	}

	public void setFi(String fi) {
		this.fi = fi;
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
