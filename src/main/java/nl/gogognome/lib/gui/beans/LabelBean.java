package nl.gogognome.lib.gui.beans;

import javax.swing.JLabel;

import nl.gogognome.lib.swing.models.AbstractModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.swing.models.StringModel;

public class LabelBean extends JLabel implements Bean {

	private static final long serialVersionUID = 1L;

	private final StringModel stringModel;
	private ModelChangeListenerImpl modelChangeListener;

    public LabelBean(StringModel stringModel) {
    	this.stringModel = stringModel;
    }

	@Override
	public void initBean() {
		modelChangeListener = new ModelChangeListenerImpl();
		stringModel.addModelChangeListener(modelChangeListener);
		setModelContentsInLabel();
	}

	@Override
	public void close() {
		stringModel.removeModelChangeListener(modelChangeListener);
		modelChangeListener = null;
	}

	private void setModelContentsInLabel() {
		setText(stringModel.getString());
	}

	private class ModelChangeListenerImpl implements ModelChangeListener {

		@Override
		public void modelChanged(AbstractModel model) {
			setModelContentsInLabel();
		}
	}

}
