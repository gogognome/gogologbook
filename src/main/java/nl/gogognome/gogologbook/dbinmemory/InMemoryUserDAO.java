package nl.gogognome.gogologbook.dbinmemory;

import com.google.common.collect.Maps;
import nl.gogognome.gogologbook.dao.DAOException;
import nl.gogognome.gogologbook.dao.UserDAO;
import nl.gogognome.gogologbook.entities.User;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class InMemoryUserDAO implements UserDAO {

    private final Map<Integer, User> idToUser = Maps.newTreeMap();

    @Override
    public User createUser(User user) {
        int maxId = 0;
        for (int id : idToUser.keySet()) {
            maxId = Math.max(maxId, id);
        }

        User storedUser = cloneUser(user, maxId + 1);
        idToUser.put(storedUser.id, storedUser);
        return cloneUser(storedUser, storedUser.id);
    }

    @Override
    public void updateUser(User user) {
        if (!idToUser.containsKey(user.id)) {
            throw new DAOException("User " + user.id + " does not exist. It cannot be updated.");
        }

        User storedUser = cloneUser(user, user.id);
        idToUser.put(storedUser.id, storedUser);
    }

    @Override
    public void deleteUser(int userId) {
        if (!idToUser.containsKey(userId)) {
            throw new DAOException("User " + userId + " does not exist. It cannot de deleted.");
        }
        idToUser.remove(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return idToUser.values().stream()
                .map(u -> cloneUser(u, u.id))
                .collect(toList());
    }

    @Override
    public List<User> findAllActiveUsers() {
        return idToUser.values().stream()
                .filter(u -> u.active)
                .map(u -> cloneUser(u, u.id))
                .collect(toList());
    }

    private User cloneUser(User origUser, int clonedId) {
        User clonedUser = new User(clonedId);
        clonedUser.name = origUser.name;
        clonedUser.active = origUser.active;
        return clonedUser;
    }
}
