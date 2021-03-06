package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.UserSearchRequest;
import com.example.todo.model.TodoModel;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("c1")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@GetMapping("/items")
	public List<TodoModel> getAll() {
		return todoService.getAll();
	}

	@GetMapping("/items/{id}")
	public TodoModel get(@PathVariable("id") Integer id) {
		return todoService.get(id);
	}

	@PostMapping("/items")
	public String add(@RequestBody TodoModel item) {
		boolean result = todoService.add(item);

		String resultStr;

		if (result) {
			resultStr = "{\"result\":\"ok\"}";
		} else {
			resultStr = "{\"result\":\"error\"}";
		}
		return resultStr;
	}

	@PutMapping("/items")
	public String update(@RequestBody TodoModel item) {
		boolean result = todoService.update(item);

		String resultStr;

		if (result) {
			resultStr = "{\"result\":\"ok\"}";
		} else {
			resultStr = "{\"result\":\"error\"}";
		}
		return resultStr;
	}

	@DeleteMapping("/items/{id}")
	public String delete(@PathVariable("id") Integer id) {
		boolean result = todoService.delete(id);

		String resultStr;

		if(result) {
			resultStr = "{\"result\":\"ok\"}";
		}	else {
			resultStr ="{\"result\":\"error\"}";
		}
		return resultStr;
	}

	//?????????todo????????????(?????????:get?????????URL??????????????????????????????@PathVariable?????????)
//	@GetMapping("/search/{name}")
//	public List<TodoModel> search(@PathVariable("name") String name) {
//		List<TodoModel> searchResult = new ArrayList<TodoModel>();
//		searchResult= todoService.search(name);
//		return searchResult;
//	}

	//?????????todo????????????(????????????post?????????body???????????????????????????)
//	@PostMapping("/search")
//	public List<TodoModel> search( @RequestBody UserSearchRequest name) {
//		List<TodoModel> searchResult = new ArrayList<TodoModel>();
//		searchResult= todoService.search(name.getName());
//		return searchResult;
//	}

	//?????????todo????????????(????????????get?????????body??????????????????????????????@RequestParam?????????)
	@GetMapping("/search")
	public List<TodoModel> search( @RequestParam("name") String name,@RequestParam("searchDate") String searchDate) {
		List<TodoModel> searchResult = new ArrayList<TodoModel>();
		TodoModel todoModel=new TodoModel();
		todoModel.setName(name);
		todoModel.setDeadline(searchDate);
		searchResult= todoService.search(todoModel);
		return searchResult;
	}

	//?????????todo????????????????????????:get?????????URL??????????????????????????????@ModelAttribute????????????
//	@GetMapping("/search/{name}")
//	public List<TodoModel> search( @ModelAttribute UserSearchRequest name) {
//		List<TodoModel> searchResult = new ArrayList<TodoModel>();
//		searchResult= todoService.search(name.getName());
//		return searchResult;
//	}

}
