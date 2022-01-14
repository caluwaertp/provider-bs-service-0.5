package be.bosa.ebox.providerbsservice.services.mappers;

import be.bosa.ebox.providerbsservice.api.model.MessageProvider;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistry;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistryEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.MappingTarget;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class MessageRegistryMapperDecorator implements MessageRegistryMapper {
    private final MessageRegistryMapper delegate;

    protected MessageRegistryMapperDecorator(MessageRegistryMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public MessageProvider mapToMessageProvider(final RegBsMessageRegistry messageRegistry, final AppConfig appConfig) {
        MessageProvider messageProvider = delegate.mapToMessageProvider(messageRegistry, appConfig);
        messageRegistry.getEndpoints().stream()
                .filter(endpoint -> appConfig.getBootstrapApiVersion().equals(endpoint.getApiVersion()))
                .findFirst()
                .ifPresent(endpoint -> this.updateFromMessageProvider(messageProvider, endpoint));
        return messageProvider;
    }

    @Override
    public void updateFromMessageProvider(@MappingTarget MessageProvider messageProvider, final RegBsMessageRegistryEndpoint messageRegistryEndpoint) {
        delegate.updateFromMessageProvider(messageProvider, messageRegistryEndpoint);
        final UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(messageRegistryEndpoint.getUrl()).build();
        final String path = uriComponents.getPath();
        messageProvider.setMessageProviderUrl(StringUtils.removeEnd(messageRegistryEndpoint.getUrl(), path));
        messageProvider.setMessageProviderPath(path);
    }
}
