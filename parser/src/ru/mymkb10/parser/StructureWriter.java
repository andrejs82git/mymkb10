package ru.mymkb10.parser;

/**
 * Created by andrejs on 23.01.2017.
 */
public interface StructureWriter {

    void startWrite();

    void writeMkbCode(final String code);

    void writeDescr(final String desc);

    void endWrite();
}
