package be.bosa.ebox.providerbsservice.exceptions;

import be.bosa.dp.common.exceptions.CentralResponseExceptionHandler;
import be.bosa.ebox.providerbsservice.api.model.StatusMessage;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Maps internal exceptions thrown inside to {@link StatusMessage} object.
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ResponseExceptionHandler extends CentralResponseExceptionHandler<StatusMessage> {
    private final AppConfig appConfig;

    @Override
    protected StatusMessage buildResponseBody(Exception ex, HttpStatus status) {
        final StatusMessage statusMessage = new StatusMessage()
                .id(UUID.randomUUID())
                .environment(StatusMessage.EnvironmentEnum.fromValue(appConfig.getEnvironmentName()))
                .contact(appConfig.getEnvironmentContact());

        if (isNull(ex)) {
            return statusMessage
                    .code(String.valueOf(status.value()))
                    .message("");
        }

        return statusMessage
                .code(String.valueOf(status.value()))
                .message(ex.getMessage());
    }

}
