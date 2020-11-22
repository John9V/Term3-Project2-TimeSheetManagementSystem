package bcit.infosys.student.employee;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Adds methods needed for the DAO object.
 * @author Scott Reid, Ivan Vaganov
 *
 */
public interface EditableEmployeeList extends EmployeeList {
	void save();
	public EditableEmployee getEmployeeByUserName(String userName);
	public List<EditableEmployee> getEditableEmployees();
	public boolean isAdmin(Employee employee);
	public void addEmployee(EditableEmployee e);
	public void updateEmployee(EditableEmployee e);
}
