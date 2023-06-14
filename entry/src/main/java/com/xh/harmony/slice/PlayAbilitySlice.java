package com.xh.harmony.slice;

import com.xh.harmony.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;


public class PlayAbilitySlice extends AbilitySlice {

    // 记录当前要点击的数字
    private int flag = 1;
    // 记录开始时间
    private long startTime = -1;
    // 当前排列数
    private int arrayNumber = -1;
    // 当前列阵数
    private int currentCount = -1;
    // 记录当前点击后的按钮,方便点击下一个按钮时,恢复为默认按钮状态
    private Button beforeButton = null;
    // 奖状对话框
    private CommonDialog dialog = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_game_play_layout);
        // 头部初始化
        headerBar();
        // 初始化本地端视图
        initLocalView(intent);
        // 页脚初始化
        footerBar();
    }

    /**
     * 初始化标题按钮事件
     */
    private void headerBar() {
        Text title = (Text)findComponentById(ResourceTable.Id_title);
        title.setText("加油，拿下它们");

        // 回到游戏首页
        Image imgBack = (Image)findComponentById(ResourceTable.Id_imgBack);
        imgBack.setClickedListener(component -> terminateAbility());
    }

    /**
     * 初始化本地端视图
     * @param intent
     */
    private void initLocalView(Intent intent) {
        // 获取主界面传过来的参数, 如果为空, 使用默认值3列阵
        int count = intent.getIntParam(MainAbilitySlice.KEY_COUNT, 3);

        initGame(count);
    }

    /**
     * 初始化游戏界面
     * @param count
     */
    private void initGame(int count) {
        currentCount = count;
        arrayNumber = -1;
        beforeButton = null;
        flag = 1;
        startTime = new Date().getTime();

        TableLayout table = (TableLayout)findComponentById(ResourceTable.Id_table);
        table.setColumnCount(count);
        table.setRowCount(count);
        table.removeAllComponents();

        int deviceWidth = getContext().getResourceManager().getDeviceCapability().width;
        int btnHeight = (deviceWidth - (count-1) * 20) / count;

        TableLayout.LayoutConfig tlc = new TableLayout.LayoutConfig(0, vp2px(btnHeight));
        tlc.setMargins(20,20,20,20);
        tlc.columnSpec = TableLayout.specification(TableLayout.DEFAULT, 1, 1.0f);
        tlc.rowSpec = TableLayout.specification(TableLayout.DEFAULT, 1);

        arrayNumber = count * count;
        String[] btnTextArr = genRandomNums(arrayNumber);
        for (int i=0; i<arrayNumber; i++) {
            Button btn = new Button(getContext());
            btn.setId(i);
            btn.setText( btnTextArr[i] );
            btn.setTextColor(new Color(Color.rgb(3, 131, 144)));
            ShapeElement element = new ShapeElement();
            element.setRgbColor(new RgbColor(0x20e0efFF));
            element.setShape(ShapeElement.OVAL);
            element.setStroke(5, new RgbColor(0x70c9cdFF));
            btn.setBackground(element);
            btn.setTextSize(240/count);
            btn.setFont(Font.DEFAULT_BOLD);
            btn.setLayoutConfig(tlc);

            table.addComponent(btn);
        }

        // 设置表格布局内所有按钮事件
        setBtnListener(ResourceTable.Id_table);
    }

    /**
     * 随机生成1-count参数的整数
     * @param count
     * @return
     */
    private String[] genRandomNums(int count) {
        Random random = new Random(System.currentTimeMillis());
        String[] array = new String[count];
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            list.add(String.valueOf(i+1));//集合里存 0-count
        }
        for (int j = 0; j < count; j++) {
            int s = random.nextInt(list.size());//从集合长度,随机产生一个int
            array[j] = list.get(s); //获取集合下标的数
            //集合中每次移除添加得数,集合的长度也减一
            list.remove(s);

        }
        return array;
    }

    /**
     * 监听表格内所有按钮的事件
     * @param tableLayout
     */
    private void setBtnListener(int tableLayout) {
        TableLayout table = (TableLayout)findComponentById(tableLayout);
        int childNum = table.getChildCount();
        for (int index = 0; index < childNum; index++) {
            Button child = (Button)(table.getComponentAt(index));
            child.setClickedListener(component -> {
                if (component instanceof Button) {
                    Button button = (Button)component;
                    String currentNum = button.getText();
                    setButtonNormal(beforeButton);

                    // 判断是否点击最后一个数字,如果是,弹出所用时间
                    if (flag == arrayNumber) {
                        long endTime = new Date().getTime();
                        double time = (endTime - startTime) * 1.0 / 1000;
                        Button btnTrophy = (Button)findComponentById(ResourceTable.Id_btnTrophy);
                        String timeStr = time + "\"";
                        btnTrophy.setText(timeStr);
                        popupCertificate(timeStr);
                    }

                    // 如果点击的数字和当前的一样,修改背景色
                    if (flag == Integer.valueOf(currentNum)) {
                        ShapeElement element = new ShapeElement();
                        element.setRgbColor(new RgbColor(0xf8c129FF));
                        element.setShape(ShapeElement.OVAL);
                        element.setStroke(5, new RgbColor(0xffdf18FF));
                        button.setBackground(element);

                        button.setTextColor(new Color(Color.rgb(255, 237, 125)));
                        // 生成下一个数字
                        flag++;
                        // 记录当前按钮,方便点击下一次时,恢复默认状态颜色
                        beforeButton = button;
                    }


                }
            });
        }
    }

    /**
     * 恢复按钮为默认状态颜色
     * @param button
     */
    private void setButtonNormal(Button button) {
        if (button == null) return;
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(0x20e0efFF));
        element.setShape(ShapeElement.OVAL);
        element.setStroke(5, new RgbColor(0x70c9cdFF));
        button.setBackground(element);
        button.setTextColor(new Color(Color.rgb(7, 135, 148)));
    }

    /**
     * 页脚初始化
     */
    private void footerBar() {
        // 获取帮助
        Button btnHelp = (Button)findComponentById(ResourceTable.Id_btnHelp);
        btnHelp.setClickedListener(component -> {
            CommonDialog helpDialog = new CommonDialog(getContext());
            helpDialog.setTitleText("笨笨看下面");
            helpDialog.setTitleSubText(" ");
            helpDialog.setContentText("以最快速度从1选到后面这个数："+arrayNumber);
            helpDialog.setButton(IDialog.BUTTON1, "好", (iDialog, i) -> iDialog.destroy());
            helpDialog.setSize(800, MATCH_CONTENT);
            helpDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
            helpDialog.show();
        });

    }

    /**
     * 挑战结果对话框
     * @param time
     */
    private void popupCertificate(String time) {
        // 初始一个公共对话框
        dialog = new CommonDialog(getContext());
        // 对话框内容为一张图片
        dialog.setContentImage(ResourceTable.Media_certificate);
        // 背景为透明
        dialog.setTransparent(true);
        // 查找对话框容器组件
        Component container = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_dialog_certificate, null, false);
        // 从对话框容器组件查找显示时间文本组件
        Text txtTime = (Text)container.findComponentById(ResourceTable.Id_txtTime);
        // 显示最终使用时间,单位为秒
        txtTime.setText(time);

        // 返回游戏首页
        Image btnGoHome = (Image)container.findComponentById(ResourceTable.Id_btnGoHome);
        btnGoHome.setClickedListener(this::onClickGoHome);
        // 再来一次
        Image btnTryAgain = (Image)container.findComponentById(ResourceTable.Id_btnTryAgain);
        btnTryAgain.setClickedListener(this::onClickTryAgain);

        // 设置对话框内容为自定义组件
        dialog.setContentCustomComponent(container);
        // 设置对话框宽度和高度为内容宽高
        dialog.setSize(MATCH_CONTENT, MATCH_CONTENT);
        // 显示对话框
        dialog.show();
    }

    /**
     * 返回游戏首页
     * @param component
     */
    private void onClickGoHome(Component component) {
        // 关闭对话框
        dialog.destroy();
        // 终结当前Albility
        terminateAbility();
    }

    /**
     * 再来一次
     * @param component
     */
    private void onClickTryAgain(Component component) {
        // 关闭对话框
        dialog.destroy();
        // 初始化游戏界面
        initGame(currentCount);
    }

    /**
     * vp转px
     * @param vp
     * @return
     */
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
