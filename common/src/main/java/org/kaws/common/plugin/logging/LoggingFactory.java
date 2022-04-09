package org.kaws.common.plugin.logging;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.kaws.common.domain.dto.SysLogDTO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Bosco
 * @date 2022/4/9 3:39 下午
 */

@Component
public class LoggingFactory {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/my_test?useSSL=false";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "123456";

    public void saveBatch(List<SysLogDTO> batchList) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "insert into sys_log(id,title,description,method,operate_url,request_param,request_body,response_body,success,error_msg,extra) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);

            for (int i = 0; i < batchList.size(); i++) {
                SysLogDTO item = batchList.get(i);
                preparedStatement.setObject(1, item.getId());
                preparedStatement.setObject(2, item.getTitle());
                preparedStatement.setObject(3, item.getDescription());
                preparedStatement.setObject(4, item.getMethod());
                preparedStatement.setObject(5, item.getOperateUrl());
                preparedStatement.setObject(6, item.getRequestParam());
                preparedStatement.setObject(7, item.getRequestBody());
                preparedStatement.setObject(8, item.getResponseBody());
                preparedStatement.setObject(9, BooleanUtil.isFalse(item.isSuccess()) ? 0 : 1);
                preparedStatement.setObject(10, item.getErrorMsg());
                preparedStatement.setObject(11, ObjectUtil.isNotEmpty(item.getMap()) ? JSONUtil.toJsonStr(item.getMap()) : null);
                preparedStatement.addBatch();
                if (i % 100 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }


}
