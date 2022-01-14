package be.bosa.ebox.providerbsservice.services.mappers;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;
import be.bosa.ebox.providerbsservice.integration.cqengine.repositories.ImageRepository;

import static java.util.Objects.isNull;

public abstract class ImageMapperDecorator implements ImageMapper {
    private final ImageMapper delegate;

    protected ImageMapperDecorator(ImageMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public Image mapToImage(final RegBsImage regBsImage, final ImageRepository imageRepository) {
        Image image = delegate.mapToImage(regBsImage, imageRepository);
        // set image.size in KB
        imageRepository.findBinaryById(regBsImage.getBinaryId()).ifPresent(
                regBsBinary -> {
                    if (!isNull(regBsBinary.getContent()) && regBsBinary.getContent().length > 0) {
                        image.setSize(regBsBinary.getContent().length / 1024.0);
                    } else {
                        image.setSize(0.0);
                    }
                });
        return image;
    }

}
