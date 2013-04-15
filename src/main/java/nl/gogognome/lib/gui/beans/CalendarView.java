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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.AbstractSpinnerModel;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.gogognome.lib.gui.Closeable;
import nl.gogognome.lib.swing.SwingUtils;
import nl.gogognome.lib.swing.models.DateModel;
import nl.gogognome.lib.swing.views.OkCancelButtonPanel;
import nl.gogognome.lib.swing.views.View;
import nl.gogognome.lib.text.TextResource;
import nl.gogognome.lib.util.DateUtil;
import nl.gogognome.lib.util.Factory;

/**
 * Panel showing a calendar. It lets the user pick a date.
 *
 * @author Sander Kooijmans
 */
class CalendarView extends View implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private DateModel dateModel;

	private JSpinner monthSpinner;

	private JSpinner yearSpinner;

	private Calendar calendar;

	/** Contains the days shown in the calendar. */
	private int[][] days = new int[6][7];

	/** Contains the first day of the week (typically Sunday or Monday). */
	private int firstDayOfWeek;

	private DateFormat firstLetterOfDayFormat = new SimpleDateFormat("E");

	private String[] dayNames = new String[7];

	private int currentDay;

	public CalendarView(DateModel dateModel) {
		this.dateModel = dateModel;
	}

	@Override
	public void onInit() {
		initModels();
		addComponents();
		updateDaysOfMonth();
		addListeners();
	}

	@Override
	public void onClose() {
		removeListners();
	}

	@Override
	public String getTitle() {
		return "";
	}

	private void initModels() {
		calendar = Calendar.getInstance(textResource.getLocale());
		if (dateModel.getDate() != null) {
			calendar.setTime(dateModel.getDate());
		}
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		firstDayOfWeek = calendar.getFirstDayOfWeek();
	}

	private void addComponents() {
		setLayout(new BorderLayout());
		add(createMonthYearPanel(), BorderLayout.NORTH);

		DaysOfMonthPanel p = new DaysOfMonthPanel();
		addCloseable(p);
		add(p, BorderLayout.CENTER);

		add(new OkCancelButtonPanel(createOkAction(), closeAction), BorderLayout.SOUTH);
	}

	private JPanel createMonthYearPanel() {
		Date initialDate = getInitialDate();
		JPanel panel = new JPanel(new GridBagLayout());

		int month = DateUtil.getField(initialDate, Calendar.MONTH);
		monthSpinner = new JSpinner(new MonthSpinnerModel(month));
		panel.add(monthSpinner, SwingUtils.createGBConstraints(0, 0, 1, 0, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5));

		yearSpinner = new JSpinner();
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "0"));
		yearSpinner.setValue(DateUtil.getField(initialDate, Calendar.YEAR));
		panel.add(yearSpinner, SwingUtils.createGBConstraints(1, 0, 1, 0, 0.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 5, 0, 5, 5));

		return panel;
	}

	private void updateDaysOfMonth() {
		calendar.set(getSelectedYear(), getSelectedMonth(), 1);
		clearDays();
		int row = 0;
		int col = 0;
		int dayOfWeek = firstDayOfWeek;
		while (dayOfWeek != calendar.get(Calendar.DAY_OF_WEEK)) {
			col++;
			dayOfWeek++;
			if (dayOfWeek > Calendar.SATURDAY) {
				dayOfWeek = Calendar.SUNDAY;
			}
		}

		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		boolean finished = false;
		while (!finished) {
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			days[row][col] = day;
			dayNames[col] = firstLetterOfDayFormat.format(calendar.getTime());
			finished = day == lastDayOfMonth;
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			col++;
			if (col == 7) {
				col = 0;
				row++;
			}
		}
	}

	private void clearDays() {
		for (int i=0; i<days.length; i++) {
			for (int j=0; j<days[i].length; j++) {
				days[i][j] = 0;
			}
		}
	}

	private void addListeners() {
		monthSpinner.addChangeListener(this);
		yearSpinner.addChangeListener(this);
	}

	private void removeListners() {
		monthSpinner.removeChangeListener(this);
		yearSpinner.removeChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent changeevent) {
		calendar.set(Calendar.MONTH, getSelectedMonth());
		calendar.set(Calendar.YEAR, getSelectedYear());
		currentDay = Math.min(currentDay, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		updateDaysOfMonth();
		repaint();
	}

	private int getSelectedMonth() {
		return ((Month) monthSpinner.getValue()).nr;
	}

	private int getSelectedYear() {
		return (Integer) yearSpinner.getValue();
	}

	private Date getInitialDate() {
		Date initialDate = dateModel.getDate();
		if (initialDate == null) {
			initialDate = new Date();
		}
		return initialDate;
	}

	private void onOk() {
		Date date = DateUtil.createDate(getSelectedYear(), getSelectedMonth() + 1, currentDay);
		dateModel.setDate(date, null);
		requestClose();
	}

	private Action createOkAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				onOk();
			}
		};
	}

	private class DaysOfMonthPanel extends JPanel implements MouseListener, Closeable {

		private final static int CELL_WIDTH = 22;
		private final static int CELL_HEIGHT = 22;

		public DaysOfMonthPanel() {
			addMouseListener(this);
		}

		@Override
		public void close() {
			removeMouseListener(this);
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);

			int x = 0;
			int y = 0;
			for (int i=0; i<7; i++) {
				paintCell(g, 0, i, dayNames[i].substring(0, 1), Color.BLACK, Color.LIGHT_GRAY);
			}

			for (int r=0; r<days.length; r++) {
				for (int c=0; c<days[r].length; c++) {
					String text = days[r][c] != 0 ? Integer.toString(days[r][c]) : "";
					Color background = days[r][c] == currentDay ? Color.LIGHT_GRAY : Color.WHITE;
					paintCell(g, r+1, c, text, Color.BLACK, background);
				}
			}
		}

		private void paintCell(Graphics g, int row, int col, String text, Color textColor,
				Color background) {
			g.setColor(background);
			int x = col * CELL_HEIGHT;
			int y = row * CELL_WIDTH;
			g.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);

			FontMetrics fm = g.getFontMetrics();
			int h = fm.getAscent();
			int w = fm.stringWidth(text);

			g.setColor(textColor);
			g.drawString(text, x + (CELL_WIDTH - w) / 2, y + (CELL_HEIGHT + h) / 2 - 1);
		}

		private int getRow(Point p) {
			return p.y / CELL_HEIGHT;
		}

		private int getColumn(Point p) {
			return p.x / CELL_WIDTH;
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(7 * CELL_WIDTH, (days.length + 1) * CELL_HEIGHT);
		}

		@Override
		public void mouseClicked(MouseEvent mouseevent) {
			if (mouseevent.getClickCount() == 2) {
				onOk();
			}
		}

		@Override
		public void mouseEntered(MouseEvent mouseevent) {
		}

		@Override
		public void mouseExited(MouseEvent mouseevent) {
		}

		@Override
		public void mousePressed(MouseEvent mouseevent) {
		}

		@Override
		public void mouseReleased(MouseEvent mouseevent) {
			int r = getRow(mouseevent.getPoint());
			r--; // skip row with day names
			int c = getColumn(mouseevent.getPoint());
			if (0 <= c && c < 7 && 0 <= r && r <= days.length) {
				int d = days[r][c];
				if (d != 0) {
					currentDay = d;
					repaint();
				}
			}
		}
	}

}

class Month {
	public final int nr;
	public final String description;

	public Month(int nr, String description) {
		this.nr = nr;
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}

class MonthSpinnerModel extends AbstractSpinnerModel {

	private Month[] values;
	private int currentValue;

	public MonthSpinnerModel(int initialValue) {
		values = new Month[12];
		TextResource tr = Factory.getInstance(TextResource.class);
		for (int i=Calendar.JANUARY; i<=Calendar.DECEMBER; i++) {
			values[i] = new Month(i, tr.getString("CalendarView." + i));
		}
		currentValue = initialValue;
	}

	@Override
	public Object getNextValue() {
		currentValue++;
		if (currentValue >= values.length) {
			currentValue = 0;
		}
		fireStateChanged();
		return getValue();
	}

	@Override
	public Object getPreviousValue() {
		currentValue--;
		if (currentValue < 0) {
			currentValue = values.length - 1;
		}
		fireStateChanged();
		return getValue();
	}

	@Override
	public Object getValue() {
		return values[currentValue];
	}

	@Override
	public void setValue(Object obj) {
		for (int i=0; i<values.length; i++) {
			if (values[i].description.equals(obj)) {
				if (currentValue != i) {
					currentValue = i;
		            fireStateChanged();
				}
			}
		}
	}

}