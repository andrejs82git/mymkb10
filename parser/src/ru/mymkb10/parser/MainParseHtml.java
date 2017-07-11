package ru.mymkb10.parser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;


/**
 * Created by andrejs on 23.01.2017.
 */

public class MainParseHtml {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String url = "http://mymkb10.ru/index2.html";
        String fileOut = "./out.php";

        URL urlIn = new URL(url);
        InputSource inputSource = new InputSource(new InputStreamReader(urlIn.openStream(), "windows-1251"));

        FileWriter fileWriter = new FileWriter(fileOut);

        doParse(inputSource, fileWriter);
    }

    static void doParse(InputSource inputSource, Writer writer) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser parser = factory.newSAXParser();
        Mkb10StreamHandler mkb10StreamHandler = new Mkb10StreamHandler(new PHPStructureWriter(writer));

        parser.parse(inputSource, mkb10StreamHandler);
    }
}


class Mkb10StreamHandler extends org.xml.sax.helpers.DefaultHandler {


    private final StructureWriter writer;
    private final StringBuffer buffer = new StringBuffer();

    private enum STATE {
        ZERO, TR, TD_CODE, TD_DESCR;
    }

    private STATE state = STATE.ZERO;

    public Mkb10StreamHandler(final StructureWriter writer) {
        this.writer = writer;
    }

    @Override
    public void startDocument() throws org.xml.sax.SAXException {
        writer.startWrite();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws org.xml.sax.SAXException {
        if (qName.equalsIgnoreCase("tr")) {
            state = STATE.TR;
            return;
        }

        if (qName.equalsIgnoreCase("td")) {
            if (state == STATE.TR) {
                state = STATE.TD_CODE;
            } else if (state == STATE.TD_CODE) {
                state = STATE.TD_DESCR;
            }
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
        buffer.append(chars, start, length);
    }

    @Override
    public void endElement(String s, String s1, String s2) {
        if (state == STATE.TD_CODE) {
            writer.writeMkbCode(this.buffer.toString());
        } else if (state == STATE.TD_DESCR) {
            writer.writeDescr(this.buffer.toString());
            state = STATE.ZERO;
        }
        this.buffer.delete(0, this.buffer.length());
    }

    @Override
    public void endDocument() throws org.xml.sax.SAXException {
        writer.endWrite();
    }


}
