package org.example;

import io.mybatis.mapper.Mapper;
import io.mybatis.mapper.list.ListMapper;

public interface PersonMapper extends Mapper<Person, Long>, ListMapper<Person> {
}
