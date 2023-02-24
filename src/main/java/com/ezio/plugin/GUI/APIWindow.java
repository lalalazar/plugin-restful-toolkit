package com.ezio.plugin.GUI;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ezio.plugin.ChickenSoup.TanutDialog;
import com.ezio.plugin.navigator.action.TreePopupHandler;
import com.ezio.plugin.navigator.component.RestApiNavigator;
import com.ezio.plugin.navigator.component.RestApiProjectManager;
import com.ezio.plugin.navigator.domain.RestServiceItem;
import com.ezio.plugin.navigator.domain.RestServiceProject;
import com.ezio.plugin.utils.LogUtils;
import com.ezio.plugin.utils.ToolkitUtils;
import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.treeStructure.SimpleTree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class APIWindow {
    private JButton button1;
    private JPanel toolWindow;
    private JTextField textField1;
    private JButton runButton;
    private JComboBox<String> comboBox1;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;

    public void setTextField1(String text) {
        this.textField1.setText(text);
    }

    public void setTabbedPane(String text) {
        tabbedPane1.getSelectedIndex();
    }

    private JTextArea textArea2;
    private JTextArea textArea3;
    //    public JPanel panel;
    public SimpleTree treeApi;
    private JToolBar toolBar;
    private JButton refresh;
    private JButton popup;
    private JTextArea responseArea;
    private JScrollPane scrollPane;
    private final Project project;


    public APIWindow(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        setToolBar();
        setComboBox();
        setTextArea();
        setButton();
        setRunButton();
        setPopup();
        initAndRefresh();
        addTreeListener();

    }

    private void setRunButton() {
        runButton.addActionListener(a -> {
            String method = comboBox1.getSelectedIndex() == 0 ? "GET" : "POST";
            String url = textField1.getText();
            String header = textArea1.getText();
            String params = textArea1.getText();
            HttpRequest httpRequest;
            if ("GET".equals(method)) {
                httpRequest = HttpUtil.createGet(url);
            } else {
                String body = textArea1.getText();
                httpRequest = HttpUtil.createPost(url).body(body);
                if (StringUtils.isNotEmpty(params) && JSONUtil.isTypeJSON(params)) {
                    JSONObject entries = JSONUtil.parseFromXml(params);
                    Map<String, Object> formMap = new HashMap<>(entries);
                    if (ObjectUtil.isNotEmpty(formMap)) {
                        httpRequest = httpRequest.form(formMap);
                    }
                }
            }
            if (StringUtils.isNotEmpty(header) && JSONUtil.isTypeJSON(header)) {
                JSONObject headerJson = JSONUtil.parseObj(header);
                Map<String, String> headerMap = new HashMap<>();
                headerJson.forEach((key, value) -> headerMap.put(key, String.valueOf(value)));
                httpRequest.addHeaders(headerMap);
            }
            HttpResponse response;
            try {
                response = httpRequest.execute();
                if (response.isOk()) {
                    String body = response.body();
                    responseArea.setText(body);
                    tabbedPane1.setSelectedIndex(3);
                } else {
                    responseArea.setText(response.toString());
                    tabbedPane1.setSelectedIndex(3);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Notifications.Bus.notify(new Notification("ToolsMenu", textField1.getText(),
                    comboBox1.getSelectedItem() + "+" + textArea1.getText() + "+" + textArea2.getText() + "+" + textArea3.getText(), NotificationType.INFORMATION));
        });
        runButton.setIcon(AllIcons.Actions.Rerun);
    }

    public void refreshTree() {
        RestApiNavigator restApiNavigator = RestApiNavigator.getInstance(project);
        restApiNavigator.scheduleStructureUpdate(treeApi);
    }

    private void addTreeListener() {
        treeApi.addMouseListener(new TreePopupHandler(comboBox1, textField1));
    }

    private void initTree() {
        DefaultTreeModel model = (DefaultTreeModel) treeApi.getModel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(project.getName());
        model.setRoot(root);
        model.reload();
    }

    private void setPopup() {
        popup.addActionListener(e -> {
            Messages.showMessageDialog(project, "Popup something ! ", "按钮弹窗", Messages.getInformationIcon());
        });
    }

    private void setButton() {
        button1.setText("鸡汤");
        button1.setToolTipText("tips");
        button1.addActionListener(e -> {
            TanutDialog tanutDialog = new TanutDialog();
            tanutDialog.show();
        });
    }

    private void setTextArea() {
        textArea1.setAutoscrolls(true);
        textArea1.setVisible(true);
        textArea2.setAutoscrolls(true);
        textArea2.setVisible(true);
        textArea3.setAutoscrolls(true);
        textArea3.setVisible(true);
    }

    public void setComboBox() {
        comboBox1.insertItemAt("GET", 0);
        comboBox1.insertItemAt("POST", 1);
        comboBox1.setSelectedIndex(0);
        comboBox1.setEditable(true);
    }

    public void initAndRefresh() {
        ToolkitUtils.runWhenInitialized(project, () -> {
            if (!project.isDisposed()) {
                refreshTree();
            }
        });
        refresh.addActionListener(e -> {
            refreshTree();
            LogUtils.showInfo("Refresh Success!");
        });
        refresh.setIcon(AllIcons.Actions.Refresh);
    }

    public void setToolBar() {
        final ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar("Navigator Toolbar",
                (DefaultActionGroup) actionManager
                        .getAction("NavigatorToolbar"),
                true);
        toolBar.add(actionToolbar.getComponent());
    }

    private List<RestServiceProject> getApiList() {
        RestApiProjectManager restApiProjectManager = new RestApiProjectManager(project);
        return restApiProjectManager.getServiceProjectList();
    }

    private DefaultMutableTreeNode getApiNodes() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(project.getName());
        List<RestServiceProject> apiList = getApiList();
        Optional.ofNullable(apiList).ifPresent(apis -> {
            if (apis.size() > 1) {
                List<RestServiceItem> restServiceItemList = apis.get(0).getRestServiceItemList();
                if (CollectionUtils.isNotEmpty(restServiceItemList)) {
                    restServiceItemList.forEach(item -> {
                        DefaultMutableTreeNode path = new DefaultMutableTreeNode(item.getUrl());
                        root.add(path);
                    });
                }
            } else {
                for (RestServiceProject service : apis) {
                    DefaultMutableTreeNode module = new DefaultMutableTreeNode(service.getModuleName());
                    root.add(module);
                    for (RestServiceItem item : service.getRestServiceItemList()) {
                        DefaultMutableTreeNode path = new DefaultMutableTreeNode(item.getUrl());
                        module.add(path);
                    }
                }
            }
        });

        return root;
    }

    public static APIWindow getInstance(Project project) {
        return project.getComponent(APIWindow.class);
    }

    private JTree treeNode() {
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("软件部");
        node1.add(new DefaultMutableTreeNode("xiaoxiao"));
        node1.add(new DefaultMutableTreeNode("小虎"));
        node1.add(new DefaultMutableTreeNode("小龙"));
        //创建树的第一种方式
        return new JTree(node1);
    }

    public DefaultMutableTreeNode getNode() {
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("软件部");
        node1.add(new DefaultMutableTreeNode("xiaoxiao"));
        node1.add(new DefaultMutableTreeNode("小虎"));
        node1.add(new DefaultMutableTreeNode("小龙"));
        return node1;
    }

    public JScrollPane getPanel() {
        JTree jTree = new JTree();

        //创建树的第二种方式：推荐
        //创建根节点
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("根节点");
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("软件部");
        node1.add(new DefaultMutableTreeNode("xiaoxiao"));
        node1.add(new DefaultMutableTreeNode("小虎"));
        node1.add(new DefaultMutableTreeNode("小龙"));
        defaultMutableTreeNode.add(node1);
        //创建树模型
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(defaultMutableTreeNode);

        //创建树
        JTree jTree2 = new JTree(defaultTreeModel);
        //创建滚动面板
        JScrollPane jScrollPane = new JScrollPane(jTree2);
        return jScrollPane;
    }

    public JPanel getContent() {
        return toolWindow;
    }
}
