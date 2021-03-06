package com.zhy.mapping;

import com.zhy.entity.GeneratorTest;
import com.zhy.entity.GeneratorTestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GeneratorTestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    long countByExample(GeneratorTestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int deleteByExample(GeneratorTestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int insert(GeneratorTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int insertSelective(GeneratorTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    List<GeneratorTest> selectByExample(GeneratorTestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    GeneratorTest selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") GeneratorTest record, @Param("example") GeneratorTestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") GeneratorTest record, @Param("example") GeneratorTestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(GeneratorTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table generator_test
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(GeneratorTest record);
}