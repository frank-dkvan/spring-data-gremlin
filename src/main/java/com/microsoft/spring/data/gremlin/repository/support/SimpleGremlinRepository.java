/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.query.GremlinOperations;
import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleGremlinRepository<T, ID extends Serializable> implements GremlinRepository<T, ID> {

    private final GremlinOperations operations;
    private final GremlinEntityInformation<T, ID> information;

    public SimpleGremlinRepository(GremlinEntityInformation<T, ID> information, @NonNull ApplicationContext context) {
        this(information, context.getBean(GremlinOperations.class));
    }

    public SimpleGremlinRepository(GremlinEntityInformation<T, ID> information, @NonNull GremlinOperations operations) {
        this.information = information;
        this.operations = operations;
    }

    @Deprecated
    public GremlinEntityInformation getGremlinEntityInformation() {
        return this.information;
    }

    @Override
    public <S extends T> S save(@NonNull S domain) {
        this.operations.save(domain);

        return domain;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(@NonNull Iterable<S> domains) {
        domains.forEach(this::save);

        return domains;
    }

    @Override
    public Iterable<T> findAll() {
        throw new NotImplementedException("findAll of Repository is not implemented yet");
    }

    @Override
    public List<T> findAllById(@NonNull Iterable<ID> ids) {
        final List<T> results = new ArrayList<>();

        ids.forEach(id -> this.findById(id).ifPresent(results::add));

        return results;
    }

    @Override
    public Optional<T> findById(@NonNull ID id) {
        final T domain = this.operations.findById(id, this.information.getJavaType());

        return domain == null ? Optional.empty() : Optional.of(domain);
    }

    @Override
    public Iterable<T> findAll(@NonNull Class<T> domainClass) {
        return this.operations.findAll(domainClass);
    }

    @Override
    public long count() {
        throw new NotImplementedException("count of Repository is not implemented yet");
    }

    @Override
    public void delete(@NonNull T domain) {
        this.operations.deleteById(this.information.getId(domain), domain.getClass());
    }

    @Override
    public void deleteById(@NonNull ID id) {
        this.operations.deleteById(id, this.information.getJavaType());
    }

    @Override
    public void deleteAll() {
        this.operations.deleteAll();
    }

    @Override
    public void deleteAll(@NonNull Iterable<? extends T> domains) {
        domains.forEach(this::delete);
    }

    @Override
    public boolean existsById(@NonNull ID id) {
        return this.findById(id).isPresent();
    }
}

