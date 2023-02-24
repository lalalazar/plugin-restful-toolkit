package com.ezio.plugin.utils;

import com.ezio.plugin.constant.Icons;
import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class MessageUtil {

    public void test(Project project) {
        Notifications.Bus.notify(new Notification("ToolsMenu", "22", "33", NotificationType.INFORMATION));
        Messages.showMessageDialog(project, "Popup something ! ", "按钮弹窗", Messages.getInformationIcon());
        Messages.showMessageDialog("1","2", AllIcons.Windows.Help);
        Messages.showInfoMessage("22","33");

    }
}
