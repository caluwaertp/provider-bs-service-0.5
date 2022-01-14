package be.bosa.ebox.providerbsservice.api;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.api.model.MessageProvider;
import be.bosa.ebox.providerbsservice.api.model.MessageProviders;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsBinary;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;
import be.bosa.ebox.providerbsservice.services.MessageProviderServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class ProviderRegistryApiImpl implements ProviderregistryApi {
    private final MessageProviderServiceImpl messageProviderService;

    @Override
    public ResponseEntity<MessageProviders> findAllMessageProviders(Integer page, Integer pageSize) {
        return ResponseEntity.ok(messageProviderService.findAllMessageProviders(page, pageSize));
    }

    @Override
    public ResponseEntity<MessageProvider> getMessageProviderById(String messageProviderId) {
        return ResponseEntity.ok(messageProviderService.getMessageProviderById(messageProviderId));
    }

    @Override
    public ResponseEntity<byte[]> getImageById(String imageId, String messageProviderId, String formatId) {
        final RegBsImage regBsImage = messageProviderService.getRegBsImageByMessageProviderIdAndImageIdAndFormat(
                messageProviderId, imageId, Image.FormatEnum.fromValue(formatId));
        final RegBsBinary regBsBinary = messageProviderService.getRegBsBinaryById(regBsImage.getBinaryId());
        final MediaType mediaType = MediaType.parseMediaType(regBsImage.getMediaType().toString());
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(mediaType);
        return new ResponseEntity<>(regBsBinary.getContent(), httpHeaders, HttpStatus.OK);
    }
}
