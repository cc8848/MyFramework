/***********************************************************************
 * Module:  FirstGenerateDirector.java
 * Author:  lenovo
 * Purpose: Defines the Class FirstGenerateDirector
 ***********************************************************************/

package com.code.core.impl;

import com.code.core.CodeType;
import com.code.core.CodeWoker;
import com.code.core.GenerateDirector;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/** @pdOid 5b0721c4-cd25-458e-ad44-9f9bd3302ac5 */
public class FirstGenerateDirector extends GenerateDirector {
    private CodeWoker codeWoker;
    @Override
    public void generateCode(Set<CodeType> codeTypes) {
        if(codeTypes==null)
            return;
        if(codeTypes.contains(CodeType.ENTITY)){
            codeWoker.generateEntity();
        }

        if(codeTypes.contains(CodeType.DAO)){
            codeWoker.generateDao();
        }
        if(codeTypes.contains(CodeType.SERVICE)){
            codeWoker.generateService();
        }
        if(codeTypes.contains(CodeType.ACTION)){
            codeWoker.generateController();
        }
        if(codeTypes.contains(CodeType.VIEW)){
            codeWoker.generateView();
        }
    }

    public CodeWoker getCodeWoker() {
        return codeWoker;
    }

    public void setCodeWoker(CodeWoker codeWoker) {
        this.codeWoker = codeWoker;
    }

    public FirstGenerateDirector() {
    }

    public FirstGenerateDirector(CodeWoker codeWoker) {
        this.codeWoker = codeWoker;
    }
}