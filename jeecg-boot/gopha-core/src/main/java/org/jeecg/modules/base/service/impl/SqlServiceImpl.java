package org.jeecg.modules.base.service.impl;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jeecg.modules.base.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SqlServiceImpl implements SqlService {
    private final MSUtils msUtils;
    private final SqlSession sqlSession;

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 构造方法，默认缓存MappedStatement
     *
     * @param sqlSession
     */
    @Autowired
    public SqlServiceImpl(SqlSession sqlSession, SqlSessionFactory sqlSessionFactory) {
        this.sqlSession = sqlSession;
        this.sqlSessionFactory = sqlSessionFactory;
        this.msUtils = new MSUtils(sqlSession.getConfiguration());
    }

    /**
     * 获取List中最多只有一个的数据
     *
     * @param list List结果
     * @param <T>  泛型类型
     * @return
     */
    private <T> T getOne(List<T> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql 执行的sql
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, Object> selectOne(String sql) {
        List<Map<String, Object>> list = selectList(sql);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Map<String, Object> selectOne(String sql, Object value) {
        List<Map<String, Object>> list = selectList(sql, value);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql        执行的sql
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> T selectOne(String sql, Class<T> resultType) {
        List<T> list = selectList(sql, resultType);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql        执行的sql
     * @param value      参数
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> T selectOne(String sql, Object value, Class<T> resultType) {
        List<T> list = selectList(sql, value, resultType);
        return getOne(list);
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql 执行的sql
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> selectList(String sql) {
        String msId = msUtils.select(sql);
        return sqlSession.selectList(msId);
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> selectList(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.selectDynamic(sql, parameterType);
        return sqlSession.selectList(msId, value);
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql        执行的sql
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> List<T> selectList(String sql, Class<T> resultType) {
        String msId;
        if (resultType == null) {
            msId = msUtils.select(sql);
        } else {
            msId = msUtils.select(sql, resultType);
        }
        return sqlSession.selectList(msId);
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql        执行的sql
     * @param value      参数
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
        String msId;
        Class<?> parameterType = value != null ? value.getClass() : null;
        if (resultType == null) {
            msId = msUtils.selectDynamic(sql, parameterType);
        } else {
            msId = msUtils.selectDynamic(sql, parameterType, resultType);
        }
        return sqlSession.selectList(msId, value);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertRequiresNew(String sql) {
        return insert(sql);
    }
    /**
     * 插入数据
     *
     * @param sql 执行的sql
     * @return
     */
    public int insert(String sql) {
        String msId = msUtils.insert(sql);
        return sqlSession.insert(msId);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertRequiresNew(String sql, Object value) {
        return insert(sql,value);
    }
    /**
     * 插入数据
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
    public int insert(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.insertDynamic(sql, parameterType);
        return sqlSession.insert(msId, value);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertBatchRequiresNew(String sql, List<Object> value){
        return insertBatch(sql, value);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertBatchRequiresNew(String sql, List<Object> value, int batchSize){
        return insertBatch(sql, value, batchSize);
    }
    public int insertBatch(String sql, List<Object> value) {
        return insertBatch(sql, value, 1000);
    }

    public int insertBatch(String sql, List<Object> value, int batchSize) {
        Class<?> parameterType = (value != null && !value.isEmpty()) ? value.getClass() : null;
        String msId = msUtils.insertDynamic(sql, parameterType);
        SqlSession sqlSessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < value.size(); i++) {
                sqlSessionBatch.insert(msId, value.get(i));
                if (i % batchSize == 0) {
                    sqlSessionBatch.flushStatements();
                    sqlSessionBatch.commit();
                }
            }
            sqlSessionBatch.flushStatements();
            sqlSessionBatch.commit();
        } catch (Exception e) {
            sqlSessionBatch.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSessionBatch.close();
        }
        return 1;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertBatchRequiresNew(List<String> sqlList) {
        return insertBatch(sqlList, 1000);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertBatchRequiresNew(List<String> sqlList, int batchSize) {
        return insertBatch(sqlList, batchSize);
    }
    public int insertBatch(List<String> sqlList) {
        return insertBatch(sqlList, 1000);
    }

    public int insertBatch(List<String> sqlList, int batchSize) {
        SqlSession sqlSessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < sqlList.size(); i++) {
                String msId = msUtils.insertDynamic(sqlList.get(i), null);
                sqlSessionBatch.insert(msId);
                if (i % batchSize == 0) {
                    sqlSessionBatch.flushStatements();
                    sqlSessionBatch.commit();
                }
            }
            sqlSessionBatch.flushStatements();
            sqlSessionBatch.commit();
        } catch (Exception e) {
            sqlSessionBatch.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSessionBatch.close();
        }
        return 1;
    }

    /**
     * 更新数据
     *
     * @param sql 执行的sql
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateRequiresNew(String sql) {
        return update(sql);
    }
    public int update(String sql) {
        String msId = msUtils.update(sql);
        return sqlSession.update(msId);
    }

    /**
     * 更新数据
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateRequiresNew(String sql, Object value) {
        return update(sql,value);
    }
    public int update(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.updateDynamic(sql, parameterType);
        return sqlSession.update(msId, value);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateBatchRequiresNew(String sql, List<Object> value) {
        return updateBatch(sql, value);
    }
    public int updateBatch(String sql, List<Object> value) {
        return updateBatch(sql, value, 1000);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateBatchRequiresNew(String sql, List<Object> value, int batchSize) {
        return updateBatch(sql, value,batchSize);
    }
    public int updateBatch(String sql, List<Object> value, int batchSize) {
        Class<?> parameterType = (value != null && !value.isEmpty()) ? value.getClass() : null;
        String msId = msUtils.updateDynamic(sql, parameterType);
        SqlSession sqlSessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < value.size(); i++) {
                sqlSessionBatch.update(msId, value.get(i));
                if (i % batchSize == 0) {
                    sqlSessionBatch.flushStatements();
                    sqlSessionBatch.commit();
                }
            }
            sqlSessionBatch.flushStatements();
            sqlSessionBatch.commit();
        } catch (Exception e) {
            sqlSessionBatch.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlSessionBatch.close();
        }
        return 1;
    }

    public int updateBatch(List<String> sqlList) {
        return updateBatch(sqlList, 1000);
    }

    public int updateBatch(List<String> sqlList, int batchSize) {
        SqlSession sqlSessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < sqlList.size(); i++) {
                String msId = msUtils.updateDynamic(sqlList.get(i), null);
                sqlSessionBatch.update(msId);
                if (i % batchSize == 0) {
                    sqlSessionBatch.flushStatements();
                    sqlSessionBatch.commit();
                }
            }
            sqlSessionBatch.flushStatements();
            sqlSessionBatch.commit();
        } catch (Exception e) {
            sqlSessionBatch.rollback();
            throw new RuntimeException(e);
        }finally {
            sqlSessionBatch.close();
        }
        return 1;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteRequiresNew(String sql) {
        return delete(sql);
    }

    /**
     * 删除数据
     *
     * @param sql 执行的sql
     * @return
     */
    public int delete(String sql) {
        String msId = msUtils.delete(sql);
        return sqlSession.delete(msId);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteRequiresNew(String sql, Object value) {
        return delete(sql,value);
    }
    /**
     * 删除数据
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
    public int delete(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.deleteDynamic(sql, parameterType);
        return sqlSession.delete(msId, value);
    }

    private class MSUtils {
        private Configuration configuration;
        private LanguageDriver languageDriver;

        private MSUtils(Configuration configuration) {
            this.configuration = configuration;
            languageDriver = configuration.getDefaultScriptingLanuageInstance();
        }

        /**
         * 创建MSID
         *
         * @param sql 执行的sql
         * @param sql 执行的sqlCommandType
         * @return
         */
        private String newMsId(String sql, SqlCommandType sqlCommandType) {

            // 原内容
            // StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
            // msIdBuilder.append(".").append(sql.hashCode());
            // return msIdBuilder.toString();

            System.out.println("=== SqlServiceImpl newMsId 方法 === 开始 === ");

            System.out.println("=== 传入参数1 sql s=== ");
            System.out.println(sql);
            System.out.println("=== 传入参数1 sql e=== ");
            System.out.println("=== 传入参数2 sqlCommandType s=== ");
            System.out.println(sqlCommandType.toString());
            System.out.println("=== 传入参数2 sqlCommandType e=== ");

            StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
            msIdBuilder.append(".").append(getSHA256Hex(sql));

            System.out.println("=== 返回数据 msIdBuilder.toString() s=== ");
            System.out.println(msIdBuilder.toString());
            System.out.println("=== 返回数据 msIdBuilder.toString() e=== ");

            System.out.println("=== SqlServiceImpl newMsId 方法 === 结束 === ");

            return msIdBuilder.toString();
            // return getSHA256Hex(sql);
        }

        /**
         * 使用SHA-256算法计算多个对象（字符串）的联合哈希值
         *
         * @param inputs 输入的对象数组
         * @return 十六进制的哈希值(64位)
         */
        public String getSHA256Hex(Object... inputs) {
            try {
                // 创建MessageDigest实例，初始化为SHA-256算法
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // 遍历所有输入字符串，将它们添加到哈希计算中
                for (Object input : inputs) {
                    // 如果为空，则设置为null字符串
                    String inputStr = input== null ? "null" : input.toString();
                    md.update(inputStr.getBytes(StandardCharsets.UTF_8));
                }

                // 计算哈希值
                byte[] hash = md.digest();

                // 将字节数组转换为十六进制字符串
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 是否已经存在该ID
         *
         * @param msId
         * @return
         */
        private boolean hasMappedStatement(String msId) {
            return configuration.hasStatement(msId, false);
        }

        /**
         * 创建一个查询的MS
         *
         * @param msId
         * @param sqlSource  执行的sqlSource
         * @param resultType 返回的结果类型
         */
        private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
                    .resultMaps(new ArrayList<ResultMap>() {
                        {
                            add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
                        }
                    })
                    .build();
            //缓存
            configuration.addMappedStatement(ms);
        }

        /**
         * 创建一个简单的MS
         *
         * @param msId
         * @param sqlSource      执行的sqlSource
         * @param sqlCommandType 执行的sqlCommandType
         */
        private void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType)
                    .resultMaps(new ArrayList<ResultMap>() {
                        {
                            add(new ResultMap.Builder(configuration, "defaultResultMap", int.class, new ArrayList<ResultMapping>(0)).build());
                        }
                    })
                    .build();
            //缓存
            configuration.addMappedStatement(ms);
        }

        private String select(String sql) {

            System.out.println("=== SqlServiceImpl select 方法 === 开始 === ");

            System.out.println("=== 传入参数 sql s=== ");
            System.out.println(sql);
            System.out.println("=== 传入参数 sql e=== ");

            String msId = newMsId(sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {

                System.out.println("=== SqlServiceImpl select 方法 进入判断方法 hasMappedStatement(msId) ");
                System.out.println("=== 返回数据 msId s=== ");
                System.out.println(msId);
                System.out.println("=== 返回数据 msId e=== ");

                System.out.println("=== SqlServiceImpl select 方法 进入判断方法 hasMappedStatement(msId) === 结束 === ");

                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, Map.class);

            System.out.println("=== 返回数据 msId s=== ");
            System.out.println(msId);
            System.out.println("=== 返回数据 msId e=== ");
            System.out.println("=== SqlServiceImpl select 方法 没有进入判断方法 hasMappedStatement(msId) === 结束 === ");
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, Map.class);
            return msId;
        }

        private String select(String sql, Class<?> resultType) {
            String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
            String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String insert(String sql) {
            String msId = newMsId(sql, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String insertDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String update(String sql) {
            String msId = newMsId(sql, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String updateDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String delete(String sql) {
            String msId = newMsId(sql, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }

        private String deleteDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }
    }
}