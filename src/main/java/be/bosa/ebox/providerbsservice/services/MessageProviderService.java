package be.bosa.ebox.providerbsservice.services;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.api.model.MessageProvider;
import be.bosa.ebox.providerbsservice.api.model.MessageProviders;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsBinary;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;

public interface MessageProviderService {
    MessageProviders findAllMessageProviders(int page, int pageSize);

    MessageProvider getMessageProviderById(String messageProviderId);

    RegBsImage getRegBsImageByMessageProviderIdAndImageIdAndFormat(String messageProviderId, String imageId, Image.FormatEnum format);

    RegBsBinary getRegBsBinaryById(String binaryId);
}
