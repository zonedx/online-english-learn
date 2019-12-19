package com.zone.dao;

import com.zone.entity.Sentential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SententialRepository extends JpaRepository<Sentential,Integer> {

    public Page<Sentential> findByType(@Param("type") Integer type, Pageable var1);

    public Sentential findBySentenceId(@Param("sentenceId") String sentenceId);

    /**
     *   查询0到10-3之间的随机数  10--MAX(id) type=1
     *   之所以要减3 如果随机到22 那么查询结果只有一条
     *注意：ORDER BY 后面不能加limit 4  因为Pageable 本身查询就有limit了(大坑)
     * 第一版本：
     * value =
     *                     "SELECT * " +
     *                             " FROM oel_sentential as sentential JOIN" +
     *                             "(SELECT ROUND(RAND()*(SELECT MAX(id)-3 FROM oel_sentential WHERE type=:type))as id)as t2 " +
     *                             " WHERE sentential.id>=t2.id AND type=:type" +
     *                             " ORDER BY sentential.id ASC "
     * 优化：（目的：初级题目随机查询初级题目对应的id 中级题目随机查询中级题目对应的id   合理的随机性，若不这样 中级题目很有可能一直随机id靠前的题）
     *  改进部分(SELECT ROUND(RAND()*((SELECT MAX(id) FROM oel_sentential WHERE type=:type)-(SELECT id FROM oel_sentential WHERE type=:type limit 1)-3)+
     *  (SELECT id FROM oel_sentential WHERE type=:type limit 1))as id)as t2
     *  SELECT id FROM oel_sentential WHERE type=:type limit 1//查询该类型的首条记录
     * 查询 234--5678之间的随机数 mysql语法：ROUND(RAND() * 5444 + 234)
     * 参考：https://www.jb51.net/article/92375.htm
     * https://zhoushijun.iteye.com/blog/980560
     * @param type
     * @param var1
     * @return
     */
    @Query(nativeQuery = true,
            value =
                    "SELECT * " +
                            " FROM oel_sentential as sentential JOIN" +
                            "(SELECT ROUND(RAND()*((SELECT MAX(id) FROM oel_sentential WHERE type=:type)-(SELECT id FROM oel_sentential WHERE type=:type limit 1)-:size)+(SELECT id FROM oel_sentential WHERE type=:type limit 1))as id)as t2 " +
                            " WHERE sentential.id>=t2.id AND type=:type" +
                            " ORDER BY sentential.id ASC "
            //countQuery = "select count(*) from oel_sentential where type=1"// 不具备JPA命名规范的DAO方法，有pageable，必须写countQuery
    )
    public Page<Sentential> findSententialsByRandAndType(@Param("type") Integer type, @Param("size") Integer size, Pageable var1);
}
