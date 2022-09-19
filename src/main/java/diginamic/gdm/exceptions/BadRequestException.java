package diginamic.gdm.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends Exception {

    private String code;

    public BadRequestException(String message, String code){
        super(message);
        this.code = code;
    }
}
