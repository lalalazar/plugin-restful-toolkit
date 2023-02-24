package com.ezio.plugin.GUI;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.SimpleTree;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreeSelectionModel;

public class APIWindowFactory implements ToolWindowFactory {


    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        APIWindow myToolWindow = new APIWindow(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }


}
