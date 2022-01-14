package be.bosa.ebox.providerbsservice.integration.cqengine.repositories;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsBinary;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {

    List<RegBsImage> findAllByMessageRegistryId(String messageRegistryId);

    List<RegBsImage> findAllByMessageRegistryIdAndImageId(String messageRegistryId, String id);

    Optional<RegBsImage> findByMessageRegistryIdAndImageIdAndFormat(String messageRegistryId, String imageId, Image.FormatEnum format);

    Optional<RegBsBinary> findBinaryById(String id);
}
