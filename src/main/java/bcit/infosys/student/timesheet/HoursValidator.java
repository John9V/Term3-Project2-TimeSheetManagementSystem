package bcit.infosys.student.timesheet;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("ca.bcit.infosys.timesheet.HoursValidator")
public class HoursValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        if (value != null) {
            String stringVal = value.toString();
            boolean condition1 = false;
            int count = 0;

            for (char c : stringVal.toCharArray()) {
                if (condition1) {
                    count++;
                }
                if (c == '.') {
                    condition1 = true;
                }

                if (count >= 2) {
                    FacesMessage msg = new FacesMessage("invalid format,"
                            + " maximum one decimal place", "invalid format,"
                                    + " maximum one decimal place");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
        }
    }

}
