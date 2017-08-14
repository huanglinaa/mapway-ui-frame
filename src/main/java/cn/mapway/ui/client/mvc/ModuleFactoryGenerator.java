package cn.mapway.ui.client.mvc;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Files;
import org.nutz.repo.Base64;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;


/**
 * 管理系统模块类，自动搜索IModule类,生成一个代理类,使用此代理类实例化模块,模块的生成是单例模式 +.
 *
 * @author zhangjianshe
 */
public class ModuleFactoryGenerator extends Generator {

  private static final String DEFAULT_ICON =
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAABEklEQVR4Xu2XsRGDQAwEjz5cCDNuw4HbcFVOKNIJJOYD7hWIkdaRg9cI7Z/uYFHz39J8fgEABTQnwAo0FwAmyAqwAs0JsALNBWClwFPSZwLYa6/Jrh8+urMCb0nfCQBHj+x6AIwIoABD0tkSjvZnBViBAQE8AA+4TiBqQtn1mCAmiAmeCZAC1z1Q2SYW7Y8JRk3wIWk1FHMc3fY/2fVhBUzMfv8SxwTvP83EEzoAoiaUXR9egewBov0BEE2B6A1k16MAFMDHEB9DJwK8BxgvT9kuHu1PCpACpAApQAr8EyAGjRgsedRRAAAqEkABFW/VmQkFOLQqnkUBFW/VmQkFOLQqnkUBFW/VmQkFOLQqnm2vgB/ajWNBbfOCCQAAAABJRU5ErkJggg==";

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger,
   * com.google.gwt.core.ext.GeneratorContext, java.lang.String)
   */
  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {

    TypeOracle oracle = context.getTypeOracle();

    // 需要查找的管理模块
    JClassType instantiableType = oracle.findType(IModule.class.getName());

    // 需要管理的模块集合
    List<JClassType> clazzes = new ArrayList<JClassType>();


    // 查找所有的类，并统计出需要管理的类
    for (JClassType classType : oracle.getTypes()) {
      if (!classType.equals(instantiableType) && classType.isAssignableTo(instantiableType)) {
        clazzes.add(classType);
      }
    }

    // 生成代理类的package
    final String genPackageName = "cn.mapway.ui.client.mvc";

    // 代理类名称
    final String genClassName = "ModuleFactoryImpl";

    // 代码生成器工厂类
    ClassSourceFileComposerFactory composer =
        new ClassSourceFileComposerFactory(genPackageName, genClassName);

    // 代理类继承需要代理的接口
    composer.addImplementedInterface(ModuleFactory.class.getCanonicalName());
    // 代理类要引用的类包
    composer.addImport("cn.mapway.ui.client.mvc.*");

    // 创建一个源代码生成器对象
    PrintWriter printWriter = context.tryCreate(logger, genPackageName, genClassName);

    if (printWriter != null) {
      // 源代码生成器
      SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);
      // 生成一个无参数构造函数
      sourceWriter.println("ModuleFactoryImpl() {");
      sourceWriter.println("}");

      sourceWriter.println("\r\n   private static String DEFAULT_ICON=\"" + DEFAULT_ICON + "\";");

      // 输出代码方法
      printFactoryMethod(clazzes, sourceWriter);

      // 写入磁盘
      sourceWriter.commit(logger);
    }

    // 返回生成的代理对象类名称
    return composer.getCreatedClassName();
  }

  /**
   * Prints the factory method.
   *
   * @param clazzes 需要代理的模块集合
   * @param sourceWriter the source writer
   */
  private void printFactoryMethod(List<JClassType> clazzes, SourceWriter sourceWriter) {
    sourceWriter.println();

    StringBuilder sb = new StringBuilder();

    StringBuilder sbModules = new StringBuilder();
    sbModules.append(" private final static ModuleInfo[] gModules={");

    int count = 0;
    for (JClassType classType : clazzes) {
      if (classType.isAbstract()) {
        continue;
      }
      ModuleInfo item = findModuleName(classType);
      if (item == null) {
        continue;
      }
      if (count > 0) {
        sbModules.append(",");
      }

      if (item.icon.equals(DEFAULT_ICON)) {
        sbModules.append("new ModuleInfo(\"" + item.name + "\",\"" + item.code + "\",\""
            + item.summary + "\"," + (item.isPublic ? "true" : "false") + ",DEFAULT_ICON)\r\n");
      } else {
        sbModules.append("new ModuleInfo(\"" + item.name + "\",\"" + item.code + "\",\""
            + item.summary + "\"," + (item.isPublic ? "true" : "false") + ",\"" + item.icon
            + "\")\r\n");
      }


      sb.append("\r\n\t private static IModule " + item.code + "=null;\r\n");

      count++;

    }
    sbModules.append("};");
    sourceWriter.println(sbModules.toString());

    sourceWriter.println(sb.toString());


    // 输出 接口 getModules();

    sourceWriter.println("public ModuleInfo[] getModules(){");
    sourceWriter.println("return gModules;");
    sourceWriter.println("}");

    // 输出 public boolean isModulePublic(String moduleCode);
    sourceWriter.println("\r\npublic boolean isModulePublic(String moduleCode){");
    sourceWriter.println("\r\n    if(moduleCode==null || moduleCode.length()==0) {return false;}");
    sourceWriter.println("\r\n    for(int i=0;i<gModules.length;i++) {\r\n");
    sourceWriter.println("\r\n      ModuleInfo item=gModules[i];");
    sourceWriter.println("\r\n      if ( moduleCode.equals(item.code)) {\r\n");
    sourceWriter.println("\r\n          return item.isPublic;\r\n");
    sourceWriter.println("\r\n      }");
    sourceWriter.println("\r\n    }");
    sourceWriter.println("\r\n    return false;\r\n");
    sourceWriter.println("\r\n}");

    // 输出 模块信息

    sourceWriter.println("\r\n public ModuleInfo findModuleInfo(String moduleCode){");
    sourceWriter.println("\r\n    if(moduleCode==null || moduleCode.length()==0) {return null;}");
    sourceWriter.println("\r\n    for(int i=0;i<gModules.length;i++) {\r\n");
    sourceWriter.println("\r\n      ModuleInfo item=gModules[i];");
    sourceWriter.println("\r\n      if ( moduleCode.equals(item.code)) {\r\n");
    sourceWriter.println("\r\n          return item;\r\n");
    sourceWriter.println("\r\n      }");
    sourceWriter.println("\r\n    }");
    sourceWriter.println("\r\n    return null;\r\n");
    sourceWriter.println("\r\n}");
    // 实例化签名
    sourceWriter.println("public  IModule createModule( String code ,boolean single) {");

    sourceWriter.println();
    sourceWriter.indent();
    for (JClassType classType : clazzes) {
      if (classType.isAbstract()) {
        continue;
      }
      ModuleInfo item = findModuleName(classType);
      if (item == null) {
        continue;

      }


      sourceWriter.println("if(\"" + item.code + "\".equals(code)) {\r\n");
      sourceWriter.println("if(single==false) {\r\n");
      sourceWriter.println("      return new " + classType.getQualifiedSourceName() + "();\r\n");
      sourceWriter.println("}\r\n");

      sourceWriter.println("if (" + item.code + " == null) {\r\n");
      sourceWriter.println("     " + item.code + "=new " + classType.getQualifiedSourceName()
          + "();\r\n");
      sourceWriter.println("}\r\n");
      sourceWriter.println("return " + item.code + ";\r\n");
      sourceWriter.println("}\r\n");
    }
    sourceWriter.outdent();
    sourceWriter.println();

    sourceWriter.indent();
    sourceWriter.println("return  null;");
    sourceWriter.outdent();
    sourceWriter.println();
    sourceWriter.println("}");
    sourceWriter.outdent();
    sourceWriter.println();
  }

  /**
   * Find module name.
   *
   * @param classType the class type
   * @return the module item
   */
  private ModuleInfo findModuleName(JClassType classType) {
    ModuleMarker marker = classType.getAnnotation(ModuleMarker.class);
    if (marker == null) {
      return null;
    }
    String modulecode = null;
    String modulename = null;
    String summary = "";
    boolean isPublic = false;
    if (marker != null) {
      modulecode = marker.value();
      modulename = marker.name();
      isPublic = marker.isPublic();
      summary = marker.summary();
    }
    if (modulecode == null || modulecode.length() == 0) {
      modulecode = classType.getSimpleSourceName();
    }
    if (modulename == null || modulename.length() == 0) {
      modulename = modulecode;
    }
    String icondata = "";
    String iconname = marker.icon();

    icondata = loadIconData(classType, iconname);

    ModuleInfo item = new ModuleInfo("", "", "", false, icondata);

    item.code = modulecode;
    item.name = modulename;
    item.isPublic = isPublic;
    item.summary = summary;
    return item;
  }

  /**
   * @param classType
   * @param iconname
   * @return
   */
  private String loadIconData(JClassType classType, String iconname) {

    if (iconname == null || "icon.png".equals(iconname)) {
      return DEFAULT_ICON;
    }
    String packageName = classType.getPackage().getName().replaceAll("\\.", "/");
    String filename = packageName + "/" + iconname;
    String type = Files.getSuffix(filename).substring(1);
    File f = Files.findFile(filename);
    if (f != null) {
      return "data:image/" + type + ";base64," + Base64.encodeToString(Files.readBytes(f), false);
    } else {
      System.out.println("Xcan find " + filename);
      return DEFAULT_ICON;
    }
  }
}