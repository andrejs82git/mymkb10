package ru.mymkb10.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Created by andrejs on 23.01.2017.
 */
public class PHPStructure implements StructureWriter {

    private final Writer writer;

    public PHPStructure(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void startWrite() {
        wr("<?php\n\n" +
                "$mkbArr= array (\n");
    }

    @Override
    public void writeMkbCode(final String code) {
        wr("\""+code+"\"");
    }

    @Override
    public void writeDescr(final String desc) {
        this.wr("=>"+"\""+desc.replaceAll("[\"]","'")+"\",\n");
    }

    @Override
    public void endWrite() {
        wr("\n);\n");
    }

    private void wr(String txt) {
        try {
            this.writer.write(txt);
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error!", e);
        }
    }

}
