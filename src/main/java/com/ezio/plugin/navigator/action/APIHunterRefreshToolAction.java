package com.ezio.plugin.navigator.action;

import com.ezio.plugin.GUI.APIWindow;
import com.ezio.plugin.utils.LogUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

public class APIHunterRefreshToolAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = CommonDataKeys.PROJECT.getData(e.getDataContext());
        assert project != null;
        APIWindow window = APIWindow.getInstance(project);
        window.refreshTree();
        LogUtils.showInfo("refresh success");
    }
}
