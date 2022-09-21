package diginamic.gdm.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends Exception {

    /** serialVersionUID */
	private static final long serialVersionUID = 4968915439221191862L;
	private String code;

    public BadRequestException(String message, String code){
        super(message);
        this.code = code;
    }
}
