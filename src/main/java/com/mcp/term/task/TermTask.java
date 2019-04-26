package com.mcp.term.task;


import com.mcp.term.service.OnlineService;
import com.mcp.term.service.QiaoService;
import com.mcp.term.service.SourceService;
import com.mcp.term.util.Cons;
import com.mcp.term.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TermTask {

    @Autowired
    private QiaoService qiaoService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private OnlineService onlineService;

    /**
     * 获取时时彩预测
     */
    @Scheduled(fixedDelay = 180000)// 180秒执行一次
    public void updateChongqi() {
        try {
            qiaoService.updatePrediction(Cons.Game.CQSSC);
        } catch (Exception e) {
        }
    }

    /**
     * 获取时时彩开奖结果
     */
    @Scheduled(fixedDelay = 600000)// 600秒执行一次
    public void updatePrizeChongqi() {
        try {
            sourceService.updateCQSSCPrize(1);
        } catch (Exception e) {
        }
    }

    /**
     * 获取分分彩预测
     */
    @Scheduled(fixedDelay = 10000)
    public void updateFenfencai() {
        qiaoService.updatePrediction(Cons.Game.TXFFC);
    }

//    /**
//     * 获取分分彩预测BY
//     */
//    @Scheduled(fixedDelay = 10000)
//    public void updateFenfencai() {
//        qiaoService.updatePredictionBy(Cons.Game.TXFFC);
//    }


    /**
     * 获取分分彩开奖结果
     */
    @Scheduled(fixedDelay = 5000) // 5秒执行一次
    public void updatePrizeFenfencai() {
        try {
            onlineService.updatePrizeNumber();
        } catch (Exception e) {
        }
    }

    /**
     * 获取前一天预测结果
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void updatePrizeYesterday() {
        try {
            qiaoService.historyPrediction(Cons.Game.TXFFC, DateUtil.addDay(new Date(), -1));
        } catch (Exception e) {
        }
    }


}
