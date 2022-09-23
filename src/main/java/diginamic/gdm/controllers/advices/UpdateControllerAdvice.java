package diginamic.gdm.controllers.advices;

import diginamic.gdm.dto.errors.BadRequestDTO;
import diginamic.gdm.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UpdateControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class})
    public @ResponseBody BadRequestDTO updateFailed(BadRequestException badRequestException, WebRequest request){
        return new BadRequestDTO(badRequestException.getMessage(), badRequestException.getCode());
    }
}
