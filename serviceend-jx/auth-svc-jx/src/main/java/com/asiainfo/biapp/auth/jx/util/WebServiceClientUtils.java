package com.asiainfo.biapp.auth.jx.util;


import com.asiainfo.biapp.auth.jx.query.UserRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class WebServiceClientUtils {

    private static int SOCKET_TIMEOUT = 30000;
    private static int CONNECT_TIMEOUT = 30000;
    private static String SOAP_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cmcnj=\"{0}\"><soapenv:Header/><soapenv:Body><cmcnj:{1}><RequestInfo><![CDATA[{2}]]></RequestInfo></cmcnj:{1}></soapenv:Body></soapenv:Envelope>";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * @param wsdlUrl      WSDL URL
     * @param nameSpaceURI 命名空间
     * @param methodName   调用方法名
     * @param args         调用参数
     * @param returnTypes  返回类型
     * @return
     * @throws AxisFault
     */
    public static Object[] invokeWS(String wsdlUrl, String nameSpaceURI, String methodName, Object[] args, Class[] returnTypes) throws AxisFault {

        RPCServiceClient client = new RPCServiceClient();

        Options option = client.getOptions();
        //	指定客户端访问的webservice服务器端地址
        EndpointReference erf = new EndpointReference(wsdlUrl);
        option.setTo(erf);
        //	指定命名空间,指定要调用的方法
        QName name = new QName(nameSpaceURI, methodName);
        //	调用远程服务，得到返回的object数组
        Object[] response = client.invokeBlocking(name, args, returnTypes);

        return response;
    }

    /**
     * 获取账号
     *
     * @param token
     * @param appAcctId
     * @return
     */
    public static String getAccInfoXml(String token, String appAcctId) {
        StringBuffer soapHeader = new StringBuffer();
        soapHeader.append("<HEAD>");
        soapHeader.append("<CODE>" + "111" + "</CODE>");
        soapHeader.append("<SID>" + "111" + "</SID>");
        String currentDate = sdf.format(new Date());
        soapHeader.append("<TIMESTAMP>" + currentDate + "</TIMESTAMP>");
        soapHeader.append("<SERVICEID>" + "JXIOP" + "</SERVICEID>");
        soapHeader.append("</HEAD>");

        StringBuilder builder = new StringBuilder();//构造请求报文
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        builder.append("<USERREQ>");
        builder.append(soapHeader);
        builder.append("<BODY>");
        builder.append("<APPACCTID>" + appAcctId + "</APPACCTID>");
        builder.append("<TOKEN>" + token + "</TOKEN>");
//        builder.append("</GetAccInfo>");
        builder.append("</BODY>");
        builder.append("</USERREQ>");
        return builder.toString();
    }

   /* public static String getXml(String token, String appAcctId){
        Document document = DocumentHelper.createDocument();
        Element reqmsg = document.addElement("USERREQ");

        Element head = reqmsg.addElement("HEAD");
        head.addElement("CODE").setText("111");
        head.addElement("SID").setText("111");
        String currentDate = DateTool.getDateFormatStr(new Date(), "yyyyMMddHHmmss");
        head.addElement("TIMESTAMP").setText(currentDate);
        head.addElement("SERVICEID").setText(appAcctId);
        Element body = reqmsg.addElement("BODY");
        body.addElement("APPACCTID").setText(appAcctId);
        body.addElement("TOKEN").setText(token);
        String xml = document.asXML();
        return xml;
    }*/

    /**
     * 行销请求方式
     *
     * @param wsUil
     * @param paramName
     * @param function
     * @param obj
     * @return
     */
    public static String accessServiceFunction(String wsUil, String[] paramName, String function,
                                               Object[] obj) {
        String dataJson = "";
        Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            URL url = new URL(wsUil);
            call.setTargetEndpointAddress(url);
            call.setOperationName(function);
            //call.setOperationName(new QName("http://impl.server.mxq.com/", function));
            for (int i = 0; i < paramName.length; i++) {
                call.addParameter(paramName[i], XMLType.XSD_STRING, ParameterMode.IN);// 操作的参数
            }
            call.setReturnType(XMLType.XSD_STRING);// 设置返回类型
            call.setUseSOAPAction(true);
            dataJson = (String) call.invoke(obj);

            log.info("respData:" + dataJson);
            int startDataTag = dataJson.indexOf("<RSP>");
            int endDataTag = dataJson.indexOf("</RSP>");
            if (startDataTag != -1 && endDataTag != -1) {
                dataJson = dataJson.substring(dataJson.indexOf("<RSP>") + 1, dataJson.indexOf("</RSP>"));
                log.info("dataJson:" + dataJson);
            }
        } catch (MalformedURLException e) {
            dataJson = "远程调用服务失败！" + e.toString();
        } catch (RemoteException e) {
            dataJson = "远程调用服务失败！" + e.toString();
        } catch (ServiceException e) {
            dataJson = "远程调用服务失败！" + e.toString();
        } catch (Exception e) {
            dataJson = "远程调用服务失败！" + e.toString();
        }
        return dataJson;
    }

    public static UserRsp getAccInfo01(String fourAUrl, String token, String appAcctId) {
        String accInfoXml = getAccInfoXml(token, appAcctId);
        String respData = doPostSoap1_1(fourAUrl, fourAUrl.replace("?wsdl", ""), "CheckAiuapTokenSoap", accInfoXml);
        if (StringUtils.isNotEmpty(respData)) {
            log.info("respData:" + respData);
            JaxbUtil util = new JaxbUtil(UserRsp.class);
            UserRsp userRsp = util.fromXml(respData);
            if (userRsp != null && "0".equals(userRsp.getBody().getRsp())) {
                return userRsp;
            }
        }
        return null;
    }

    public static String doPostSoap1_1(String url, String namespace, String operation, String content) {
        log.info("soap begin, the parameter is: url={},namespace={},operation={},content={}", url, namespace, operation, content);
        String rsp = "";
        // HttpClient
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", "");
            //
            content = MessageFormat.format(SOAP_REQUEST, namespace, operation, content);
            log.info("soap request content is: {}", content);
            StringEntity data = new StringEntity(content, Charset.forName("UTF-8"));
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            log.info("soap response is: {}", response);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                //
                String rspString = EntityUtils.toString(httpEntity, "UTF-8");
                log.info("soap response content is: {}", rspString);
                rsp = parseResponse(rspString);
            }
            //
            closeableHttpClient.close();
        } catch (Exception e) {
            log.error("soap error is: {}", e);
        }
        log.info("soap end, return content is: {}", rsp);
        return rsp;
    }

    public static String parseResponse(String response) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            log.info("soap response format is: {}", response);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));
            Element element = document.getDocumentElement();
            NodeList list = element.getElementsByTagName("soapenv:Body");
            if (list.getLength() > 0) {
                Node body = list.item(0);
                Node rsp = body.getFirstChild().getFirstChild();
                if (rsp != null) {
                    String text = rsp.getTextContent();
                    log.info("soap response return is: {}", text);
                    return text;
                }

            }
        } catch (ParserConfigurationException e) {
            log.error("soap response is not a xml type", e.getMessage());
        } catch (SAXException e) {
            log.error("soap response is not a xml type", e.getMessage());
        } catch (IOException e) {
            log.error("convert string to input stream error", e.getMessage());
        }
        return null;
    }

    /*  public static void main(String[] args) {
			 String fourAUrl = "http://10.175.104.105:18080/uac/services/CheckAiuapTokenSoap";
			 String token = "djadjn";
			 String appAcctId="asasdah";
 //			getAccInfo(fourAUrl,token,appAcctId);

		  getAccInfo01(fourAUrl,token,appAcctId);

	  }*/
    public static String getAccInfo(String fourAUrl, String token, String appAcctId) {
        String dataJson = "";
//        String urlStr = "http://xxxx.xxx.xxx.xx/ThirdWebservice.asmx";
        String paraXml = getAccInfoXml(token, appAcctId);
        String soapAction = "http://www.hzsun.com/GetAccInfo";
        OutputStream out = null;
        try {
            log.info("fourAUrl:" + fourAUrl + "参数 paraXml:" + paraXml);
//            URL url = new URL(fourAUrl);
            URL url = new URL(fourAUrl);
            log.info("getAccInfo url: " + url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);
            con.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            //con.setRequestProperty("WSS-Password Type", "PasswordText");

            con.setRequestProperty("SOAPAction", soapAction);
            //con.setRequestProperty("Encoding", "UTF-8");
            out = con.getOutputStream();
            con.getOutputStream().write(paraXml.getBytes());
            out.flush();
            out.close();
            int code = con.getResponseCode();
            log.info("responseCode:" + code);
            String tempString = null;
            StringBuffer sb1 = new StringBuffer();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                while ((tempString = reader.readLine()) != null) {
                    sb1.append(tempString);
                }
                if (null != reader) {
                    reader.close();
                }
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    sb1.append(tempString);
                }
                if (null != reader) {
                    reader.close();
                }
            }
            //响应报文
            String respData = sb1.toString();
            log.info("respData:" + respData);
            int startDataTag = respData.indexOf("<RSP>");
            int endDataTag = respData.indexOf("</RSP>");
            if (startDataTag != -1 && endDataTag != -1) {
                dataJson = respData.substring(respData.indexOf("<RSP>") + 1, respData.indexOf("</RSP>"));
                log.info("dataJson:" + dataJson);
            }
        } catch (Exception e) {
            log.error("getAccInfo exception message=" + e.getMessage() + e.getClass());
        }
        return dataJson;
    }
}
