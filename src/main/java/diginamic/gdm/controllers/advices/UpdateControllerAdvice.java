package diginamic.gdm.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import diginamic.gdm.dto.errors.BadRequestDTO;
import diginamic.gdm.exceptions.BadRequestException;

@ControllerAdvice
public class UpdateControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class})
    public @ResponseBody BadRequestDTO updateFailed(BadRequestException badRequestException, WebRequest request){
        return new BadRequestDTO(badRequestException.getMessage(), badRequestException.getCode());
    }

    /**
     * this method has to disappear
     * @param exception the exception
     * @param request the request
     * @return the communication exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {Exception.class})
    public @ResponseBody BadRequestDTO updateFailed(Exception exception, WebRequest request){
        return new BadRequestDTO(exception.getMessage(), "generic");
    }

}
