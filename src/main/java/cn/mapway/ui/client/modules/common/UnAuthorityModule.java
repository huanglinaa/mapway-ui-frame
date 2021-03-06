package cn.mapway.ui.client.modules.common;

import java.util.Map;

import cn.mapway.ui.client.mvc.BaseAbstractModule;
import cn.mapway.ui.client.mvc.IModule;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

@ModuleMarker(value = UnAuthorityModule.MODULE_CODE, name = "未经授权的模块")
public class UnAuthorityModule extends BaseAbstractModule {

  public static final String MODULE_CODE = "SYS_UNAUTHORITY_MODULE";

  public static final String SYS_UNAUTHORITY_MODULE = "SYS_UNAUTHORITY_MODULE";
  private static UnAuthorityModuleUiBinder uiBinder = GWT.create(UnAuthorityModuleUiBinder.class);

  interface UnAuthorityModuleUiBinder extends UiBinder<Widget, UnAuthorityModule> {
  }

  public UnAuthorityModule() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public Widget getRootWidget() {
    return this;
  }

  @UiField
  Label lbModuelName;

  /**
   * 模块参数:模块名称
   */
  public static final String PARA_MODULE_NAME = "ModuelName";

  @Override
  public void initialize(IModule parentModule, Map<String, Object> parameters) {
    super.initialize(parentModule, parameters);

    String moduelName = (String) parameters.get(PARA_MODULE_NAME);
    if (moduelName != null) {
      lbModuelName.setText(moduelName);
    } else {
      lbModuelName.setText("Unknown");
    }

  }

  @Override
  public String getModuleCode() {
    return MODULE_CODE;
  }
}
