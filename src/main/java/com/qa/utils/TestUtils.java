package com.qa.utils;

import com.qa.Base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {
    public static final long WAIT=10;
    public Map<String, String> parseXML(InputStream file) throws ParserConfigurationException, IOException, SAXException {

        Map<String,String> map=new HashMap<String, String>();

        //get document object builder
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();

        //Build document
        Document document=documentBuilder.parse(file);

        //normalize xml structure
        document.getDocumentElement().normalize();

        Element root=document.getDocumentElement();

        //get all elements
        NodeList nodeList=root.getElementsByTagName("string");

        for(int i=0;i<nodeList.getLength();i++){
            Node node=nodeList.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE){
                Element element=(Element)node;
                map.put(element.getAttribute("name"),element.getTextContent());
            }
        }
        return map;
    }
    public  String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
       return dateFormat.format(date);
    }
    public static void log(String txt){
        BaseTest baseTest=new BaseTest();
        String msg=Thread.currentThread().getId()+":"+baseTest.getPlatform()+":"+baseTest.getDeviceName()+ ":"
                + Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;
        System.out.println(msg);

        String strFile = "logs" + File.separator + baseTest.getPlatform() + "_" + baseTest.getDeviceName()
                + File.separator + baseTest.getDateTime();

        File logFile = new File(strFile);

        if (!logFile.exists()) {
            logFile.mkdirs();
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile + File.separator + "log.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(msg);
        printWriter.close();
    }
    public Logger log(){
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

}
