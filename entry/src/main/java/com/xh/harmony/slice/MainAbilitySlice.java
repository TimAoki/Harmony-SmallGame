package com.xh.harmony.slice;

import com.xh.harmony.MainAbility;
import com.xh.harmony.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

// 主页面 Slice 类
public class MainAbilitySlice extends AbilitySlice {


    /**
     * 主界面Action标识
     */
    public static final String ABILITY_MAIN = MainAbility.class.getName();

    /**
     * 游戏页面Action标识
     */
    public static final String PLAY_PAGE = "action.system.play";

    /**
     * 列阵数
     */
    public static final String KEY_COUNT= "count";


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        // 设置布局文件
        super.setUIContent(ResourceTable.Layout_game_home_layout);

        // 初始化视图
        initView();
    }

    // 初始化视图
    private void initView() {

        // 设置按钮行列数
        int count = 4;

        // 获取设备宽度
        int deviceWidth = getContext().getResourceManager().getDeviceCapability().width;

        // 计算每个按钮的高度
        int btnHeight = (deviceWidth - (count - 1) * 30) / count;

        // 设置第一行按钮的布局
        TableLayout.LayoutConfig tlc1 = new TableLayout.LayoutConfig(0, vp2px(btnHeight));
        tlc1.columnSpec = TableLayout.specification(TableLayout.DEFAULT, 1, 1.0f);
        tlc1.rowSpec = TableLayout.specification(TableLayout.DEFAULT, 1);
        tlc1.setMargins(vp2px(10), vp2px(10), vp2px(10), vp2px(10));
        findComponentById(ResourceTable.Id_btn3).setLayoutConfig(tlc1);
        findComponentById(ResourceTable.Id_btn4).setLayoutConfig(tlc1);
        findComponentById(ResourceTable.Id_btn5).setLayoutConfig(tlc1);

        // 设置第二行按钮的布局
        findComponentById(ResourceTable.Id_btn6).setLayoutConfig(tlc1);
        findComponentById(ResourceTable.Id_btn7).setLayoutConfig(tlc1);
        findComponentById(ResourceTable.Id_btn8).setLayoutConfig(tlc1);
        findComponentById(ResourceTable.Id_btn9).setLayoutConfig(tlc1);

        // 监听所有按钮的点击事件
        setBtnListener();
    }

    // 监听表格内所有按钮的事件
    private void setBtnListener() {
        TableLayout table = (TableLayout)findComponentById(ResourceTable.Id_tabelHome);
        int childNum = table.getChildCount();
        for (int index = 0; index < childNum; index++) {

            // 获取当前按钮
            Button child = (Button)(table.getComponentAt(index));

            // 设置按钮的点击监听器
            // 点击事件
            child.setClickedListener(component -> {

                // 判断组件是否是按钮
                if (component instanceof Button) {

                    // 获取组件的 hint 值
                    Button button = (Button)component;
                    String currentNum = button.getHint();


                    // 跳转到游戏界面
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withBundleName(getBundleName())
                            .withAbilityName(MainAbilitySlice.ABILITY_MAIN)
                            .withAction(MainAbilitySlice.PLAY_PAGE).build();
                    intent.setOperation(operation);
                    intent.setParam(MainAbilitySlice.KEY_COUNT, Integer.valueOf(currentNum));
                    startAbility(intent);
                }
            });
        }
    }

    // 将 vp 单位转换为 px 单位
    private int vp2px(float vp) {
        return AttrHelper.vp2px(vp, getContext());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
