package be.bosa.ebox.providerbsservice.integration.cqengine.repositories;

import be.bosa.ebox.registrybsservice.api.model.RegBsMessageRegistry;

import java.util.List;
import java.util.Optional;

public interface MessageRegistryRepository {
    List<RegBsMessageRegistry> findAll();

    List<RegBsMessageRegistry> findAllOn(String apiVersion);

    Optional<RegBsMessageRegistry> findById(String id);
}
