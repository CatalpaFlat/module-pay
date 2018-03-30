package com.github.catalpaflat.pay.utils;

import com.github.catalpaflat.pay.constant.EncodeConstant;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CatalpaFlat
 */
public final class XmlUtil {
    private XmlUtil() {
    }

    /**
     * 轮询解析XNL
     *
     * @param root 根节点
     * @param map  map
     */
    private static void recursiveParseXML(Element root, HashMap<String, String> map) {
        //得到根节点的子节点列表
        List<Element> elementList = root.elements();
        //判断有没有子元素列表
        if (elementList.size() == 0) {
            map.put(root.getName(), root.getTextTrim());
        } else {
            //遍历
            for (Element e : elementList) {
                recursiveParseXML(e, map);
            }
        }
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strXml xml
     * @return 将str解析为Map
     * @throws Exception 数据流异常
     */
    public static Map<String, Object> doXMLParse(String strXml) throws Exception {
        if (null == strXml || "".equals(strXml)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>(10, 10);
        InputStream in = new ByteArrayInputStream(strXml.getBytes(EncodeConstant.ENCODE_UTF_8));
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        for (Element element : elementList) {
            map.put(element.getName(), element.getText());
        }
        //关闭流
        in.close();

        return map;
    }

    /**
     * 转换obj为xml
     *
     * @param object 对象
     * @return xml
     */
    public static String messageToXML(Object object) {
        xstream.alias("xml", Object.class);
        return xstream.toXML(object);
    }

    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点都增加CDATA标记
                boolean cdata = true;

                @Override
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

}
