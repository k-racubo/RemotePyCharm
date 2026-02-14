package com.kracubo.controlPanel.layout

import java.awt.GridLayout

class DynamicGridLayout(rows: Int, cols: Int, hgap: Int, vgap: Int) : GridLayout(rows, cols, hgap, vgap) {
    /**
     * @param dynamicRows set GridLayout rows value
     * @see GridLayout
     */

    var dynamicRows: Int = rows
        set(value) {
            field = value
            this.rows = value
        }
}