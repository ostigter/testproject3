// This file is part of the 'portfolio-manager' (Portfolio Manager)
// project, an open source stock portfolio manager application
// written in Java.
//
// Copyright 2015 Oscar Stigter
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

package org.ozsoft.portfoliomanager.ui.table.column;

import org.ozsoft.datatable.DefaultColumnRenderer;

/**
 * Column renderer for (neutral) percentage values.
 *
 * @author Oscar Stigter
 */
public class PercentageColumnRenderer extends DefaultColumnRenderer {

    private static final long serialVersionUID = -765351086313615291L;

    @Override
    public String formatValue(Object value) {
        if (value instanceof Double) {
            double percValue = (double) value;
            if (percValue == 0.0) {
                return null;
            } else {
                return String.format("%.2f %%", (double) value);
            }
        } else {
            return null;
        }
    }
}
