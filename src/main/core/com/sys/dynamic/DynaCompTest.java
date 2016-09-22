package com.sys.dynamic;

public class DynaCompTest
{
    public static void main(String[] args) throws Exception {
        String fullName = "DynaClass";
        StringBuilder src = new StringBuilder();
       // src.append("package com.sys.dynamic;");
        src.append("public class DynaClass {\n");
        src.append("    public static void main(String[] a){\n");
       // src.append("        return \"Hello, I am \" + ");
       // src.append("this.getClass().getSimpleName();\n");
        src.append(" System.out.println(\"你好呀 世界!\");");
        src.append("    }\n");
        src.append("}\n");
 
        System.out.println(src);
        DynamicEngine de = DynamicEngine.getInstance();
        //de.javaCodetoClassFile(fullName,src.toString(),"E:/pdf/DynaClass.class");
    }
    public Object getTestObject(){
        String fullName = "com.sys.dynamic.DynaClass";
        StringBuilder src = new StringBuilder();
        src.append("package com.sys.dynamic;");
        src.append("public class DynaClass {\n");
        src.append("    public String toString() {\n");
        src.append("        return \"Hello, I am \" + ");
        src.append("this.getClass().getSimpleName();\n");
        src.append("    }\n");
        src.append("}\n");

        System.out.println(src);
        DynamicEngine de = DynamicEngine.getInstance();
        Object instance=null;
        try{
             instance =  de.javaCodeToObject(fullName,src.toString());
        }catch (Exception e){

        }
       return instance;
    }
}
