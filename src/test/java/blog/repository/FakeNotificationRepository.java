package blog.repository;

import blog.notification.entity.Notification;
import blog.notification.repository.NotificationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeNotificationRepository implements NotificationRepository {
    private final Map<String, Notification> notificationList = new HashMap();

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(notificationList.get(id));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Notification> findNotificationsByUserId(String userId) {
        return notificationList.values().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .toList();
    }

    @Override
    public <S extends Notification> S save(S entity) {
        notificationList.put(entity.getNotificationId(), entity);
        return entity;
    }

    @Override
    public void deleteById(String notificationId) {
        notificationList.remove(notificationId);
    }

    @Override
    public void delete(Notification entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Notification> entities) {

    }

    @Override
    public void deleteAll() {

    }

    public void clear() {
        notificationList.clear();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Notification> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Notification> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Notification> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Notification getOne(String s) {
        return null;
    }

    @Override
    public Notification getById(String s) {
        return null;
    }

    @Override
    public Notification getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Notification> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Notification> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Notification> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Notification> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Notification, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Notification> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Notification> findAll() {
        return List.of();
    }

    @Override
    public List<Notification> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Notification> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return null;
    }
}