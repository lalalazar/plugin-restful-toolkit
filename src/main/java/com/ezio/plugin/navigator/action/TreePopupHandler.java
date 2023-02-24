package com.ezio.plugin.navigator.action;

import cn.hutool.core.util.ObjectUtil;
import com.ezio.plugin.GUI.APIWindow;
import com.ezio.plugin.navigator.component.RestApiStructure;
import com.ezio.plugin.navigator.domain.NodeDataContext;
import com.ezio.plugin.navigator.domain.RestServiceItem;
import com.ezio.plugin.utils.RestApiDataKeys;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Here be dragons !
 *
 * @author: Ezio
 * created on 2020/3/5
 */
public class TreePopupHandler extends PopupHandler {

    private JPopupMenu popup;
    private JComboBox<String> comboBox;
    private JTextField textField;

    public TreePopupHandler(JComboBox<String> comboBox, JTextField textField) {
        this.comboBox = comboBox;
        this.textField = textField;
    }
//    public TreePopupHandler(){}

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (!(e.getSource() instanceof SimpleTree)) {
            return;
        }
        SimpleNode selectedNode = ((SimpleTree) e.getSource()).getSelectedNode();
        if (!(selectedNode instanceof RestApiStructure.ServiceNode)) {
            return;
        }
        RestApiStructure.ServiceNode serviceNode = (RestApiStructure.ServiceNode) selectedNode;
        if (ObjectUtil.isNotEmpty(serviceNode) && ObjectUtil.isNotEmpty(serviceNode.getRestServiceItem())) {
            RestServiceItem restServiceItem = serviceNode.getRestServiceItem();
            RestApiDataKeys.put(restServiceItem);
            if (e.getClickCount() == 1) {

                String method = restServiceItem.getRequestMethod().toUpperCase();
                int index = method.equals("POST") ? 1 : 0;
                comboBox.setSelectedIndex(index);
                textField.setText(restServiceItem.getFullUrl());
            }
        }
        if (e.getClickCount() >= 2) {
            final ActionManager actionManager = ActionManager.getInstance();
            AnAction action = actionManager.getAction("Ezio.GotoRequestMappingAction");
            AnActionEvent newEvent =
                    new AnActionEvent(null, new NodeDataContext(serviceNode.getRestServiceItem()), ActionPlaces.UNKNOWN, new Presentation(""),
                            actionManager, 0);
            action.actionPerformed(newEvent);
        }


    }

    @Override
    public void invokePopup(Component comp, int x, int y) {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction("Ezio.NavigatorServiceMenu");
        popup = ActionManager.getInstance().createActionPopupMenu("", actionGroup).getComponent();
        popup.show(comp, x, y);

    }

    public void hidePopup() {
        if (popup != null && popup.isVisible()) {
            popup.setVisible(false);
            popup = null;
        }
    }
}
