package com.ezio.plugin.ChickenSoup;

import com.ezio.plugin.utils.HttpUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TanutDialog extends DialogWrapper {
    JLabel label;

    public TanutDialog() {
        super(true);
        setTitle("每天一碗毒鸡汤");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel jpanel = new JPanel();
        label = new JLabel(HttpUtils.getTauntHttpGet());
        jpanel.add(label);
        return jpanel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel jPanel = new JPanel();
        JButton bu = new JButton("再干一碗");
        bu.addActionListener(e -> {
            String taunt = HttpUtils.getTauntHttpGet();
            label.setText(taunt);
        });
        jPanel.add(bu);
        return jPanel;
    }

}
