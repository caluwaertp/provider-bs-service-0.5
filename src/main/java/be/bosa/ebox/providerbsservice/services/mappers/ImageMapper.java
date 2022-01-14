package be.bosa.ebox.providerbsservice.services.mappers;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;
import be.bosa.ebox.providerbsservice.integration.cqengine.repositories.ImageRepository;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(ImageMapperDecorator.class)
public interface ImageMapper {
    @Mapping(target = "imageId", source = "id")
    @Mapping(target = "format", source = "format")
    @Mapping(target = "mediaType", source = "mediaType")
    @Mapping(target = "language", source = "language")
    Image mapToImage(final RegBsImage regBsImage, @Context final ImageRepository imageRepository);
}
