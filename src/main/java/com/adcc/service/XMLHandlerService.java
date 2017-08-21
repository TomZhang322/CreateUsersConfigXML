package com.adcc.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.adcc.util.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.adcc.bean.InvalidUser;
import com.adcc.bean.Route;
import com.adcc.bean.User;

public class XMLHandlerService {


    /**
     * 生成用户配置xml
     * @param uMap
     * @param rMap
     * @return
     */
    public Document pushGWXml(Map<String, User> uMap, Map<String, Route> rMap) {

        Document document = DocumentHelper.createDocument();

        try {
//			String Master_Host = PropertyUtil.getProperty("config/createxml.properties","Master_Host");
//			String Master_Port = PropertyUtil.getProperty("config/createxml.properties","Master_Port");
//			String Master_Channel = PropertyUtil.getProperty("config/createxml.properties","Master_Channel");
//			String Master_QM = PropertyUtil.getProperty("config/createxml.properties","Master_QM");
//			String Slave_Host = PropertyUtil.getProperty("config/createxml.properties","Slave_Host");
//			String Slave_Port = PropertyUtil.getProperty("config/createxml.properties","Slave_Port");
//			String Slave_Channel = PropertyUtil.getProperty("config/createxml.properties","Slave_Channel");
//			String Slave_QM = PropertyUtil.getProperty("config/createxml.properties","Slave_QM");

            document.setXMLEncoding("utf-8");

            // 添加Root节点
            Element eleRoot = document.addElement("adcc");
            eleRoot.addAttribute("version","1");
            eleRoot.addAttribute("type","UserConfiguration");

            // 添加Super节点
/*			Element eltSuperNode1 = eleRoot.addElement("mq");
			Element eltNode1 = eltSuperNode1.addElement("node");
			eltNode1.addAttribute("type","Master");
			Element eltUnit1 = eltNode1.addElement("unit");
			eltUnit1.addAttribute("name", "Host");
			eltUnit1.setText(Master_Host);
			Element eltUnit2 = eltNode1.addElement("unit");
			eltUnit2.addAttribute("name", "Port");
			eltUnit2.setText(Master_Port);
			Element eltUnit3 = eltNode1.addElement("unit");
			eltUnit3.addAttribute("name", "Channel");
			eltUnit3.setText(Master_Channel);
			Element eltUnit4 = eltNode1.addElement("unit");
			eltUnit4.addAttribute("name", "QM");
			eltUnit4.setText(Master_QM);
			
			Element eltNode2 = eltSuperNode1.addElement("node");
			eltNode2.addAttribute("type","Slave");
			Element eltUnit5 = eltNode2.addElement("unit");
			eltUnit5.addAttribute("name", "Host");
			eltUnit5.setText(Slave_Host);
			Element eltUnit6 = eltNode2.addElement("unit");
			eltUnit6.addAttribute("name", "Port");
			eltUnit6.setText(Slave_Port);
			Element eltUnit7 = eltNode2.addElement("unit");
			eltUnit7.addAttribute("name", "Channel");
			eltUnit7.setText(Slave_Channel);
			Element eltUnit8 = eltNode2.addElement("unit");
			eltUnit8.addAttribute("name", "QM");
			eltUnit8.setText(Slave_QM);*/

            Element eltSuperNode2 = eleRoot.addElement("userList");
            Set<Entry<String, User>> entries = uMap.entrySet();
            for(Entry<String, User> entry : entries) {
                String uIDKey = entry.getKey();
                User user = entry.getValue();
                //用户节点赋值
                Element eltNode3 = eltSuperNode2.addElement("node");
                eltNode3.addAttribute("id",uIDKey);
                eltNode3.addAttribute("name",user.getName());
                eltNode3.addAttribute("downlinkForward",String.valueOf(user.isDownlinkForward()));

//            Element eltUnit9 = eltNode3.addElement("unit");
//            eltUnit9.addAttribute("name", "IP");
//            eltUnit9.setText(user.getIp());
                Element eltUnit10 = eltNode3.addElement("unit");
                eltUnit10.addAttribute("name", "SendQueue");
                eltUnit10.setText(user.getSendQueue());
                Element eltUnit11 = eltNode3.addElement("unit");
                eltUnit11.addAttribute("name", "RecvQueue");
                eltUnit11.setText(user.getRecvQueue());
                Element eltUnit12 = eltNode3.addElement("unit");
                eltUnit12.addAttribute("name", "MsgType");
                eltUnit12.setText(user.getMsgType());

                //获取路由节点并赋值
                for(String rIDKey : rMap.keySet()) {
                    Route route = rMap.get(rIDKey);
                    if(uIDKey.equals(route.getUserId())) {
                        //上行路由
                        if(route.getType().equals("Uplink")) {
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","SendAddress");
                            eltSubUnit1.setText(route.getSendAddress()==null?"":route.getSendAddress());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","RecvAddress");
                            eltSubUnit2.setText(route.getRecvAddress()==null?"":route.getRecvAddress());
                            Element eltSubUnit3 = eltSubNode1.addElement("unit");
                            eltSubUnit3.addAttribute("name","Destination");
                            eltSubUnit3.setText(route.getDestination()==null?"":route.getDestination());
                            Element eltSubUnit4 = eltSubNode1.addElement("unit");
                            eltSubUnit4.addAttribute("name","SMI");
                            eltSubUnit4.setText(route.getSmi()==null?"":route.getSmi());
                            Element eltSubUnit5 = eltSubNode1.addElement("unit");
                            eltSubUnit5.addAttribute("name","AN");
                            eltSubUnit5.setText(route.getAn()==null?"":route.getAn());
                            Element eltSubUnit6 = eltSubNode1.addElement("unit");
                            eltSubUnit6.addAttribute("name","SpecLabel");
                            eltSubUnit6.addAttribute("index",route.getIndex());
                            eltSubUnit6.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else if(route.getType().equals("Downlink")) {//下行路由
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","SendAddress");
                            eltSubUnit1.setText(route.getSendAddress()==null?"":route.getSendAddress());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","RecvAddress");
                            eltSubUnit2.setText(route.getRecvAddress()==null?"":route.getRecvAddress());
                            Element eltSubUnit3 = eltSubNode1.addElement("unit");
                            eltSubUnit3.addAttribute("name","SMI");
                            eltSubUnit3.setText(route.getSmi()==null?"":route.getSmi());
                            Element eltSubUnit4 = eltSubNode1.addElement("unit");
                            eltSubUnit4.addAttribute("name","FI");
                            eltSubUnit4.setText(route.getFi()==null?"":route.getFi());
                            Element eltSubUnit5 = eltSubNode1.addElement("unit");
                            eltSubUnit5.addAttribute("name","AN");
                            eltSubUnit5.setText(route.getAn()==null?"":route.getAn());
                            Element eltSubUnit6 = eltSubNode1.addElement("unit");
                            eltSubUnit6.addAttribute("name","RGS");
                            eltSubUnit6.setText(route.getRgs()==null?"":route.getRgs());
                            Element eltSubUnit7 = eltSubNode1.addElement("unit");
                            eltSubUnit7.addAttribute("name","SpecLabel");
                            eltSubUnit7.addAttribute("index",route.getIndex());
                            eltSubUnit7.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else if(route.getType().equals("Ground")) {//地地路由
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","Destination");
                            eltSubUnit1.setText(route.getDestination()==null?"":route.getDestination());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","SpecLabel");
                            eltSubUnit2.addAttribute("index",route.getIndex());
                            eltSubUnit2.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else {//其它不处理

                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error("-----------生成用户配置失败!", e);
        }
        return document;
    }

    public boolean saveGWXml(Map<String, User> uMap, Map<String, Route> rMap) {

        boolean flag = false;

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");

        try {
//			String Master_Host = PropertyUtil.getProperty("config/createxml.properties","Master_Host");
//			String Master_Port = PropertyUtil.getProperty("config/createxml.properties","Master_Port");
//			String Master_Channel = PropertyUtil.getProperty("config/createxml.properties","Master_Channel");
//			String Master_QM = PropertyUtil.getProperty("config/createxml.properties","Master_QM");
//			String Slave_Host = PropertyUtil.getProperty("config/createxml.properties","Slave_Host");
//			String Slave_Port = PropertyUtil.getProperty("config/createxml.properties","Slave_Port");
//			String Slave_Channel = PropertyUtil.getProperty("config/createxml.properties","Slave_Channel");
//			String Slave_QM = PropertyUtil.getProperty("config/createxml.properties","Slave_QM");

            // 添加Root节点
            Element eleRoot = document.addElement("adcc");
            eleRoot.addAttribute("version","1");
            eleRoot.addAttribute("type","UserConfiguration");

            // 添加Super节点
//			Element eltSuperNode1 = eleRoot.addElement("mq");
//			Element eltNode1 = eltSuperNode1.addElement("node");
//			eltNode1.addAttribute("type","Master");
//			Element eltUnit1 = eltNode1.addElement("unit");
//			eltUnit1.addAttribute("name", "Host");
//			eltUnit1.setText(Master_Host);
//			Element eltUnit2 = eltNode1.addElement("unit");
//			eltUnit2.addAttribute("name", "Port");
//			eltUnit2.setText(Master_Port);
//			Element eltUnit3 = eltNode1.addElement("unit");
//			eltUnit3.addAttribute("name", "Channel");
//			eltUnit3.setText(Master_Channel);
//			Element eltUnit4 = eltNode1.addElement("unit");
//			eltUnit4.addAttribute("name", "QM");
//			eltUnit4.setText(Master_QM);
//			
//			Element eltNode2 = eltSuperNode1.addElement("node");
//			eltNode2.addAttribute("type","Slave");
//			Element eltUnit5 = eltNode2.addElement("unit");
//			eltUnit5.addAttribute("name", "Host");
//			eltUnit5.setText(Slave_Host);
//			Element eltUnit6 = eltNode2.addElement("unit");
//			eltUnit6.addAttribute("name", "Port");
//			eltUnit6.setText(Slave_Port);
//			Element eltUnit7 = eltNode2.addElement("unit");
//			eltUnit7.addAttribute("name", "Channel");
//			eltUnit7.setText(Slave_Channel);
//			Element eltUnit8 = eltNode2.addElement("unit");
//			eltUnit8.addAttribute("name", "QM");
//			eltUnit8.setText(Slave_QM);

            Element eltSuperNode2 = eleRoot.addElement("userList");
            Set<Entry<String, User>> entries = uMap.entrySet();
            for(Entry<String, User> entry : entries) {
                String uIDKey = entry.getKey();
                User user = entry.getValue();
                //用户节点赋值
                Element eltNode3 = eltSuperNode2.addElement("node");
                eltNode3.addAttribute("id",uIDKey);
                eltNode3.addAttribute("name",user.getName());
                eltNode3.addAttribute("downlinkForward",String.valueOf(user.isDownlinkForward()));

//            Element eltUnit9 = eltNode3.addElement("unit");
//            eltUnit9.addAttribute("name", "IP");
//            eltUnit9.setText(user.getIp());
                Element eltUnit10 = eltNode3.addElement("unit");
                eltUnit10.addAttribute("name", "SendQueue");
                eltUnit10.setText(user.getSendQueue());
                Element eltUnit11 = eltNode3.addElement("unit");
                eltUnit11.addAttribute("name", "RecvQueue");
                eltUnit11.setText(user.getRecvQueue());
                Element eltUnit12 = eltNode3.addElement("unit");
                eltUnit12.addAttribute("name", "MsgType");
                eltUnit12.setText(user.getMsgType());

                //获取路由节点并赋值
                for(String rIDKey : rMap.keySet()) {
                    Route route = rMap.get(rIDKey);
                    if(uIDKey.equals(route.getUserId())) {
                        //上行路由
                        if(route.getType().equals("Uplink")) {
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","SendAddress");
                            eltSubUnit1.setText(route.getSendAddress()==null?"":route.getSendAddress());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","RecvAddress");
                            eltSubUnit2.setText(route.getRecvAddress()==null?"":route.getRecvAddress());
                            Element eltSubUnit3 = eltSubNode1.addElement("unit");
                            eltSubUnit3.addAttribute("name","Destination");
                            eltSubUnit3.setText(route.getDestination()==null?"":route.getDestination());
                            Element eltSubUnit4 = eltSubNode1.addElement("unit");
                            eltSubUnit4.addAttribute("name","SMI");
                            eltSubUnit4.setText(route.getSmi()==null?"":route.getSmi());
                            Element eltSubUnit5 = eltSubNode1.addElement("unit");
                            eltSubUnit5.addAttribute("name","AN");
                            eltSubUnit5.setText(route.getAn()==null?"":route.getAn());
                            Element eltSubUnit6 = eltSubNode1.addElement("unit");
                            eltSubUnit6.addAttribute("name","SpecLabel");
                            eltSubUnit6.addAttribute("index",route.getIndex());
                            eltSubUnit6.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else if(route.getType().equals("Downlink")) {//下行路由
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","SendAddress");
                            eltSubUnit1.setText(route.getSendAddress()==null?"":route.getSendAddress());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","RecvAddress");
                            eltSubUnit2.setText(route.getRecvAddress()==null?"":route.getRecvAddress());
                            Element eltSubUnit3 = eltSubNode1.addElement("unit");
                            eltSubUnit3.addAttribute("name","SMI");
                            eltSubUnit3.setText(route.getSmi()==null?"":route.getSmi());
                            Element eltSubUnit4 = eltSubNode1.addElement("unit");
                            eltSubUnit4.addAttribute("name","FI");
                            eltSubUnit4.setText(route.getFi()==null?"":route.getFi());
                            Element eltSubUnit5 = eltSubNode1.addElement("unit");
                            eltSubUnit5.addAttribute("name","AN");
                            eltSubUnit5.setText(route.getAn()==null?"":route.getAn());
                            Element eltSubUnit6 = eltSubNode1.addElement("unit");
                            eltSubUnit6.addAttribute("name","RGS");
                            eltSubUnit6.setText(route.getRgs()==null?"":route.getRgs());
                            Element eltSubUnit7 = eltSubNode1.addElement("unit");
                            eltSubUnit7.addAttribute("name","SpecLabel");
                            eltSubUnit7.addAttribute("index",route.getIndex());
                            eltSubUnit7.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else if(route.getType().equals("Ground")) {//地地路由
                            Element eltSubNode1 = eltNode3.addElement("childNode");
                            eltSubNode1.addAttribute("id",route.getId());
                            eltSubNode1.addAttribute("name",route.getName());
                            eltSubNode1.addAttribute("type",route.getType());

                            Element eltSubUnit1 = eltSubNode1.addElement("unit");
                            eltSubUnit1.addAttribute("name","Destination");
                            eltSubUnit1.setText(route.getDestination()==null?"":route.getDestination());
                            Element eltSubUnit2 = eltSubNode1.addElement("unit");
                            eltSubUnit2.addAttribute("name","SpecLabel");
                            eltSubUnit2.addAttribute("index",route.getIndex());
                            eltSubUnit2.setText(route.getSpecLabel()==null?"":route.getSpecLabel());

                        } else {//其它不处理

                        }
                    }
                }
            }
        } catch (Exception e1) {
            Log.error("-----------生成用户配置失败!", e1);
        }

        if(document != null){
            try {
                String filePath = "./messages/userList.xml";
                XMLWriter writer = null;
                writer = new XMLWriter(new FileWriter(filePath), OutputFormat.createPrettyPrint());
                writer.write(document);
                writer.close();
                flag = true;
            } catch (IOException e) {
                Log.error("-----------保存用户配置到本地失败!", e);
            }
        }

        return flag;
    }

    /**
     * 保存黑名单用户的xml
     */
    public boolean saveInUserXML(Map<String, InvalidUser> inUsersMap) {

        boolean flag = false;
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");

        // 添加Root节点
        Element eleRoot = document.addElement("adcc");
        eleRoot.addAttribute("version","1");
        eleRoot.addAttribute("type","BlackListConfiguration");

        Element eltSuperNode = eleRoot.addElement("blackList");
        Set<Entry<String, InvalidUser>> entries = inUsersMap.entrySet();
        for(Entry<String, InvalidUser> entry : entries) {
            String uIDKey = entry.getKey();
            InvalidUser iuser = entry.getValue();
            //用户节点赋值
            Element eltNode = eltSuperNode.addElement("node");
            eltNode.addAttribute("id", iuser.getId());
            eltNode.addAttribute("name", iuser.getName());

            Element eltUnit1 = eltNode.addElement("unit");
            eltUnit1.addAttribute("name", "SendAddress");
            eltUnit1.setText(iuser.getSendAddress());
            Element eltUnit2 = eltNode.addElement("unit");
            eltUnit2.addAttribute("name", "RecvAddress");
            eltUnit2.setText(iuser.getRecvAddress());
            Element eltUnit3 = eltNode.addElement("unit");
            eltUnit3.addAttribute("name", "SMI");
            eltUnit3.setText(iuser.getSmi());
            Element eltUnit4 = eltNode.addElement("unit");
            eltUnit4.addAttribute("name", "AN");
            eltUnit4.setText(iuser.getAn());
            Element eltUnit5 = eltNode.addElement("unit");
            eltUnit5.addAttribute("name", "SpecLabel");
            eltUnit5.addAttribute("index", iuser.getIndex());
            eltUnit5.setText(iuser.getSpecLabel());
        }

        if(document != null){
            try {
                String filePath = "./messages/blackList.xml";
                XMLWriter writer = null;
                writer = new XMLWriter(new FileWriter(filePath), OutputFormat.createPrettyPrint());
                writer.write(document);
                writer.close();
                flag = true;
            } catch (IOException e) {
                Log.error("-----------保存黑名单用户配置到本地失败!", e);
            }
        }

        return flag;

    }

    /**
     * 生成黑名单用户的xml的字符串
     */
    public String createInuserXML(Map<String, InvalidUser> inUsersMap) {

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");

        // 添加Root节点
        Element eleRoot = document.addElement("adcc");
        eleRoot.addAttribute("version","1");
        eleRoot.addAttribute("type","BlackListConfiguration");

        Element eltSuperNode = eleRoot.addElement("blackList");
        Set<Entry<String, InvalidUser>> entries = inUsersMap.entrySet();
        for(Entry<String, InvalidUser> entry : entries) {
            String uIDKey = entry.getKey();
            InvalidUser iuser = entry.getValue();
            //用户节点赋值
            Element eltNode = eltSuperNode.addElement("node");
            eltNode.addAttribute("id", iuser.getId());
            eltNode.addAttribute("name", iuser.getName());

            Element eltUnit1 = eltNode.addElement("unit");
            eltUnit1.addAttribute("name", "SendAddress");
            eltUnit1.setText(iuser.getSendAddress());
            Element eltUnit2 = eltNode.addElement("unit");
            eltUnit2.addAttribute("name", "RecvAddress");
            eltUnit2.setText(iuser.getRecvAddress());
            Element eltUnit3 = eltNode.addElement("unit");
            eltUnit3.addAttribute("name", "SMI");
            eltUnit3.setText(iuser.getSmi());
            Element eltUnit4 = eltNode.addElement("unit");
            eltUnit4.addAttribute("name", "AN");
            eltUnit4.setText(iuser.getAn());
            Element eltUnit5 = eltNode.addElement("unit");
            eltUnit5.addAttribute("name", "SpecLabel");
            eltUnit5.addAttribute("index", iuser.getIndex());
            eltUnit5.setText(iuser.getSpecLabel());
        }
        return document.asXML();
    }


}
