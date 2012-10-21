// This file is part of the BackupTool project.
//
// Copyright 2010 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.photomanager.gui;

import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Abstract base class for Swing dialogs.
 * 
 * @author Oscar Stigter
 */
public abstract class Dialog {
    
    public static final int OK = 0;
    
    public static final int CANCEL = 1;
    
    protected final JFrame owner;
    
    protected final JDialog dialog;
    
    protected int result = CANCEL;
    
    public Dialog(String title, JFrame owner) {
        this.owner = owner;
        dialog = new JDialog(owner, title, true);
        dialog.setLayout(new GridBagLayout());
        initUI();
    }
    
    public int show() {
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
        return result;
    }
    
    public void hide() {
        dialog.setVisible(false);
    }
    
    public int getResult() {
        return result;
    }

    protected abstract void initUI();

}
