package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgp.webmanager.domain.Favorites;
import com.lgp.webmanager.repository.FavoritesRepository;
import com.lgp.webmanager.service.FavoritesService;
import com.lgp.webmanager.util.DateUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 收藏文件夹服务
 **/
@Service("favoritesService")
public class FavoritesServiceImpl implements FavoritesService{
	
	@Autowired
	private FavoritesRepository favoritesRepository;
	
	/**
	 * 保存
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class})
	@Log(description = "保存收藏夹")
	public Favorites saveFavorites(Long userId, Long count, String name){
		Favorites favorites = new Favorites();
		favorites.setName(name);
		favorites.setUserId(userId);
		favorites.setCount(count);
		favorites.setCreateTime(DateUtil.getCurrentTime());
		favorites.setLastModifyTime(DateUtil.getCurrentTime());
		favoritesRepository.save(favorites);
		return favorites;
	}
}