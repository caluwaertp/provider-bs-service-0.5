package be.bosa.ebox.providerbsservice.integration.cqengine.repositories;

import be.bosa.ebox.providerbsservice.config.AppConfig;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistries;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistry;
import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistryEndpoint;
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
import static com.googlecode.cqengine.query.QueryFactory.attribute;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class MessageRegistryRepositoryImpl implements MessageRegistryRepository {
    private final AppConfig appConfig;
    private final IndexedCollection<RegBsMessageRegistry> collection;

    public MessageRegistryRepositoryImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.collection = new ConcurrentIndexedCollection<>();
        this.collection.addIndex(NavigableIndex.onAttribute(MESSAGE_REGISTRY_ID));
        this.collection.addIndex(NavigableIndex.onAttribute(MESSAGE_REGISTRY_ENDPOINT_API_VERSION));
    }

    @PostConstruct
    public void initMessageRegistryRepository() {
        bootstrap(appConfig.getBootstrapFile());
    }

    @Override
    public List<RegBsMessageRegistry> findAll() {
        final ResultSet<RegBsMessageRegistry> result = collection.retrieve(
                equal(MESSAGE_REGISTRY_ENDPOINT_API_VERSION, appConfig.getBootstrapApiVersion()));
        return result.stream().collect(Collectors.toList());
    }

    @Override
    public List<RegBsMessageRegistry> findAllOn(String apiVersion) {
        final ResultSet<RegBsMessageRegistry> result = collection.retrieve(
                equal(MESSAGE_REGISTRY_ENDPOINT_API_VERSION, apiVersion));
        return result.stream().collect(Collectors.toList());
    }

    @Override
    public Optional<RegBsMessageRegistry> findById(String id) {
        final ResultSet<RegBsMessageRegistry> result = collection.retrieve(
                equal(MESSAGE_REGISTRY_ID, id));
        return result.stream().findFirst();
    }

    @SneakyThrows
    private void bootstrap(Resource bootstrapFile) {
        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setDefaultSetterInfo(forContentNulls(AS_EMPTY));

        final RegBsMessageRegistries messageRegistries = mapper.readValue(bootstrapFile.getInputStream(), RegBsMessageRegistries.class);
        if (!isNull(messageRegistries) && !isNull(messageRegistries.getItems())) {
            collection.addAll(messageRegistries.getItems());
        }
    }

    private static final SimpleAttribute<RegBsMessageRegistry, String> MESSAGE_REGISTRY_ID =
            attribute("id", RegBsMessageRegistry::getId);

    public static final Attribute<RegBsMessageRegistry, String> MESSAGE_REGISTRY_ENDPOINT_API_VERSION = new MultiValueAttribute<>() {
        public Iterable<String> getValues(RegBsMessageRegistry messageRegistry, QueryOptions queryOptions) {
            return messageRegistry.getEndpoints().stream()
                    .map(RegBsMessageRegistryEndpoint::getApiVersion)::iterator;
        }
    };

}
