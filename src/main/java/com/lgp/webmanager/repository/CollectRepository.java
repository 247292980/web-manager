package com.lgp.webmanager.repository;

import com.lgp.webmanager.domain.Collect;
import com.lgp.webmanager.domain.view.CollectSummaryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectRepository extends JpaRepository<Collect, Long> {
	
	String baseSql="select c.id as id,c.title as title, c.type as type,c.url as url,c.logoUrl as logoUrl,c.userId as userId, "
			+ "c.remark as remark,c.description as description,c.lastModifyTime as lastModifyTime,c.createTime as createTime, "
			+ "u.userName as userName,u.profilePicture as profilePicture,f.id as favoriteId,f.name as favoriteName "
			+ "from Collect c,User u,Favorites f WHERE c.userId=u.id and c.favoritesId=f.id and c.isDelete='NO'";
	
	String isDeleteBaseSql="select c.id as id,c.title as title, c.type as type,c.url as url,c.logoUrl as logoUrl,c.userId as userId, "
			+ "c.remark as remark,c.description as description,c.lastModifyTime as lastModifyTime, "
			+ "u.userName as userName,u.profilePicture as profilePicture,f.id as favoriteId,f.name as favoriteName "
			+ "from Collect c,User u,Favorites f WHERE c.userId=u.id and c.favoritesId=f.id and c.isDelete='YES'";

	Long countByUserIdAndIsDelete(Long userId,String isDelete);
	
	Long countByUserIdAndTypeAndIsDelete(Long userId,String type,String isDelete);
	
	Collect findByIdAndUserId(Long id,Long userId);
	 
	@Transactional
    Long deleteById(Long id);
	
	Page<Collect> findByFavoritesIdAndIsDelete(Long favoritesId,Pageable pageable,String isDelete);
	
	List<Collect> findByFavoritesIdAndIsDelete(Long favoritesId,String isDelete);
	
	List<Collect> findByFavoritesIdAndUrlAndUserIdAndIsDelete(Long favoritesId,String url,Long userId,String isDelete);
	
	@Transactional
	@Modifying
	@Query("update Collect c set c.type = ?1 where c.id = ?2 and c.userId=?3 ")
	int modifyByIdAndUserId(String type, Long id, Long userId);
	
	@Transactional
	@Modifying
	@Query("delete from Collect where favoritesId = ?1")
	void deleteByFavoritesId(Long favoritesId);
	
	@Query(baseSql+ " and c.userId=?1 ")
	Page<CollectSummaryView> findViewByUserId(Long userId, Pageable pageable);
	
	@Query(isDeleteBaseSql+ " and c.userId=?1 ")
	Page<CollectSummaryView> findViewByUserIdAndIsDelete(Long userId, Pageable pageable);
	
	@Query(baseSql+ " and c.userId=?1 and c.type=?2")
	Page<CollectSummaryView> findViewByUserIdAndType(Long userId, Pageable pageable, String type);
	
	@Query(baseSql+ " and c.userId=?1 and c.type=?2 and c.favoritesId=?3")
	Page<CollectSummaryView> findViewByUserIdAndTypeAndFavoritesId(Long userId, Pageable pageable, String type, Long favoritesId);
	
	@Query(baseSql+ " and c.favoritesId=?1 ")
	Page<CollectSummaryView> findViewByFavoritesId(Long favoritesId, Pageable pageable);
	
	@Query(baseSql+ " and c.type='public' and c.userId!=?1 ")
	Page<CollectSummaryView> findExploreView(Long userId, Pageable pageable);
	
	@Query(baseSql+ " and (c.userId=?1 or ( c.userId in ?2 and c.type='PUBLIC' )) ")
	Page<CollectSummaryView> findViewByUserIdAndFollows(Long userId, List<Long> userIds, Pageable pageable);
	
	
	@Query(baseSql+ " and c.userId=?1 and ( c.title like ?2 or c.description like ?2) ")
	Page<CollectSummaryView> searchMyByKey(Long userId, String key, Pageable pageable);
	
	@Query(baseSql+ " and c.type='public' and c.userId!=?1 and ( c.title like ?2 or c.description like ?2) ")
	Page<CollectSummaryView> searchOtherByKey(Long userId, String key, Pageable pageable);
	
	Long countByFavoritesIdAndTypeAndIsDelete(Long favoritesId,String type,String isDelete);
	
	List<Collect> findByCreateTimeLessThanAndIsDeleteAndFavoritesIdIn(Long createTime,String isDelete,List<Long> favoritesIds);
	
	@Transactional
	@Modifying
	@Query("update Collect c set c.isDelete = ?1,c.lastModifyTime = ?2 where c.id = ?3")
	int modifyIsDeleteById(String isDelete,Long lastModifyTime,Long id);

	@Transactional
	@Modifying
	@Query("update Collect c set c.logoUrl = ?1,c.lastModifyTime = ?2 where c.url = ?3")
	int updateLogoUrlByUrl(String logoUrl,Long lastModifyTime,String url);

	List<Collect> findByUserIdAndIsDelete(Long userId,String isDelete);

}