package org.jeecg.modules.base.service;

import java.util.List;
import java.util.Map;


public interface SqlService {

    public Map<String, Object> selectOne(String sql);

    public Map<String, Object> selectOne(String sql, Object value);

    public <T> T selectOne(String sql, Class<T> resultType);
    
    public <T> T selectOne(String sql, Object value, Class<T> resultType) ;

    public List<Map<String, Object>> selectList(String sql);

    public List<Map<String, Object>> selectList(String sql, Object value) ;

    public <T> List<T> selectList(String sql, Class<T> resultType) ;

    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) ;
    public int insertRequiresNew(String sql);
    public int insertRequiresNew(String sql, Object value);
    public int insert(String sql);

    public int insert(String sql, Object value) ;
    public int insertBatchRequiresNew(String sql, List<Object> value);
    public int insertBatchRequiresNew(String sql, List<Object> value, int batchSize);
    public int insertBatchRequiresNew(List<String> sqlList);
    public int insertBatchRequiresNew(List<String> sqlList, int batchSize);
    public int insertBatch(String sql, List<Object> value);

    public int insertBatch(List<String> sqlList);
    public int insertBatch(List<String> sqlList, int batchSize);
    public int insertBatch(String sql, List<Object> value, int batchSize);
    public int update(String sql) ;
    public int updateRequiresNew(String sql);
    public int updateRequiresNew(String sql, Object value);
    public int update(String sql, Object value) ;
    public int updateBatchRequiresNew(String sql, List<Object> value);
    public int updateBatchRequiresNew(String sql, List<Object> value, int batchSize);
    public int updateBatch(String sql, List<Object> value) ;
    public int updateBatch(List<String> sqlList) ;
    public int deleteRequiresNew(String sql);
    public int deleteRequiresNew(String sql, Object value);
    public int delete(String sql) ;

    public int delete(String sql, Object value) ;

}
