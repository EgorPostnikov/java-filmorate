package ru.yandex.practicum.filmorate.storage.impl.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class FriendStorageUtils {

        private FriendStorageUtils() {
        }

        public static Collection<Integer> makeFriend (ResultSet resultSet, int rowNum) throws SQLException {
            Collection<Integer> friends = (Collection<Integer>) resultSet.getArray("FRIEND_ID");
            return friends;
        }
    }