package com.adcc.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {

    private final static String NULL_STRING = "";
    private static Map<String, Properties> allPropertiesMap = new HashMap<String, Properties>();

    public static String getProperty(String fileName, String key) throws NullPointerException,IOException{
        if (null == key || NULL_STRING.equals(key)) {
            throw new NullPointerException("The parameters is null. key = " + key);
        }
        String fileKey = getFileKey(fileName);
        if (!allPropertiesMap.containsKey(fileKey)) {// key should be properties file name
            initProperties(fileName);
        }
        if (!allPropertiesMap.containsKey(fileKey) || null == allPropertiesMap.get(fileKey)) {
            return null;
        } else {
            return allPropertiesMap.get(fileKey).getProperty(key);
        }
    }

    /**
     * init properties by filename
     *
     * @param filePath File Name
     * @throws NullPointerException null pointer exception
     * @throws java.io.IOException io exception
     */
    public static void initProperties(String filePath) throws NullPointerException, IOException{
        if (null == filePath || NULL_STRING.equals(filePath)) {
            throw new NullPointerException("The parameters is null. key = " + filePath);
        }

        Properties props = new Properties();
        InputStream in = null;
        File propFile = new File(filePath).getAbsoluteFile();
        try {
            in = new FileInputStream(propFile);
            props.load(in);
            allPropertiesMap.put(getFileKey(filePath), props);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("Loading property file error. filePath = " + filePath);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Loading property file error. filePath = " + filePath);
        } catch (IOException e) {
            throw new IOException("Loading property file error. filePath = " + filePath);
        } finally {
            //Must close the input stream
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static String getFileKey(String filePath){
        String fileSeparator = "/";
        String key = "";
        if(filePath.indexOf(fileSeparator) != -1){
            key = filePath.substring(filePath.lastIndexOf(fileSeparator) + fileSeparator.length());
        }else{
            key = filePath;
        }
        return key;
    }
    
    public static void main(String[] args) {
		try {
			String ss = PropertyUtil.getProperty("config/createxml.properties","url");
			System.out.println(ss);
		} catch (NullPointerException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
            e2.printStackTrace();
        }
		
	}
}