package com.example.todo.model;

import lombok.Data;

@Data
public class TodoModel {
	Integer id;
	String deadline;
	String name;
	Integer status;
}
