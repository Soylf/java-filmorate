package storage.user.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import storage.user.InMemoryUserStorage;

import java.util.*;


@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public void addFriend(Integer id, Integer friendId) {
        if (Objects.equals(id, friendId)) {
            throw new ValidationException("Ты не можешь быть своим жне другом");
        }
        inMemoryUserStorage.getUserId(id).addFriend(friendId);
        inMemoryUserStorage.getUserId(friendId).addFriend(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        if (Objects.equals(id, friendId)) {
            throw new ValidationException("Ты не можешь удалить сам себя");
        }
        inMemoryUserStorage.getUserId(id).deleteFriend(friendId);
        inMemoryUserStorage.getUserId(friendId).deleteFriend(id);
    }

    public List<User> mutualFriends(Integer id, Integer otherId) {
        User u1 = inMemoryUserStorage.getUserId(id);
        User u2 = inMemoryUserStorage.getUserId(otherId);

        Set<Integer> friend = new HashSet<>(u1.getFriends());
        List<User> friends = new ArrayList<>();
        friend.retainAll(u2.getFriends());

        if (friend.isEmpty()) {
            throw new ValidationException("Нет одинаковых объектов");
        }
        friend.forEach(num -> friends.add(inMemoryUserStorage.getUserId(num)));
        return friends;
    }

    public List<User> friends(Integer userId) {
        List<Integer> nums = new ArrayList<>(inMemoryUserStorage.getUserId(userId).getFriends());
        List<User> friend = new ArrayList<>();
        nums.forEach(num -> friend.add(inMemoryUserStorage.getUserId(num)));
        return friend;
    }
}
