package be.bosa.ebox.providerbsservice.services.mappers;

import be.bosa.ebox.providerbsservice.api.model.MessageProvider;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistry;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistryEndpoint;
import org.mapstruct.*;

@Mapper
@DecoratedWith(MessageRegistryMapperDecorator.class)
public interface MessageRegistryMapper {
    @Mapping(target = "messageProviderId", source = "id")
    @Mapping(target = "messageProviderName", source = "name")
    @Mapping(target = "messageProviderDescription", source = "description")
    MessageProvider mapToMessageProvider(final RegBsMessageRegistry messageRegistry, @Context final AppConfig appConfig);

    @Mapping(target = "messageProviderActivationDate", source = "activationDate")
    @Mapping(target = "messageProviderDeactivationDate", source = "deactivationDate")
    @Mapping(target = "messageProviderTechnicalContact", source = "technicalContact")
    void updateFromMessageProvider(@MappingTarget MessageProvider messageProvider, final RegBsMessageRegistryEndpoint messageRegistryEndpoint);

}
