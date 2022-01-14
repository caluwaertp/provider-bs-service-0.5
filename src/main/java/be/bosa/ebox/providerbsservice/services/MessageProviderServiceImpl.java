package be.bosa.ebox.providerbsservice.services;

import be.bosa.dp.common.exceptions.ResourceNotFoundException;
import be.bosa.ebox.providerbsservice.api.model.*;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsBinary;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;
import be.bosa.ebox.providerbsservice.integration.cqengine.repositories.ImageRepository;
import be.bosa.ebox.providerbsservice.integration.cqengine.repositories.MessageRegistryRepository;
import be.bosa.ebox.providerbsservice.services.mappers.ImageMapper;
import be.bosa.ebox.providerbsservice.services.mappers.MessageRegistryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static be.bosa.ebox.providerbsservice.services.utils.ContentHrefUtils.getContentHrefForImage;
import static be.bosa.ebox.providerbsservice.services.utils.ContentHrefUtils.getContentHrefForMessageProvider;
import static java.util.Objects.isNull;

@Slf4j
@Service("messageProviderService")
@AllArgsConstructor
public class MessageProviderServiceImpl implements MessageProviderService {
    private final MessageRegistryMapper messageRegistryMapper = Mappers.getMapper(MessageRegistryMapper.class);
    private final ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);
    private final MessageRegistryRepository messageRegistryRepository;
    private final ImageRepository imageRepository;
    private final AppConfig appConfig;

    @Override
    public MessageProviders findAllMessageProviders(final int page, final int pageSize) {
        long pageOffset = ((long) (page > 0 ? page - 1 : 0) * pageSize);
        final List<MessageProvider> messageProviderList = messageRegistryRepository.findAll().stream()
                .skip(pageOffset).limit(pageSize)
                .map(registry -> messageRegistryMapper.mapToMessageProvider(registry, appConfig)).collect(Collectors.toList());
        messageProviderList.forEach(messageProvider -> messageProvider.setMessageProviderLogo(getMessageProviderLogo(messageProvider.getMessageProviderId())));
        final MessageProviders messageProviders = new MessageProviders().items(messageProviderList).totalItems(messageProviderList.size());
        setContentHref(messageProviders);
        return messageProviders;
    }

    @Override
    public MessageProvider getMessageProviderById(String messageProviderId) {
        final MessageProvider messageProvider = messageRegistryRepository.findById(messageProviderId)
                .map(registry -> messageRegistryMapper.mapToMessageProvider(registry, appConfig)).orElseThrow(ResourceNotFoundException::new);
        messageProvider.setMessageProviderLogo(getMessageProviderLogo(messageProviderId));
        setContentHref(messageProvider);
        return messageProvider;
    }

    private List<Image> getMessageProviderLogo(String messageProviderId) {
        return imageRepository.findAllByMessageRegistryId(messageProviderId).stream()
                .map(regBsImage -> imageMapper.mapToImage(regBsImage, imageRepository)).collect(Collectors.toList());
    }

    @Override
    public RegBsImage getRegBsImageByMessageProviderIdAndImageIdAndFormat(String messageProviderId, String imageId, Image.FormatEnum format) {
        return imageRepository.findByMessageRegistryIdAndImageIdAndFormat(messageProviderId, imageId, format)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public RegBsBinary getRegBsBinaryById(String binaryId) {
        return imageRepository.findBinaryById(binaryId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private void setContentHref(MessageProviders messageProviders) {
        if (!CollectionUtils.isEmpty(messageProviders.getItems())) {
            messageProviders.getItems().forEach(this::setContentHref);
        }
    }

    private void setContentHref(MessageProvider messageProvider) {
        if (!isNull(messageProvider)) {
            messageProvider.setLinks(new HalLinks().self(new HalLink().href(getContentHrefForMessageProvider(appConfig.getUri(),
                    messageProvider.getMessageProviderId()))));
            if (!CollectionUtils.isEmpty(messageProvider.getMessageProviderLogo())) {
                messageProvider.getMessageProviderLogo().forEach(image -> setContentHref(messageProvider, image));
            }
        }
    }

    private void setContentHref(MessageProvider messageProvider, Image image) {
        if (!isNull(image)) {
            final String contentHref = getContentHrefForImage(appConfig.getUri(), messageProvider.getMessageProviderId(),
                    image.getImageId(), image.getFormat().toString());
            image.setLinks(new HalLinks().self(new HalLink().href(contentHref)));
            image.setContentHref(contentHref);
        }
    }
}
