package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.model.TodoModel;

@Mapper
public interface TodoMapper {
	public boolean add(TodoModel item);

	public List<TodoModel> getAll();

	public TodoModel get (Integer id);

	public boolean update(TodoModel item);

	public boolean delete(Integer id);

	//未完成todoを検索
	public List<TodoModel> search(TodoModel item);

}
