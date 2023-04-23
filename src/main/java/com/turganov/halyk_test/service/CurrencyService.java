package com.turganov.halyk_test.service;

import com.turganov.halyk_test.model.R_CURRENCY;
import com.turganov.halyk_test.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;


    public int saveCurrency(String date) throws Exception {
        List<R_CURRENCY> currencies = getCurrencyFromAPI(date);
        currencyRepository.saveAll(currencies);
        return currencies.size();
    }

    private List<R_CURRENCY> getCurrencyFromAPI(String date) throws Exception {

        String apiURL = "https://nationalbank.kz/rss/get_rates.cfm?fdate=" + date;

        RestTemplate restTemplate = new RestTemplate();
        String xmlString = restTemplate.getForObject(apiURL, String.class);

        return parseCurrencyXML(xmlString, date);
    }

    private List<R_CURRENCY> parseCurrencyXML(String xmlString, String date) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        Document document = builder.parse(inputSource);

        NodeList items = document.getElementsByTagName("item");

        List<R_CURRENCY> currencies = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (int i = 0; i < items.getLength(); i++) {
            Node itemNode = items.item(i);

            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;

                R_CURRENCY currency = new R_CURRENCY();
                currency.setDate(LocalDate.parse(date, formatter));
                currency.setTitle(getElementValue(itemElement, "fullname"));
                currency.setCode(getElementValue(itemElement, "title"));
                currency.setValue(BigDecimal.valueOf(Double.parseDouble(getElementValue(itemElement, "description"))));
                currencies.add(currency);
            }
        }
        return currencies;
    }

    private String getElementValue(Element parentElement, String elementName) {
        NodeList nodeList = parentElement.getElementsByTagName(elementName);
        if (nodeList != null && nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            if (element.getFirstChild() != null) {
                return element.getFirstChild().getNodeValue();
            }
        }
        return "";
    }

}
