package org.kaws.common.plugin.logging;

import cn.hutool.core.util.BooleanUtil;
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

    private static final String URL = "jdbc:mysql://121.199.14.146:3306/my_test?serverTimezone=Asia/Shanghai&useSSL=false";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "123456";

    public void saveBatch(List<SysLogDTO> batchList) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "insert into sys_log values(?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);

            for (int i = 0; i < batchList.size(); i++) {
                SysLogDTO item = batchList.get(i);
                preparedStatement.setString(1, item.getId());
                preparedStatement.setString(2, item.getTitle());
                preparedStatement.setString(3, item.getDescription());
                preparedStatement.setString(4, item.getMethod());
                preparedStatement.setString(5, item.getOperateUrl());
                preparedStatement.setString(6, item.getRequestParam());
                preparedStatement.setString(7, item.getRequestBody());
                preparedStatement.setString(8, item.getResponseBody());
                preparedStatement.setInt(9, BooleanUtil.isFalse(item.isSuccess()) ? 0 : 1);
                preparedStatement.setString(10, item.getErrorMsg());
                preparedStatement.setObject(11, item.getCreateTime());
                preparedStatement.setString(12, item.getTitle());
                preparedStatement.addBatch();
                if (i % 100 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            conn.commit();
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
