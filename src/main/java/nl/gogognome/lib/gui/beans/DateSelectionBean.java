/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.gui.beans;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import nl.gogognome.lib.swing.SwingUtils;
import nl.gogognome.lib.swing.WidgetFactory;
import nl.gogognome.lib.swing.models.DateModel;
import nl.gogognome.lib.swing.models.ModelChangeListener;
import nl.gogognome.lib.swing.views.ViewPopup;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.Factory;

/**
 * This class implements a bean for selecting a Date.
 *
 * @author Sander Kooijmans
 */
public class DateSelectionBean extends AbstractTextFieldBean<DateModel> {

	private static final long serialVersionUID = 1L;
	private final String dateFormatId;

	public static DateSelectionBean createBeanForTimestampWithMinuteAccuracy(DateModel model) {
		return new DateSelectionBean(model, "gen.timestampFormat.hhmm", 12);
	}

	public static DateSelectionBean createBeanForTimestampWithSecondAccuracy(DateModel model) {
		return new DateSelectionBean(model, "gen.timestampFormat.hhmmss", 14);
	}

	public static DateSelectionBean createBeanForDate(DateModel model) {
		return new DateSelectionBean(model, "gen.dateFormat", 10);
	}

	private DateSelectionBean(DateModel dateModel, String dateFormatId, int nrColumns) {
		super(dateModel, nrColumns);
		this.dateFormatId = dateFormatId;
	}

	@Override
	public void initBean() {
		super.initBean();
		WidgetFactory wf = Factory.getInstance(WidgetFactory.class);
		JButton button = wf.createIconButton("gen.btnCalendar", new ShowCalendarPopupAction(), 21);
		add(button);
	}

	@Override
	protected String getStringFromModel() {
		Date date = model.getDate();
		if (date != null) {
			return Factory.getInstance(TextResource.class).formatDate(dateFormatId, date);
		} else {
			return "";
		}
	}

	@Override
	protected void parseUserInput(String text,
			ModelChangeListener modelChangeListener) throws ParseException {
		Date date = null;
		try {
			date = Factory.getInstance(TextResource.class).parseDate(dateFormatId, text);
		} finally {
			// Set the date in the finally block. This ensures that the
			// model is cleared in case a ParseException is thrown
			model.setDate(date, modelChangeListener);
		}
	}

	private void showCalendarPopup() {
		CalendarView calendarPanel = new CalendarView(model);
		ViewPopup viewPopup = new ViewPopup(calendarPanel);
		viewPopup.show(this, SwingUtils.getCoordinatesRelativeToTopLevelContainer(this));
	}

	private final class ShowCalendarPopupAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			showCalendarPopup();
		}
	}

}
