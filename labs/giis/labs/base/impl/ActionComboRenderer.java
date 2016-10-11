package giis.labs.base.impl;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ActionComboRenderer extends JLabel implements ListCellRenderer {
	
	public ActionComboRenderer() {
		setOpaque(true);
        setVerticalAlignment(CENTER);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		LabsAction action = (LabsAction)value;
		setText((String)action.getValue(LabsAction.NAME));
		setFont(list.getFont());
		setIcon((Icon)action.getValue(LabsAction.SMALL_ICON));
		
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

		return this;
	}

}
