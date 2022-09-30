package com.bulpros.integrations.regix.model.client;

import com.bulpros.integrations.regix.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegixHttpClient {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${com.bulpros.regix.http.url}")
    private String regixUrl;
    @Value("${com.bulpros.regix.http.header.soap.action.synchronous}")
    private String regixSynchronousSoapAction;
    @Value("${com.bulpros.regix.http.request.template}")
    private ClassPathResource regixRequestTemplate;

    private final HttpClient regix;

    public RegixHttpClient(HttpClient regix) {
        this.regix = regix;
    }

    public RegixResponseData execute(RegixSearchData searchData) throws Exception {
        Document documentRequest = prepareXmlDocumentRequest(searchData);
        InputStream xmlInputStreamRequest = nodeToInputStream(documentRequest);

        HttpPost httpPost = new HttpPost(regixUrl);
        httpPost.setHeader("content-type", "text/xml; charset=utf-8");
        httpPost.setHeader("SOAPAction", regixSynchronousSoapAction);
        httpPost.setEntity(new InputStreamEntity(xmlInputStreamRequest));

        HttpResponse httpResponse = regix.execute(httpPost);

        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            EntityUtils.consume(httpResponse.getEntity());
            throw new RuntimeException("HTTP problems posting method " +
                    httpResponse.getStatusLine().getReasonPhrase());
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document documentResponse = parser.parse(httpResponse.getEntity().getContent());

        NodeList responseNode = documentResponse.getElementsByTagName("Response");
        if (responseNode != null && responseNode.getLength() > 0
                && responseNode.item(0).hasChildNodes()) {
            RegixResponseData regixResponseData = new RegixResponseData();
            NodeList responseChildren = responseNode.item(0).getChildNodes();
            for (int i = 0; i < responseChildren.getLength(); i++) {
                Node child = responseChildren.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    prepareResponse(child, regixResponseData.getResponseData());
                }
            }
            return regixResponseData;
        } else {
            return null;
        }
    }

    private Document prepareXmlDocumentRequest(RegixSearchData searchData) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document document = parser.parse(regixRequestTemplate.getInputStream());

        prepareOperationNodeRequest(document, searchData);
        prepareArgumentNodeRequest(document, searchData);
        prepareContextNodeRequest(document, searchData);

        return document;
    }

    private void prepareOperationNodeRequest(Document document, RegixSearchData searchData) {
        if (document == null || searchData == null || searchData.getOperation() == null) {
            return;
        }
        NodeList operations = document.getElementsByTagName("tem:Operation");
        if (operations != null && operations.getLength() > 0) {
            operations.item(0).appendChild(document.createTextNode(searchData.getOperation()));
        }
    }

    private void prepareArgumentNodeRequest(Document document, RegixSearchData searchData) {
        if (document == null || searchData == null || searchData.getArgument() == null) {
            return;
        }
        NodeList arguments = document.getElementsByTagName("tem:Argument");
        if (arguments != null && arguments.getLength() > 0) {
            Element searchElement = document.createElement(searchData.getArgument().getType());
            searchElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
            searchElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            searchElement.setAttribute("xmlns", searchData.getArgument().getXmlns());
            prepareParametersRequest(document, searchElement, searchData.getArgument().getParameters());
            arguments.item(0).appendChild(searchElement);
        }
    }

    private void prepareParametersRequest(Document document, Element searchElement,
                                          List<RegixSearchDataParameterMap> parameters) {
        for (RegixSearchDataParameterMap parameter : parameters) {
            for (Map.Entry<String, RegixSearchDataParameter> entry : parameter.getParameters().entrySet()) {
                Element param = document.createElement(entry.getKey());
                RegixSearchDataParameter value = entry.getValue();
                switch (entry.getValue().getParameterType()) {
                    case STRING:
                        param.appendChild(document.createTextNode(value.getParameterStringValue()));
                        break;
                    case DATE:
                        param.appendChild(document.createTextNode(format.format(value.getParameterDateValue())));
                        break;
                    case LONG:
                        param.appendChild(document.createTextNode(String.format("%d", entry.getValue().getParameterNumberValue().longValue())));
                        break;
                    case DOUBLE:
                        String doubleFormat = String.format("%%.%df", value.getPrecision() != null ? value.getPrecision() : 2);
                        param.appendChild(document.createTextNode(String.format(doubleFormat, value.getParameterNumberValue().doubleValue())));
                    case COMPLEX:
                        prepareParametersRequest(document, param, value.getParameters());
                }
                for (RegixSearchDataAttributeMap attributeMap : entry.getValue().getAttributes()) {
                    for (Map.Entry<String, String> attributeEntry : attributeMap.getAttributes().entrySet()) {
                        param.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
                    }
                }
                searchElement.appendChild(param);
            }
        }
    }

    private void prepareContextNodeRequest(Document document, RegixSearchData searchData) {
        if (document == null || searchData == null || searchData.getContext() == null) {
            return;
        }
        RegixSearchDataContext contextData = searchData.getContext();
        for (Field field : RegixSearchDataContext.class.getDeclaredFields()) {
            try {
                String fieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                String getter = "get" + fieldName;
                Object result = contextData.getClass().getMethod(getter).invoke(contextData);
                if (result != null) {
                    NodeList nodeList = document.getElementsByTagName("tem:" + fieldName);
                    if (nodeList != null && nodeList.getLength() > 0
                            && nodeList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nodeList.item(0);
                        element.setTextContent(result.toString());
                    }

                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private InputStream nodeToInputStream(Node node) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(node);
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void prepareResponse(Node node, Map<String, Object> data) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (data.containsKey(node.getNodeName())) {
            Object obj = data.get(node.getNodeName());
            if (obj instanceof List) {
                ((List<Map<String, Object>>) obj).add(map);
            } else if (obj instanceof Map) {
                List<Map<String, Object>> list = new ArrayList<>();
                list.add((Map<String, Object>) obj);
                list.add(map);
                data.put(node.getNodeName(), list);
            } else {
                Object newObj = null;
                NodeList children = node.getChildNodes();
                if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                    newObj = children.item(0).getNodeValue();
                }

                Object[] objects = {obj, newObj};
                data.put(node.getNodeName(), objects);
                return;
            }
        } else {
            if (!node.hasChildNodes()) {
                data.put(node.getNodeName(), "");
                return;
            } else {
                NodeList children = node.getChildNodes();
                if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                    data.put(node.getNodeName(), children.item(0).getNodeValue());
                    return;
                }
            }
            data.put(node.getNodeName(), map);
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                prepareResponse(child, map);
            }
        }
    }

}
