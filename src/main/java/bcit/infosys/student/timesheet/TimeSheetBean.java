package bcit.infosys.student.timesheet;

import java.util.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.timesheetrow.EditableTimesheetRow;
import bcit.infosys.student.timesheetrow.RowListBean;
import ca.bcit.infosys.timesheet.Timesheet;

import java.io.Serializable;

/**
 * Bean used for editing timesheets.
 * @author Ivan (John) Vaganov and Scott Reid
 *
 */
@Named
@SessionScoped
public class TimeSheetBean extends EditableTimesheet implements Serializable {
    
	@Inject private RowListBean rowListBean;
	@Inject private TimesheetCollectionBean timesheetCollectionBean;
	
    /**
     * The timesheet loaded and being edited.
     */
    private EditableTimesheet t;
    /**
     * The date of the timesheet.
     */
    private Date ld;
      
    /**
     * Zero-param constructor.
     */
    public TimeSheetBean() { }
    
    /**
     * Getter for the date of this timesheet.
     * @return the date entered on the edit timesheet screen.
     */
    public Date getLd() {
		return ld;
	}

	/**
	 * Setter for the date, converts the date entered to a localdate.
	 * @param ld is the date entered into the edit timesheet screen by
	 * the user.
	 */
	public void setLd(Date ld) {
		if (ld != null) {
			System.out.println("ld is not null");
			LocalDate date = ld.toInstant()
			        .atZone(ZoneId.systemDefault()).toLocalDate();
			date = date.with(TemporalAdjusters
			        .nextOrSame(DayOfWeek.FRIDAY));
			t.setEndWeek(date);
			timesheetCollectionBean.updateTimesheet(t);
			rowListBean.refresh();
			timesheetCollectionBean.refresh();
			this.ld = ld;
		}
	}

	/**
	 * Constructor that includes a timesheet.
	 * @param t the timesheet being edited.
	 */
	public TimeSheetBean(EditableTimesheet t) {
        this.t = t;
    }
    
    /**
     * Setter for the timesheet currently loaded into this user's
     * edit timesheet screen.
     * @param t is the timesheet being edited.
     */
    public void setTimesheet(EditableTimesheet t) {
    	rowListBean.setCurrentSheet(t);
    	rowListBean.refresh();
        this.t = t;
    }
    
    /** 
     * Getter for the timesheet being modified.
     * @return the timesheet being edited.
     */
    public Timesheet getT() {
        return t;
    }

    /**
     * Setter for the timesheet being edited.
     * @param t is the timesheet being edited.
     */
    public void setT(EditableTimesheet t) {
        this.t = t;
    }
    
    /**
     * Method that adds a row from the current timesheet being edited.
     * @return a string that will reload the page.
     */
    public String addRowFromBean() {
    	EditableTimesheetRow newTsr = new EditableTimesheetRow();
    	newTsr.setOwnerSheetId(t.getTimesheetId());
    	newTsr.setProjectID(31);
    	rowListBean.addTimesheetRow(newTsr);
    	rowListBean.refresh();
    	return "";
    }
}
