package com.lgp.webmanager.service.impl;

import com.lgp.webmanager.comm.aop.Log;
import com.lgp.webmanager.domain.Notice;
import com.lgp.webmanager.domain.User;
import com.lgp.webmanager.domain.CollectSummary;

import com.lgp.webmanager.domain.view.CollectSummaryView;
import com.lgp.webmanager.domain.view.CommentView;
import com.lgp.webmanager.repository.CommentRepository;
import com.lgp.webmanager.repository.NoticeRepository;
import com.lgp.webmanager.repository.PraiseRepository;
import com.lgp.webmanager.repository.UserRepository;
import com.lgp.webmanager.service.NoticeService;
import com.lgp.webmanager.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 通知服务
 **/
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PraiseRepository praiseRepository;
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 保存消息通知
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class})
	@Log(description = "保存消息通知")
	public void saveNotice(String collectId, String type, Long userId, String operId){
		Notice notice = new Notice();
		if(StringUtils.isNotBlank(collectId)){
			notice.setCollectId(collectId);
		}
		notice.setReaded("unread");
		notice.setType(type);
		if(StringUtils.isNotBlank(operId)){
			notice.setOperId(operId);
		}
		notice.setUserId(userId);
		notice.setCreateTime(DateUtil.getCurrentTime());
		noticeRepository.save(notice);
	}

	/**
	 * 展示消息通知
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class})
	@Log(description = "展示消息通知")
	public List<CollectSummary> getNoticeCollects(String type, Long userId, Pageable pageable) {
		// TODO Auto-generated method stub
		Page<CollectSummaryView> views = noticeRepository.findViewByUserIdAndType(userId, type, pageable);
		return convertCollect(views, type);
	}

	private List<CollectSummary> convertCollect(Page<CollectSummaryView> views, String type) {
		List<CollectSummary> summarys=new ArrayList<CollectSummary>();
		for (CollectSummaryView view : views) {
			CollectSummary summary=new CollectSummary(view);
			if("at".equals(type)){
				summary.setCollectTime(DateUtil.getTimeFormatText(view.getLastModifyTime())+" at了你");
			}else if("comment".equals(type)){
				CommentView comment = commentRepository.findReplyUser(Long.valueOf(view.getOperId()));
				if(comment == null){
					continue;
				}
				summary.setUserId(comment.getUserId());
				summary.setUserName(comment.getUserName());
				summary.setProfilePicture(comment.getProfilePicture());
				if(comment.getReplyUserId() != null && comment.getReplyUserId() != 0L){
					User replyUser = userRepository.findOne(comment.getReplyUserId());
				    summary.setRemark("回复@"+replyUser.getUserName()+": "+comment.getContent());
				}else{
					summary.setRemark(comment.getContent());
				}
				summary.setCollectTime(DateUtil.getTimeFormatText(comment.getCreateTime()));
			}else if("praise".equals(type)){
				CommentView comment = praiseRepository.findPraiseUser(Long.valueOf(view.getOperId()));
				if(comment == null){
					continue;
				}
				summary.setUserId(comment.getUserId());
				summary.setUserName(comment.getUserName());
				summary.setProfilePicture(comment.getProfilePicture());
				summary.setCollectTime(DateUtil.getTimeFormatText(comment.getCreateTime())+" 赞了你的收藏");
			}		
			summarys.add(summary);
		}
		return summarys;
	}
}
