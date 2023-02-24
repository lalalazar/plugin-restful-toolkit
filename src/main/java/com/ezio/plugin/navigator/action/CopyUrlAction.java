package com.ezio.plugin.navigator.action;

import cn.hutool.core.util.ObjectUtil;
import com.ezio.plugin.GUI.APIWindow;
import com.ezio.plugin.navigator.domain.RestServiceItem;
import com.ezio.plugin.utils.LogUtils;
import com.ezio.plugin.utils.RestApiDataKeys;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ide.CopyPasteManager;

import java.awt.datatransfer.StringSelection;
import java.util.Objects;
import java.util.Optional;

/**
 * Here be dragons !
 *
 * @author: Ezio
 * created on 2020/3/5
 */
public class CopyUrlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        RestServiceItem restServiceItem = RestApiDataKeys.get();
        if (ObjectUtil.isNotEmpty(restServiceItem)) {
            CopyPasteManager.getInstance().setContents(new StringSelection(restServiceItem.getFullUrl()));
            LogUtils.showInfo("copied to pasteboard success");
        } else {
            LogUtils.showInfo("copied to pasteboard fail");
        }
    }


}
