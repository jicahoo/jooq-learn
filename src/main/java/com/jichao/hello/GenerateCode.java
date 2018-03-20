package com.jichao.hello;

import org.jooq.util.GenerationTool;

public class GenerateCode {
    public static void main(String[] args) throws Exception {
        GenerationTool.main(new String[]{"library.xml"});
    }
}
