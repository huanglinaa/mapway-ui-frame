package cn.mapway.ui.client.modules.test;

import java.util.Map;

import cn.mapway.ui.client.frames.NavigatorModule;
import cn.mapway.ui.client.modules.baidu.BaiduModule;
import cn.mapway.ui.client.modules.daywork.DayWorkModule;
import cn.mapway.ui.client.mvc.IModule;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;



@ModuleMarker(value = TestSubModule.MODULE_CODE, name = "测试子模块")
public class TestSubModule extends NavigatorModule {

  private static TestSubModuleUiBinder uiBinder = GWT.create(TestSubModuleUiBinder.class);

  interface TestSubModuleUiBinder extends UiBinder<Widget, TestSubModule> {
  }

  public static final String MODULE_CODE = "TEST_SUB_MODULE";

  @Override
  public String getModuleCode() {
    return MODULE_CODE;
  }

  public TestSubModule() {
    initModuleContent(uiBinder.createAndBindUi(this));
    registerSubModule(BaiduModule.MODULE_CODE, true);
    registerSubModule(DayWorkModule.MODULE_CODE, true);
    registerSubModule(NoToolsModule.MODULE_CODE, true);
  }



  @Override
  public void initialize(IModule parentModule, Map<String, Object> parameters) {
    super.initialize(parentModule, parameters);
    TextBox t = new TextBox();
    updateTools(t);
  }
}