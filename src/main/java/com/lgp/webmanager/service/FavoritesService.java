package com.lgp.webmanager.service;

import com.lgp.webmanager.domain.Favorites;

public interface FavoritesService {

    Favorites saveFavorites(Long userId, Long count, String name);

}
