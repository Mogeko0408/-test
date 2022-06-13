package com.example.todo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.TodoModel;

@Service
public class TodoService {

	@Autowired
	TodoMapper todoMapper;

	public boolean add(TodoModel item) {
		return todoMapper.add(item);
	}

	public List<TodoModel> getAll(){
		return todoMapper.getAll();
	}

	public TodoModel get(Integer id) {
		return todoMapper.get(id);
	}

	public boolean update(TodoModel item) {
		return todoMapper.update(item);
	}

	public boolean delete(Integer id) {
		return todoMapper.delete(id);
	}

	//未完成todoを検索
	public List<TodoModel> search(TodoModel item) {
		return todoMapper.search(item);
	}
}
