/**
 *
 */
package com.code.core;

import java.util.*;

/**
 * 代码类型
 */
public enum CodeType {
   ENTITY("entity"),
   DAO("dao"),
   SERVICE("service"),
   ACTION("action"),
   VIEW("view");
    private String name;
    private CodeType(String name){
        this.name=name;
    }

    /**
     * 通过名称获取实例
     * @param name
     * @return
     */
    public static CodeType getInstance(String name){
        switch (name){
            case "entity":
                return ENTITY;
            case "dao":
                return DAO;
            case "service":
                return SERVICE;
            case "action":
                return ACTION;
            case "view":
                return VIEW;
            default:
                return null;
        }
    }
}