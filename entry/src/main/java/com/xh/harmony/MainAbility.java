package com.xh.harmony;

import com.xh.harmony.slice.MainAbilitySlice;
import com.xh.harmony.slice.PlayAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

// 主页面 Ability 类
public class MainAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        // 设置主页面 Slice
        super.setMainRoute(MainAbilitySlice.class.getName());

        // 添加 PlayAbilitySlice 到路由表中
        addActionRoute(MainAbilitySlice.PLAY_PAGE, PlayAbilitySlice.class.getName());
    }

    @Override
    public void onBackground() {
        super.onBackground();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }


}