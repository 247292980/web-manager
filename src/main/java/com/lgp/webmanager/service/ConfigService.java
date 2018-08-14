package com.lgp.webmanager.service;

import com.lgp.webmanager.domain.Config;

public interface ConfigService {

    Config saveConfig(Long userId, String favoritesId);

    void updateConfig(Long id, String type, String defaultFavorites);
}
