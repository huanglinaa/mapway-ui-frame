package cn.mapway.ui.client.mvc;


/**
 * 模块对象.
 *
 * @author zhangjianshe
 */
public class ModuleItem {

  /** The name. */
  public String name;

  /** The code. */
  public String code;

  /**
   * sumamry.
   */
  public String summary;
  /**
   * 是否是公共模块
   */
  public boolean isPublic;
  /**
   * module icon data base64
   */
  public String icon;

  /**
   * 是否单例创建.
   */
  public Boolean single;

  /**
   * Instantiates a new module item.
   *
   * @param name the name
   * @param code the code
   */
  public ModuleItem(String name, String code, String summary, boolean isPublic, String icon) {
    this.name = name;
    this.code = code;
    this.isPublic = isPublic;
    this.icon = icon;
    this.summary = summary;
  }


  public ModuleItem copy() {
    ModuleItem n = new ModuleItem(name, code, summary, isPublic, icon);
    return n;
  }

  /**
   * 设置单例模式.
   * 
   * @param single
   * @return
   */
  public ModuleItem setSingle(boolean single) {
    this.single = single;
    return this;
  }
}
