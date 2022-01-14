package be.bosa.ebox.providerbsservice.integration.cqengine.repositories;

import be.bosa.ebox.providerbsservice.api.model.Image;
import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsBinary;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImage;
import be.bosa.ebox.providerbsservice.integration.cqengine.entities.RegBsImages;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonSetter.Value.forContentNulls;
import static com.fasterxml.jackson.annotation.Nulls.AS_EMPTY;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class ImageRepositoryImpl implements ImageRepository {
    private final AppConfig appConfig;
    private final IndexedCollection<RegBsImage> imageCollection;
    private final IndexedCollection<RegBsBinary> binaryCollection;

    public ImageRepositoryImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.imageCollection = new ConcurrentIndexedCollection<>();
        this.imageCollection.addIndex(NavigableIndex.onAttribute(IMAGE_ID));
        this.imageCollection.addIndex(NavigableIndex.onAttribute(MESSAGE_REGISTRY_ID));
        this.binaryCollection = new ConcurrentIndexedCollection<>();
        this.binaryCollection.addIndex(NavigableIndex.onAttribute(BINARY_ID));
    }

    @PostConstruct
    public void initMessageRegistryRepository() {
        bootstrap(appConfig.getBootstrapImageFile());
    }

    @Override
    public List<RegBsImage> findAllByMessageRegistryIdAndImageId(String messageRegistryId, String imageId) {
        final ResultSet<RegBsImage> result = imageCollection.retrieve(
                and(equal(MESSAGE_REGISTRY_ID, messageRegistryId), equal(IMAGE_ID, imageId)));
        return result.stream().collect(Collectors.toList());
    }

    @Override
    public List<RegBsImage> findAllByMessageRegistryId(String messageRegistryId) {
        final ResultSet<RegBsImage> result = imageCollection.retrieve(
                equal(MESSAGE_REGISTRY_ID, messageRegistryId));
        return result.stream().collect(Collectors.toList());
    }

    @Override
    public Optional<RegBsImage> findByMessageRegistryIdAndImageIdAndFormat(String messageRegistryId, String imageId, Image.FormatEnum format) {
        final ResultSet<RegBsImage> result = imageCollection.retrieve(
                and(equal(MESSAGE_REGISTRY_ID, messageRegistryId), equal(IMAGE_ID, imageId), equal(FORMAT, format)));
        return result.stream().findFirst();
    }

    @Override
    public Optional<RegBsBinary> findBinaryById(String id) {
        final ResultSet<RegBsBinary> result = binaryCollection.retrieve(
                equal(BINARY_ID, id));
        return result.stream().findFirst();
    }

    @SneakyThrows
    private void bootstrap(Resource bootstrapFile) {
        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setDefaultSetterInfo(forContentNulls(AS_EMPTY));

        final RegBsImages images = mapper.readValue(bootstrapFile.getInputStream(), RegBsImages.class);
        if (!isNull(images) && !isNull(images.getItems())) {
            imageCollection.addAll(images.getItems());
        }
        if (!isNull(images) && !isNull(images.getBinaries())) {
            binaryCollection.addAll(images.getBinaries());
        }
    }

    private static final SimpleAttribute<RegBsBinary, String> BINARY_ID =
            attribute("id", RegBsBinary::getId);

    private static final SimpleAttribute<RegBsImage, String> IMAGE_ID =
            attribute("id", RegBsImage::getId);

    private static final SimpleAttribute<RegBsImage, Image.FormatEnum> FORMAT =
            attribute("format", RegBsImage::getFormat);

    public static final Attribute<RegBsImage, String> MESSAGE_REGISTRY_ID = new MultiValueAttribute<>() {
        public Iterable<String> getValues(RegBsImage image, QueryOptions queryOptions) {
            return image.getMessageRegistryIds();
        }
    };

}
